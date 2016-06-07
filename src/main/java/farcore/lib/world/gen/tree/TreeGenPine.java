package farcore.lib.world.gen.tree;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TreeGenPine extends TreeGenAbstract
{
	@Override
	public boolean generate(World world, Random random, int x, int y, int z, boolean isNatural)
	{
        int l = random.nextInt(5) + 7;
        int i1 = l - random.nextInt(2) - 3;
        int j1 = l - i1;
        int k1 = 1 + random.nextInt(j1 + 1);
        boolean flag = true;

        if (y >= 1 && y + l + 1 <= 256)
        {
            int i2;
            int j2;
            int i3;

            for (int l1 = y; l1 <= y + 1 + l && flag; ++l1)
            {
                boolean flag1 = true;

                if (l1 - y < i1)
                {
                    i3 = 0;
                }
                else
                {
                    i3 = k1;
                }

                for (i2 = x - i3; i2 <= x + i3 && flag; ++i2)
                {
                    for (j2 = z - i3; j2 <= z + i3 && flag; ++j2)
                    {
                        if (l1 >= 0 && l1 < 256)
                        {
                            Block block = world.getBlock(i2, l1, j2);

                            if (!this.isReplaceable(world, i2, l1, j2))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                Block block1 = world.getBlock(x, y - 1, z);

                boolean isSoil = block1.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
                if (isSoil && y < 256 - l - 1)
                {
                    block1.onPlantGrow(world, x, y - 1, z, x, y, z);
                    i3 = 0;

                    for (i2 = y + l; i2 >= y + i1; --i2)
                    {
                        for (j2 = x - i3; j2 <= x + i3; ++j2)
                        {
                            int j3 = j2 - x;

                            for (int k2 = z - i3; k2 <= z + i3; ++k2)
                            {
                                int l2 = k2 - z;

                                if ((Math.abs(j3) != i3 || Math.abs(l2) != i3 || i3 <= 0) && world.getBlock(j2, i2, k2).canBeReplacedByLeaves(world, j2, i2, k2))
                                {
                                    this.setBlockAndNotifyAdequately(world, j2, i2, k2, leaves, 0);
                                }
                            }
                        }

                        if (i3 >= 1 && i2 == y + i1 + 1)
                        {
                            --i3;
                        }
                        else if (i3 < k1)
                        {
                            ++i3;
                        }
                    }

                    for (i2 = 0; i2 < l - 1; ++i2)
                    {
                        Block block2 = world.getBlock(x, y + i2, z);

                        if (block2.isAir(world, x, y + i2, z) || block2.isLeaves(world, x, y + i2, z))
                        {
                            this.setBlockAndNotifyAdequately(world, x, y + i2, z, log, 0);
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
	}
}