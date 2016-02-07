package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenTaiga;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenTaiga1;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenTallGrass;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FLEBiomeTaiga extends FLEBiome
{
    private static final WorldGenTaiga1 taiga1 = new WorldGenTaiga1();
    private static final WorldGenTaiga2 taiga2 = new WorldGenTaiga2(false);
    private static final WorldGenMegaPineTree pine1 = new WorldGenMegaPineTree(false, false);
    private static final WorldGenMegaPineTree pine2 = new WorldGenMegaPineTree(false, true);
    private static final WorldGenBlockBlob blob = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
//    private FleTreeCardGen beechGen = new FleTreeCardGen(Plants.beech);
    private int type;
    
	public FLEBiomeTaiga(String name, int index, int aType)
	{
		super(name, index);
        this.type = aType;
        this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityWolf.class, 8, 4, 4));
        this.theBiomeDecorator.treesPerChunk = 6;

        if (aType != 1 && aType != 2)
        {
            this.theBiomeDecorator.grassPerChunk = 1;
            this.theBiomeDecorator.mushroomsPerChunk = 1;
        }
        else
        {
            this.theBiomeDecorator.grassPerChunk = 7;
            this.theBiomeDecorator.deadBushPerChunk = 1;
            this.theBiomeDecorator.mushroomsPerChunk = 3;
        }
	}

	@Override
	public WorldGenAbstractTree getTreeGenerator(double noise, Random rand)
	{
		return ((this.type == 1 || this.type == 2) && rand.nextInt(3) == 0 ? 
				(this.type != 2 && rand.nextInt(13) != 0 ? pine1 : pine2) : rand.nextInt(3) == 0 ? taiga1 : taiga2);
					//(noise > 0.7 ? beechGen : rand.nextInt(3) == 0 ? taiga1 : taiga2));
	}

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForGrass(Random rand)
    {
        return rand.nextInt(5) > 0 ? new WorldGenTallGrass(Blocks.tallgrass, 2) : new WorldGenTallGrass(Blocks.tallgrass, 1);
    }

    public void decorate(World aWorld, Random aRand, int x, int z)
    {
        int k;
        int l;
        int i1;
        int j1;

        if (this.type == 1 || this.type == 2)
        {
            k = aRand.nextInt(3);

            for (l = 0; l < k; ++l)
            {
                i1 = x + aRand.nextInt(16) + 8;
                j1 = z + aRand.nextInt(16) + 8;
                int k1 = aWorld.getHeightValue(i1, j1);
                blob.generate(aWorld, aRand, i1, k1, j1);
            }
        }

        genTallFlowers.func_150548_a(3);

        for (k = 0; k < 7; ++k)
        {
            l = x + aRand.nextInt(16) + 8;
            i1 = z + aRand.nextInt(16) + 8;
            j1 = aRand.nextInt(aWorld.getHeightValue(l, i1) + 32);
            genTallFlowers.generate(aWorld, aRand, l, j1, i1);
        }

        super.decorate(aWorld, aRand, x, z);
    }

    public void genTerrainBlocks(World aWorld, Random aRand, Block[] blocks, byte[] meta, int x, int z, double h)
    {
        if (this.type == 1 || this.type == 2)
        {
            this.topBlock = Blocks.grass;
            this.field_150604_aj = 0;
            this.fillerBlock = Blocks.dirt;

            if (h > 1.75D)
            {
                this.topBlock = Blocks.dirt;
                this.field_150604_aj = 1;
            }
            else if (h > -0.95D)
            {
                this.topBlock = Blocks.dirt;
                this.field_150604_aj = 2;
            }
        }

        this.genBiomeTerrain(aWorld, aRand, blocks, meta, x, z, h);
    }

    /**
     * Creates a mutated version of the biome and places it into the biomeList with an index equal to the original plus
     * 128
     */
    public BiomeGenBase createMutation()
    {
        return this.biomeID == BiomeGenBase.megaTaiga.biomeID ? (new BiomeGenTaiga(this.biomeID + 128, 2)).func_150557_a(5858897, true).setBiomeName("Mega Spruce Taiga").func_76733_a(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(new BiomeGenBase.Height(this.rootHeight, this.heightVariation)) : super.createMutation();
    }
}