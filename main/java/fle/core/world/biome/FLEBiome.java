package fle.core.world.biome;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;
import fle.api.util.FleLog;
import fle.core.util.Util;
import fle.core.world.dim.FLEBiomeDecoratorBase;

public class FLEBiome extends BiomeGenBase
{
	public static final FLEBiome ocean = new FLEBiomeOcean("FLE Ocean", BiomeGenBase.ocean.biomeID).setColor(112).setH(height_Oceans);
	public static final FLEBiome warm_plains = new FLEBiomePlains("FLE Plains", BiomeGenBase.plains.biomeID).setColor(9286496);
	public static final FLEBiome warm_forest = new FLEBiomeForest("FLE Forest", BiomeGenBase.forest.biomeID, 0).setColor(353825);
	public static final FLEBiome mid_forest = new FLEBiomeForest("FLE Forest B", BiomeGenBase.birchForest.biomeID, 2).setColor(3175492);
	
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
		warm_plains.createMutation();
	}
	
	int mColor;
	
	public FLEBiome(String name, int index)
	{
		super(index);
		setBiomeName(name);
	}
	public FLEBiome(String name, int index, boolean reg)
	{
		super(index, reg);
		setBiomeName(name);
	}
	
	@Override
	public FLEBiome setColor(int color)
	{
		mColor = color;
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
	
	@Override
	public void genTerrainBlocks(World aWorld, Random aRand,
			Block[] aBlocks, byte[] aByte, int x,
			int z, double yLevel)
	{
        boolean flag = true;
        int k = -1;
        int l = (int)(yLevel * 3.0D + 3.0D + aRand.nextDouble() * 0.25D);
        int i1 = x & 15;
        int j1 = z & 15;
        int k1 = aBlocks.length / 256;
        short height = (short) aWorld.getWorldInfo().getTerrainType().getHorizon(aWorld);

        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (j1 * 16 + i1) * k1 + l1;

            if (l1 <= 0 + aRand.nextInt(2))
            {
                aBlocks[i2] = Blocks.bedrock;
            }
            else if(l1 <= 8)
            {
            	aBlocks[i2] = Blocks.lava;
            }
            else if(l1 <= 10 + aRand.nextInt(6))
            {
            	aBlocks[i2] = Blocks.air;
            }
            else
            {
                Block block2 = aBlocks[i2];
                if (block2 != null && block2.getMaterial() != Material.air)
                {
                    if (block2 == Blocks.stone)
                    {
                        if(k == -1)
                        {
                        	if(l > 0)
                        	{
                                if (l1 >= height / 2)
                                {
                                	if(flag)
                                	{
                                		genTargetBlockAt(0, getFloatTemperature(x, l1, z), aRand, aBlocks, aByte, i2);
                                        k = l;
                                	}
                                	else
                                	{
                                		genTargetBlockAt(1, getFloatTemperature(x, l1, z), aRand, aBlocks, aByte, i2);
                                        k = l - 2;
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
                        	if(k > l / 2)
                        	{
                        		genTargetBlockAt(1, getFloatTemperature(x, l1, z), aRand, aBlocks, aByte, i2);
                        	}
                        	else if(k == 0)
                        	{
                        		genTargetBlockAt(3, getFloatTemperature(x, l1, z), aRand, aBlocks, aByte, i2);
                        	}
                        	else
                        	{
                        		genTargetBlockAt(2, getFloatTemperature(x, l1, z), aRand, aBlocks, aByte, i2);
                        	}
                        }
                    }
                    else if((block2.getMaterial() == Material.water || block2.getMaterial() == Material.lava) && flag)
                    {
                    	if(getFloatTemperature(x, l1, z) < 0.15F) aBlocks[i2] = Blocks.ice;
                    	flag = false;
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
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
	{
		return new WorldGenTallGrass(Blocks.tallgrass, 1);
	}
	
	@Override
	public BiomeDecorator createBiomeDecorator()
	{
		return new FLEBiomeDecoratorBase();
	}
	
	@Override
	public void decorate(World aWorld, Random aRNG, int x,
			int z)
	{
		super.decorate(aWorld, aRNG, x, z);
	}
}