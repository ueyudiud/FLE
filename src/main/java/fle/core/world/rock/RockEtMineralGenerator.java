package fle.core.world.rock;

import java.util.Random;

import farcore.lib.world.biome.BiomeBase;
import farcore.util.ChunkBuilder;
import farcore.util.U;
import farcore.util.noise.NoiseBasic;
import farcore.util.noise.NoisePerlin;
import fle.core.block.BlockRock;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class RockEtMineralGenerator
{
	private static final int smoothSize = 2;
	private static final int smoothLength = 5;
    private static final float[] parabolicField = new float[smoothLength * smoothLength];
    
    static
    {
        for (int j = -smoothSize; j <= smoothSize; ++j)
        {
            for (int k = -smoothSize; k <= smoothSize; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
                parabolicField[j + smoothSize + (k + smoothSize) * smoothLength] = f;
            }
        }
    }
    
    private NoiseBasic octavesRandHeight;
	private NoiseBasic octavesPH;
	private NoiseBasic octavesRare;
	private NoiseBasic octavesCa;
	private NoiseBasic octavesSedimentary;
	private NoiseBasic octavesMetamorphism;
	private NoiseBasic octavesMineral;

	private double[] cacheHeight1;
	private double[] cacheHeight2;
	private double[] cacheHeight3;
	private double[] cachePH;
	private double[] cacheCa;
	private double[] cache3;
	private double[] cache4;
	private double[] cache5;
	private double[] cache6;
	
	private BiomeGenBase[] biomes;
	
	public RockEtMineralGenerator(Random random)
	{
		octavesRandHeight = new NoisePerlin(random.nextLong(), 5, 2D, 2D, 1.2D);
		octavesPH = new NoisePerlin(random.nextLong(), 8, 2D, 2D, 1.8D);
		octavesRare = new NoisePerlin(random.nextLong(), 8, 5D, 3D, 1.5D);
		octavesCa = new NoisePerlin(random.nextLong(), 8, 2D, 2D, 2D);
		octavesSedimentary = new NoisePerlin(random.nextLong(), 8, 5D, 3D, 1.5D);
		octavesMetamorphism = new NoisePerlin(random.nextLong(), 8, 5D, 3D, 1.5D);
		octavesMineral = new NoisePerlin(random.nextLong(), 8, 5D, 3D, 1.5D);
	}
	
	public void generateRockAndMineral(int x, int z, ChunkBuilder builder, WorldChunkManager manager)
	{
		cacheHeight1 = octavesRandHeight.noise(cacheHeight1, 16, 1, 16, x, 10D, z);
		cacheHeight2 = octavesRandHeight.noise(cacheHeight2, 16, 1, 16, x, 40D, z);
		cacheHeight3 = octavesRandHeight.noise(cacheHeight3, 16, 1, 16, x, 820D, z);
		cachePH = octavesPH.noise(cachePH, 16, 16, x, z);
		cacheCa = octavesCa.noise(cacheCa, 16, 16, x, z);
		for(int i = 0; i < 16; ++i)
			for(int j = 0; j < 16; ++j)
			{
				int c = 0;
				int id = 16 * j + i;
				int height1 = 24 + (int) (cacheHeight1[id] * 24);
				int height2 = 64 + (int) (cacheHeight2[id] * 24);
				int height3 = (int) (cacheHeight3[id] * cacheHeight3[id] * 160);
				RockLayer layer = provideLayer(cachePH[id], cacheCa[id]);
				for(int k = 255; k >= 0; --k)
				{
					Block block = builder.get(i, k, j);
					if(block == Blocks.stone)
					{
						++c;
						if(k < height3)
						{
							builder.add(i, k, j, layer.rocks[0]);
						}
						else if(k < height1)
						{
							builder.add(i, k, j, layer.rocks[2]);
						}
						else if(k < height2)
						{
							builder.add(i, k, j, layer.rocks[1]);
						}
						else
						{
							builder.add(i, k, j, layer.rocks[0]);
						}
					}
				}
			}
	}
	
	public double[] provideInfo(BiomeGenBase biome)
	{
		if(biome instanceof BiomeBase)
		{
			BiomeBase base = (BiomeBase) biome;
			return new double[]{base.rareMultiply, base.sedimentaryMultiply, base.metamorphismMultiply, 1D};
		}
		return new double[]{0.2D, 0D, 1D, 1D};
	}
	
	public RockLayer provideLayer(double ph, double ca)
	{
		RockLayer.init();
		if(ph > 0.8D)
		{
			return RockLayer.layerAcid;
		}
		else if(ph > 0.5)
		{
			return RockLayer.layerBasic;
		}
		else if(ph > 0.3)
		{
			return RockLayer.layerAcid;
		}
		return RockLayer.layerUltramafic;
	}
}