package fla.core.world.biome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import fla.api.world.DampnessBand;
import fla.api.world.TempretureBand;
import fla.core.world.biome.FlaBiome.BiomeBlockGen;

public class FlaBiomeOcean extends FlaBiome
{
	public FlaBiomeOcean(String name, int id, TempretureBand level) 
	{
		super(name, id, true, level);
	}

	@Override
	protected float getBaseTemperature() 
	{
		return level.getTempreture();
	}

	@Override
	protected float getBaseRainfall() 
	{
		return DampnessBand.Mid.getDampness();
	}

	@Override
	protected Height getBaseHeight() 
	{
		return height_Oceans;
	}

	@Override
	protected boolean canRain() 
	{
		return level != TempretureBand.Low_Tem;
	}

	@Override
	public boolean generateLake() 
	{
		return false;
	}

	@Override
	public BiomeBlockGen getBlockGen() 
	{
		return new BiomeBlockGen(Blocks.sand, 2, 2, Blocks.cobblestone, 1, 1);
	}
}
