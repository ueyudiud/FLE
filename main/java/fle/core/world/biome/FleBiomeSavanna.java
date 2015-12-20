package fle.core.world.biome;

import java.util.Random;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;

public class FleBiomeSavanna extends FLEBiome
{
    private static final WorldGenSavannaTree treeGen = new WorldGenSavannaTree(false);
    
	public FleBiomeSavanna(String name, int index)
	{
		super(name, index);
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityHorse.class, 1, 2, 6));
        this.theBiomeDecorator.treesPerChunk = 1;
        this.theBiomeDecorator.flowersPerChunk = 4;
        this.theBiomeDecorator.grassPerChunk = 20;
	}

    public WorldGenAbstractTree func_150567_a(Random rand)
    {
        return (WorldGenAbstractTree) (rand.nextInt(5) > 0 ? treeGen : worldGeneratorTrees);
    }

    /**
     * Creates a mutated version of the biome and places it into the biomeList with an index equal to the original plus
     * 128
     */
    public BiomeGenBase createMutation()
    {
        Mutated mutated = new Mutated(this.biomeID + 128, this);
        mutated.temperature = (this.temperature + 1.0F) * 0.5F;
        mutated.rootHeight = this.rootHeight * 0.5F + 0.3F;
        mutated.heightVariation = this.heightVariation * 0.5F + 1.2F;
        return mutated;
    }

    public void decorate(World world, Random rand, int x, int z)
    {
        genTallFlowers.func_150548_a(2);

        for (int k = 0; k < 7; ++k)
        {
            int l = x + rand.nextInt(16) + 8;
            int i1 = z + rand.nextInt(16) + 8;
            int j1 = rand.nextInt(world.getHeightValue(l, i1) + 32);
            genTallFlowers.generate(world, rand, l, j1, i1);
        }

        super.decorate(world, rand, x, z);
    }

    public static class Mutated extends BiomeGenMutated
    {
    	public Mutated(int index, BiomeGenBase biome)
    	{
    		super(index, biome);
    		this.theBiomeDecorator.treesPerChunk = 2;
    		this.theBiomeDecorator.flowersPerChunk = 2;
            this.theBiomeDecorator.grassPerChunk = 5;
        }
    	
    	public void decorate(World world, Random rand, int x, int z)
    	{
            this.theBiomeDecorator.decorateChunk(world, rand, this, x, z);
        }
    }
}