package fle.core.world.biome;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenHills;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenTaiga2;
import net.minecraft.world.gen.feature.WorldGenerator;

public class FLEBiomeHills extends FLEBiome
{
    private WorldGenerator theWorldGenerator;
    private WorldGenTaiga2 treeGen;
    private int a;
    private int b;
    private int c;
    private int d;

	public FLEBiomeHills(String name, int index, boolean flag)
	{
		super(name, index);
        this.theWorldGenerator = new WorldGenMinable(Blocks.monster_egg, 8);
        this.treeGen = new WorldGenTaiga2(false);
        this.a = 0;
        this.b = 1;
        this.c = 2;
        this.d = this.a;

        if (flag)
        {
            this.theBiomeDecorator.treesPerChunk = 3;
            this.d = this.b;
        }
    }

    public WorldGenAbstractTree func_150567_a(Random p_150567_1_)
    {
        return (WorldGenAbstractTree)(p_150567_1_.nextInt(3) > 0 ? this.treeGen : super.func_150567_a(p_150567_1_));
    }

    public void decorate(World aWorld, Random aRand, int x, int z)
    {
        super.decorate(aWorld, aRand, x, z);
        /**
        int k = 3 + aRand.nextInt(6);
        int l;
        int i1;
        int j1;

        for (k = 0; k < 7; ++k)
        {
            l = x + aRand.nextInt(16);
            i1 = aRand.nextInt(64);
            j1 = z + aRand.nextInt(16);
            this.theWorldGenerator.generate(aWorld, aRand, l, i1, j1);
        }
         */
    }

    public void genTerrainBlocks(World aWorld, Random aRand, Block[] aBlocks, byte[] aByte, int x, int z, double yLevel)
    {
        this.topBlock = Blocks.grass;
        this.field_150604_aj = 0;
        this.fillerBlock = Blocks.dirt;

        if ((yLevel < -1.0D || yLevel > 2.0D) && this.d == this.c)
        {
            this.topBlock = Blocks.gravel;
            this.fillerBlock = Blocks.gravel;
        }
        else if (yLevel > 1.0D && this.d != this.b)
        {
            this.topBlock = Blocks.stone;
            this.fillerBlock = Blocks.stone;
        }
        
        super.genTerrainBlocks(aWorld, aRand, aBlocks, aByte, x, z, yLevel);
    }

    /**
     * this creates a mutation specific to Hills biomes
     */
    public FLEBiomeHills mutateHills(BiomeGenBase biome)
    {
        this.d = this.c;
        this.func_150557_a(biome.color, true);
        this.setBiomeName(biome.biomeName + " M");
        this.setHeight(new BiomeGenBase.Height(biome.rootHeight, biome.heightVariation));
        this.setTemperatureRainfall(biome.temperature, biome.rainfall);
        return this;
    }

    /**
     * Creates a mutated version of the biome and places it into the biomeList with an index equal to the original plus
     * 128
     */
    public BiomeGenBase createMutation()
    {
        return (new BiomeGenHills(this.biomeID + 128, false)).mutateHills(this);
    }
}