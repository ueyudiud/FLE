package fle.core.world.biome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenForest;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;
import fle.api.util.FleLog;
import fle.api.util.ISubTagContainer;
import fle.api.util.SubTag;
import fle.core.util.Util;
import fle.core.world.dim.FLEBiomeDecoratorBase;
import fle.core.world.dim.FLEWorldType;

public class FLEBiome extends BiomeGenBase implements ISubTagContainer
{
    protected static final BiomeGenBase.Height height_ShallowWaters = new BiomeGenBase.Height(-0.09375F, 0.0F);
    protected static final BiomeGenBase.Height height_MidWaters = new BiomeGenBase.Height(-0.046875F, 0.0F);
    protected static final BiomeGenBase.Height height_HighWaters = new BiomeGenBase.Height(0.015625F, 0.0F);
    protected static final BiomeGenBase.Height height_PartiallySubmerged = new BiomeGenBase.Height(-0.03125F, 0.0625F);
    
	private static final FLEBiome[] biomeList = new FLEBiome[256];
	
	public static final FLEBiome ocean = new FLEBiomeOcean("FLE Ocean", BiomeGenBase.ocean.biomeID).setColor(112).setH(height_Oceans);
	public static final FLEBiome warm_plains = new FLEBiomePlains("FLE Plains", BiomeGenBase.plains.biomeID).setColor(9286496);
	public static final BiomeGenBase warm_plains_M;
	public static final FLEBiome warm_forest = new FLEBiomeForest("FLE Forest", BiomeGenBase.forest.biomeID, 0).setColor(353825);
	public static final FLEBiome mid_forest = new FLEBiomeForest("FLE Forest B", BiomeGenBase.birchForest.biomeID, 2).setColor(3175492);
	public static final FLEBiome slope = new FLEBiomeOcean("FLE Slope", 41).setColor(0x8EBFFF).setBiomeHeight(-0.3F, -0.03125F);
	public static final FLEBiome wasteland = new FLEBiomeWasteland("FLE Wasteland", 42).setColor(0xA4B360).setH(height_Default).setDisableRain();
	public static final FLEBiome swampland = new FLEBiomeSwamp("FLE Swampland", BiomeGenBase.swampland.biomeID).setH(height_PartiallySubmerged).setColor(0x307252).setTemperatureRainfall(1.0F, 0.9F);
	public static final FLEBiome hill = new FLEBiomeHill("FLE Hill", 43).setColor(0x60DB60).setH(height_MidPlains);
	public static final FLEBiome roofedForest = new FLEBiomeForest("FLE Roofed Forest", BiomeGenBase.roofedForest.biomeID, 3).setColor(4215066);
	public static final FLEBiome roofedForest_hill = new FLEBiomeForest("FLE Roofed Forest Hill", 44, 3).setH(height_LowHills).setColor(4215066);
	public static final FLEBiome river_low = new FLEBiomeRiver("FLE River", BiomeGenBase.river.biomeID).setWaterHeight(0).setH(height_ShallowWaters).setColor(0x0000FF);
	public static final FLEBiome river_mid = new FLEBiomeRiver("FLE River +", 45).setWaterHeight(4).setH(height_MidWaters).setColor(0x0F0FFF);
	public static final FLEBiome river_high = new FLEBiomeRiver("FLE River ++", 46).setWaterHeight(12).setH(height_HighWaters).setColor(0x1F1FFF).setTemperatureRainfall(0.53125F, 0.4F);
	public static final FLEBiome frozenSlope = new FLEBiomeOcean("FLE Frozen Slope", 47).setColor(0x9ECFFF).setBiomeHeight(-0.3F, -0.03125F).setTemperatureRainfall(0.1F, 0.3F);
	public static final FLEBiome frozenRiver = new FLEBiomeRiver("FLE Frozen River", BiomeGenBase.frozenRiver.biomeID).setH(height_ShallowWaters).setColor(0x3F3FFF).setTemperatureRainfall(0.1F, 0.5F).setEnableSnow();

	public static final FLEBiome hell = new FLEBiomeHellSimple("FLE Hell", BiomeGenBase.hell.biomeID).setColor(BiomeGenBase.hell.color);
	public static final FLEBiome hell_lava_sea = new FLEBiomeLavaSea("FLE Lava Sea", 65).setColor(BiomeGenBase.hell.color);
	public static final FLEBiome hell_crystalland = new FleBiomeHellCrystalLand("FLE Crystal Land", 66).setColor(BiomeGenBase.hell.color);
    
	static
	{
		try
		{
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76771_b", "ocean"), ocean, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76772_c", "plains"), warm_plains, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_76767_f", "forest"), warm_forest, false);
			Util.overrideStaticFinalField(BiomeGenBase.class, Arrays.asList("field_150583_P", "birchForest"), mid_forest, false);
		}
		catch (Exception e)
		{
			FleLog.getLogger().catching(e);
		}
		warm_plains_M = warm_plains.createMutation();
		BiomeDictionary.registerBiomeType(wasteland, BiomeDictionary.Type.DRY, BiomeDictionary.Type.HOT);
		BiomeDictionary.registerBiomeType(slope, BiomeDictionary.Type.WATER);
		BiomeDictionary.registerBiomeType(roofedForest_hill, BiomeDictionary.Type.HOT, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.FOREST);
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

	protected void genTargetBlockAt(int aLayer, float aTemp, Random aRand, Block[] aBlocks, byte[] aBytes, int targetID)
	{
		Block placeBlock;
		byte metadata;
		switch(aLayer)
		{
		case 0 : ;
		if(topBlock == null || topBlock.getMaterial() == Material.air)
		{
			placeBlock = aTemp < 0.15F ? Blocks.ice : Blocks.water;
			metadata = 0;
		}
		else
		{
			placeBlock = topBlock;
			metadata = (byte) (field_150604_aj & 255);
		}
		break;
		case 3 : ;
		if(fillerBlock == Blocks.sand)
		{
			placeBlock = Blocks.sandstone;
			metadata = 0;
			break;
		}
		case 1 : ;
		case 2 : ;
		placeBlock = fillerBlock;
		metadata = (byte) (field_150604_aj & 255);
		break;
		default: ;
		placeBlock = Blocks.stone;
		metadata = 0;
		}
		
		aBlocks[targetID] = placeBlock;
		aBytes[targetID] = metadata;
	}
	
	public boolean isBeach()
	{
		return false;
	}
	
	public boolean isOcean()
	{
		return false;
	}
	
	protected void genTerrainBlocks(Random rand, Block[] blocks, byte[] bytes, boolean isFlat, boolean isNonwaterTop, int rootHeight, int x, int z, int size, int height)
	{
        boolean waterFlag = false;
        int k = -1;
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
            else if(l1 <= 10 + rand.nextInt(6))
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
                        if(k == -1)
                        {
                        	if(rootHeight > 0)
                        	{
                                if (l1 >= height)
                                {
                                	if((!waterFlag || isFlat) && isNonwaterTop)
                                	{
                                		genTargetBlockAt(0, getFloatTemperature(x, l1, z), rand, blocks, bytes, i2);
                                        k = rootHeight;
                                	}
                                	else
                                	{
                                		genTargetBlockAt(1, getFloatTemperature(x, l1, z), rand, blocks, bytes, i2);
                                        k = isNonwaterTop ? rootHeight - 1 : rootHeight;
                                	}
                                }
                                else
                                {
                                	k = -2;
                                	continue;
                                }
                        	}
                        }
                        else if(k > 0)
                        {
                        	--k;
                        	if(k > rootHeight / 2)
                        	{
                        		genTargetBlockAt(1, getFloatTemperature(x, l1, z), rand, blocks, bytes, i2);
                        	}
                        	else if(k == 0)
                        	{
                        		genTargetBlockAt(3, getFloatTemperature(x, l1, z), rand, blocks, bytes, i2);
                        	}
                        	else
                        	{
                        		genTargetBlockAt(2, getFloatTemperature(x, l1, z), rand, blocks, bytes, i2);
                        	}
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
                           	waterFlag = true;
                    	}
                    }
                }
                else
                {
                    k = -1;
                }
            }
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

        genTerrainBlocks(aRand, aBlocks, aByte, flag2, flag1, l, i1, j1, k1, height - 1);
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