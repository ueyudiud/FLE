package fle.core.world.dim;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import flapi.util.FleLog;
import fle.core.util.noise.NoiseBase;
import fle.core.util.noise.NoiseFuzzy;
import fle.core.util.noise.NoiseGauss;
import fle.core.util.noise.NoiseMix;
import fle.core.util.noise.NoiseMontain;
import fle.core.util.noise.NoisePerlin;
import fle.core.util.noise.NoiseSmooth;
import fle.core.util.noise.VecNoiseBase;
import fle.core.util.noise.VecNoiseHandler;
import fle.core.util.noise.VecNoisePerlin;
import fle.core.util.noise.VecNoiseSimple;
import fle.core.world.FWM;
import fle.core.world.biome.FLEBiome;
import fle.core.world.layer.FLELayer;
import fle.resource.world.FleCavesGen;

public class FLESurfaceChunkProvider extends ChunkProviderGenerate
{
	private Random rand;
	private World worldObj;
	private BiomeGenBase[] biomesForGeneration;
	private Block[] idsBig;
	private byte[] metaBig;
	/** For generate **/
    public NoiseBase baseNoise;
	/** For mountain generate **/
    public NoiseBase mountainNoise;
    public VecNoiseBase vecNoise;
    public VecNoiseBase vecMountainNoise;
    /** For rock layer generate **/
    public NoiseBase noiseGen3;
    public NoiseBase noiseGen4;
    private MapGenBase caveGenerator = new FleCavesGen();
    private MapGenBase ravineGenerator = new MapGenRavine();
	private double[] rockHeightCache;
	private double[] heightCache;
	private static final float[] parabolicField;
	static
	{
		parabolicField = new float[25];
        for (int j = -2; j <= 2; ++j)
        {
            for (int k = -2; k <= 2; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
                parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }
	}
	
	public FLESurfaceChunkProvider(World aWorld, long aSeed,
			boolean flag)
	{
		super(aWorld, aSeed, flag);
		worldObj = aWorld;
		rand = new Random(aSeed);
		baseNoise = new NoiseMix(aSeed * 17591759721L + 5719571923L, 
				new NoiseFuzzy(1L, 4, 1.6D, 3.2D, 0.4F), 
				new NoiseSmooth(
						new NoiseGauss(1L, 3, 
								new NoiseSmooth(18F, 
										new NoisePerlin(1L)))));
		mountainNoise = new NoiseMontain(aSeed * 37591741L, 14, 2, 1.0F);
		vecNoise = new VecNoisePerlin(aSeed * 20482229L + 3759180L, 4, 1.0F);
		vecMountainNoise = new VecNoiseSimple(aSeed * 75917L + 38491L, 0.3F);
		noiseGen3 = new NoiseSmooth(119, new NoisePerlin(aSeed * 1829471L + 73957293L, 4));
		idsBig = new Block[65536];
		metaBig = new byte[65536];
        heightCache = new double[25];
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{
        /**
        if (this.mapFeaturesEnabled)
        {
            this.mineshaftGenerator.func_151539_a(this, this.worldObj, x, z, ablock);
            this.villageGenerator.func_151539_a(this, this.worldObj, x, z, ablock);
            this.strongholdGenerator.func_151539_a(this, this.worldObj, x, z, ablock);
            this.scatteredFeatureGenerator.func_151539_a(this, this.worldObj, x, z, ablock);
        }
         */
		FWM.generateFlag = false;
        rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        getTerrinHeight(heightCache, x * 4, z * 4);
		fillBlockIn();
		biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x * 16, z * 16, 16, 16);
		replaceBlocksForBiome(x, z, idsBig, metaBig, biomesForGeneration);
		caveGenerator.func_151539_a(this, worldObj, x, z, idsBig);
		ravineGenerator.func_151539_a(this, worldObj, x, z, idsBig);
		Chunk chunk = new Chunk(worldObj, idsBig, metaBig, x, z);
        byte[] abyte1 = chunk.getBiomeArray();

        for (int k = 0; k < abyte1.length; ++k)
        {
            abyte1[k] = (byte) biomesForGeneration[k].biomeID;
        }
        chunk.generateSkylightMap();
        FWM.generateFlag = true;
        return chunk;
	}

	@Override
    public void replaceBlocksForBiome(int x, int z, Block[] aBlock, byte[] aMeta, BiomeGenBase[] aBiome)
    {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, x, z, aBlock, aMeta, aBiome, worldObj);
        
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        rockHeightCache = noiseGen3.noise(rockHeightCache, x * 16, z * 16, 16, 16);
        FLEBiome.setRockNoiseSeed(worldObj.getSeed());
        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                BiomeGenBase biomegenbase = aBiome[l + k * 16];
                biomegenbase.genTerrainBlocks(worldObj, rand, aBlock, aMeta, x * 16 + k, z * 16 + l, rockHeightCache[l + k * 16]);
            }
        }
    }
	
	@Override
	public void populate(IChunkProvider provider, int x, int z)
	{
		FWM.generateFlag = false;
		super.populate(provider, x, z);
		FWM.generateFlag = true;
	}
	
	public void fillBlockIn()
	{
		Arrays.fill(idsBig, Blocks.air);
        short b0 = (short) MathHelper.ceiling_double_int((worldObj.provider.getHorizon() + 1));
        
        for (int x0 = 0; x0 < 4; ++x0)
        {
        	for (int y0 = 0; y0 < 4; ++y0)
        	{
        		double d0 = 0.25D;
                double d1 = heightCache[x0     + 5 * y0];
                double d2 = heightCache[x0 + 1 + 5 * y0];
                double d3 = heightCache[x0     + 5 * (y0 + 1)];
                double d4 = heightCache[x0 + 1 + 5 * (y0 + 1)];
                double d5 = (d2 - d1) * d0;
                double d6 = (d4 - d3) * d0;
                for(int x1 = 0; x1 < 4; ++x1)
                {
                	double d7 = d1;
                	double d8 = d3;
                	double d9 = (d8 - d7) * d0;
                	for(int y1 = 0; y1 < 4; ++y1)
                	{
                		int height = (int) d7 + b0;
                		int idOffset = x0 * 4 + x1 << 12 | y0 * 4 + y1 << 8;
                		if(height >= 256) height = 255;
                		for(int k = Math.max(height, b0); k >= 0; --k)
                		{
                    		if (height - k >= 0)
                            {
                                idsBig[idOffset + k] = Blocks.stone;
                            }
                            else
                            {
                            	idsBig[idOffset + k] = Blocks.water;
                            }
                		}
                		d7 += d9;
                	}
                	d1 += d5;
                	d3 += d6;
                }
        	}
        }
	}

	public void getTerrinHeight(double[] heights, int x, int z)
	{
		Arrays.fill(heights, 0);
		biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, x - 5, z - 5, 16, 16);
		double[] randHeight = VecNoiseHandler.getValue(x - 1, z - 1, baseNoise, vecNoise, 7);
		double[] mountainHeight = VecNoiseHandler.getValue(x - 1, z - 1, mountainNoise, vecMountainNoise, 7);
		float f1, f2, f3, f4;
        short b0 = (short) MathHelper.ceiling_double_int(worldObj.provider.getHorizon()),
        		b1 = (short) (b0 * 3 / 10),
        		b2 = (short) (b0 * 5 / 10),
        		b3 = (short) (b0 * 3 / 10);
        double[] cache = new double[49];
		for(int y0 = 0; y0 < 7; ++y0)
			for(int x0 = 0; x0 < 7; ++x0)
			{
				BiomeGenBase biome = biomesForGeneration[(y0 + 3) * 16 + x0 + 3];
				f1 = 0;
				f2 = 0;
				f3 = 0;
				f4 = 0;
				for(int y1 = -2; y1 <= 2; ++y1)
					for(int x1 = -2; x1 <= 2; ++x1)
					{
						BiomeGenBase biome1 = biomesForGeneration[(y0 + y1 + 2) * 16 + x0 + x1 + 3];
						float f = parabolicField[(y1 + 2) * 5 + x1 + 2];
						if(biome1.rootHeight > biome.rootHeight) f /= 2.0F;
						f1 += biome1.rootHeight * f;
						f2 += biome1.heightVariation * f;
						if(biome1 instanceof FLEBiome)
							f4 += ((FLEBiome) biome1).montainHeight * f;
						f3 += f;
					}
				f1 /= f3;
				f2 /= f3;
				f4 /= f3;
				
				cache[y0 * 7 + x0] = f1 * b1 + f4 * b2 * (mountainHeight[y0 * 7 + x0] + 1D) + f2 * b3 * randHeight[y0 * 7 + x0];
			}
		for(int s = 0; s < offsets.length; ++s)
		{
			for(int i = 0; i < 5; ++i)
				for(int j = 0; j < 5; ++j)
					heights[j * 5 + i] += cache[offsets[s] + j * 7 + i] * scale[s];
		}
		for(int i = 0; i < heights.length; ++i) heights[i] /= 20D;
	}
	static final int[] offsets = {0, 1, 2, 7, 8, 9, 14, 15, 16};
	static final int[] scale = {1, 2, 1, 2, 8, 2, 1, 2, 1};
	
	private static double[] getHeight(long seed, int size)
	{
		GenLayer layer = FLELayer.initializeAllBiomeGenerators(seed, WorldType.DEFAULT)[0];
		IntCache.resetIntCache();
		size += 4;
		BiomeGenBase[] biomes = new BiomeGenBase[size * size];
		int[] var6 = layer.getInts(0, 0, size, size);
		for (int var7 = 0; var7 < size * size; var7++)
		{
			int index = Math.max(var6[var7], 0);
			biomes[var7] = BiomeGenBase.getBiome(index);
		}
		size -= 4;
		double[] randHeight = VecNoiseHandler.getValue(0, 0, new NoiseSmooth(16, new NoisePerlin(38573013L, 2)), new VecNoisePerlin(3759180L, 3, 1.0F), size);
		float f1, f2, f3;
		double[] ret = new double[size * size];
		for(int y0 = 0; y0 < size; ++y0)
		{
			for(int x0 = 0; x0 < size; ++x0)
			{
				BiomeGenBase biome = biomes[(y0 + 2) * 10 + x0 + 2];
				f1 = 0;
				f2 = 0;
				f3 = 0;
				for(int y1 = -2; y1 <= 2; ++y1)
					for(int x1 = -2; x1 <= 2; ++x1)
					{
						BiomeGenBase biome1 = biomes[(y0 + y1 + 2) * size + x0 + x1 + 2];
						float f = parabolicField[(y1 + 2) * 5 + x1 + 2];
						if(biome1.rootHeight > biome.rootHeight) f /= 2;
						
						f1 += biome1.rootHeight;
						f2 += biome1.heightVariation;
						f3 += f / 6D;
					}
				f1 /= f3;
				f2 /= f3;
				
				ret[y0 * size + x0] = f1 * 24 + f2 * 36 * randHeight[y0 * size + x0];
			}
		}
		return ret;
	}
	
	public static void drawImage(int size, String name)
	{
	    try
	    {
	    	File outFile = new File(name + ".png");
	    	if (outFile.exists())
	    	{
	    		return;
	    	}
	    	double[] ds = getHeight(23818492L, size);
	    	BufferedImage outBitmap = new BufferedImage(size, size, 1);
	    	Graphics2D graphics = (Graphics2D)outBitmap.getGraphics();
	    	graphics.clearRect(0, 0, size, size);
	    	FleLog.getLogger().info(name + ".png");
	    	for (int x = 0; x < size; x++)
	    	{
	    		for (int z = 0; z < size; z++)
	    		{
	    			graphics.setColor(new Color(0x010101 * (int) (ds[size * z + x])));
	    			graphics.drawRect(x, z, 1, 1);
	    		}
	    	}
	    	ImageIO.write(outBitmap, "png", outFile);
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
}