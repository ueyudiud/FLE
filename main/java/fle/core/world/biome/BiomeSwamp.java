package fle.core.world.biome;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.world.biome.BiomeBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeSwamp extends BiomeBase
{
	public BiomeSwamp(int id)
	{
		super(id);
        this.theBiomeDecorator.treesPerChunk = 2;
        this.theBiomeDecorator.flowersPerChunk = 1;
        this.theBiomeDecorator.deadBushPerChunk = 1;
        this.theBiomeDecorator.mushroomsPerChunk = 8;
        this.theBiomeDecorator.reedsPerChunk = 10;
        this.theBiomeDecorator.clayPerChunk = 1;
        this.theBiomeDecorator.waterlilyPerChunk = 4;
        this.theBiomeDecorator.sandPerChunk2 = 0;
        this.theBiomeDecorator.sandPerChunk = 0;
        this.theBiomeDecorator.grassPerChunk = 5;
        this.waterColorMultiplier = 14745518;
        this.spawnableMonsterList.add(new SpawnListEntry(EntitySlime.class, 1, 1, 1));
        this.flowers.clear();
        this.addFlower(Blocks.red_flower, 1, 10);
	}

    public WorldGenAbstractTree func_150567_a(Random random)
    {
        return this.worldGeneratorSwamp;
    }

    public String func_150572_a(Random random, int x, int y, int z)
    {
        return BlockFlower.field_149859_a[1];
    }

    public void genTerrainBlocks(World world, Random random, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        double d1 = plantNoise.func_151601_a((double)x * 0.25D, (double)z * 0.25D);

        if (d1 > 0.0D)
        {
            int k = x & 15;
            int l = z & 15;
            int i1 = blocks.length / 256;

            for (int j1 = 255; j1 >= 0; --j1)
            {
                int k1 = (l * 16 + k) * i1 + j1;

                if (blocks[k1] == null || blocks[k1].getMaterial() != Material.air)
                {
                    if (j1 == 62 && blocks[k1] != Blocks.water)
                    {
                        blocks[k1] = Blocks.water;

                        if (d1 < 0.12D)
                        {
                            blocks[k1 + 1] = Blocks.waterlily;
                        }
                    }

                    break;
                }
            }
        }
        super.genTerrainBlocks(world, random, blocks, metas, x, z, layer);
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