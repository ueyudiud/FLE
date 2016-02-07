package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.core.world.dim.FLEBiomeDecoratorBase;

public class FLEBiomeSwamp extends FLEBiome
{
    public FLEBiomeSwamp(String name, int index)
    {
    	super(name, index);
        theBiomeDecorator.treesPerChunk = 1;
        theBiomeDecorator.flowersPerChunk = 1;
        theBiomeDecorator.deadBushPerChunk = 1;
        theBiomeDecorator.mushroomsPerChunk = 8;
        theBiomeDecorator.reedsPerChunk = 10;
        theBiomeDecorator.clayPerChunk = 1;
        theBiomeDecorator.waterlilyPerChunk = 4;
        theBiomeDecorator.sandPerChunk2 = 0;
        theBiomeDecorator.sandPerChunk = 0;
        theBiomeDecorator.grassPerChunk = 6;
        ((FLEBiomeDecoratorBase) theBiomeDecorator).peatPerChunk = 2;
        waterColorMultiplier = 14745518;
        spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntitySlime.class, 1, 1, 1));
        flowers.clear();
        addFlower(Blocks.red_flower, 1, 10);
        func_76733_a(9154376);
        topBlock = Blocks.grass;
        fillerBlock = Blocks.dirt;
    }

    public WorldGenAbstractTree func_150567_a(Random rand)
    {
        return this.worldGeneratorSwamp;
    }

    public String func_150572_a(Random rand, int x, int z, int p_150572_4_)
    {
        return BlockFlower.field_149859_a[1];
    }

    @Override
    public void genTerrainBlocks(World aWorld, Random aRand, Block[] aBlocks,
			byte[] aByte, int x, int z, double yLevel)
    {
        double d1 = plantNoise.func_151601_a((double)x * 0.25D, (double)z * 0.25D);

        if (d1 > 0.0D)
        {
            int k = x & 15;
            int l = z & 15;
            int i1 = aBlocks.length / 256;

            for (int j1 = 255; j1 >= 0; --j1)
            {
                int k1 = (l * 16 + k) * i1 + j1;

                if (aBlocks[k1] == null || aBlocks[k1].getMaterial() != Material.air)
                {
                    if (j1 == 62 && aBlocks[k1] != Blocks.water)
                    {
                        aBlocks[k1] = Blocks.water;

                        if (d1 < 0.12D)
                        {
                            aBlocks[k1 + 1] = Blocks.waterlily;
                        }
                    }

                    break;
                }
            }
        }
		super.genTerrainBlocks(aWorld, aRand, aBlocks, aByte, x, z, yLevel);
	}

    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int x, int y, int z)
    {
        double d0 = plantNoise.func_151601_a((double)x * 0.0225D, (double)z * 0.0225D);
        return d0 < -0.1D ? 5011004 : 6975545;
    }

    /**
     * Provides the basic foliage color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int x, int y, int z)
    {
        return 6975545;
    }
}