package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenSavanna;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class BiomeSavanna extends BiomeBase
{
    private static final WorldGenSavannaTree savannaTree = new WorldGenSavannaTree(false);
    
	public BiomeSavanna(int id)
	{
		super(id);
        spawnableCreatureList.add(new SpawnListEntry(EntityHorse.class, 1, 2, 6));
        theBiomeDecorator.treesPerChunk = 1;
        theBiomeDecorator.flowersPerChunk = 4;
        theBiomeDecorator.grassPerChunk = 20;
	}

    public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
    {
        return (WorldGenAbstractTree)(p_150567_1_.nextInt(5) > 0 ? savannaTree : this.worldGeneratorTrees);
    }

    public void decorate(World world, Random random, int x, int z)
    {
        genTallFlowers.func_150548_a(2);

        for (int k = 0; k < 7; ++k)
        {
            int l = x + random.nextInt(16) + 8;
            int i1 = z + random.nextInt(16) + 8;
            int j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
            genTallFlowers.generate(world, random, l, j1, i1);
        }

        super.decorate(world, random, x, z);
    }
}