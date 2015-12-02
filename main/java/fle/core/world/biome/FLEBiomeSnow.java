package fle.core.world.biome;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.BiomeGenSnow;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenIcePath;
import net.minecraft.world.gen.feature.WorldGenIceSpike;
import net.minecraft.world.gen.feature.WorldGenTaiga2;

public class FLEBiomeSnow extends FLEBiome
{
    private boolean flag;
    private WorldGenIceSpike spikeGen = new WorldGenIceSpike();
    private WorldGenIcePath pathGen = new WorldGenIcePath(4);

	public FLEBiomeSnow(String name, int index, boolean flag)
	{
		super(name, index);
        this.flag = flag;

        if (flag)
        {
            this.topBlock = Blocks.snow;
        }

        this.spawnableCreatureList.clear();
	}

	public void decorate(World aWorld, Random aRand, int x, int z)
    {
        if (this.flag)
        {
            int k;
            int l;
            int i1;

            for (k = 0; k < 3; ++k)
            {
                l = x + aRand.nextInt(16) + 8;
                i1 = z + aRand.nextInt(16) + 8;
                this.spikeGen.generate(aWorld, aRand, l, aWorld.getHeightValue(l, i1), i1);
            }

            for (k = 0; k < 2; ++k)
            {
                l = x + aRand.nextInt(16) + 8;
                i1 = z + aRand.nextInt(16) + 8;
                this.pathGen.generate(aWorld, aRand, l, aWorld.getHeightValue(l, i1), i1);
            }
        }

        super.decorate(aWorld, aRand, x, z);
    }

    public WorldGenAbstractTree func_150567_a(Random aRand)
    {
        return new WorldGenTaiga2(false);
    }

    /**
     * Creates a mutated version of the biome and places it into the biomeList with an index equal to the original plus
     * 128
     */
    public BiomeGenBase createMutation()
    {
        BiomeGenBase biomegenbase = (new BiomeGenSnow(this.biomeID + 128, true)).func_150557_a(13828095, true).setBiomeName(this.biomeName + " Spikes").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(new BiomeGenBase.Height(this.rootHeight + 0.1F, this.heightVariation + 0.1F));
        biomegenbase.rootHeight = this.rootHeight + 0.3F;
        biomegenbase.heightVariation = this.heightVariation + 0.4F;
        return biomegenbase;
    }
}