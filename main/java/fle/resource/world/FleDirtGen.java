package fle.resource.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public abstract class FleDirtGen extends WorldGenerator
{
    protected Block target;
    private int numberOfBlocks;

    public FleDirtGen(Block aBlock, int num)
    {
        target = aBlock;
        numberOfBlocks = num;
    }

    public boolean generate(World aWorld, Random aRand, int x, int y, int z)
    {
        if (checkEnvironmentBlockAndMeta(aWorld.getBlock(x, y, z), aWorld.getBlockMetadata(x, y, z)))
        {
            return false;
        }
        else
        {
            int l = aRand.nextInt(numberOfBlocks - 2) + 2;
            byte b0 = 1;

            for (int i1 = x - l; i1 <= x + l; ++i1)
            {
                for (int j1 = z - l; j1 <= z + l; ++j1)
                {
                    int k1 = i1 - x;
                    int l1 = j1 - z;

                    if (k1 * k1 + l1 * l1 <= l * l)
                    {
                        for (int i2 = y - b0; i2 <= y + b0; ++i2)
                        {
                            if (checkTargetBlockAndMeta(aWorld.getBlock(i1, i2, j1), aWorld.getBlockMetadata(i1, i2, j1)))
                            {
                            	generateBlockAt(aWorld, i1, i2, j1);
                                aWorld.setBlock(i1, i2, j1, target, 0, 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
    
    protected abstract boolean checkEnvironmentBlockAndMeta(Block block, int metadata);
    
    protected abstract boolean checkTargetBlockAndMeta(Block block, int metadata);
    
    protected abstract void generateBlockAt(World aWorld, int x, int y, int z);
}