package fle.core.world.dim;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.NoiseGenerator;
import net.minecraft.world.gen.NoiseGeneratorImproved;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.NoiseGeneratorSimplex;

public class NGFLEBase extends NoiseGenerator
{
	private Random rand;
    private NoiseGeneratorImproved[] generatorCollection;
    private int octaves;
    private float decayLevel;
    
	public NGFLEBase(Random aRand, int size)
	{
		this(aRand, size, 2.0F);
	}
	public NGFLEBase(Random aRand, int size, float decay)
	{
		rand = aRand;
		octaves = size;
		generatorCollection = new NoiseGeneratorImproved[size];
		for(int i = 0; i < size; ++i)
			generatorCollection[i] = new NoiseGeneratorImproved(aRand);
		decayLevel = decay;
	}

	public double[] genNoise(double[] baseArray, int xOffset, int zOffset, int xSize, int zSize, double xLevel, double zLevel)
	{
		return genNoise(baseArray, xOffset, 10, zOffset, xSize, 1, zSize, xLevel, 0.5D, zLevel);
	}
	public double[] genNoise(double[] baseArray, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, double xLevel, double yLevel, double zLevel)
	{
		if(baseArray == null || baseArray.length < xSize * ySize * zSize)
		{
			baseArray = new double[xSize * ySize * zSize];
		}
		else
		{
			Arrays.fill(baseArray, 0.0D);
		}

        double d6 = 1.0D;

        for (int l1 = 0; l1 < octaves; ++l1)
        {
            double d3 = (double)xOffset * d6 * xLevel;
            double d4 = (double)yOffset * d6 * yLevel;
            double d5 = (double)zOffset * d6 * zLevel;
            long i2 = MathHelper.floor_double_long(d3);
            long j2 = MathHelper.floor_double_long(d5);
            d3 -= (double) i2;
            d5 -= (double) j2;
            i2 %= 0xFFFFFFFFL;
            j2 %= 0xFFFFFFFFL;
            d3 += (double) i2;
            d5 += (double) j2;
            generatorCollection[l1].populateNoiseArray(baseArray, d3, d4, d5, xSize, ySize, zSize, xLevel * d6, yLevel * d6, zLevel * d6, d6);
            d6 /= decayLevel;
        }

        return baseArray;
	}
}