package fle.core.world.biome;

import farcore.lib.world.biome.BiomeBase;
import net.minecraft.init.Blocks;

public class BiomeBeach extends BiomeBase
{
	public BiomeBeach(int id, boolean isStone)
	{
		super(id);
		sedimentaryMultiply = 0.3F;
		metamorphismMultiply = 1.2F;
		setHeight(height_Shores);
        this.spawnableCreatureList.clear();
        if(isStone)
        {
        	topBlock = Blocks.stone;
        	secondBlock = Blocks.stone;
        	fillerBlock = Blocks.stone;
        }
        else
        {
        	topBlock = Blocks.sand;
        	secondBlock = Blocks.sand;
        	fillerBlock = Blocks.sandstone;
        }
        this.biomeDecorator.treesPerChunk = -999;
        this.biomeDecorator.deadBushPerChunk = 0;
        this.biomeDecorator.reedsPerChunk = 0;
        this.biomeDecorator.cactiPerChunk = 0;
	}
}