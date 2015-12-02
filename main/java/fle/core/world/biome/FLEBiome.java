package fle.core.world.biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenHills;
import net.minecraft.world.biome.BiomeGenJungle;
import net.minecraft.world.biome.BiomeGenSnow;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import fle.api.enums.EnumFLERock;
import fle.api.material.MaterialRock;
import fle.api.util.FleLog;
import fle.api.util.ISubTagContainer;
import fle.api.util.SubTag;
import fle.core.block.BlockFleRock;
import fle.core.block.BlockRock;
import fle.core.init.IB;
import fle.core.util.Util;
import fle.core.util.noise.NoiseFuzzy;
import fle.core.util.noise.NoiseMix;
import fle.core.util.noise.NoisePerlin;
import fle.core.world.dim.FLEBiomeDecoratorBase;
import fle.core.world.dim.FLEWorldType;

public class FLEBiome extends BiomeGenBase implements ISubTagContainer
{
    protected static final BiomeGenBase.Height height_ShallowWaters = new BiomeGenBase.Height(-0.09375F, 0.0F);
    protected static final BiomeGenBase.Height height_MidWaters = new BiomeGenBase.Height(-0.046875F, 0.0F);
    protected static final BiomeGenBase.Height height_HighWaters = new BiomeGenBase.Height(0.015625F, 0.0F);
    protected static final BiomeGenBase.Height height_PartiallySubmerged = new BiomeGenBase.Height(-0.03125F, 0.0625F);
    
	private static final FLEBiome[] biomeList = new FLEBiome[256];
	
	public static final FLEBiome ocean = new FLEBiomeOcean("FLE Ocean", BiomeGenBase.ocean.biomeID, false).setColor(112).setH(height_Oceans);
	public static final FLEBiome warm_plains = new FLEBiomePlains("FLE Plains", BiomeGenBase.plains.biomeID).setColor(9286496);
	public static final BiomeGenBase warm_plains_M;
	public static final FLEBiome warm_forest = new FLEBiomeForest("FLE Forest", BiomeGenBase.forest.biomeID, 0).setColor(353825);
	public static final FLEBiome mid_forest = new FLEBiomeForest("FLE Forest B", BiomeGenBase.birchForest.biomeID, 2).setColor(3175492);
	public static final FLEBiome extremeHills = new FLEBiomeHills("FLE Extreme Hill", BiomeGenBase.extremeHills.biomeID, false).setColor(6316128).setH(height_MidHills).setTemperatureRainfall(0.2F, 0.3F);
    public static final FLEBiome slope = new FLEBiomeOcean("FLE Slope", 41, true).setColor(0x8EBFFF).setBiomeHeight(-0.3F, -0.03125F);
	public static final FLEBiome wasteland = new FLEBiomeWasteland("FLE Wasteland", 42).setColor(0xA4B360).setH(height_Default).setDisableRain();
	public static final FLEBiome swampland = new FLEBiomeSwamp("FLE Swampland", BiomeGenBase.swampland.biomeID).setH(height_PartiallySubmerged).setColor(0x307252).setTemperatureRainfall(1.0F, 0.9F);
	public static final FLEBiome hill = new FLEBiomeHill("FLE Hill", 43).setColor(0x60DB60).setH(height_MidPlains);
	public static final FLEBiome roofedForest = new FLEBiomeForest("FLE Roofed Forest", BiomeGenBase.roofedForest.biomeID, 3).setColor(4215066);
	public static final FLEBiome roofedForest_hill = new FLEBiomeForest("FLE Roofed Forest Hill", 44, 3).setH(height_LowHills).setColor(4215066);
	public static final FLEBiome jungle = new FLEBiomeJungle("FLE Jungle", 21, false).setColor(5470985).func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.9F);
    public static final FLEBiome jungleHills = new FLEBiomeJungle("FLE Jungle Hill", 22, false).setColor(2900485).func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.9F).setH(height_LowHills);
    public static final FLEBiome jungleEdge = new FLEBiomeJungle("FLE Jungle Edge", 23, true).setColor(6458135).func_76733_a(5470985).setTemperatureRainfall(0.95F, 0.8F);
    public static final FLEBiome river_low = new FLEBiomeRiver("FLE River", BiomeGenBase.river.biomeID).setWaterHeight(0).setH(height_ShallowWaters).setColor(0x0000FF);
	public static final FLEBiome river_mid = new FLEBiomeRiver("FLE River +", 45).setWaterHeight(4).setH(height_MidWaters).setColor(0x0F0FFF);
	public static final FLEBiome river_high = new FLEBiomeRiver("FLE River ++", 46).setWaterHeight(12).setH(height_HighWaters).setColor(0x1F1FFF).setTemperatureRainfall(0.53125F, 0.4F);
	public static final FLEBiome frozenSlope = new FLEBiomeOcean("FLE Frozen Slope", 47, true).setColor(0x9ECFFF).setBiomeHeight(-0.3F, -0.03125F).setTemperatureRainfall(0.1F, 0.3F);
	public static final FLEBiome frozenRiver = new FLEBiomeRiver("FLE Frozen River", BiomeGenBase.frozenRiver.biomeID).setH(height_ShallowWaters).setColor(0x3F3FFF).setTemperatureRainfall(0.1F, 0.5F).setEnableSnow();
	public static final FLEBiome desert = new FLEBiomeDesert("FLE Desert", BiomeGenBase.desert.biomeID).setColor(16421912);
	public static final FLEBiome desertHills = new FLEBiomeDesert("FLE Desert Hill", BiomeGenBase.desertHills.biomeID).setColor(13786898);
	public static final FLEBiome warm_forestHills = new FLEBiomeForest("FLE Forest Hill", BiomeGenBase.forestHills.biomeID, 0).setColor(2250012).setH(height_LowHills);
	public static final FLEBiome taiga = new FLEBiomeTaiga("FLE Taiga", BiomeGenBase.taiga.biomeID, 0).setColor(747097).setTemperatureRainfall(0.25F, 0.8F).setH(height_MidPlains);
	public static final FLEBiome taigaHills = new FLEBiomeTaiga("FLE Taiga Hill", BiomeGenBase.taigaHills.biomeID, 0).setColor(1456435).setTemperatureRainfall(0.25F, 0.8F).setH(height_LowHills);
	public static final FLEBiome coldTaiga = new FLEBiomeTaiga("FLE Cold Taiga", BiomeGenBase.coldTaiga.biomeID, 0).setColor(3233098).setTemperatureRainfall(-0.1F, 0.8F).setEnableSnow().setH(height_MidPlains);
	public static final FLEBiome coldTaigaHills = new FLEBiomeTaiga("FLE Cold Taiga Hill", BiomeGenBase.coldTaigaHills.biomeID, 0).setColor(2375478).setTemperatureRainfall(-0.15F, 0.8F).setEnableSnow().setH(height_LowHills);
	public static final FLEBiome megaTaiga = new FLEBiomeTaiga("FLE Taiga", BiomeGenBase.megaTaiga.biomeID, 1).setColor(5858897).setTemperatureRainfall(0.3F, 0.8F).setH(height_MidPlains);
	public static final FLEBiome megaTaigaHills = new FLEBiomeTaiga("FLE Taiga Hill", BiomeGenBase.megaTaigaHills.biomeID, 1).setColor(4542270).setTemperatureRainfall(0.3F, 0.8F).setH(height_LowHills);
	public static final FLEBiome icePlains = new FLEBiomeSnow("FLE Ice Plain", 12, false).setColor(16777215).setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setH(height_LowPlains);
    public static final FLEBiome iceMountains = new FLEBiomeSnow("FLE Ice Mountain", 13, false).setColor(10526880).setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setH(height_LowHills);
    
	public static final FLEBiome hell = new FLEBiomeHellSimple("FLE Hell", BiomeGenBase.hell.biomeID).setColor(BiomeGenBase.hell.color);
	public static final FLEBiome hell_lava_sea = new FLEBiomeLavaSea("FLE Lava Sea", 65).setColor(BiomeGenBase.hell.color);
	public static final FLEBiome hell_crystalland = new FLEBiomeHellCrystalLand("FLE Crystal Land", 66).setColor(BiomeGenBase.hell.color);
    
	static
	{
		try
		{
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76771_b", "ocean"), ocean, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76772_c", "plains"), warm_plains, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76767_f", "forest"), warm_forest, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_150583_P", "birchForest"), mid_forest, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76778_j", "hell"), hell, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_150585_R", "roofedForest"), roofedForest, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76781_i", "river"), river_low, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76777_m", "frozenRiver"), frozenRiver, false);
		}
		catch (Exception e)
		{
			FleLog.getLogger().catching(e);
		}
		warm_plains_M = warm_plains.createMutation();
		BiomeDictionary.registerBiomeType(wasteland, BiomeDictionary.Type.DRY, BiomeDictionary.Type.HOT);
		BiomeDictionary.registerBiomeType(slope, BiomeDictionary.Type.WATER);
		BiomeDictionary.registerBiomeType(roofedForest_hill, BiomeDictionary.Type.HOT, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST);

		rockAcidityNoise = new NoiseFuzzy(2095710L, 3, 16, 16, 1.6F);
		rockWeatheringNoise = new NoiseFuzzy(56819619L, 3, 16, 16, 1.6F);
	}
	
	protected static final NoiseFuzzy rockAcidityNoise;
	protected static final NoiseFuzzy rockWeatheringNoise;
	
	public static void setRockNoiseSeed(long seed)
	{
		rockAcidityNoise.setSeed(seed * 459571951L + 37591754L);
		rockWeatheringNoise.setSeed(seed * 386719561L + 971961810L);
	}
	
	int mColor;
	
	public FLEBiome(String name, int index)
	{
		super(index);
		biomeList[index] = this;
		setBiomeName(name);
	}
	public FLEBiome(String name, int index, boolean reg)
	{
		super(index, reg);
		if(reg)
		{
			biomeList[index] = this;
		}
		setBiomeName(name);
	}
	
	@Override
	public FLEBiome setColor(int color)
	{
		mColor = this.color = color;
		return this;
	}

    public final FLEBiome setH(BiomeGenBase.Height height)
    {
        rootHeight = height.rootHeight;
        heightVariation = height.variation;
        return this;
    }
    
	public FLEBiome setBiomeHeight(float min, float max)
	{
		setHeight(new Height((min + max) / 2.0F, (max - min) / 2.0F));
		return this;
	}
	
	@Override
	public FLEBiome setDisableRain()
	{
		super.setDisableRain();
		return this;
	}
	
	public FLEBiome setLilypads(int i)
	{
		theBiomeDecorator.waterlilyPerChunk = i;
		return this;
	}

    public FLEBiome func_76733_a(int col)
    {
        this.field_76754_C = col;
        return this;
    }
	
	@Override
	public FLEBiome setEnableSnow()
	{
		super.setEnableSnow();
		return this;
	}
	
	@Override
	public FLEBiome setTemperatureRainfall(float temp, float rain)
	{
		super.setTemperatureRainfall(temp, rain);
		return this;
	}
	
	public int getBiomeColor()
	{
		return mColor;
	}
	
	public boolean isBeach()
	{
		return false;
	}
	
	public boolean isOcean()
	{
		return false;
	}
	
	protected Block getBlock(boolean isFirstTop, boolean isNoCover, boolean hasFluidOnSide, boolean isBaseDecorateBlock, Block replaceBlock, Random rand, float temp)
	{
		return isNoCover ? topBlock : fillerBlock;
	}
	
	protected void genTerrainBlocks(World aWorld, Random rand, Block[] blocks, byte[] bytes, boolean isFlat, boolean isNonwaterTop, int rootHeight, int x, int z, int size, int height)
	{
		boolean firstCoverFlag = true;
        boolean waterFlag = false;
        boolean coverFlag = true;
        int k = -1;
        Block[] rock = EnumFLERock.getRockBlock(rockAcidityNoise.noise(x, z), rockWeatheringNoise.noise(x, z));
        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (z * 16 + x) * size + l1;

            if (l1 <= rand.nextInt(2))
            {
                blocks[i2] = Blocks.bedrock;
            }
            else if(l1 <= 8)
            {
            	blocks[i2] = Blocks.lava;
            }
            else if(l1 <= 10 + rand.nextInt(4))
            {
            	blocks[i2] = Blocks.air;
            }
            else
            {
                Block block2 = blocks[i2];
                if (block2 != null && block2.getMaterial() != Material.air)
                {
                    if (block2 == Blocks.stone)
                    {
                        if(k == -1 && rootHeight > 0)
                        {
                        	if (l1 >= height)
                        	{
                        		blocks[i2] = getBlock(firstCoverFlag, coverFlag, waterFlag, false, block2, rand, getFloatTemperature(x, l1, z));
                                k = rootHeight;
                            }
                        	else
                        	{
                        		k = -2;
                        	}
                        }
                        else if(k > 0)
                        {
                        	blocks[i2] = getBlock(firstCoverFlag, coverFlag, waterFlag, k == 1, block2, rand, getFloatTemperature(x, l1, z));
                        	--k;
                        }
                        else
                        {
                        	blocks[i2] = rock[Math.min(0, (255 - l1) / 64)];
                        }
                    }
                    else if(block2 == Blocks.water)
                    {
                    	if(isFlat || isNonwaterTop)
                    	{
                    		blocks[i2] = Blocks.air;
                    	}
                    	else if(!waterFlag)
                    	{
                        	if(k > 0) --k;
                          	if(getFloatTemperature(x, l1, z) < 0.15F) blocks[i2] = Blocks.ice;
                    	}
                    }
                }
                else
                {
                    k = -1;
                }
            }
            coverFlag = blocks[i2] == null || blocks[i2] == Blocks.air;
            waterFlag = blocks[i2] != Blocks.water && blocks[i2] != Blocks.ice;
            firstCoverFlag &= coverFlag;
        }
	}
	
	@Override
	public void genTerrainBlocks(World aWorld, Random aRand,
			Block[] aBlocks, byte[] aByte, int x,
			int z, double yLevel)
	{
        boolean flag1 = !(isBeach() || isOcean());
        boolean flag2 = aWorld.getWorldInfo().getTerrainType() == FLEWorldType.FLAT;
        int l = (int)(yLevel * 0.33D + 2.0D + (aRand.nextDouble()+ 1.0D) * 0.5D);
        int i1 = x & 15;
        int j1 = z & 15;
        int k1 = aBlocks.length / 256;
        short height = (short) aWorld.getWorldInfo().getTerrainType().getHorizon(aWorld);
        rockAcidityNoise.setSeed(aWorld.getSeed() * 1851515381L + 581917195L);
        rockWeatheringNoise.setSeed(aWorld.getSeed() * 278491741L + 819518761L);
        genTerrainBlocks(aWorld, aRand, aBlocks, aByte, flag2, flag1, l, i1, j1, k1, height - 1);
	}

	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenTallGrass(Blocks.tallgrass, 1);
	}
	
	@Override
	public FLEBiomeDecoratorBase createBiomeDecorator()
	{
		return new FLEBiomeDecoratorBase();
	}
	
	@Override
	public void decorate(World aWorld, Random aRNG, int x,
			int z)
	{
		super.decorate(aWorld, aRNG, x, z);
	}
	
	private final Collection<SubTag> tags = new ArrayList(1);
	
	@Override
	public boolean contain(SubTag aTag) 
	{
		return tags.contains(aTag);
	}

	@Override
	public void add(SubTag...aTags) 
	{
		if(aTags != null)
			for(SubTag tTag : aTags)
				if(tTag != null && (!tags.contains(tTag)))
					tags.add(tTag);
	}

	@Override
	public void remove(SubTag aTag) 
	{
		tags.contains(aTag);
	}
}