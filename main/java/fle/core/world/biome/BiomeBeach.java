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
        this.biomeDecorator.treesPerChunk = -999;
        this.biomeDecorator.deadBushPerChunk = 0;
        this.biomeDecorator.reedsPerChunk = 0;
        this.biomeDecorator.cactiPerChunk = 0;
	}
}