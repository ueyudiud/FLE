package fle.core.world;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import flapi.enums.EnumWorldNBT;
import flapi.world.BlockPos;
import flapi.world.BlockPos.ChunkPos;
import flapi.world.IAirConditionProvider;
import flapi.world.IWorldManager;
import fle.FLE;
import fle.core.net.FleSyncFWMSmallPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

/**
 * The far land era world manager (FWM).<br>
 * Provide more world data to save.
 * @author ueyudiud
 *
 */
public class FWM implements IWorldManager, IAirConditionProvider
{
	private static final short[] defaultData;
	private static final byte checkRange = 32;
	private static byte generateFlag = 0;
	protected Map<Integer, FWMLocal> nbts = new HashMap(3);
	private Map<Integer, Map<ChunkPos, Integer>> airConditions = new HashMap(3);
	
	static
	{
		defaultData = new short[EnumWorldNBT.values().length];
	}
	
	/**
	 * Get sync meta type.<br>
	 * 0 for also send changes in client during update.
	 * 1 for also send changes when changing.
	 * 2 for not send changes in client.
	 * 3 for generate flag.
	 */
	public static void setSyncType(byte type)
	{
		generateFlag = type;
	}
	
	public FWM()
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}

	private Integer dim(World world)
	{
		return world.provider.dimensionId;
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load evt)
	{
		if(!nbts.containsKey(new Integer(dim(evt.world))))
			nbts.put(new Integer(dim(evt.world)), new FWMLocal(evt.world));
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload evt)
	{
		if(nbts.containsKey(new Integer(dim(evt.world))))
			nbts.get(new Integer(dim(evt.world))).clear();
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load evt)
	{
		if(!evt.world.isRemote)
		{
			Integer dim = dim(evt.world);
			if(nbts.containsKey(dim))
			{
				nbts.get(dim).onChunkLoad(new ChunkPos(evt.getChunk()));
			}
			//sendChunkData(dim(evt.world), new ChunkPos(evt.getChunk()));
		}
		else
		{
			askChunkData(dim(evt.world), new ChunkPos(evt.getChunk()));
		}
	}
	
	@SubscribeEvent
	public void onChunkUnload(ChunkEvent.Unload evt)
	{
		Integer dim = dim(evt.world);
		if(nbts.containsKey(dim))
		{
			nbts.get(dim).onChunkUnload(new ChunkPos(evt.getChunk()));
		}
	}
	
	@SubscribeEvent
	public void onDataLoad(ChunkDataEvent.Load evt)
	{
		try
		{
			Integer tDim = new Integer(dim(evt.world));
			
			if(!nbts.containsKey(tDim))
			{
				nbts.put(tDim, new FWMLocal(evt.world));
			}
			ChunkPos tPos = new ChunkPos(evt.getChunk().xPosition, evt.getChunk().zPosition);
			if(evt.getData().hasKey("FLE"))
			{
				NBTTagCompound list = evt.getData().getCompoundTag("FLE");
				nbts.get(tDim).onChunkLoad(tPos, list);
				if(list.hasKey("Tag_Air"))
				{
					if(!airConditions.containsKey(tDim))
					{
						airConditions.put(tDim, new HashMap(1));
					}
					airConditions.get(tDim).put(tPos, list.getInteger("Tag_Air"));
				}
			}
		}
		catch(Throwable e)
		{
			throw new RuntimeException("FWM: Fail to load nbt tag from chunk data.", e);
		}
	}

	@SubscribeEvent
	public void onDataSave(ChunkDataEvent.Save evt)
	{
		try
		{
			Integer dim = new Integer(evt.world.provider.dimensionId);
			boolean flagData = true;
			boolean flagAir = true;
			if(!nbts.containsKey(dim)) flagData = false;
			if(!airConditions.containsKey(dim)) flagAir = false;
			ChunkPos tPos = new ChunkPos(evt.getChunk().xPosition, evt.getChunk().zPosition);
			NBTTagCompound list = new NBTTagCompound();
			if(flagData)
			{
				nbts.get(dim).onChunkSave(tPos, list);
			}
			if(flagAir)
				list.setInteger("Tag_Air", airConditions.get(dim).get(tPos));
			if(evt.getData().hasKey("FLE"))
				evt.getData().removeTag("FLE");
			evt.getData().setTag("FLE", list);
		}
		catch(Throwable e)
		{
			throw new RuntimeException("FWM: Fail to save chunk data.", e);
		}
	}

	public short getData(BlockPos pos, int dataType) 
	{
		int dim = pos.getDim();
		return getManager(pos).get(pos, dataType);
	}

	public short setData(BlockPos pos, int dataType, int data) 
	{
		return setData(pos, dataType, data, generateFlag);
	}

	public short setData(BlockPos pos, int dataType, int data, byte syncType) 
	{
		boolean flag = pos.world().checkChunksExist(pos.x - checkRange, pos.y - checkRange, pos.z - checkRange, pos.x + checkRange, pos.y + checkRange, pos.z + checkRange);
		int dim = 0;
		if(pos.access instanceof World)
		{
			dim = dim((World) pos.access);
		}
		else if(FLE.fle.getPlatform().getPlayerInstance() != null)
		{
			dim = dim(FLE.fle.getPlatform().getPlayerInstance().worldObj);
		}
		switch(syncType)
		{
		case 0 : return getManager(pos).setAndCallInClient(pos, dataType, (short) data, flag);
		case 1 : return getManager(pos).setAndCallInClient(pos, dataType, (short) data, true);
		case 2 : return getManager(pos).set(pos, dataType, (short) data);
		case 3 : return getManager(pos).setGen(pos, dataType, (short) data);
		default: return 0;
		}
	}
	
	protected FWMLocal getManager(BlockPos pos)
	{
		int dim = pos.getDim();
		if(!nbts.containsKey(dim)) nbts.put(dim, new FWMLocal(pos.world()));
		return nbts.get(dim);
	}

	@Override
	public int getPolluteLevel(BlockPos aPos)
	{
		if(!airConditions.containsKey(aPos.getDim())) return 1;
		return airConditions.get(aPos.getDim()).get(aPos.getChunkPos()) / 100;
	}

	public void setPollute(BlockPos aPos, int pollute)
	{
		int tDim = aPos.getDim();
		if(!airConditions.containsKey(tDim)) airConditions.put(tDim, new HashMap());
		int i = airConditions.get(tDim).get(aPos.getChunkPos()) + pollute;
		airConditions.get(tDim).put(aPos.getChunkPos(), i);
	}

	public short[] getDatas(BlockPos pos)
	{
		return getManager(pos).get(pos);
	}

	public void setDatas(BlockPos pos, short[] data)
	{
		setDatas(pos, data, generateFlag);
	}

	public void setDatas(BlockPos pos, short[] data, byte syncType)
	{
		if(data != null)
		{
			if(syncType == 3)
			{
				for(int i = 0; i < data.length; ++i)
					setData(pos, i, data[i], syncType);
			}
			else
			{
				for(int i = 0; i < data.length - 1; ++i)
					setData(pos, i, data[i], (byte) 2);
				setData(pos, data.length - 1, data[data.length - 1], syncType);
			}
		}
	}

	@Override
	public short getData(BlockPos pos, EnumWorldNBT dataType)
	{
		return getData(pos, dataType.ordinal());
	}

	@Override
	public short setData(BlockPos pos, EnumWorldNBT dataType, int data)
	{
		return setData(pos, dataType.ordinal(), data);
	}

	@Override
	public short removeData(BlockPos pos, EnumWorldNBT dataType)
	{
		return setData(pos, dataType.ordinal(), 0);
	}

	public short removeData(BlockPos pos, int type)
	{
		return setData(pos, type, 0);
	}

	@Override
	public short[] removeData(BlockPos pos)
	{
		short[] ret = getDatas(pos);
		setDatas(pos, defaultData);
		return ret;
	}
	
	@Override
	public void markData(BlockPos pos)
	{
		FLE.fle.getNetworkHandler().sendToAll(new FleSyncFWMSmallPacket(pos, getDatas(pos)));
	}
	
	public void askChunkData(int dim, ChunkPos pos)
	{
		;
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent evt)
	{
		if(!evt.world.isRemote)
		{
			int id = dim(evt.world);
			if(nbts.containsKey(id))
			{
				nbts.get(id).sendUpdate();
			}
		}
	}

	public void markPosForUpdate(EntityPlayerMP player, ChunkPos pos)
	{
		if(nbts.containsKey(dim(player.worldObj)))
		{
			nbts.get(dim(player.worldObj)).mark(player, pos);
		}
	}
	
	public void syncMeta(World world, BlockPos pos, short[] data)
	{
		;
	}

	public void syncMetas(int dim, ChunkPos pos, int[][] datas)
	{
		;
	}
}