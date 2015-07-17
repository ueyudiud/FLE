package fla.core.world.biome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import fla.api.world.ForestType;
import fla.api.world.TempretureBand;
import fla.core.world.biome.FlaBiome.BiomeBlockGen;

public class FlaBiomeForest extends FlaBiome
{
	protected ForestType type;

	public FlaBiomeForest(String name, int id, boolean register, ForestType type) 
	{
		super(name, id, register, type);
		this.type = type;
		double a = type.getRainfall() * type.getTempreture();
		int b = a > 0.95F ? 10 : a > 0.6F ? 9 : a > 0.4F ? 8 : a > 0.3F ? 7 : 6;
		this.theBiomeDecorator.treesPerChunk = b;
		this.theBiomeDecorator.flowersPerChunk = type.getTempreture() > 0.5F ? (b > 8 ? 4 : b > 5 ? 8 : 10) : -999;
	}
	public FlaBiomeForest(String name, int id, ForestType type) 
	{
		this(name, id, true, type);
	}

	@Override
	protected float getBaseTemperature() 
	{
		return (float) level.getTempreture();
	}

	@Override
	protected float getBaseRainfall() 
	{
		return (float) type.getRainfall();
	}

	@Override
	protected Height getBaseHeight() 
	{
		return height_Default;
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
