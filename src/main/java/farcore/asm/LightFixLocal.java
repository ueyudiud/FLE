/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.asm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import farcore.data.Config;
import farcore.lib.collection.IntegerArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LightFixLocal implements Runnable
{
	private static final int cacheSize = 64;
	private static final int[][] caches = new int[cacheSize][];
	
	static class $
	{
		World world;
		EnumSkyBlock type;
		int x;
		int y;
		int z;
		long time;
		
		$(World world, EnumSkyBlock type, BlockPos pos)
		{
			this.world = world;
			this.type = type;
			this.x = pos.getX();
			this.y = pos.getY();
			this.z = pos.getZ();
			this.time = System.currentTimeMillis();
		}
	}
	final LinkedList<$> list = new LinkedList();
	final Map<IntegerArray, Byte> lightMap = new HashMap(65536, 1.0F);
	private final int[] lightUpdateBlockList = new int[32768];
	
	$ checking;
	World world;
	EnumSkyBlock type;
	int x;
	int y;
	int z;
	IntegerArray array = new IntegerArray(5);
	MutableBlockPos pos = new MutableBlockPos();
	
	public void startThread()
	{
		if(Config.multiThreadLight)
		{
			new Thread(this, "Far Light Thread " + FMLCommonHandler.instance().getSide()).start();
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			synchronized (this)
			{
				try
				{
					wait();
				}
				catch(InterruptedException exception){}
			}
			long time = System.currentTimeMillis();
			while(!this.list.isEmpty())
			{
				synchronized (this.list)
				{
					this.checking = this.list.removeFirst();
				}
				if(time - this.checking.time >= 10000)
				{
					continue;
				}
				boolean flag = true;
				try
				{
					check0(this.checking);
				}
				catch(Exception exception)
				{
					;
				}
			}
		}
	}
	
	public void onWorldUnload(World world)
	{
		int i = world.provider.getDimension();
		synchronized (this.list)
		{
			Iterator<$> iterator = this.list.iterator();
			while(iterator.hasNext())
			{
				if(iterator.next().world == world)
				{
					iterator.remove();
				}
			}
		}
		synchronized (this.lightMap)
		{
			Set<IntegerArray> set = ImmutableSet.copyOf(this.lightMap.keySet());
			for(IntegerArray array : set)
			{
				if(array.array[1] == i)
				{
					this.lightMap.remove(array);
				}
			}
		}
	}
	
	public void markLightForCheck(World world, EnumSkyBlock type, BlockPos pos)
	{
		synchronized (this.list)
		{
			$ info = new $(world, type, pos);
			this.list.add(info);
			synchronized (this)
			{
				notifyAll();
			}
		}
	}
	
	private void check0($ check)
	{
		this.world = check.world;
		this.type = check.type;
		this.x = check.x;
		this.y = check.y;
		this.z = check.z;
		this.array.array[0] = this.type.ordinal();
		this.array.array[1] = this.world.provider.getDimension();
		check();
	}
	
	private void check()
	{
		int i = 0;
		int j = 0;
		int k = getLight(0, 0, 0);
		int l = getRawLight(0, 0, 0);
		int i1 = this.pos.getX();
		int j1 = this.pos.getY();
		int k1 = this.pos.getZ();
		
		if (l > k)
		{
			this.lightUpdateBlockList[j++] = 133152;
		}
		else if (l < k)
		{
			this.lightUpdateBlockList[j++] = 133152 | k << 18;
			
			while (i < j)
			{
				int l1 = this.lightUpdateBlockList[i++];
				int i2 = (l1 & 63) - 32;
				int j2 = (l1 >> 6 & 63) - 32;
				int k2 = (l1 >> 12 & 63) - 32;
				int l2 = l1 >> 18 & 15;
				BlockPos blockpos = new BlockPos(i2 + i1, j2 + j1, k2 + k1);
				int i3 = getLight(i2, j2, k2);
				if (i3 == l2)
				{
					setLight(i2, j2, k2, 0);
					if (l2 > 0)
					{
						int j3 = Math.abs(i2);
						int k3 = Math.abs(j2);
						int l3 = Math.abs(k2);
						if (j3 + k3 + l3 < 17)
						{
							BlockPos.PooledMutableBlockPos pos3 = BlockPos.PooledMutableBlockPos.retain();
							for (EnumFacing facing : EnumFacing.values())
							{
								int i4 = i2 + facing.getFrontOffsetX();
								int j4 = j2 + facing.getFrontOffsetY();
								int k4 = k2 + facing.getFrontOffsetZ();
								pos3.setPos(i4 + i1, j4 + j1, k4 + k1);
								IBlockState state;
								int l4 = Math.max(1, (state = this.world.getBlockState(pos3)).getBlock().getLightOpacity(state, this.world, pos3));
								i3 = getLight(i4, j4, k4);
								if (i3 == l2 - l4 && j < this.lightUpdateBlockList.length)
								{
									this.lightUpdateBlockList[j++] = i4 - i1 + 32 | j4 - j1 + 32 << 6 | k4 - k1 + 32 << 12 | l2 - l4 << 18;
								}
							}
							pos3.release();
						}
					}
				}
			}
			i = 0;
		}
		while (i < j)
		{
			int i5 = this.lightUpdateBlockList[i++];
			int j5 = (i5 & 63) - 32;
			int k5 = (i5 >> 6 & 63) - 32;
			int l5 = (i5 >> 12 & 63) - 32;
			BlockPos blockpos1 = new BlockPos(j5 + i1, k5 + j1, l5 + k1);
			int i6 = getLight(j5, k5, l5);
			int j6 = getRawLight(j5, k5, l5);
			
			if (j6 != i6)
			{
				setLight(j5, k5, l5, j6);
				
				if (j6 > i6)
				{
					int k6 = Math.abs(j5);
					int l6 = Math.abs(k5);
					int i7 = Math.abs(l5);
					boolean flag = j < this.lightUpdateBlockList.length - 6;
					if (k6 + l6 + i7 < 17 && flag)
					{
						if (getLight(blockpos1.west()) < j6)
						{
							this.lightUpdateBlockList[j++] = j5 - 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.east()) < j6)
						{
							this.lightUpdateBlockList[j++] = j5 + 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.down()) < j6)
						{
							this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.up()) < j6)
						{
							this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 + 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.north()) < j6)
						{
							this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - 1 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.south()) < j6)
						{
							this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 + 1 - k1 + 32 << 12);
						}
					}
				}
			}
		}
	}
	
	/**
	 * gets the light level at the supplied position
	 */
	private int getRawLight(int x, int y, int z)
	{
		BlockPos pos = pos(x, y, z);
		if (this.type == EnumSkyBlock.SKY && this.world.canSeeSky(pos))
			return 15;
		else
		{
			IBlockState iblockstate = this.world.getBlockState(pos);
			int blockLight = iblockstate.getBlock().getLightValue(iblockstate, this.world, pos);
			int i = this.type == EnumSkyBlock.SKY ? 0 : blockLight;
			int j = iblockstate.getBlock().getLightOpacity(iblockstate, this.world, pos);
			if (j >= 15 && blockLight > 0)
			{
				j = 1;
			}
			
			if (j < 1)
			{
				j = 1;
			}
			if (j >= 15)
				return 0;
			else if (i >= 14)
				return i;
			else
			{
				PooledMutableBlockPos pos2 = PooledMutableBlockPos.retain();
				for (EnumFacing enumfacing : EnumFacing.values())
				{
					pos2.setPos(pos).move(enumfacing);
					int k = getLight(pos2) - j;
					
					if (k > i)
					{
						i = k;
					}
					
					if (i >= 14)
						return i;
				}
				pos2.release();
				return i;
			}
		}
	}
	
	private void setLight(int ofX, int ofY, int ofZ, int value)
	{
		this.array.set(2, this.x + ofX).set(3, this.y + ofY).set(4, this.z + ofZ);
		synchronized (this.lightMap)
		{
			this.lightMap.put(this.array.copy(), (byte) value);
		}
	}
	
	private int getLight(int ofX, int ofY, int ofZ)
	{
		this.array.set(2, this.x + ofX).set(3, this.y + ofY).set(4, this.z + ofZ);
		if(this.lightMap.containsKey(this.array))
			return this.lightMap.get(this.array).byteValue();
		this.pos.setPos(this.x + ofX, this.y + ofY, this.z + ofZ);
		return this.world.getLightFor(this.type, this.pos);
	}
	
	private int getLight(BlockPos pos)
	{
		this.array.set(2, pos.getX()).set(3, pos.getY()).set(4, pos.getZ());
		if(this.lightMap.containsKey(this.array))
			return this.lightMap.get(this.array).byteValue();
		return this.world.getLightFor(this.type, pos);
	}
	
	private BlockPos pos(int ofX, int ofY, int ofZ)
	{
		return this.pos.setPos(this.x + ofX, this.y + ofY, this.z + ofZ);
	}
	
	
	public void tickLightUpdate(World world)
	{
		int dim = world.provider.getDimension();
		PooledMutableBlockPos pos = PooledMutableBlockPos.retain();
		synchronized (this.lightMap)
		{
			Set<IntegerArray> set = ImmutableSet.copyOf(this.lightMap.keySet());
			for(IntegerArray array : set)
			{
				if(array.array[1] == dim)
				{
					pos.setPos(array.array[2], array.array[3], array.array[4]);
					if(world.isAreaLoaded(pos, 1))
					{
						world.setLightFor(EnumSkyBlock.values()[array.array[0]], pos, this.lightMap.get(array));
					}
					this.lightMap.remove(array);
				}
			}
		}
		pos.release();
	}
}