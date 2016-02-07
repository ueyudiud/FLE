package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenMutated;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FLEBiomeForest extends FLEBiome
{
    private int type;
    protected static final WorldGenForest genTree1 = new WorldGenForest(false, true);
    protected static final WorldGenForest genTree2 = new WorldGenForest(false, false);
    protected static final WorldGenCanopyTree genTree3 = new WorldGenCanopyTree(false);
    
    public FLEBiomeForest(String name, int index, int aType)
	{
		super(name, index);
		topBlock = Blocks.grass;
		fillerBlock = Blocks.dirt;
		type = aType;
        theBiomeDecorator.treesPerChunk = 6;
        theBiomeDecorator.grassPerChunk = 2;

        if (type == 1)
        {
            theBiomeDecorator.treesPerChunk = 4;
            theBiomeDecorator.flowersPerChunk = 100;
            theBiomeDecorator.grassPerChunk = 1;
        }

        func_76733_a(5159473);
        setTemperatureRainfall(0.7F, 0.8F);

        if (type == 2)
        {
            field_150609_ah = 353825;
            color = 3175492;
            setTemperatureRainfall(0.6F, 0.6F);
        }

        if (type == 0)
        {
            spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }

        if (type == 3)
        {
            theBiomeDecorator.treesPerChunk = -999;
        }

        if (type == 1)
        {
            flowers.clear();
            for (int x = 0; x < BlockFlower.field_149859_a.length; x++)
            {
                addFlower(Blocks.red_flower, x == 1 ? 0 : x, 10);
            }
        }
	} 
	
	public BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_)
    {
        if (type == 2)
        {
            field_150609_ah = 353825;
            color = p_150557_1_;

            if (p_150557_2_)
            {
                field_150609_ah = (field_150609_ah & 16711422) >> 1;
            }

            return this;
        }
        else
        {
            return super.func_150557_a(p_150557_1_, p_150557_2_);
        }
    }

	@Override
	public WorldGenAbstractTree getTreeGenerator(double noise, Random rand)
	{
		switch(type)
		{
		case 3 :
			return rand.nextInt(3) > 0 ? genTree3 : noise > 0.7 ? genTree2 : genTree1;
		case 2 :
			return genTree2;
		default:
			return noise > 0.7 ? genTree2 : worldGeneratorTrees;
		}
	}

    public String func_150572_a(Random aRand, int x, int y, int z)
    {
        if (type == 1)
        {
            double d0 = MathHelper.clamp_double((1.0D + plantNoise.func_151601_a((double)x / 48.0D, (double)z / 48.0D)) / 2.0D, 0.0D, 0.9999D);
            int l = (int)(d0 * (double)BlockFlower.field_149859_a.length);

            if (l == 1)
            {
                l = 0;
            }

            return BlockFlower.field_149859_a[l];
        }
        else
        {
            return super.func_150572_a(aRand, x, y, z);
        }
    }

    public void decorate(World aWorld, Random aRand, int x, int z)
    {
        int k;
        int l;
        int i1;
        int j1;
        int k1;

        if (type == 3)
        {
            for (k = 0; k < 4; ++k)
            {
                for (l = 0; l < 4; ++l)
                {
                    i1 = x + k * 4 + 1 + 8 + aRand.nextInt(3);
                    j1 = z + l * 4 + 1 + 8 + aRand.nextInt(3);
                    k1 = aWorld.getHeightValue(i1, j1);

                    if (aRand.nextInt(20) == 0)
                    {
                    	//Remove big mushroom generate.
                        //WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
                        //worldgenbigmushroom.generate(aWorld, aRand, i1, k1, j1);
                    }
                    else
                    {
                        WorldGenAbstractTree worldgenabstracttree = getTreeGenerator(x, z, aRand);
                        worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

                        if (worldgenabstracttree.generate(aWorld, aRand, i1, k1, j1))
                        {
                            worldgenabstracttree.func_150524_b(aWorld, aRand, i1, k1, j1);
                        }
                    }
                }
            }
        }

        k = aRand.nextInt(5) - 3;

        if (type == 1)
        {
            k += 2;
        }

        l = 0;

        while (l < k)
        {
            i1 = aRand.nextInt(3);

            if (i1 == 0)
            {
                genTallFlowers.func_150548_a(1);
            }
            else if (i1 == 1)
            {
                genTallFlowers.func_150548_a(4);
            }
            else if (i1 == 2)
            {
                genTallFlowers.func_150548_a(5);
            }

            j1 = 0;

            while (true)
            {
                if (j1 < 5)
                {
                    k1 = x + aRand.nextInt(16) + 8;
                    int i2 = z + aRand.nextInt(16) + 8;
                    int l1 = aRand.nextInt(aWorld.getHeightValue(k1, i2) + 32);

                    if (!genTallFlowers.generate(aWorld, aRand, k1, l1, i2))
                    {
                        ++j1;
                        continue;
                    }
                }

                ++l;
                break;
            }
        }

        super.decorate(aWorld, aRand, x, z);
    }
    
    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int x, int y, int z)
    {
        int l = super.getBiomeGrassColor(x, y, z);
        return type == 3 ? (l & 16711422) + 2634762 >> 1 : l;
    }

    /**
     * Creates a mutated version of the biome and places it into the biomeList with an index equal to the original plus
     * 128
     */
    public BiomeGenBase createMutation()
    {
        if (biomeID == FLEBiome.forest.biomeID)
        {
            FLEBiomeForest biomegenforest = new FLEBiomeForest("FLE Flower Forest", biomeID + 128, 1);
            biomegenforest.setHeight(new BiomeGenBase.Height(rootHeight, heightVariation + 0.2F));
            biomegenforest.func_150557_a(6976549, true);
            biomegenforest.func_76733_a(8233509);
            return biomegenforest;
        }
        else
        {
            return biomeID != BiomeGenBase.birchForest.biomeID && biomeID != BiomeGenBase.birchForestHills.biomeID ? 
            		new BiomeGenMutated(biomeID + 128, this)
            {
                public void decorate(World aWorld, Random aRand, int x, int z)
                {
                    baseBiome.decorate(aWorld, aRand, x, z);
                }
            }: 
            	new BiomeGenMutated(biomeID + 128, this)
            {
                public WorldGenAbstractTree func_150567_a(Random aRand)
                {
                    return aRand.nextBoolean() ? FLEBiomeForest.genTree1 : FLEBiomeForest.genTree2;
                }
            };
        }
    }
    
    @Override
    protected Block getBlock(boolean isFirstTop, boolean isNoCover,
    		boolean hasFluidOnSide, boolean isBaseDecorateBlock,
    		Block replaceBlock, Random rand, float temp)
    {
    	return isBaseDecorateBlock ? Blocks.gravel : super.getBlock(isFirstTop, isNoCover, hasFluidOnSide, isBaseDecorateBlock, replaceBlock, rand, temp);
    }
}