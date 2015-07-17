package fla.core.world.biome;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import fla.api.util.FlaValue;
import fla.api.util.IColorMap;
import fla.api.world.BlockPos;
import fla.api.world.ForestType;
import fla.api.world.TempretureBand;
import fla.core.Fla;
import fla.core.world.generate.FlaWorldChunkManager;
import fla.core.world.generate.FlaWorldGenerateHelper;

public abstract class FlaBiome extends BiomeGenBase
{
	//GrassLand
	public static final FlaBiome ocean = new FlaBiomeOcean("FLA_Ocean", 64, TempretureBand.Temprate);
	public static final FlaBiome plain_tr = new FlaBiomePlain("FLA_Tropic Grassland", 65, TempretureBand.Tropic);
	public static final FlaBiome plain_st = new FlaBiomePlain("FLA_Sbutropic Grassland", 66, TempretureBand.Subtropic);
	public static final FlaBiome plain_te = new FlaBiomePlain("FLA_Temprate Grassland", 67, TempretureBand.Temprate);
	public static final FlaBiome plain_sf = new FlaBiomePlain("FLA_Subfrigid Grassland", 68, TempretureBand.Subfrigid);
	public static final FlaBiome forest_tr_a = new FlaBiomeForest("FLA_Rain Forest", 69, ForestType.Rain_Forest);
	public static final FlaBiome forest_tr_b = new FlaBiomeForest("FLA_Monsoon Forest", 70, ForestType.Monsoon_Forest);
	public static final FlaBiome forest_tr_c = new FlaBiomeForest("FLA_Dry Tropic Forest", 71, ForestType.Dry_Tropic_Forest);
	public static final FlaBiome forest_tr_d = new FlaBiomeForest("FLA_Dry Tropic Forest", 72, ForestType.Edge_Tropic_Forest);
	public static final FlaBiome forest_st_a = new FlaBiomeForest("FLA_Evergreen Board-leaf Forest", 73, ForestType.Evergreen_Boardleaf_Forest);
	public static final FlaBiome forest_st_b = new FlaBiomeForest("FLA_Evergreen Board-leaf and Coniferous Forest", 74, ForestType.Evergreen_B_and_C_Forest);
	public static final FlaBiome forest_st_c = new FlaBiomeForest("FLA_Evergreen Coniferous Forest", 75, ForestType.Evergreen_Coniferous_Forest);
	public static final FlaBiome forest_st_d = new FlaBiomeForest("FLA_Evergreen Board-leaf Forest", 76, ForestType.Edge_Subtropic_Forest);
	public static final FlaBiome forest_te_a = new FlaBiomeForest("FLA_Evergreen and Deciduous Mix Forest", 77, ForestType.E_and_D_Broadleaf_Forest);
	public static final FlaBiome forest_te_b = new FlaBiomeForest("FLA_Deciduous Borad-leaf Forest", 78, ForestType.Deciduous_Broadleaf_Forest);
	public static final FlaBiome forest_te_c = new FlaBiomeForest("FLA_Board-leaf and Coniferous Forest", 79, ForestType.Deciduous_B_and_Coniferous_Forest);
	public static final FlaBiome forest_te_d = new FlaBiomeForest("FLA_Deciduous Board-leaf Forest", 80, ForestType.Edge_Temprate_Forest);
	public static final FlaBiome forest_sf_a = new FlaBiomeForest("FLA_Coniferous Forest", 81, ForestType.Coniferous_Forest);
	public static final FlaBiome forest_sf_b = new FlaBiomeForest("FLA_Deciduous Coniferous Forest", 82, ForestType.Deciduous_Coniferous_Forest);
	public static final FlaBiome forest_sf_c = new FlaBiomeForest("FLA_Coniferous Forest", 83, ForestType.Edge_Subfrigid_Forest);
	public static final FlaBiome desert_tr = new FlaBiomeDesert("FLA_Desert", 84, TempretureBand.Tropic);
	public static final FlaBiome desert_st = new FlaBiomeDesert("FLA_Desert", 85, TempretureBand.Subtropic);
	
	public static FlaBiome getFlaBiome(BiomeGenBase biome) 
	{
		if(biome instanceof FlaBiome) return (FlaBiome) biome;
		if(biome == BiomeGenBase.ocean) return ocean;
		if(biome == BiomeGenBase.desert) return desert_st;
		if(biome == BiomeGenBase.birchForest || biome == BiomeGenBase.birchForestHills) return forest_te_b;
		if(biome == BiomeGenBase.forest || biome == BiomeGenBase.forestHills) return forest_te_b;
		if(biome == BiomeGenBase.jungle) return forest_tr_a;
		if(biome == BiomeGenBase.jungleHills) return forest_tr_b;
		if(biome == BiomeGenBase.jungleEdge) return forest_tr_d;
		if(biome == BiomeGenBase.plains) return plain_te;
		if(biome == BiomeGenBase.roofedForest) return forest_st_a;
		
		return ocean;
	}
	public static FlaBiome getBiome(long seed, int height, BlockPos pos)
	{
		return getBiome(FlaWorldGenerateHelper.getTempreture(seed, (int) pos.x, (int) pos.z) - (double) (height - FlaValue.SEALEVEL) * 0.003D, FlaWorldGenerateHelper.getDampness(seed, (int) pos.x, (int) pos.z));
	}
	private static FlaBiome getBiome(double tem, double rain)
	{
		if(rain > 0.6F)
		{
			if(tem > TempretureBand.Tropic.getTempreture())
			{
				return rain > 0.9625F ? forest_tr_a : rain > 0.9375F ? forest_tr_b : rain > 0.75F ? forest_tr_c : forest_tr_d;
			}
			else if(tem > TempretureBand.Subfrigid.getTempreture())
			{
				return rain > 0.8F ? forest_st_a : rain > 0.725F ? forest_st_b : rain > 0.625F ? forest_st_c : forest_st_d;
			}
			else if(tem > TempretureBand.Temprate.getTempreture())
			{
				return rain > 0.75F ? forest_te_a : rain > 0.7F ? forest_te_b : rain > 0.65F ? forest_te_c : forest_te_d;
			}
			else if(tem > TempretureBand.Subfrigid.getTempreture())
			{
				return forest_sf_a;
			}
		}
		if(rain > 0.3F)
		{
			if(tem > TempretureBand.Tropic.getTempreture())
			{
				return plain_tr;
			}
			if(tem > TempretureBand.Subtropic.getTempreture())
			{
				return plain_st;
			}
			if(tem > TempretureBand.Temprate.getTempreture())
			{
				return plain_te;
			}
			if(tem > TempretureBand.Subfrigid.getTempreture())
			{
				return rain > 0.4F ? forest_sf_b : rain > 0.3F ? forest_sf_c : plain_sf;
			}
		}
		if(tem > TempretureBand.Tropic.getTempreture())
		{
			return desert_tr;
		}
		if(tem > TempretureBand.Subtropic.getTempreture())
		{
			return desert_st;
		}
		return ocean;
	}
	private static final IColorMap colormap = Fla.fla.cmm.getColorMap(new ResourceLocation(FlaValue.TEXT_FILE_NAME, "grass"));
	
	protected TempretureBand level;
	
	FlaBiome(String name, int id, boolean register, ForestType type) 
	{
		super(id, register);
		this.setBiomeName(name);
		this.level = type.getBand();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.setTemperatureRainfall(getBaseTemperature(), (float) type.getRainfall());
		this.setHeight(getBaseHeight());
		this.setColor(getBaseBiomeColor());
		if(!this.canRain()) this.setDisableRain();
		FlaWorldChunkManager.biomesToSpawnIn.add(this);
	}
	public FlaBiome(String name, int id, boolean register, TempretureBand level) 
	{
		super(id, register);
		this.setBiomeName(name);
		this.level = level;
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCaveCreatureList.clear();
		this.setTemperatureRainfall(getBaseTemperature(), getBaseRainfall());
		this.setHeight(getBaseHeight());
		this.setColor(getBaseBiomeColor());
		if(!this.canRain()) this.setDisableRain();
		FlaWorldChunkManager.biomesToSpawnIn.add(this);
	}

	protected abstract float getBaseTemperature();

	protected abstract float getBaseRainfall();
	
	protected abstract Height getBaseHeight();

	private int getBaseBiomeColor()
	{
		return colormap == null ? 0xFFFFFF : colormap.getColor((int)getBaseRainfall() * colormap.getWidth(), (int)getBaseTemperature() * colormap.getHeight());
	}
	
	public boolean canRainWithRainfall(int seed, int x, int z)
	{
		return FlaWorldGenerateHelper.getDampness(seed, x, z) >= 0.2F && canRain();
	}
	
	protected abstract boolean canRain();
	   
	public abstract boolean generateLake();
	
	@Override
	public BiomeGenBase setTemperatureRainfall(float tem, float rain)
    {
        this.temperature = tem;
        this.rainfall = rain;
        return this;
    }
	
	public abstract BiomeBlockGen getBlockGen();
	
	public class BiomeBlockGen
	{
		Map<Block, List<Integer>> fillBlockMap = new HashMap();
		Map<Integer, Block> idMap = new HashMap();
		
		public BiomeBlockGen(Map<Block, List<Integer>> map)
		{
			this.fillBlockMap = new HashMap(map);
			for(Block b : map.keySet())
			{
				idMap.put(map.get(b).get(0), b);
			}
		}
		public BiomeBlockGen(Block topBlock, int minHeight1, int maxHeight1, Block fillBlock, int minHeight2, int maxHeight2)
		{
			Map<Block, List<Integer>> map = new HashMap();
			map.put(topBlock, Arrays.asList(0, minHeight1, maxHeight1));
			map.put(fillBlock, Arrays.asList(1, minHeight2, maxHeight2));
			this.fillBlockMap = map;
			idMap.put(0, topBlock);
			idMap.put(1, fillBlock);
		}
		public BiomeBlockGen(Block topBlock, Block fillBlock, int height)
		{
			this(topBlock, 1, 1, fillBlock, height, height);
		}
		public BiomeBlockGen(Block topBlock, Block fillBlock)
		{
			this(topBlock, fillBlock, 3);
		}
		public BiomeBlockGen(Block block)
		{
			this(block, block);
		}
		
		public int[] getFullHeight(Random rand)
		{
			int[] i = new int[fillBlockMap.size()];
			for(Block block : fillBlockMap.keySet())
			{
				int a = fillBlockMap.get(block).get(1);
				int b = fillBlockMap.get(block).get(2);
				if(a == b) i[fillBlockMap.get(block).get(0)] = a;
				else i[fillBlockMap.get(block).get(0)] = Math.min(a, b) + rand.nextInt(Math.abs(a - b));
			}
			return i;
		}
		
		public Map<Integer, Block> getBlockMap()
		{
			return idMap;
		}
	}
}