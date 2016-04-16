package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;

public class BiomeBeach extends BiomeBase
{
	public BiomeBeach(int id, boolean isStone)
	{
		super(id);
		setHeight(height_Shores);
        this.spawnableCreatureList.clear();
        if(isStone)
        {
        	topBlock = Blocks.stone;
        	fillerBlock = Blocks.stone;
        }
        else
        {
        	topBlock = Blocks.sand;
        	fillerBlock = Blocks.sand;
        }
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 0;
        this.theBiomeDecorator.reedsPerChunk = 0;
        this.theBiomeDecorator.cactiPerChunk = 0;
	}
}