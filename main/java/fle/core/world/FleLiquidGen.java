package fle.core.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FleLiquidGen extends WorldGenerator
{
    protected Block liquid;
    protected Block repleaceTarget;
    protected Block decoration;
    protected Block repleaseTop;

    public FleLiquidGen(Block aLiquid)
    {
        this(Blocks.stone, aLiquid);
    }
    public FleLiquidGen(Block aTarget, Block aLiquid)
    {
        this(Blocks.stone, aLiquid, null, null);
    }
    public FleLiquidGen(Block aTarget, Block aLiquid, Block aDecoration, Block aRepleace)
    {
        liquid = aLiquid;
        repleaceTarget = aTarget;
        decoration = aDecoration;
        repleaseTop = aRepleace;
    }

    public boolean generate(World aWorld, Random aRand, int x, int y, int z)
    {
        x -= 8;

        for (z -= 8; y > 5 && aWorld.isAirBlock(x, y, z); --y);

        if (y <= 4)
        {
            return false;
        }
        else
        {
            y -= 4;
            boolean[] aboolean = new boolean[2048];
            int l = aRand.nextInt(4) + 4;
            int i1;

            for (i1 = 0; i1 < l; ++i1)
            {
                double d0 = aRand.nextDouble() * 6.0D + 3.0D;
                double d1 = aRand.nextDouble() * 4.0D + 2.0D;
                double d2 = aRand.nextDouble() * 6.0D + 3.0D;
                double d3 = aRand.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
                double d4 = aRand.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
                double d5 = aRand.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

                for (int k1 = 1; k1 < 15; ++k1)
                {
                    for (int l1 = 1; l1 < 15; ++l1)
                    {
                        for (int i2 = 1; i2 < 7; ++i2)
                        {
                            double d6 = ((double)k1 - d3) / (d0 / 2.0D);
                            double d7 = ((double)i2 - d4) / (d1 / 2.0D);
                            double d8 = ((double)l1 - d5) / (d2 / 2.0D);
                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

                            if (d9 < 1.0D)
                            {
                                aboolean[(k1 * 16 + l1) * 8 + i2] = true;
                            }
                        }
                    }
                }
            }

            int j1;
            int j2;
            boolean flag;

            for (i1 = 0; i1 < 16; ++i1)
            {
                for (j2 = 0; j2 < 16; ++j2)
                {
                    for (j1 = 0; j1 < 8; ++j1)
                    {
                        flag = !aboolean[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j1] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i1 * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j1 - 1)]);

                        if (flag)
                        {
                            Material material = aWorld.getBlock(x + i1, y + j1, z + j2).getMaterial();

                            if (j1 >= 4 && material.isLiquid())
                            {
                                return false;
                            }

                            if (j1 < 4 && !material.isSolid() && aWorld.getBlock(x + i1, y + j1, z + j2) != liquid)
                            {
                                return false;
                            }
                        }
                    }
                }
            }

            for (i1 = 0; i1 < 16; ++i1)
            {
                for (j2 = 0; j2 < 16; ++j2)
                {
                    for (j1 = 0; j1 < 8; ++j1)
                    {
                        if (aboolean[(i1 * 16 + j2) * 8 + j1])
                        {
                            aWorld.setBlock(x + i1, y + j1, z + j2, j1 >= 4 ? Blocks.air : liquid, 0, 2);
                        }
                    }
                }
            }            

            if (repleaceTarget != null)
            {
                for (i1 = 0; i1 < 16; ++i1)
                {
                    for (j2 = 0; j2 < 16; ++j2)
                    {
                        for (j1 = 0; j1 < 8; ++j1)
                        {
                            flag = !aboolean[(i1 * 16 + j2) * 8 + j1] && (i1 < 15 && aboolean[((i1 + 1) * 16 + j2) * 8 + j1] || i1 > 0 && aboolean[((i1 - 1) * 16 + j2) * 8 + j1] || j2 < 15 && aboolean[(i1 * 16 + j2 + 1) * 8 + j1] || j2 > 0 && aboolean[(i1 * 16 + (j2 - 1)) * 8 + j1] || j1 < 7 && aboolean[(i1 * 16 + j2) * 8 + j1 + 1] || j1 > 0 && aboolean[(i1 * 16 + j2) * 8 + (j1 - 1)]);

                            if (flag && (j1 < 4 || aRand.nextInt(2) != 0) && aWorld.getBlock(x + i1, y + j1, z + j2).getMaterial().isSolid())
                            {
                                aWorld.setBlock(x + i1, y + j1, z + j2, repleaceTarget, 0, 2);
                            }
                        }
                    }
                }
            }
            else
            {
            	for (i1 = 0; i1 < 16; ++i1)
                {
                    for (j2 = 0; j2 < 16; ++j2)
                    {
                        for (j1 = 4; j1 < 8; ++j1)
                        {
                            if (aboolean[(i1 * 16 + j2) * 8 + j1] && aWorld.getBlock(x + i1, y + j1 - 1, z + j2) == Blocks.dirt && aWorld.getSavedLightValue(EnumSkyBlock.Sky, x + i1, y + j1, z + j2) > 0)
                            {
                                BiomeGenBase biomegenbase = aWorld.getBiomeGenForCoords(x + i1, z + j2);

                                if (biomegenbase.topBlock == Blocks.mycelium)
                                {
                                    aWorld.setBlock(x + i1, y + j1 - 1, z + j2, Blocks.mycelium, 0, 2);
                                }
                                else if(biomegenbase.topBlock == Blocks.sand)
                                {
                                    aWorld.setBlock(x + i1, y + j1 - 1, z + j2, Blocks.sand, 0, 2);
                                }
                                else
                                {
                                    aWorld.setBlock(x + i1, y + j1 - 1, z + j2, Blocks.grass, 0, 2);
                                }
                            }
                        }
                    }
                }
            }

            if (repleaseTop != null)
            {
                for (i1 = 0; i1 < 16; ++i1)
                {
                    for (j2 = 0; j2 < 16; ++j2)
                    {
                        byte b0 = 4;

                        if (aWorld.isBlockFreezable(x + i1, y + b0, z + j2))
                        {
                            aWorld.setBlock(x + i1, y + b0, z + j2, repleaseTop, 0, 2);
                        }
                    }
                }
            }

            return true;
        }
    }
}