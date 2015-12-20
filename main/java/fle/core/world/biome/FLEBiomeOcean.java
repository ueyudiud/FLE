package fle.core.world.biome;

import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

public class FLEBiomeOcean extends FLEBiome
{
	public FLEBiomeOcean(String name, int index, boolean isSandBase)
	{
		super(name, index);
		spawnableCreatureList.clear();
		theBiomeDecorator.generateLakes = false;
		topBlock = isSandBase ? Blocks.sand : Blocks.gravel;
		fillerBlock = Blocks.gravel;
	}
	
	@Override
	public boolean isOcean()
	{
		return true;
	}

    public BiomeGenBase.TempCategory getTempCategory()
    {
        return BiomeGenBase.TempCategory.OCEAN;
    }
}