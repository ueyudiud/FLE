package farcore.asm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import farcore.data.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightFix implements Runnable
{
	private static class Infos
	{
		World world;
		EnumSkyBlock type;
		BlockPos pos;
		int[] result;
		int off;
		
		Infos(World world, EnumSkyBlock type, BlockPos pos)
		{
			this.world = world;
			this.type = type;
			this.pos = pos;
		}
		
		int[] result()
		{
			if(result == null)
			{
				synchronized (caches)
				{
					if(!caches.isEmpty())
					{
						result = caches.remove(caches.size() - 1);
					}
				}
				if(result == null)
				{
					result = new int[32768];
				}
			}
			return result;
		}
		
		void release()
		{
			if(result != null)
			{
				synchronized (caches)
				{
					if(caches.size() < 4)
					{
						caches.add(result);
					}
				}
				result = null;
			}
		}
	}

	private static final int[] calculateCache = new int[32768];
	private static final LightFix instance = new LightFix();
	private static final List<int[]> caches = new ArrayList();
	
	public static void startThread()
	{
		new Thread(instance, "Far Light Thread").start();
	}

	@SideOnly(Side.CLIENT)
	public static String getOverlayInfo()
	{
		return String.format("Lu %d Lc $d", instance.listUncalculate.size(), instance.listCalculated.size());
	}
	
	public static void tickLightUpdate(World world)
	{
		if(world.isRemote) return;
		world.theProfiler.startSection("far core light update");
		synchronized (instance.listCalculated)
		{
			if(!instance.listCalculated.isEmpty())
			{
				for(Infos infos : instance.listCalculated)
				{
					if(infos.result != null && world.isAreaLoaded(infos.pos, 17))
					{
						int x = infos.pos.getX();
						int y = infos.pos.getY();
						int z = infos.pos.getZ();
						BlockPos.PooledMutableBlockPos pos = BlockPos.PooledMutableBlockPos.retain();
						for(int i = 0; i < infos.off; ++i)
						{
							int v = infos.result[i];
							pos.setPos(x + ((v >> 18) & 0x3F) - 32, y + ((v >> 12) & 0x3F) - 32, z + ((v >> 6) & 0x3F) - 32);
							world.setLightFor(infos.type, pos, v & 0xF);
						}
					}
					infos.release();
				}
				instance.listCalculated.clear();
			}
		}
		world.theProfiler.endSection();
	}

	public static boolean checkLightFor(World world, EnumSkyBlock type, BlockPos pos)
	{
		//Should this method just share the calculation of light with server?
		if(Config.multiThreadLight && !world.isRemote)
		{
			if (!world.isAreaLoaded(pos, 17, false))
				return false;
			else
			{
				markLightForCheck(world, type, pos);
				return true;
			}
		}
		else return world.checkLightFor(type, pos);
	}

	public static void onWorldUnload(World world)
	{
		if(world.isRemote) return;
		synchronized (instance.listUncalculate)
		{
			Iterator<Infos> itr = instance.listUncalculate.iterator();
			while(itr.hasNext())
			{
				if(itr.next().world == world)
				{
					itr.remove();
				}
			}
		}
		synchronized (instance.listCalculated)
		{
			Iterator<Infos> itr = instance.listCalculated.iterator();
			while(itr.hasNext())
			{
				if(itr.next().world == world)
				{
					itr.remove();
				}
			}
		}
	}

	private LinkedList<Infos> listUncalculate = new LinkedList();
	private List<Infos> listCalculated = new ArrayList();

	private Infos checking;
	private boolean isCalculating = false;

	private static void markLightForCheck(World world, EnumSkyBlock type, BlockPos pos)
	{
		synchronized (instance)
		{
			synchronized (instance.listUncalculate)
			{
				instance.listUncalculate.addLast(new Infos(world, type, pos));
			}
			instance.notify();
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
				catch(InterruptedException exception)
				{
					;
				}
			}
			while(!listUncalculate.isEmpty())
			{
				synchronized (listUncalculate)
				{
					checking = listUncalculate.removeFirst();
				}
				checkLightFor(checking.world, checking.type, checking.pos, calculateCache);
				synchronized (listCalculated)
				{
					listCalculated.add(checking);
				}
				checking = null;
			}
		}
	}

	public void checkLightFor(World world, EnumSkyBlock lightType, BlockPos pos, int[] lightUpdateBlockList)
	{
		int i = 0;
		int j = 0;
		int k = world.getLightFor(lightType, pos);
		int l = getRawLight(world, pos, lightType);
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
				BlockPos pos3 = new BlockPos(i2 + i1, j2 + j1, k2 + k1);
				int i3 = world.getLightFor(lightType, pos3);
				if (i3 == l2)
				{
					setLightFor(i2, j2, k2, 0);
					if (l2 > 0)
					{
						byte j3 = (byte) Math.abs(i2);
						byte k3 = (byte) Math.abs(j2 - j1);
						byte l3 = (byte) Math.abs(k2 - k1);

						if (j3 + k3 + l3 < 17)
						{
							BlockPos.PooledMutableBlockPos pos2 = BlockPos.PooledMutableBlockPos.retain();
							for (EnumFacing enumfacing : EnumFacing.values())
							{
								int i4 = i2 + i1 + enumfacing.getFrontOffsetX();
								int j4 = j2 + j1 + enumfacing.getFrontOffsetY();
								int k4 = k2 + k1 + enumfacing.getFrontOffsetZ();
								pos2.setPos(i4, j4, k4);
								IBlockState state;
								int l4 = Math.max(1, (state = world.getBlockState(pos2)).getBlock().getLightOpacity(state, world, pos2));
								i3 = world.getLightFor(lightType, pos2);
								if (i3 == l2 - l4 && j < lightUpdateBlockList.length)
								{
									lightUpdateBlockList[j++] = i4 - i1 + 32 | j4 - j1 + 32 << 6 | k4 - k1 + 32 << 12 | l2 - l4 << 18;
								}
							}
							pos2.release();
						}
					}
				}
			}
			i = 0;
		}
		while (i < j)
		{
			int i5 = lightUpdateBlockList[i++];
			byte j5 = (byte) ((i5 & 63) - 32);
			byte k5 = (byte) ((i5 >> 6 & 63) - 32);
			byte l5 = (byte) ((i5 >> 12 & 63) - 32);
			BlockPos blockpos1 = new BlockPos(j5 + i1, k5 + j1, l5 + k1);
			int i6 = world.getLightFor(lightType, blockpos1);
			int j6 = getRawLight(world, blockpos1, lightType);
			if (j6 != i6)
			{
				setLightFor(j5, k5, l5, j6);
				if (j6 > i6)
				{
					int k6 = Math.abs(j5);
					int l6 = Math.abs(k5);
					int i7 = Math.abs(l5);
					boolean flag = j < lightUpdateBlockList.length - 6;
					if (k6 + l6 + i7 < 17 && flag)
					{
						if (world.getLightFor(lightType, blockpos1.west()) < j6)
						{
							lightUpdateBlockList[j++] = j5 - 1 + 32 + (k5 + 32 << 6) + (l5 + 32 << 12);
						}
						if (world.getLightFor(lightType, blockpos1.east()) < j6)
						{
							lightUpdateBlockList[j++] = j5 + 1 + 32 + (k5 + 32 << 6) + (l5 + 32 << 12);
						}
						if (world.getLightFor(lightType, blockpos1.down()) < j6)
						{
							lightUpdateBlockList[j++] = j5 + 32 + (k5 - 1 + 32 << 6) + (l5 + 32 << 12);
						}
						if (world.getLightFor(lightType, blockpos1.up()) < j6)
						{
							lightUpdateBlockList[j++] = j5 + 32 + (k5 + 1 + 32 << 6) + (l5 + 32 << 12);
						}
						if (world.getLightFor(lightType, blockpos1.north()) < j6)
						{
							lightUpdateBlockList[j++] = j5 + 32 + (k5 + 32 << 6) + (l5 - 1 + 32 << 12);
						}
						if (world.getLightFor(lightType, blockpos1.south()) < j6)
						{
							lightUpdateBlockList[j++] = j5 + 32 + (k5 + 32 << 6) + (l5 + 1 + 32 << 12);
						}
					}
				}
			}
		}
	}

	private void setLightFor(int offX, int offY, int offZ, int value)
	{
		if(checking == null) throw new RuntimeException("Not checking light!");
		checking.result()[checking.off++] = (offX + 32 << 18 | offY + 32 << 12 | offZ + 32 << 6 | value);
	}

	private int getRawLight(World world, BlockPos pos, EnumSkyBlock lightType)
	{
		if (lightType == EnumSkyBlock.SKY && world.canSeeSky(pos))
			return 15;
		else
		{
			IBlockState state = world.getBlockState(pos);
			int blockLight = state.getBlock().getLightValue(state, world, pos);
			int i = lightType == EnumSkyBlock.SKY ? 0 : blockLight;
			int j = state.getBlock().getLightOpacity(state, world, pos);
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
				BlockPos.PooledMutableBlockPos pos2 = BlockPos.PooledMutableBlockPos.retain();
				for (EnumFacing enumfacing : EnumFacing.values())
				{
					pos2.setPos(pos).move(enumfacing);
					int k = world.getLightFor(lightType, pos2) - j;
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
	
}