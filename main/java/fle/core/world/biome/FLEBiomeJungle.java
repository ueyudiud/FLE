package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FLEBiomeJungle extends FLEBiome
{
    private boolean flag;

	public FLEBiomeJungle(String name, int index, boolean flag)
	{
		super(name, index);
        this.flag = flag;

        if (flag)
        {
            this.theBiomeDecorator.treesPerChunk = 2;
        }
        else
        {
            this.theBiomeDecorator.treesPerChunk = 25;
        }

        this.theBiomeDecorator.grassPerChunk = 25;
        this.theBiomeDecorator.flowersPerChunk = 4;

        if (!flag)
        {
            this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(EntityOcelot.class, 2, 1, 1));
        }

        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
    }
	
	@Override
	public WorldGenAbstractTree getTreeGenerator(double noise, Random rand)
	{
		return (WorldGenAbstractTree)(rand.nextInt(10) == 0 ? 
        		worldGeneratorBigTree : 
        			(rand.nextInt(2) == 0 ? new WorldGenShrub(3, 0) : 
        				(!flag && rand.nextInt(3) == 0 ? new WorldGenMegaJungle(false, 10, 20, 3, 3) : 
        					new WorldGenTrees(false, 4 + rand.nextInt(7), 3, 3, true))));
	}

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random aRand)
    {
        return aRand.nextInt(4) == 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2) : new WorldGenTallGrass(Blocks.tallgrass, 1);
    }

    public void decorate(World aWorld, Random aRand, int x, int z)
    {
        super.decorate(aWorld, aRand, x, z);
        int k = x + aRand.nextInt(16) + 8;
        int l = z + aRand.nextInt(16) + 8;
        int height = aWorld.getHeightValue(k, l) * 2; //This was the original input for the nextInt below.  But it could == 0, which crashes nextInt
        if (height < 1) height = 1;
        int i1 = aRand.nextInt(height);
        (new WorldGenMelon()).generate(aWorld, aRand, k, i1, l);
        WorldGenVines worldgenvines = new WorldGenVines();

        for (l = 0; l < 50; ++l)
        {
            i1 = x + aRand.nextInt(16) + 8;
            short short1 = 128;
            int j1 = z + aRand.nextInt(16) + 8;
            worldgenvines.generate(aWorld, aRand, i1, short1, j1);
        }
    }
    
    @Override
    protected Block getBlock(boolean isFirstTop, boolean isNoCover,
    		boolean hasFluidOnSide, boolean isBaseDecorateBlock,
    		Block replaceBlock, Random rand, float temp)
    {
    	return isBaseDecorateBlock ? Blocks.gravel : super.getBlock(isFirstTop, isNoCover, hasFluidOnSide, isBaseDecorateBlock, replaceBlock, rand, temp);
    }
}