package fle.core.world.biome;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.interfaces.ITreeGenerator;
import farcore.lib.substance.SubstanceWood;
import farcore.lib.world.biome.BiomeBase;
import farcore.lib.world.gen.tree.TreeGenCanopy;
import farcore.lib.world.gen.tree.TreeGenSimple;
import fle.api.world.gen.TreeGenStraight;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraft.world.gen.feature.WorldGenForest;

public class BiomeForest extends BiomeBase
{
//    protected static final WorldGenForest genHighTree = new WorldGenForest(false, true);
//    protected static final WorldGenForest genLowTree = new WorldGenForest(false, false);
//    protected static final WorldGenCanopyTree genCanopyTree = new WorldGenCanopyTree(false);

	private static boolean init = false;
	
	protected static final ITreeGenerator genOak1 = new TreeGenSimple(5, false);
	protected static final ITreeGenerator genBirch1 = new TreeGenSimple(5, false);
	protected static final ITreeGenerator genDarkOak = new TreeGenCanopy();
	protected static final ITreeGenerator genAspen1 = new TreeGenStraight(7, 4, 5, 8, 1.5F, true);
	protected static final ITreeGenerator genMorus1 = new TreeGenStraight(4, 3, 2, 5, 4.2F, true);
	
	private void init()
	{
		if(!init)
		{
			SubstanceWood wood = SubstanceWood.getSubstance("oak");
			genOak1.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("birch");
			genBirch1.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("oak-black");
			genDarkOak.initLogBlock(wood.leaves, wood.leaves);
			wood = SubstanceWood.getSubstance("aspen");
			genAspen1.initLogBlock(wood.log, wood.leaves);
			wood = SubstanceWood.getSubstance("morus");
			genMorus1.initLogBlock(wood.log, wood.leaves);
			init = true;
		}
	}
	
    private int type;
	
    public BiomeForest(int id, int type)
	{
		super(id);
		init();
        this.type = type;
        this.biomeDecorator.treesPerChunk = 10;
        this.theBiomeDecorator.grassPerChunk = 2;

        if (this.type == 1)
        {
            this.biomeDecorator.treesPerChunk = 6;
            this.biomeDecorator.flowersPerChunk = 100;
            this.biomeDecorator.grassPerChunk = 1;
        }

        func_76733_a(0x4EBA31);

        if (this.type == 2)
        {
            field_150609_ah = 353825;
            color = 3175492;
        }

        if (this.type == 0)
        {
            this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
        }

        if (this.type == 3)
        {
            this.biomeDecorator.treesPerChunk = -999;
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

    public BiomeGenBase func_150557_a(int color, boolean flag)
    {
        if (this.type == 2)
        {
            this.field_150609_ah = 353825;
            this.color = color;

            if (flag)
            {
                this.field_150609_ah = (this.field_150609_ah & 16711422) >> 1;
            }

            return this;
        }
        else
        {
            return super.func_150557_a(color, flag);
        }
    }

    @Override
    protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
    {
    	if(type == 3)
    	{
    		return rand.nextInt(5) == 0 ? genOak1 : genDarkOak;
    	}
    	if(treeNoise > 0.8)
    	{
    		return genBirch1;
    	}
    	else if(treeNoise < -0.6)
    	{
    		return genOak1;
    	}
    	switch (rand.nextInt(6))
    	{
		case 0 : return genAspen1;
		case 1 : return genMorus1;
		case 2 : 
		case 3 : return genOak1;
		case 4 : return genBirch1;
		default: return genOak1;
		}
    }
    
//    @Deprecated
//    public WorldGenAbstractTree func_150567_a(Random random)
//    {
//        return (WorldGenAbstractTree)(this.type == 3 && random.nextInt(3) > 0 ? genCanopyTree : (this.type != 2 && random.nextInt(5) != 0 ? worldGeneratorTrees : genLowTree));
//    }

    public String func_150572_a(Random random, int x, int y, int z)
    {
        if (this.type == 1)
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
            return super.func_150572_a(random, x, y, z);
        }
    }

    public void decorate(World world, Random random, int x, int z)
    {
        int k;
        int l;
        int i1;
        int j1;
        int k1;

        if (this.type == 3)
        {
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
                        WorldGenAbstractTree worldgenabstracttree = this.func_150567_a(random);
                        worldgenabstracttree.setScale(1.0D, 1.0D, 1.0D);

                        if (worldgenabstracttree.generate(world, random, i1, k1, j1))
                        {
                            worldgenabstracttree.func_150524_b(world, random, i1, k1, j1);
                        }
                    }
                }
            }
        }

        k = random.nextInt(5) - 3;

        if (this.type == 1)
        {
            k += 2;
        }

        l = 0;

        while (l < k)
        {
            i1 = random.nextInt(3);

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
                	k1 = x + random.nextInt(16) + 8;
                    int i2 = z + random.nextInt(16) + 8;
                    int l1 = random.nextInt(world.getHeightValue(k1, i2) + 32);

                    if (!genTallFlowers.generate(world, random, k1, l1, i2))
                    {
                        ++j1;
                        continue;
                    }
                }

                ++l;
                break;
            }
        }

        super.decorate(world, random, x, z);
    }

    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int x, int y, int z)
    {
        int l = super.getBiomeGrassColor(x, y, z);
        return this.type == 3 ? (l & 16711422) + 2634762 >> 1 : l;
    }
}