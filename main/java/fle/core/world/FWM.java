package fle.core.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import fle.FLE;
import fle.api.enums.CompoundType;
import fle.api.enums.EnumWorldNBT;
import fle.api.material.Matter;
import fle.api.material.Matter.AtomStack;
import fle.api.net.FlePackets.CoderFWMUpdate;
import fle.api.util.FleLog;
import fle.api.util.IAirConditionProvider;
import fle.api.world.BlockPos;
import fle.api.world.BlockPos.ChunkPos;
import fle.api.world.IWorldManager;
import fle.core.net.FleWorldMetaSyncPacket;

public class FWM implements IWorldManager, IAirConditionProvider
{
	private static final int maxNBTSize = EnumWorldNBT.values().length;
	protected Map<Integer, Map<ChunkPos, ChunkData>> nbts = new HashMap(1);
	private Map<Integer, Map<ChunkPos, Integer>> airConditions = new HashMap(1);
	
	public FWM()
	{
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(this);
	}
	
	class ChunkData
	{
		public short[][][] datas;
		
		public ChunkData(int dataSize)
		{
			datas = new short[dataSize][][];
		}
		
		private short getDataFromCoord(int dataType, BlockPos aPos)
		{
			int x = aPos.x & 15;
			int y = aPos.y & 255;
			int z = aPos.z & 15;
			if(datas[dataType] == null)
			{
				return 0;
			}
			if(datas[dataType][y] == null)
			{
				return 0;
			}
			else
			{
				return (short) (datas[dataType][y][x << 4 | z] - 1);
			}
		}
		
		short setDataFromCoord(int dataType, BlockPos aPos, short data)
		{
			int x = aPos.x & 15;
			int y = aPos.y & 255;
			int z = aPos.z & 15;
			if(datas[dataType] == null)
			{
				datas[dataType] = new short[256][];
			}
			if(datas[dataType][y] == null)
			{
				datas[dataType][y] = new short[256];
			}
			short ret = datas[dataType][y][x << 4 | z];
			datas[dataType][y][x << 4 | z] = (short) (data + 1);
			 return ret;
		}
	
		private int[] asIntArray(int dataType)
		{
			if(datas[dataType] == null) return new int[0];
			List<Integer> ret = new ArrayList(1);
			for(int y = 0; y < datas[dataType].length; ++y)
			{	
				if(datas[dataType][y] == null) continue;
				for(int i = 0; i < datas[dataType][y].length; ++i)
				{
					if(datas[dataType][y][i] != 0)
					{
						Integer a = new Integer((datas[dataType][y][i] << 16) + (y << 8) + i);
						ret.add(a);
					}
				}
			}
			Integer[] tInt = ret.toArray(new Integer[ret.size()]);
			int[] tRet = new int[tInt.length];
			int c = 0;
			for(Integer i : tInt)
				tRet[c++] = i;
			return tRet;
		}
		
		void loadFromIntArray(int dataType, int[] array)
		{
			for(int i = 0; i < array.length; ++i)
			{
				if(datas[dataType] == null) datas[dataType] = new short[256][];
				int y = (array[i] >> 8) & 255;
				if(datas[dataType][y] == null) datas[dataType][y] = new short[256];
				int x = array[i] & 255;
				datas[dataType][y][x] = (short) (array[i] >> 16);
			}
		}

		public short[] getDatasFromCoord(BlockPos aPos)
		{
			short[] t = new short[maxNBTSize];
			for(int i = 0; i < t.length; ++i)
				t[i] = getDataFromCoord(i, aPos);
			return t;
		}
	}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load evt)
	{
		if(!nbts.containsKey(new Integer(evt.world.provider.dimensionId)))
			nbts.put(new Integer(evt.world.provider.dimensionId), new HashMap());
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload evt)
	{
		if(nbts.containsKey(new Integer(evt.world.provider.dimensionId)))
			nbts.get(new Integer(evt.world.provider.dimensionId)).clear();
	}
	
	@SubscribeEvent
	public void onChunkLoad(ChunkEvent.Load evt)
	{
		if(!evt.world.isRemote)
		{
			sendAllData(evt.world.provider.dimensionId, new ChunkPos(evt.getChunk()));
		}
		else
		{
			
		}
	}
	
	@SubscribeEvent
	public void onDataLoad(ChunkDataEvent.Load evt)
	{
		try
		{
			NBTTagCompound tNBT1 = evt.getData();
			Integer tDim = new Integer(evt.world.provider.dimensionId);
			
			if(!nbts.containsKey(tDim))
			{
				nbts.put(tDim, new HashMap());
			}
			ChunkPos tPos = new ChunkPos(evt.getChunk().xPosition, evt.getChunk().zPosition);
			if(evt.getData().hasKey("FLE"))
			{
				if(!nbts.get(tDim).containsKey(tPos))
				{
					nbts.get(tDim).put(tPos, new ChunkData(maxNBTSize));
				}
				NBTTagCompound list = evt.getData().getCompoundTag("FLE");
				for(int i = 0; i < maxNBTSize; ++i)
				{
					int[] is = list.getIntArray("Tag_" + String.valueOf(i));
					if(is.length != 0)
						nbts.get(tDim).get(tPos).loadFromIntArray(i, is);
				}
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
			if(!nbts.get(dim).containsKey(tPos)) flagData = false;
			NBTTagCompound list = new NBTTagCompound();
			ChunkData data = nbts.get(dim).get(tPos);
			if(flagData)
				for(int i = 0; i < maxNBTSize; ++i)
				{
					int[] is = data.asIntArray(i);
					if(is != null)
						list.setIntArray("Tag_" + String.valueOf(i), is);
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
		return getChunkData(dim, pos).getDataFromCoord(dataType, pos);
	}

	public short setData(BlockPos pos, int dataType, int data) 
	{
		return setData(pos, dataType, data, true);
	}

	public short setData(BlockPos pos, int dataType, int data, boolean sync) 
	{
		int dim = 0;
		boolean flag = false;
		if(pos.access instanceof World)
		{
			dim = ((World) pos.access).provider.dimensionId;
			flag = !((World) pos.access).isRemote;
		}
		else if(FLE.fle.getPlatform().getPlayerInstance() != null)
		{
			dim = FLE.fle.getPlatform().getPlayerInstance().worldObj.provider.dimensionId;
		}
		ChunkData tData = getChunkData(dim, pos);
		short ret = tData.setDataFromCoord(dataType, pos, (short) data);
		if(flag && sync)
		{
			FLE.fle.getNetworkHandler().sendTo(new CoderFWMUpdate(pos, tData.getDatasFromCoord(pos)));
		}
		return ret;
	}
	
	protected ChunkData getChunkData(int dim, BlockPos pos)
	{
		int tDim = new Integer(dim);
		if(!nbts.containsKey(tDim)) nbts.put(tDim, new HashMap());
		if(!nbts.get(tDim).containsKey(pos.getChunkPos())) nbts.get(tDim).put(pos.getChunkPos(), new ChunkData(maxNBTSize));
		return nbts.get(tDim).get(pos.getChunkPos());
	}

	@Override
	public Matter getAirLevel(BlockPos aPos)
	{
		switch(aPos.getDim())
		{
		case -1 : return new Matter(CompoundType.Mix, new AtomStack(Matter.mCO2, 12), new AtomStack(Matter.mCO, 6), new AtomStack(Matter.mHF, 2), new AtomStack(Matter.mN2, 29), new AtomStack(Matter.mH2S, 31));
		case 0 : 
			float f = 0.0F;
			for(int i = -2; i <= 2; ++i)
				for(int j = -2; j <= 2; ++j)
					f += aPos.toPos(i, 0, j).getBiome().getIntRainfall() / 65536.0F;
			return new Matter(CompoundType.Mix, new AtomStack(Matter.mN2, 79), new AtomStack(Matter.mO2, 20), new AtomStack(Matter.mH2O, (int) Math.floor(f)));
		case 1 : return new Matter(CompoundType.Mix, new AtomStack(Matter.mN2));
		}
		return Matter.mAir;
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

	public short[] getDatas(BlockPos aPos)
	{
		int dim = aPos.getDim();
		int i = 0;
		short[] ret = new short[maxNBTSize];
		for(ChunkData data = getChunkData(dim, aPos); i < maxNBTSize; ++i)
			ret[i] = data.getDataFromCoord(i, aPos);
		return ret;
	}

	public void setDatas(BlockPos pos, short[] data, boolean sync)
	{
		if(data != null)
		{
			int dim = 0;
			boolean flag = false;
			if(pos.access instanceof World)
			{
				dim = ((World) pos.access).provider.dimensionId;
				flag = !((World) pos.access).isRemote;
			}
			else if(FLE.fle.getPlatform().getPlayerInstance() != null)
			{
				dim = FLE.fle.getPlatform().getPlayerInstance().worldObj.provider.dimensionId;
			}
			ChunkData tData = getChunkData(dim, pos);
			for(int i = 0; i < data.length; ++i)
				tData.setDataFromCoord(i, pos, (short) data[i]);
			if(flag && sync)
			{
				FLE.fle.getNetworkHandler().sendTo(new CoderFWMUpdate(pos, tData.getDatasFromCoord(pos)));
			}
		}
	}

	@Override
	public short getData(BlockPos pos, EnumWorldNBT dataType)
	{
		int data = getData(pos, dataType.ordinal());
		return (short) (data == -1 ? 0 : data);
	}

	@Override
	public short setData(BlockPos pos, EnumWorldNBT dataType, int data)
	{
		return setData(pos, dataType.ordinal(), data);
	}

	@Override
	public void setDatas(BlockPos pos, Map<EnumWorldNBT, Integer> map,
			boolean sync)
	{
		for(Entry<EnumWorldNBT, Integer> e : map.entrySet())
		{
			setData(pos, e.getKey().ordinal(), e.getValue());
		}
	}

	@Override
	public short removeData(BlockPos pos, EnumWorldNBT dataType)
	{
		return setData(pos, dataType.ordinal(), -1);
	}

	@Override
	public short[] removeData(BlockPos pos)
	{
		short[] ret = new short[maxNBTSize];
		boolean flag = false;
		for(int i = 0; i < ret.length; ++i)
		{
			if(i == ret.length - 1) flag = true;
			ret[i] = setData(pos, i, -1, flag);
		}
		return ret;
	}

	public void sendData(BlockPos pos)
	{
		FLE.fle.getNetworkHandler().sendToNearBy(new CoderFWMUpdate(pos, getDatas(pos)), new TargetPoint(pos.getDim(), pos.x, pos.y, pos.z, 512D));
	}

	public void sendAllData(int dim, ChunkPos pos)
	{
		if(getClass() != FWM.class) return;
		if(!nbts.containsKey(dim)) return;
		if(!nbts.get(dim).containsKey(pos)) return;
		ChunkData data = nbts.get(dim).get(pos);
		int[][] arrays = new int[maxNBTSize][];
		for(int i = 0; i < maxNBTSize; ++i)
			arrays[i] = data.asIntArray(i);
		FLE.fle.getNetworkHandler().sendLargePacket(new FleWorldMetaSyncPacket(dim, pos, arrays), new TargetPoint(dim, pos.x * 16 + 8, 128, pos.z * 16 + 8, 512D));
	}
	
	public void syncData(int dim, ChunkPos pos, int[][] datas)
	{
		;
	}
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent evt)
	{
		Iterator<BlockPos> itr = new ArrayList(cacheList).iterator();
		while(itr.hasNext())
		{
			BlockPos pos = itr.next();
			try
			{
				sendData(pos);
				cacheList.remove(pos);
			}
			catch(Throwable e)
			{
				FleLog.getLogger().info("Fail to send data " + pos.x + "," + pos.z + " is is can't connect server or array outof bound?");;
			}
		}
	}
	
	private List<BlockPos> cacheList = new ArrayList();

	public void markPosForUpdate(BlockPos pos)
	{
		cacheList.add(pos);
	}

	public IMessage createPacket(int dim, BlockPos pos)
	{
		if(!nbts.containsKey(dim)) return null;
		ChunkPos tPos = pos.getChunkPos();
		if(!nbts.get(dim).containsKey(tPos)) return null;
		ChunkData data = nbts.get(dim).get(tPos);
		int[][] arrays = new int[maxNBTSize][];
		for(int i = 0; i < maxNBTSize; ++i)
			arrays[i] = data.asIntArray(i);
		return new FleWorldMetaSyncPacket(dim, tPos, arrays);
	}
}