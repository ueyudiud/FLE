package fla.core.world.biome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import fla.api.world.TempretureBand;

public class FlaBiomePlain extends FlaBiome
{
	
	public FlaBiomePlain(String name, int id, TempretureBand level) 
	{
		super(name, id, true, level);
		this.topBlock = Blocks.grass;
		this.fillerBlock = Blocks.dirt;
		this.theBiomeDecorator.treesPerChunk = getBaseTemperature() > 0.6F ? 2 : 1;
	}

	@Override
	protected float getBaseTemperature() 
	{
		return level.getTempreture();
	}

	@Override
	protected float getBaseRainfall() 
	{
		return 0.3F;
	}

	@Override
	protected Height getBaseHeight() 
	{
		return new Height(0.1F, 0.2F);
	}

	@Override
	protected boolean canRain()
	{
		return true;
	}

	@Override
	public boolean generateLake() 
	{
		return true;
	}

	@Override
	public BiomeBlockGen getBlockGen() 
	{
		Map<Block, List<Integer>> map = new HashMap();
		map.put(Blocks.grass, Arrays.asList(0, 1, 1));
		map.put(Blocks.dirt, Arrays.asList(1, 3, 4));
		map.put(Blocks.gravel, Arrays.asList(2, 2, 3));
		map.put(Blocks.cobblestone, Arrays.asList(3, 2, 2));
		return new BiomeBlockGen(map);
	}
}
