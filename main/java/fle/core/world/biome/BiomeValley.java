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
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.world.gen.feature.WorldGenMelon;
import net.minecraft.world.gen.feature.WorldGenVines;
import net.minecraft.world.gen.feature.WorldGenerator;

public class BiomeValley extends BiomeBase
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

	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("acacia");
			genAcacia.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("oak");
			genOak1.initLogBlock(wood.log, wood.leaves);
			genOak2.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("ceiba");
			genCeiba1.initLogBlock(wood.log, wood.leaves);
			genCeiba2.initLogBlock(wood.log, wood.leaves);
			genCeiba3.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("birch");
			genBirch1.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("oak-black");
			genDarkOak.initLogBlock(wood.leaves, wood.leaves);
			wood = SubstanceWood.getSubstance("aspen");
			genAspen1.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("morus");
			genMorus1.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("spruce");
			genSpruce1.initLogBlock(wood.log, wood.leaves);
			genSpruce2.initLogBlock(wood.log, wood.leaves);
			init = true;
		}
	}
	
	public BiomeValley(int id)
	{
		super(id);
		init();
		biomeDecorator.treesPerChunk = 1;
		biomeDecorator.brownMushroomsPerChunk = 2;
		biomeDecorator.flowersPerChunk = -999;
		biomeDecorator.ivyPerChunk = 1;
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
        		if(rainfall > 0.7F)
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
        		if(rainfall > 0.7F)
        		{
        			return rand.nextInt(4) == 0 ? genOak1 : genCeiba3;
        		}
        		else
        		{
        			return genAcacia;
        		}
    		}
    		else if(temp > 0.9F)
    		{
    			int v;
    			return plant > 1.1F ? rand.nextInt(5) == 0 ? genOak1 : genDarkOak :
    				treeNoise > 0.8F ? genCeiba3 : treeNoise > 0.5F ? genOak1 :
    					treeNoise > 0.3F ? rand.nextBoolean() ? genMorus1 : genAspen1 :
    						genBirch1;
    		}
    		else if(temp > 0.6F)
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
    		else if(temp > 0.3F)
    		{
    			return rand.nextBoolean() ? genSpruce2 : genSpruce1;
    		}
    	}
    	return super.getTreeGenerator(world, rand, x, z, treeNoise);
    }
}