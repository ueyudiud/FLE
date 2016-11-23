package farcore.lib.tree.instance;

import java.util.Random;

import farcore.lib.tree.TreeBase;
import farcore.lib.tree.TreeInfo;
import farcore.util.L;
import net.minecraft.block.Block;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TreeGenJungle extends TreeGenHuge
{
	public TreeGenJungle(TreeBase tree, float generateCoreLeavesChance)
	{
		super(tree, generateCoreLeavesChance);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		int l = baseHeight + L.nextInt(randHeight, random);
		
		if (!matchHugeTreeGrow(world, random, x, y, z, l))
			return false;
		else
		{
			growLeaves(world, x, z, y + l, 2, random, info);
			
			for (int i1 = y + l - 2 - random.nextInt(4); i1 > y + l / 2; i1 -= 2 + random.nextInt(4))
			{
				float f = random.nextFloat() * (float)Math.PI * 2.0F;
				int j1 = x + (int)(0.5F + MathHelper.cos(f) * 4.0F);
				int k1 = z + (int)(0.5F + MathHelper.sin(f) * 4.0F);
				int l1;
				
				for (l1 = 0; l1 < 5; ++l1)
				{
					j1 = x + (int)(1.5F + MathHelper.cos(f) * l1);
					k1 = z + (int)(1.5F + MathHelper.sin(f) * l1);
					generateLog(world, j1, i1 - 3 + l1 / 2, k1, 1);
				}
				
				l1 = 1 + random.nextInt(2);
				int i2 = i1;
				
				for (int j2 = i1 - l1; j2 <= i2; ++j2)
				{
					int k2 = j2 - i2;
					generateCloudlyLeaves(world, j1, j2, k1, 1 - k2, 0, random, info, (byte) 1);
				}
			}
			
			for (int l2 = 0; l2 < l; ++l2)
			{
				if (isLogReplaceable(world, x, y + l2, z))
				{
					generateLog(world, x, y + l2, z, 1);
				}
				//                    if (l2 > 0)
				//                    {
				//                        if (random.nextInt(3) > 0 && world.isAirBlock(x - 1, y + l2, z))
				//                        {
				//                            this.setBlockAndNotifyAdequately(world, x - 1, y + l2, z, Blocks.vine, 8);
				//                        }
				//
				//                        if (random.nextInt(3) > 0 && world.isAirBlock(x, y + l2, z - 1))
				//                        {
				//                            this.setBlockAndNotifyAdequately(world, x, y + l2, z - 1, Blocks.vine, 1);
				//                        }
				//                    }
				Block block;
				
				if (l2 < l - 1)
				{
					if (isLogReplaceable(world, x + 1, y + l2, z))
					{
						generateLog(world, x + 1, y + l2, z, 1);
						//                        if (l2 > 0)
						//                        {
						//                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 2, y + l2, z))
						//                            {
						//                                this.setBlockAndNotifyAdequately(world, x + 2, y + l2, z, Blocks.vine, 2);
						//                            }
						//
						//                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 1, y + l2, z - 1))
						//                            {
						//                                this.setBlockAndNotifyAdequately(world, x + 1, y + l2, z - 1, Blocks.vine, 1);
						//                            }
						//                        }
					}
					
					if (isLogReplaceable(world, x + 1, y + l2, z + 1))
					{
						generateLog(world, x + 1, y + l2, z + 1, 1);
						//                        if (l2 > 0)
						//                        {
						//                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 2, y + l2, z + 1))
						//                            {
						//                                this.setBlockAndNotifyAdequately(world, x + 2, y + l2, z + 1, Blocks.vine, 2);
						//                            }
						//
						//                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 1, y + l2, z + 2))
						//                            {
						//                                this.setBlockAndNotifyAdequately(world, x + 1, y + l2, z + 2, Blocks.vine, 4);
						//                            }
						//                        }
					}
					
					if (isLogReplaceable(world, x, y + l2, z + 1))
					{
						generateLog(world, x, y + l2, z + 1, 1);
						//                        if (l2 > 0)
						//                        {
						//                            if (random.nextInt(3) > 0 && p_76484_1_.isAirBlock(x - 1, y + l2, z + 1))
						//                            {
						//                                this.setBlockAndNotifyAdequately(p_76484_1_, x - 1, y + l2, z + 1, Blocks.vine, 8);
						//                            }
						//
						//                            if (random.nextInt(3) > 0 && p_76484_1_.isAirBlock(p_76484_3_, y + l2, z + 2))
						//                            {
						//                                this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_, y + l2, z + 2, Blocks.vine, 4);
						//                            }
						//                        }
					}
				}
			}
			return true;
		}
	}
	
	private void growLeaves(World world, int x, int z, int y, int size, Random rand, TreeInfo info)
	{
		byte b0 = 2;
		
		for (int i1 = y - b0; i1 <= y; ++i1)
		{
			int j1 = i1 - y;
			generateCloudlyLeaves(world, x, i1, z, size + 1 - j1, 0, rand, info, (byte) 2);
		}
	}
}