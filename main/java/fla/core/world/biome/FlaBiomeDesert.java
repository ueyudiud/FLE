package fla.core.world.biome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import fla.api.world.TempretureBand;
import fla.core.world.biome.FlaBiome.BiomeBlockGen;

public class FlaBiomeDesert extends FlaBiome
{
	public FlaBiomeDesert(String name, int id, TempretureBand level) 
	{
		super(name, id, true, level);
		this.theBiomeDecorator.cactiPerChunk = level == TempretureBand.Tropic ? 0 : 4;
		this.theBiomeDecorator.deadBushPerChunk = level == TempretureBand.Tropic ? 1 : 5;
		this.theBiomeDecorator.reedsPerChunk = level == TempretureBand.Tropic ? 0 : 1;
		this.theBiomeDecorator.grassPerChunk = -999;
		this.topBlock = Blocks.sand;
		this.fillerBlock = Blocks.sandstone;
	}

	@Override
	protected float getBaseTemperature() 
	{
		return level.getTempreture();
	}

	@Override
	protected float getBaseRainfall() 
	{
		return 0.0F;
	}

	@Override
	protected Height getBaseHeight() 
	{
		return height_Default;
	}

	@Override
	protected boolean canRain() 
	{
		return false;
	}

	@Override
	public boolean generateLake() 
	{
		return false;
	}

	@Override
	public BiomeBlockGen getBlockGen() 
	{
		Map<Block, List<Integer>> map = new HashMap();
		map.put(Blocks.sand, Arrays.asList(0, 1, 1));
		map.put(Blocks.sandstone, Arrays.asList(0, 2, 3));
		map.put(Blocks.cobblestone, Arrays.asList(0, 2, 2));
		return new BiomeBlockGen(map);
	}
}
