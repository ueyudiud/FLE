package fle.core.world.biome;

import java.util.Random;

import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenAcacia;
import farcore.lib.world.gen.tree.TreeGenCanopy;
import farcore.lib.world.gen.tree.TreeGenJungle;
import farcore.lib.world.gen.tree.TreeGenPine;
import farcore.lib.world.gen.tree.TreeGenPine2;
import farcore.lib.world.gen.tree.TreeGenShrub;
import farcore.lib.world.gen.tree.TreeGenSimple;
import fle.api.world.gen.TreeGenStraight;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomePlain extends BiomeBase
{
	private static boolean init = false;

	protected static final ITreeGenerator genOak1 = new TreeGenSimple(5, false);
	protected static final ITreeGenerator genOak2 = new TreeGenShrub();
	protected static final ITreeGenerator genCeiba1 = new TreeGenJungle(40, 18);
	protected static final ITreeGenerator genCeiba2 = new TreeGenJungle(55, 21);
	protected static final ITreeGenerator genCeiba3 = new TreeGenSimple(7, true);
	protected static final ITreeGenerator genBirch1 = new TreeGenSimple(5, false);
	protected static final ITreeGenerator genDarkOak = new TreeGenCanopy();
	protected static final ITreeGenerator genAspen1 = new TreeGenStraight(7, 4, 5, 8, 1.5F, true);
	protected static final ITreeGenerator genMorus1 = new TreeGenStraight(4, 3, 2, 5, 4.2F, true);
    protected static final ITreeGenerator genSpruce1 = new TreeGenPine();
	protected static final ITreeGenerator genSpruce2 = new TreeGenPine2();
	
	protected static final ITreeGenerator genAcacia = new TreeGenAcacia();
	
	protected static final WorldGenerator genVine = new WorldGenVines();
	protected static final WorldGenerator genMelon = new WorldGenMelon();
	protected static final WorldGenDesertWells genDesertwells = new WorldGenDesertWells();
	protected static final WorldGenBlockBlob genBlob = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);

	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("acacia");
			genAcacia.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("oak");
			genOak1.initLogBlock(wood);
			genOak2.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("ceiba");
			genCeiba1.initLogBlock(wood);
			genCeiba2.initLogBlock(wood);
			genCeiba3.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("birch");
			genBirch1.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("oak-black");
			genDarkOak.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("aspen");
			genAspen1.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("morus");
			genMorus1.initLogBlock(wood);
			wood = SubstanceWood.getSubstance("spruce");
			genSpruce1.initLogBlock(wood);
			genSpruce2.initLogBlock(wood);
			init = true;
		}
	}
	
	public BiomePlain(int id)
	{
		super(id);
		init();
        flowers.clear();
        addFlower(Blocks.red_flower,    4,  3);
        addFlower(Blocks.red_flower,    5,  3);
        addFlower(Blocks.red_flower,    6,  3);
        addFlower(Blocks.red_flower,    7,  3);
        addFlower(Blocks.red_flower,    0, 20);
        addFlower(Blocks.red_flower,    3, 20);
        addFlower(Blocks.red_flower,    8, 20);
        addFlower(Blocks.yellow_flower, 0, 30);
	}

    public String func_150572_a(Random rand, int x, int y, int z)
    {
        double d0 = customPlantNoise.noise((double)x / 200.0D, 0D, (double)z / 200.0D);
        int l;

        if (d0 < -0.8D)
        {
            l = rand.nextInt(4);
            return BlockFlower.field_149859_a[4 + l];
        }
        else if (rand.nextInt(3) > 0)
        {
            l = rand.nextInt(3);
            return l == 0 ? BlockFlower.field_149859_a[0] : (l == 1 ? BlockFlower.field_149859_a[3] : BlockFlower.field_149859_a[8]);
        }
        else
        {
            return BlockFlower.field_149858_b[0];
        }
    }

    protected void decorateFlower(World world, Random random, int x, int z)
    {
    	double d0 = customPlantNoise.noise((double)(x + 8) / 200.0D, 0D, (double)(z + 8) / 200.0D);
        int k;
        int l;
        int i1;
        int j1;

        if (d0 < -0.8D)
        {
//            this.biomeDecorator.flowersPerChunk = 15;
//            this.biomeDecorator.grassPerChunk = 5;
        }
        else
        {
//            this.biomeDecorator.flowersPerChunk = 4;
//            this.biomeDecorator.grassPerChunk = 10;
            genTallFlowers.func_150548_a(2);

            for (k = 0; k < 7; ++k)
            {
                l = x + random.nextInt(16) + 8;
                i1 = z + random.nextInt(16) + 8;
                j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
                genTallFlowers.generate(world, random, l, j1, i1);
            }
        }

        if (Math.abs(x * 3749174L + z * 37947191L) % 103 < 2)
        {
            genTallFlowers.func_150548_a(0);

            for (k = 0; k < 10; ++k)
            {
                l = x + random.nextInt(16) + 8;
                i1 = z + random.nextInt(16) + 8;
                j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
                genTallFlowers.generate(world, random, l, j1, i1);
            }
        }
    }
    
    protected void decorateJungle(World world, Random random, int x, int z)
    {
        int k = x + random.nextInt(16) + 8;
        int l = z + random.nextInt(16) + 8;
        int height = world.getHeightValue(k, l) * 2; //This was the original input for the nextInt below.  But it could == 0, which crashes nextInt
        if (height < 1) height = 1;
        int i1 = random.nextInt(height);
        genMelon.generate(world, random, k, i1, l);
        
        for (l = 0; l < 50; ++l)
        {
            i1 = x + random.nextInt(16) + 8;
            short short1 = 128;
            int j1 = z + random.nextInt(16) + 8;
            genVine.generate(world, random, i1, short1, j1);
        }
    }
    
    protected void decrateRoofedForest(World world, Random random, int x, int z)
    {
        int k;
        int l;
        int i1;
        int j1;
        int k1;
    	for (k = 0; k < 4; ++k)
        {
            for (l = 0; l < 4; ++l)
            {
                i1 = x + k * 4 + 1 + 8 + random.nextInt(3);
                j1 = z + l * 4 + 1 + 8 + random.nextInt(3);
                k1 = world.getHeightValue(i1, j1);

                if (random.nextInt(20) == 0)
                {
                    WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
                    worldgenbigmushroom.generate(world, random, i1, k1, j1);
                }
                else
                {
                    ITreeGenerator tree = getTreeGenerator(world, random, x, z);
	                tree.generate(world, random, x, world.getTopSolidOrLiquidBlock(x, z), z, true);
                }
            }
        }
    }
    
    public void decorate(World world, Random random, int x, int z)
    {
    	float rainfall = getRainfall(world, x, 0, z);
    	float temp = getTemperature(world, x, 0, z);
    	float plant = 0.5F + (float) customPlantNoise.noise(x, 0, z);
    	if(rainfall > 0.6F)
    	{
//    		biomeDecorator.deadBushPerChunk = 0;
//            biomeDecorator.reedsPerChunk = 6;
//            biomeDecorator.cactiPerChunk = 0;
    		if(temp > 1.3F)
    		{
    			decorateJungle(world, random, x, z);
//    	        biomeDecorator.treesPerChunk = 2;
//    	        biomeDecorator.flowersPerChunk = 1;
//    	        biomeDecorator.grassPerChunk = (int) (4 * plant);
//    	        biomeDecorator.rattanPerChunk = 1;
//    	        biomeDecorator.ivyPerChunk = 0;
//                biomeDecorator.brownMushroomsPerChunk = 0;
    		}
    		else if(temp > 1.0F)
    		{
    			decorateJungle(world, random, x, z);
//    	        biomeDecorator.treesPerChunk = 6;
//    	        biomeDecorator.flowersPerChunk = 3;
//    	        biomeDecorator.grassPerChunk = (int) (20 * plant);
//    	        biomeDecorator.rattanPerChunk = 1;
//    	        biomeDecorator.ivyPerChunk = 0;
//              biomeDecorator.brownMushroomsPerChunk = 0;
    		}
    		else if(temp > 0.6F)
    		{
    			if(plant > 1.1F)
    			{
    				decrateRoofedForest(world, random, x, z);
//        	        biomeDecorator.treesPerChunk = -999;
//        	        biomeDecorator.flowersPerChunk = 3;
//        	        biomeDecorator.grassPerChunk = (int) (3 * plant);
//        	        biomeDecorator.rattanPerChunk = 0;
//        	        biomeDecorator.ivyPerChunk = 1;
//                    biomeDecorator.brownMushroomsPerChunk = 2;
    			}
    			else
    			{
//        	        biomeDecorator.treesPerChunk = plant >= 1.0F ? (int) (8 * plant) : 8;
//        	        biomeDecorator.flowersPerChunk = 3;
//        	        biomeDecorator.grassPerChunk = (int) (3 * plant);
//        	        biomeDecorator.rattanPerChunk = 0;
//        	        biomeDecorator.ivyPerChunk = 1;
//                    biomeDecorator.brownMushroomsPerChunk = 0;
    			}
    		}
    		else if(temp > 0.05F)
    		{
//    	        biomeDecorator.treesPerChunk = 5;
//    	        biomeDecorator.flowersPerChunk = 1;
//    	        biomeDecorator.grassPerChunk = (int) (5 * plant);
//    	        biomeDecorator.rattanPerChunk = 0;
//    	        biomeDecorator.ivyPerChunk = 1;
//                biomeDecorator.brownMushroomsPerChunk = 1;
//    		}
//    		else
//    		{
//    	        biomeDecorator.treesPerChunk = -999;
//    	        biomeDecorator.flowersPerChunk = 0;
//    	        biomeDecorator.grassPerChunk = (int) (4 * plant);
//    	        biomeDecorator.rattanPerChunk = 0;
//    	        biomeDecorator.ivyPerChunk = 0;
//                biomeDecorator.brownMushroomsPerChunk = 0;
    		}
    	}
    	else if(rainfall > 0.2F)
    	{
//    		biomeDecorator.deadBushPerChunk = 0;
//            biomeDecorator.reedsPerChunk = 4;
//            biomeDecorator.cactiPerChunk = 0;
    		if(temp > 1.0F)
    		{
    	        genTallFlowers.func_150548_a(2);

    	        for (int k = 0; k < 7; ++k)
    	        {
    	            int l = x + random.nextInt(16) + 8;
    	            int i1 = z + random.nextInt(16) + 8;
    	            int j1 = random.nextInt(world.getHeightValue(l, i1) + 32);
    	            genTallFlowers.generate(world, random, l, j1, i1);
    	        }
//    	        biomeDecorator.treesPerChunk = 1;
//    	        biomeDecorator.flowersPerChunk = 4;
//    	        biomeDecorator.grassPerChunk = (int) (20 * plant);
//    	        biomeDecorator.rattanPerChunk = 1;
//    	        biomeDecorator.ivyPerChunk = 0;
//                biomeDecorator.brownMushroomsPerChunk = 0;
    		}
    		else if(temp > 0.5F)
    		{
//        		decorateFlower(world, random, x, z);
//                biomeDecorator.treesPerChunk = -999;
//                biomeDecorator.flowersPerChunk = 4;
//                biomeDecorator.grassPerChunk = (int) (10 * plant);
//    	        biomeDecorator.rattanPerChunk = 0;
//                biomeDecorator.ivyPerChunk = 0;
//                biomeDecorator.brownMushroomsPerChunk = 1;
//    		}
//    		else
//    		{
//                biomeDecorator.treesPerChunk = -999;
//                biomeDecorator.flowersPerChunk = 0;
//                biomeDecorator.grassPerChunk = (int) (2 * plant);
//    	        biomeDecorator.rattanPerChunk = 0;
//                biomeDecorator.ivyPerChunk = 0;
//                biomeDecorator.brownMushroomsPerChunk = 0;
    		}
    	}
    	else
    	{
//            biomeDecorator.reedsPerChunk = 50;
//            biomeDecorator.cactiPerChunk = 10;
//    		biomeDecorator.deadBushPerChunk = 3;
//            biomeDecorator.brownMushroomsPerChunk = 0;
//            biomeDecorator.treesPerChunk = -999;
//            biomeDecorator.flowersPerChunk = 0;
//            biomeDecorator.grassPerChunk = 0;
//	        biomeDecorator.rattanPerChunk = 0;
//            biomeDecorator.ivyPerChunk = 0;
            if(temp > 0.6F)
            {
                if (random.nextInt(1000) == 0)
                {
                    int k = x + random.nextInt(16) + 8;
                    int l = z + random.nextInt(16) + 8;
                    genDesertwells.generate(world, random, k, world.getHeightValue(k, l) + 1, l);
                }
            }
		}
    	
        super.decorate(world, random, x, z);
    }

    @Override
    protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
    {
    	float rainfall = getRainfall(world, x, 0, z);
    	float temp = getTemperature(world, x, 0, z);
    	float plant = 0.5F + (float) customPlantNoise.noise(x, 0, z);
    	if(rainfall > 0.2F)
    	{
    		if(temp > 1.3F)
    		{
        		if(rainfall > 0.6F)
        		{
        			return rand.nextInt(4) == 0 ? (treeNoise > 0.6F ? genCeiba1 : genCeiba2) :
        				rand.nextBoolean() ? genCeiba3 : rand.nextInt(4) == 0 ? genOak1 : genOak2;
        		}
        		else
        		{
        			return genAcacia;
        		}
    		}
    		else if(temp > 1.15F)
    		{
        		if(rainfall > 0.6F)
        		{
        			return rand.nextInt(4) == 0 ? genOak1 : genCeiba3;
        		}
        		else
        		{
        			return genAcacia;
        		}
    		}
    		else if(temp > 0.8F)
    		{
    			int v;
    			return plant > 1.1F ? rand.nextInt(5) == 0 ? genOak1 : genDarkOak :
    				treeNoise > 0.8F ? genCeiba3 : treeNoise > 0.5F ? genOak1 :
    					treeNoise > 0.3F ? rand.nextBoolean() ? genMorus1 : genAspen1 :
    						genBirch1;
    		}
    		else if(temp > 0.5F)
    		{
    			int v;
    			return plant > 1.1F ? rand.nextInt(5) == 0 ? genOak1 : genDarkOak :
    				treeNoise > 0.8F ? rainfall < 0.5F ? genOak2 : genOak1 : treeNoise > 0.5F ?
    						(rand.nextInt(3) == 0 ? genOak1 : treeNoise > 0.3F ?
    							((v = rand.nextInt(4)) == 0 ? genBirch1 :
    								v == 1 ? genAspen1 : 
    									v == 2 ? genMorus1 : genOak1) : genBirch1) :
    										genBirch1;
			}
    		else if(temp > 0.1F)
    		{
    			return rand.nextBoolean() ? genSpruce2 : genSpruce1;
    		}
    	}
    	return super.getTreeGenerator(world, rand, x, z, treeNoise);
    }

    @Override
    public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer,
    		float temp, float rainfall)
    {
    	if(temp > 0.3F)
    	{
    		if(rainfall > 0.23F)
    		{
        		topBlock = Blocks.grass;
        		topMeta = 0;
        		secondBlock = Blocks.dirt;
    		}
    		else if(rainfall > 0.1F)
    		{
    			if(rainfall * layer > 0.06F)
    			{
    				topBlock = Blocks.sand;
    				topMeta = 0;
    				secondBlock = Blocks.sand;
    			}
    			else
    			{
    				topBlock = Blocks.sand;
    				topMeta = 0;
    				secondBlock = layer > 0F ? Blocks.dirt : Blocks.sand;    				
    			}
    		}
    		else
    		{
    			topBlock = Blocks.sand;
    			topMeta = 0;
    			secondBlock = Blocks.sand;
			}
    	}
		else
		{
            secondBlock = Blocks.dirt;
            
            if (layer > 1.75D)
            {
                topBlock = Blocks.dirt;
                topMeta = 1;
            }
            else if (layer > -0.95D)
            {
                topBlock = Blocks.dirt;
                topMeta = 2;
            }
            else
            {
				topBlock = Blocks.grass;
				topMeta = 0;
			}
		}
    	super.genTerrainBlocks(world, rand, blocks, metas, x, z, layer, temp, rainfall);
    }
}