package farcore.lib.world.gen.tree;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TreeGenJungle extends TreeGenLarge
{
	public TreeGenJungle(int base, int rand)
	{
		super(base, rand);
	}

	@Override
	public boolean generate(World world, Random random, int x, int y, int z, boolean isNatural)
	{
        int l = this.func_150533_a(random);

        if (!this.func_150537_a(world, random, x, y, z, l))
        {
            return false;
        }
        else
        {
            this.func_150543_c(world, x, z, y + l, 2, random);

            for (int i1 = y + l - 2 - random.nextInt(4); i1 > y + l / 2; i1 -= 2 + random.nextInt(4))
            {
                float f = random.nextFloat() * (float)Math.PI * 2.0F;
                int j1 = x + (int)(0.5F + MathHelper.cos(f) * 4.0F);
                int k1 = z + (int)(0.5F + MathHelper.sin(f) * 4.0F);
                int l1;

                for (l1 = 0; l1 < 5; ++l1)
                {
                    j1 = x + (int)(1.5F + MathHelper.cos(f) * (float)l1);
                    k1 = z + (int)(1.5F + MathHelper.sin(f) * (float)l1);
                    this.setBlockAndNotifyAdequately(world, j1, i1 - 3 + l1 / 2, k1, log, 0);
                }

                l1 = 1 + random.nextInt(2);
                int i2 = i1;

                for (int j2 = i1 - l1; j2 <= i2; ++j2)
                {
                    int k2 = j2 - i2;
                    this.func_150534_b(world, j1, j2, k1, 1 - k2, random);
                }
            }

            for (int l2 = 0; l2 < l; ++l2)
            {
                Block block = world.getBlock(x, y + l2, z);

                if (block.isAir(world, x, y + l2, z) || block.isLeaves(world, x, y + l2, z))
                {
                    this.setBlockAndNotifyAdequately(world, x, y + l2, z, log, 0);

                    if (isNatural && l2 > 0)
                    {
                        if (random.nextInt(3) > 0 && world.isAirBlock(x - 1, y + l2, z))
                        {
                            this.setBlockAndNotifyAdequately(world, x - 1, y + l2, z, Blocks.vine, 8);
                        }

                        if (random.nextInt(3) > 0 && world.isAirBlock(x, y + l2, z - 1))
                        {
                            this.setBlockAndNotifyAdequately(world, x, y + l2, z - 1, Blocks.vine, 1);
                        }
                    }
                }

                if (l2 < l - 1)
                {
                    block = world.getBlock(x + 1, y + l2, z);

                    if (block.isAir(world, x + 1, y + l2, z) || block.isLeaves(world, x + 1, y + l2, z))
                    {
                        this.setBlockAndNotifyAdequately(world, x + 1, y + l2, z, log, 0);

                        if (isNatural && l2 > 0)
                        {
                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 2, y + l2, z))
                            {
                                this.setBlockAndNotifyAdequately(world, x + 2, y + l2, z, Blocks.vine, 2);
                            }

                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 1, y + l2, z - 1))
                            {
                                this.setBlockAndNotifyAdequately(world, x + 1, y + l2, z - 1, Blocks.vine, 1);
                            }
                        }
                    }

                    block = world.getBlock(x + 1, y + l2, z + 1);

                    if (block.isAir(world, x + 1, y + l2, z + 1) || block.isLeaves(world, x + 1, y + l2, z + 1))
                    {
                        this.setBlockAndNotifyAdequately(world, x + 1, y + l2, z + 1, log, 0);

                        if (isNatural && l2 > 0)
                        {
                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 2, y + l2, z + 1))
                            {
                                this.setBlockAndNotifyAdequately(world, x + 2, y + l2, z + 1, Blocks.vine, 2);
                            }

                            if (random.nextInt(3) > 0 && world.isAirBlock(x + 1, y + l2, z + 2))
                            {
                                this.setBlockAndNotifyAdequately(world, x + 1, y + l2, z + 2, Blocks.vine, 4);
                            }
                        }
                    }

                    block = world.getBlock(x, y + l2, z + 1);

                    if (block.isAir(world, x, y + l2, z + 1) || block.isLeaves(world, x, y + l2, z + 1))
                    {
                        this.setBlockAndNotifyAdequately(world, x, y + l2, z + 1, log, 0);

                        if (isNatural && l2 > 0)
                        {
                            if (random.nextInt(3) > 0 && world.isAirBlock(x - 1, y + l2, z + 1))
                            {
                                this.setBlockAndNotifyAdequately(world, x - 1, y + l2, z + 1, Blocks.vine, 8);
                            }

                            if (random.nextInt(3) > 0 && world.isAirBlock(x, y + l2, z + 2))
                            {
                                this.setBlockAndNotifyAdequately(world, x, y + l2, z + 2, Blocks.vine, 4);
                            }
                        }
                    }
                }
            }

            return true;
        }
	}
	
    private void func_150543_c(World world, int x, int z, int y, int scale, Random random)
    {
        byte b0 = 2;

        for (int i1 = y - b0; i1 <= y; ++i1)
        {
            int j1 = i1 - y;
            this.func_150535_a(world, x, i1, z, scale + 1 - j1, random);
        }
    }
}