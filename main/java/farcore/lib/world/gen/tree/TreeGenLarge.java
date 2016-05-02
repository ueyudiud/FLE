package farcore.lib.world.gen.tree;

import java.util.Random;

import fle.core.world.biome.BiomeJungle;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class TreeGenLarge extends TreeGenAbstract
{
    /** The base height of the tree */
    protected final int baseHeight;
    protected final int randHeight;
    
	public TreeGenLarge(int base, int rand)
	{
		this.baseHeight = base;
		this.randHeight = rand;
	}

    protected int func_150533_a(Random rand)
    {
        int i = rand.nextInt(3) + this.baseHeight;

        if (this.randHeight > 1)
        {
            i += rand.nextInt(this.randHeight);
        }

        return i;
    }

    private boolean func_150536_b(World world, Random random, int x, int y, int z, int p_150536_6_)
    {
        boolean flag = true;

        if (y >= 1 && y + p_150536_6_ + 1 <= 256)
        {
            for (int i1 = y; i1 <= y + 1 + p_150536_6_; ++i1)
            {
                byte b0 = 2;

                if (i1 == y)
                {
                    b0 = 1;
                }

                if (i1 >= y + 1 + p_150536_6_ - 2)
                {
                    b0 = 2;
                }

                for (int j1 = x - b0; j1 <= x + b0 && flag; ++j1)
                {
                    for (int k1 = z - b0; k1 <= z + b0 && flag; ++k1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            Block block = world.getBlock(j1, i1, k1);

                            if (!this.isReplaceable(world, j1, i1, k1))
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

            return flag;
        }
        else
        {
            return false;
        }
    }

    private boolean func_150532_c(World world, Random random, int x, int y, int z)
    {
        Block block = world.getBlock(x, y - 1, z);

        boolean isSoil = block.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling)Blocks.sapling);
        if (isSoil && y >= 2)
        {
            onPlantGrow(world, x,     y - 1, z,     x, y, z);
            onPlantGrow(world, x + 1, y - 1, z,     x, y, z);
            onPlantGrow(world, x,     y - 1, z + 1, x, y, z);
            onPlantGrow(world, x + 1, y - 1, z + 1, x, y, z);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean func_150537_a(World world, Random random, int p_150537_3_, int p_150537_4_, int p_150537_5_, int p_150537_6_)
    {
        return this.func_150536_b(world, random, p_150537_3_, p_150537_4_, p_150537_5_, p_150537_6_) && this.func_150532_c(world, random, p_150537_3_, p_150537_4_, p_150537_5_);
    }

    protected void func_150535_a(World world, int x, int y, int z, int p_150535_5_, Random p_150535_6_)
    {
        int i1 = p_150535_5_ * p_150535_5_;

        for (int j1 = x - p_150535_5_; j1 <= x + p_150535_5_ + 1; ++j1)
        {
            int k1 = j1 - x;

            for (int l1 = z - p_150535_5_; l1 <= z + p_150535_5_ + 1; ++l1)
            {
                int i2 = l1 - z;
                int j2 = k1 - 1;
                int k2 = i2 - 1;

                if (k1 * k1 + i2 * i2 <= i1 || j2 * j2 + k2 * k2 <= i1 || k1 * k1 + k2 * k2 <= i1 || j2 * j2 + i2 * i2 <= i1)
                {
                    Block block = world.getBlock(j1, y, l1);

                    if (block.isAir(world, j1, y, l1) || block.isLeaves(world, j1, y, l1))
                    {
                        this.setBlockAndNotifyAdequately(world, j1, y, l1, leaves, 0);
                    }
                }
            }
        }
    }

    protected void func_150534_b(World p_150534_1_, int p_150534_2_, int p_150534_3_, int p_150534_4_, int p_150534_5_, Random p_150534_6_)
    {
        int i1 = p_150534_5_ * p_150534_5_;

        for (int j1 = p_150534_2_ - p_150534_5_; j1 <= p_150534_2_ + p_150534_5_; ++j1)
        {
            int k1 = j1 - p_150534_2_;

            for (int l1 = p_150534_4_ - p_150534_5_; l1 <= p_150534_4_ + p_150534_5_; ++l1)
            {
                int i2 = l1 - p_150534_4_;

                if (k1 * k1 + i2 * i2 <= i1)
                {
                    Block block = p_150534_1_.getBlock(j1, p_150534_3_, l1);

                    if (block.isAir(p_150534_1_, j1, p_150534_3_, l1) || block.isLeaves(p_150534_1_, j1, p_150534_3_, l1))
                    {
                        this.setBlockAndNotifyAdequately(p_150534_1_, j1, p_150534_3_, l1, leaves, 0);
                    }
                }
            }
        }
    }

    //Just a helper macro
    private void onPlantGrow(World world, int x, int y, int z, int sourceX, int sourceY, int sourceZ)
    {
        world.getBlock(x, y, z).onPlantGrow(world, x, y, z, sourceX, sourceY, sourceZ);
    }
}