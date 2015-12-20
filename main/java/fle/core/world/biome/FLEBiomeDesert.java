package fle.core.world.biome;

import java.util.Random;

import fle.core.world.dim.FLEBiomeDecoratorBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;

public class FLEBiomeDesert extends FLEBiome
{
	public FLEBiomeDesert(String name, int index)
	{
		super(name, index);
        this.topBlock = Blocks.sand;
        this.fillerBlock = Blocks.sand;
        this.setDisableRain();
        ((FLEBiomeDecoratorBase) this.theBiomeDecorator).waterLakePerChunk = -1;
        ((FLEBiomeDecoratorBase) this.theBiomeDecorator).lavaLakePerChunk = -1;
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 2;
        this.theBiomeDecorator.reedsPerChunk = 50;
        this.theBiomeDecorator.cactiPerChunk = 10;
        this.spawnableCreatureList.clear();
    }

    public void decorate(World aWorld, Random aRand, int x, int z)
    {
        super.decorate(aWorld, aRand, x, z);

        if (aRand.nextInt(1000) == 0)
        {
            int k = x + aRand.nextInt(16) + 8;
            int l = z + aRand.nextInt(16) + 8;
            WorldGenDesertWells worldgendesertwells = new WorldGenDesertWells();
            worldgendesertwells.generate(aWorld, aRand, k, aWorld.getHeightValue(k, l) + 1, l);
        }
    }
}