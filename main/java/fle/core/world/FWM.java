package fle.core.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fle.FLE;
import fle.api.world.BlockPos;
import fle.api.world.BlockPos.ChunkPos;
import fle.api.world.IWorldManager;
import fle.core.net.FlePackets.CoderFWMUpdate;

public class FWM implements IWorldManager
{
	private static final int maxNBTSize = 8;
	private Map<Integer, Map<ChunkPos, ChunkData>> nbts = new HashMap(1);
	
	private class ChunkData
	{
		public short[][][] datas;
		
		public ChunkData(int dataSize)
		{
			datas = new short[dataSize][][];
		}
		
		private int getDataFromCoord(int dataType, BlockPos aPos)
		{
			int x = aPos.x & 15;
			int y = aPos.y & 255;
			int z = aPos.z & 15;
			if(datas[dataType] == null)
			{
				datas[dataType] = new short[256][];
				return 0;
			}
			if(datas[dataType][y] == null)
			{
				datas[dataType][y] = new short[256];
				return 0;
			}
			else
			{
				return datas[dataType][y][x << 4 | z];
			}
		}
		
		private void setDataFromCoord(int dataType, BlockPos aPos, short data)
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
			datas[dataType][y][x << 4 | z] = data;
		}
	
		private int[] asIntArray(int dataType)
		{
			if(datas[dataType] == null) return new int[0];
			List<Integer> ret = new ArrayList();
			for(int y = 0; y < datas[dataType].length; ++y)
			{	
				if(datas[dataType][y] == null) continue;
				for(int i = 0; i < datas[dataType][y].length; ++i)
				{
					Integer a = new Integer((datas[dataType][y][i] << 16) + (y << 8) + i);
					ret.add(a);
				}
			}
			Integer[] tInt = ret.toArray(new Integer[ret.size()]);
			int[] tRet = new int[tInt.length];
			int c = 0;
			for(Integer i : tInt)
				tRet[c++] = i;
			return tRet;
		}
		
		private void loadFromIntArray(int dataType, int[] array)
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
	}
	
	public FWM() 
	{
		
	}

	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload evt)
	{
		if(nbts.containsKey(new Integer(evt.world.provider.dimensionId)))
			nbts.get(new Integer(evt.world.provider.dimensionId)).clear();
	}
	
	@SubscribeEvent
	public void onDataLoad(ChunkDataEvent.Load evt)
	{
		NBTTagCompound tNBT1 = evt.getData();
		Integer tDim = new Integer(evt.world.provider.dimensionId);
		
		if(!nbts.containsKey(tDim))
		{
			nbts.put(tDim, new HashMap());
		}
		ChunkPos tPos = new ChunkPos(evt.getChunk().xPosition, evt.getChunk().zPosition);
		if(!nbts.get(tDim).containsKey(tPos))
		{
			nbts.get(tDim).put(tPos, new ChunkData(maxNBTSize));
		}
		if(evt.getData().hasKey("FLE"))
		{
			NBTTagCompound list = evt.getData().getCompoundTag("FLE");
			for(int i = 0; i < maxNBTSize; ++i)
			{
				int[] is = list.getIntArray("Tag_" + String.valueOf(i));
				if(is.length != 0)
					nbts.get(tDim).get(tPos).loadFromIntArray(i, is);
			}
		}
	}

	@SubscribeEvent
	public void onDataSave(ChunkDataEvent.Save evt)
	{
		Integer dim = new Integer(evt.world.provider.dimensionId);
		if(!nbts.containsKey(dim)) return;
		ChunkPos tPos = new ChunkPos(evt.getChunk().xPosition, evt.getChunk().zPosition);
		if(!nbts.get(dim).containsKey(tPos)) return;
		NBTTagCompound list = new NBTTagCompound();
		ChunkData data = nbts.get(dim).get(tPos);
		for(int i = 0; i < maxNBTSize; ++i)
		{
			int[] is = data.asIntArray(i);
			if(is != null)
				list.setIntArray("Tag_" + String.valueOf(i), is);
		}
		if(evt.getData().hasKey("FLE"))
			evt.getData().removeTag("FLE");
		evt.getData().setTag("FLE", list);
	}

	@Override
	public int getData(BlockPos pos, int dataType) 
	{
		int dim = 0;
		if(pos.access instanceof World) dim = ((World) pos.access).provider.dimensionId;
		else if(FLE.fle.getPlatform().getPlayerInstance() != null)
			dim = FLE.fle.getPlatform().getPlayerInstance().worldObj.provider.dimensionId;
		return getChunkData(dim, pos).getDataFromCoord(dataType, pos);
	}

	@Override
	public void setData(BlockPos pos, int dataType, int data) 
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
		getChunkData(dim, pos).setDataFromCoord(dataType, pos, (short) data);
		if(flag)
		{
			FLE.fle.getNetworkHandler().sendTo(new CoderFWMUpdate(dim, pos, dataType, data));
		}
		
	}

	@Override
	public void removeData(BlockPos pos) 
	{
		int dim = 0;
		if(pos.access instanceof World) dim = ((World) pos.access).provider.dimensionId;
		else if(FLE.fle.getPlatform().getPlayerInstance() != null)
			dim = FLE.fle.getPlatform().getPlayerInstance().worldObj.provider.dimensionId;
		for(int i = 0; i < maxNBTSize; ++i)
			getChunkData(dim, pos).setDataFromCoord(i, pos, (short) 0);
	}

	@Override
	public void removeData(BlockPos pos, int type) 
	{
		int dim = 0;
		if(pos.access instanceof World) dim = ((World) pos.access).provider.dimensionId;
		else if(FLE.fle.getPlatform().getPlayerInstance() != null)
			dim = FLE.fle.getPlatform().getPlayerInstance().worldObj.provider.dimensionId;
		getChunkData(dim, pos).setDataFromCoord(type, pos, (short) 0);
	}
	
	private ChunkData getChunkData(int dim, BlockPos pos)
	{
		int tDim = new Integer(dim);
		if(!nbts.containsKey(tDim)) nbts.put(tDim, new HashMap());
		if(!nbts.get(tDim).containsKey(pos.getChunkPos())) nbts.get(tDim).put(pos.getChunkPos(), new ChunkData(maxNBTSize));
		return nbts.get(tDim).get(pos.getChunkPos());
	}
}