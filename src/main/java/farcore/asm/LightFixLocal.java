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
			x = pos.getX();
			y = pos.getY();
			z = pos.getZ();
			time = System.currentTimeMillis();
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
			while(!list.isEmpty())
			{
				synchronized (list)
				{
					checking = list.removeFirst();
				}
				if(time - checking.time >= 10000)
				{
					continue;
				}
				boolean flag = true;
				try
				{
					check0(checking);
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
		synchronized (list)
		{
			Iterator<$> iterator = list.iterator();
			while(iterator.hasNext())
			{
				if(iterator.next().world == world)
				{
					iterator.remove();
				}
			}
		}
		synchronized (lightMap)
		{
			Set<IntegerArray> set = ImmutableSet.copyOf(lightMap.keySet());
			for(IntegerArray array : set)
			{
				if(array.array[1] == i)
				{
					lightMap.remove(array);
				}
			}
		}
	}

	public void markLightForCheck(World world, EnumSkyBlock type, BlockPos pos)
	{
		synchronized (list)
		{
			$ info = new $(world, type, pos);
			list.add(info);
			synchronized (this)
			{
				notifyAll();
			}
		}
	}
	
	private void check0($ check)
	{
		world = check.world;
		type = check.type;
		x = check.x;
		y = check.y;
		z = check.z;
		array.array[0] = type.ordinal();
		array.array[1] = world.provider.getDimension();
		check();
	}
	
	private void check()
	{
		int i = 0;
		int j = 0;
		int k = getLight(0, 0, 0);
		int l = getRawLight(0, 0, 0);
		int i1 = pos.getX();
		int j1 = pos.getY();
		int k1 = pos.getZ();

		if (l > k)
		{
			lightUpdateBlockList[j++] = 133152;
		}
		else if (l < k)
		{
			lightUpdateBlockList[j++] = 133152 | k << 18;

			while (i < j)
			{
				int l1 = lightUpdateBlockList[i++];
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
								int l4 = Math.max(1, (state = world.getBlockState(pos3)).getBlock().getLightOpacity(state, world, pos3));
								i3 = getLight(i4, j4, k4);
								if (i3 == l2 - l4 && j < lightUpdateBlockList.length)
								{
									lightUpdateBlockList[j++] = i4 - i1 + 32 | j4 - j1 + 32 << 6 | k4 - k1 + 32 << 12 | l2 - l4 << 18;
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
			int i5 = lightUpdateBlockList[i++];
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
					boolean flag = j < lightUpdateBlockList.length - 6;
					if (k6 + l6 + i7 < 17 && flag)
					{
						if (getLight(blockpos1.west()) < j6)
						{
							lightUpdateBlockList[j++] = j5 - 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.east()) < j6)
						{
							lightUpdateBlockList[j++] = j5 + 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.down()) < j6)
						{
							lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.up()) < j6)
						{
							lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 + 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.north()) < j6)
						{
							lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - 1 - k1 + 32 << 12);
						}
						if (getLight(blockpos1.south()) < j6)
						{
							lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 + 1 - k1 + 32 << 12);
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
		if (type == EnumSkyBlock.SKY && world.canSeeSky(pos))
			return 15;
		else
		{
			IBlockState iblockstate = world.getBlockState(pos);
			int blockLight = iblockstate.getBlock().getLightValue(iblockstate, world, pos);
			int i = type == EnumSkyBlock.SKY ? 0 : blockLight;
			int j = iblockstate.getBlock().getLightOpacity(iblockstate, world, pos);
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
		array.set(2, x + ofX).set(3, y + ofY).set(4, z + ofZ);
		synchronized (lightMap)
		{
			lightMap.put(array.copy(), (byte) value);
		}
	}

	private int getLight(int ofX, int ofY, int ofZ)
	{
		array.set(2, x + ofX).set(3, y + ofY).set(4, z + ofZ);
		if(lightMap.containsKey(array))
			return lightMap.get(array).byteValue();
		pos.setPos(x + ofX, y + ofY, z + ofZ);
		return world.getLightFor(type, pos);
	}

	private int getLight(BlockPos pos)
	{
		array.set(2, pos.getX()).set(3, pos.getY()).set(4, pos.getZ());
		if(lightMap.containsKey(array))
			return lightMap.get(array).byteValue();
		return world.getLightFor(type, pos);
	}

	private BlockPos pos(int ofX, int ofY, int ofZ)
	{
		return pos.setPos(x + ofX, y + ofY, z + ofZ);
	}

	
	public void tickLightUpdate(World world)
	{
		int dim = world.provider.getDimension();
		PooledMutableBlockPos pos = PooledMutableBlockPos.retain();
		synchronized (lightMap)
		{
			Set<IntegerArray> set = ImmutableSet.copyOf(lightMap.keySet());
			for(IntegerArray array : set)
			{
				if(array.array[1] == dim)
				{
					pos.setPos(array.array[2], array.array[3], array.array[4]);
					if(world.isAreaLoaded(pos, 1))
					{
						world.setLightFor(EnumSkyBlock.values()[array.array[0]], pos, lightMap.get(array));
					}
					lightMap.remove(array);
				}
			}
		}
		pos.release();
	}
}