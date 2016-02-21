package fle.core.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import farcore.util.U.W;
import flapi.enums.EnumWorldNBT;
import flapi.util.FleLog;
import flapi.world.BlockPos;
import flapi.world.BlockPos.ChunkPos;
import fle.FLE;
import fle.core.net.FleSyncFWMSmallPacket;
import fle.core.net.FleWorldMetaSyncPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class FWMLocal
{	
	class ChunkData
	{
		public short[][][] datas;
		
		public ChunkData()
		{
			datas = new short[256][][];
		}
		
		short getDataFromCoord(int dataType, BlockPos pos)
		{
			int x = pos.x & 15;
			int y = pos.y & 255;
			int z = pos.z & 15;
			if(datas[y] == null)
			{
				return 0;
			}
			if(datas[y][x << 4 | z] == null)
			{
				return 0;
			}
			else
			{
				return datas[y][x << 4 | z][dataType];
			}
		}
		
		short setDataFromCoord(int dataType, BlockPos pos, short data)
		{
			int x = pos.x & 15;
			int y = pos.y & 255;
			int z = pos.z & 15;
			if(datas[y] == null)
			{
				datas[y] = new short[256][];
			}
			if(datas[y][x << 4 | z] == null)
			{
				datas[y][x << 4 | z] = new short[maxNBTSize];
			}
			short ret = datas[y][x << 4 | z][dataType];
			datas[y][x << 4 | z][dataType] = data;
			 return ret;
		}
	
		int[][] asIntArrays()
		{
			int[][] ret = new int[maxNBTSize][];
			for(int i = 0; i < ret.length; ++i)
				ret[i] = asIntArray(i);
			return ret;
		}
		
		int[] asIntArray(int dataType)
		{
			List<Integer> ret = new ArrayList(1);
			for(int i = 0; i < datas.length; ++i)
			{
				short[][] datas1 = datas[i];
				if(datas1 == null) continue;
				for(int j = 0; j < datas1.length; ++j)
				{
					short[] datas2 = datas1[j];
					if(datas2 == null) continue;
					if(datas2[dataType] != 0)
					{
						Integer a = new Integer((datas2[dataType] << 16) + (i << 8) + j);
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
				int y = (array[i] >> 8) & 255;
				if(datas[y] == null) datas[y] = new short[256][];
				int x = array[i] & 255;
				if(datas[y][x] == null) datas[y][x] = new short[maxNBTSize];
				datas[y][x][dataType] = (short) (array[i] >> 16);
			}
		}

		short[] getDatasFromCoord(BlockPos aPos)
		{
			int x = aPos.x & 15;
			int y = aPos.y & 255;
			int z = aPos.z & 15;
			if(datas[y] == null)
			{
				return new short[maxNBTSize];
			}
			if(datas[y][x << 4 | z] == null)
			{
				return new short[maxNBTSize];
			}
			else
			{
				return datas[y][x << 4 | z];
			}
		}
	}
	
	private World world;
	private final int dim;
	private Map<ChunkPos, ChunkData> dataCache = new HashMap(256);
	private Map<ChunkPos, ChunkData> genDataCache = new HashMap(256);
	private Map<ChunkPos, ChunkData> unloadCache = new HashMap(32);
	private List<BlockPos> changedPos = new ArrayList();
	private Map<EntityPlayerMP, List<ChunkPos>> updateCache = new HashMap();
	protected static final int maxNBTSize = EnumWorldNBT.values().length;
	
	public FWMLocal(World world)
	{
		this.world = world;
		this.dim = world.provider.dimensionId;
	}
	public FWMLocal(int dim)
	{
		this.dim = dim;
	}

	protected boolean isDataInCache(ChunkPos pos)
	{
		return dataCache.containsKey(pos);
	}
	
	protected ChunkData getData(ChunkPos pos)
	{
		return isDataInCache(pos) ? dataCache.get(pos) : 
			genDataCache.containsKey(pos) ? genDataCache.get(pos) :
				setData(pos);
	}
	
	protected ChunkData getGenData(ChunkPos pos)
	{
		return genDataCache.containsKey(pos) ? genDataCache.get(pos) :
			setGenerateData(pos);
	}
	
	protected ChunkData setData(ChunkPos pos)
	{
		ChunkData data = new ChunkData();
		dataCache.put(pos, data);
		return data;
	}
	
	protected ChunkData setGenerateData(ChunkPos pos)
	{
		ChunkData data = new ChunkData();
		genDataCache.put(pos, data);
		return data;
	}
	
	public short get(BlockPos pos, int nbt)
	{
		return getData(pos.getChunkPos()).getDataFromCoord(nbt, pos);
	}
	
	public short get(BlockPos pos, EnumWorldNBT nbt)
	{
		return get(pos, nbt.ordinal());
	}
	
	public short[] get(BlockPos pos)
	{
		short[] ret = new short[maxNBTSize];
		for(int i = 0; i < ret.length; ++i)
			ret[i] = get(pos, i);
		return ret;
	}
	
	public void set(ChunkPos pos, int[][] datas)
	{
		for(int i = 0; i < datas.length; ++i)
		{
			getData(pos).loadFromIntArray(i, datas[i]);
		}
	}
	
	public void set(BlockPos pos, EnumWorldNBT nbt, short value)
	{
		set(pos, nbt.ordinal(), value);
	}
	
	public short set(BlockPos pos, int nbt, short value)
	{
		return getData(pos.getChunkPos()).setDataFromCoord(nbt, pos, value);
	}
	
	public short setGen(BlockPos pos, int nbt, short value)
	{
		return getGenData(pos.getChunkPos()).setDataFromCoord(nbt, pos, value);
	}
	
	public short setAndCallInClient(BlockPos pos, EnumWorldNBT nbt, short value)
	{
		return setAndCallInClient(pos, nbt.ordinal(), value, true);
	}
	
	public short setAndCallInClient(BlockPos pos, int nbt, short value, boolean flag)
	{
		short ret = set(pos, nbt, value);
		if(flag)
		{
			FLE.fle.getNetworkHandler().sendToNearBy(new FleSyncFWMSmallPacket(pos, getData(pos.getChunkPos()).getDatasFromCoord(pos)), new TargetPoint(dim, pos.x + 0.5F, pos.y + 0.5F, pos.z + 0.5F, 128.0F));
		}
		else
		{
			changedPos.add(pos);
		}
		return ret;
	}
	
	public void setAndCallInClient(BlockPos pos, int nbt, short value)
	{
		set(pos, nbt, value);
		changedPos.add(pos);
	}
	
	public void onChunkLoad(ChunkPos pos)
	{
		if(genDataCache.containsKey(pos))
		{
			dataCache.put(pos, genDataCache.get(pos));
			genDataCache.remove(pos);
		}
	}
	
	public void onChunkLoad(ChunkPos pos, NBTTagCompound nbt)
	{
		try
		{
			if(!dataCache.containsKey(pos))
			{
				dataCache.put(pos, new ChunkData());
			}
			for(int i = 0; i < maxNBTSize; ++i)
			{
				int[] is = nbt.getIntArray("Tag_" + String.valueOf(i));
				if(is.length != 0)
				{
					dataCache.get(pos).loadFromIntArray(i, is);
				}
			}
			FleLog.getLogger().debug("FWM : Loaded chunk " + pos.x + ", " + pos.z + " in world, "
					+ "now cached " + dataCache.size() + " chunk data in this dimention.");
		}
		catch(Exception exception)
		{
			;
		}
	}
	
	public void onChunkSave(ChunkPos pos, NBTTagCompound nbt)
	{
		try
		{
			if(genDataCache.containsKey(pos))
			{
				dataCache.put(pos, genDataCache.get(pos));
				genDataCache.remove(pos);
			}
			if(dataCache.containsKey(pos))
			{
				for(int i = 0; i < maxNBTSize; ++i)
				{
					int[] is = dataCache.get(pos).asIntArray(i);
					if(is != null)
						nbt.setIntArray("Tag_" + String.valueOf(i), is);
				}
			}
			else if(unloadCache.containsKey(pos))
			{
				for(int i = 0; i < maxNBTSize; ++i)
				{
					int[] is = dataCache.get(pos).asIntArray(i);
					if(is != null)
						nbt.setIntArray("Tag_" + String.valueOf(i), is);
				}
			}
			unloadCache.remove(pos);
			FleLog.getLogger().debug("FWM : Saved chunk " + pos.x + ", " + pos.z + " in world, "
					+ "now cached " + dataCache.size() + " chunk data in this dimention.");
		}
		catch(Exception exception)
		{
			;
		}
	}
	
	public void onChunkUnload(ChunkPos pos)
	{
		if(dataCache.containsKey(pos))
		{
			unloadCache.put(pos, dataCache.remove(pos));
		}
		FleLog.getLogger().debug("FWM : Unloaded chunk " + pos.x + ", " + pos.z + " in world, "
				+ "now cached " + dataCache.size() + " chunk data in this dimention.");
	}

	public void mark(EntityPlayerMP player, ChunkPos pos)
	{
		if(!updateCache.containsKey(player))
			updateCache.put(player, new ArrayList());
		updateCache.get(player).add(pos);
	}
	
	public void sendUpdate()
	{
		for(BlockPos pos : changedPos)
		{
			FLE.fle.getNetworkHandler().sendToAll(new FleSyncFWMSmallPacket(pos, get(pos)));
		}
		for(Entry<EntityPlayerMP, List<ChunkPos>> entry : updateCache.entrySet())
		{
			for(ChunkPos pos : entry.getValue())
			{
				FLE.fle.getNetworkHandler().sendToPlayer(new FleWorldMetaSyncPacket(dim, pos, getData(pos).asIntArrays()), entry.getKey());
			}
		}
		changedPos.clear();
		updateCache.clear();
	}
	
	public void clear()
	{
		dataCache.clear();
		genDataCache.clear();
		changedPos.clear();
		FleLog.getLogger().info("FWM : Unloaded world at " + dim + ".");
	}
}