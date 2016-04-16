package fle.core.world.biome;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenDesert;
import net.minecraft.world.gen.feature.WorldGenDesertWells;

public class BiomeDesert extends BiomeBase
{
	private static final WorldGenDesertWells worldgendesertwells = new WorldGenDesertWells();
	private boolean genWell;
	
	public BiomeDesert(int id, boolean genWell, boolean genCacti)
	{
		super(id);
        spawnableCreatureList.clear();
        topBlock = Blocks.sand;
        fillerBlock = Blocks.sand;
        theBiomeDecorator.treesPerChunk = -999;
        theBiomeDecorator.deadBushPerChunk = 2;
        theBiomeDecorator.reedsPerChunk = 50;
        if(genCacti)
        {
        	theBiomeDecorator.cactiPerChunk = 10;
        }
        spawnableCreatureList.clear();
        this.genWell = genWell;
	}

    public void decorate(World world, Random random, int x, int z)
    {
        super.decorate(world, random, x, z);

        if(genWell)
        {
            if (random.nextInt(1000) == 0)
            {
                int k = x + random.nextInt(16) + 8;
                int l = z + random.nextInt(16) + 8;
                worldgendesertwells.generate(world, random, k, world.getHeightValue(k, l) + 1, l);
            }
        }
    }
}