package fle.core.world.dim;

import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import fle.core.util.noise.NoiseBase;
import fle.core.util.noise.NoiseMix;
import fle.core.util.noise.NoisePerlin;
import fle.core.world.FleCavesGen;

public class FLESurfaceChunkProvider extends ChunkProviderGenerate
{
	private Random rand;
	private World worldObj;
	private WorldType worldType;
	private BiomeGenBase[] biomesForGeneration;
	private Block[] idsBig;
	private byte[] metaBig;
	/** For generate **/
    public NoiseBase noiseGen0;
    public NoiseBase noiseGen1;
    public NoiseBase noiseGen2;
    /** For rock layer generate **/
    public NoiseBase noiseGen3;
    public NoiseBase noiseGen4;
    private MapGenBase caveGenerator = new FleCavesGen();
    private MapGenBase ravineGenerator = new MapGenRavine();
	private float[] f1;
	private double[] d1;
	private double[] d2;
	private double[] d3;
	private double[] d4;
	private double[] d5;
	private double[] d6;
	private double[] d7;
	private double[] d8;
	private final float[] parabolicField;
	
	public FLESurfaceChunkProvider(World aWorld, long aSeed,
			boolean flag)
	{
		super(aWorld, aSeed, flag);
		worldObj = aWorld;
		rand = new Random(aSeed);
		noiseGen0 = new NoiseMix(64.0D, new NoisePerlin(aSeed * 28759282L + 72957539L, 8));
		noiseGen1 = new NoiseMix(16.0D, new NoisePerlin(aSeed * 18395739L + 38572821L, 4));
		noiseGen2 = new NoiseMix(4.0D, new NoisePerlin(aSeed * 295829482L + 38592726L, 4));
		noiseGen3 = new NoiseMix(128.0D, new NoisePerlin(aSeed * 1829471L + 73957293L, 6));
		
		parabolicField = new float[49];
        for (int j = -3; j <= 3; ++j)
        {
            for (int k = -3; k <= 3; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
                parabolicField[j + 3 + (k + 3) * 7] = f;
            }
        }
		idsBig = new Block[65536];
		metaBig = new byte[65536];
        d8 = new double[256];
	}

	@Override
	public Chunk provideChunk(int x, int z)
	{
		Arrays.fill(idsBig, null);
		Arrays.fill(metaBig, (byte) 0); 
        rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        getTerrinHeight(x, z);
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
        return chunk;
	}

	@Override
    public void replaceBlocksForBiome(int x, int z, Block[] aBlock, byte[] aMeta, BiomeGenBase[] aBiome)
    {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, x, z, aBlock, aMeta, aBiome, worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        d4 = noiseGen3.noise(d4, x * 16, z * 16, 16, 16);

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                BiomeGenBase biomegenbase = aBiome[l + k * 16];
                biomegenbase.genTerrainBlocks(worldObj, rand, aBlock, aMeta, x * 16 + k, z * 16 + l, d4[l + k * 16]);
            }
        }
    }
	
	public void fillBlockIn()
	{
		Arrays.fill(idsBig, Blocks.air);
        short b0 = (short) MathHelper.ceiling_double_int((worldObj.provider.getHorizon()));
        for (int i = 0; i < 16; ++i)
        {
        	for (int j = 0; j < 16; ++j)
        	{
        		int height = (int) d8[i + 16 * j] + b0;
        		boolean flag = false; // Not finish.
        		int idOffset = i << 12 | j << 8;
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
        	}
        }
	}

	private static final int floor1 = 1 << 12;
	private static final int floor2 = 1 << 9;
	private static final int floor3 = 1 << 6;
	private static final int floor4 = 1 << 3;
	
	public void getTerrinHeight(int x, int z)
	{
		int i, j, a1, a2, a3, a4, a5, a6;
		
		biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, x * 16 - 3, z * 16 - 3, 16 + 6, 16 + 6);
        		
		int l = 0;
		int k0 = 0;
        d1 = noiseGen0.noise(d1, x * 16, z * 16, 16, 16);
        d2 = noiseGen1.noise(d2, x * 16, z * 16, 16, 16);
        d3 = noiseGen2.noise(d3, x * 16, z * 16, 16, 16);
        float[] baseHeight = new float[256];
        float[] randHeight = new float[256];
        for(int i0 = 0; i0 < 16; ++i0)
        	for(int i1 = 0; i1 < 16; ++i1)
        	{
                float f0 = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                BiomeGenBase biome0 = biomesForGeneration[(i0 + 3) + (i1 + 3) * 22];
                for(int j0 = -3; j0 <= 3; ++j0)
                	for(int j1 = -3; j1 <= 3; ++j1)
                	{
                		BiomeGenBase biome1 = biomesForGeneration[i0 + 3 + j0 + (i1 + 3 + j1) * 22];
                		float f3 = biome1.rootHeight;
                		float f4 = biome1.heightVariation;
                		float f5 = parabolicField[j0 + 3 + (j1 + 3) * 7];

                        f0 += f3 * f5;
                        f1 += f4 * f5;
                        f2 += f5;
                	}
                f0 /= f2;
                f1 /= f2;
                baseHeight[i0 + i1 * 16] = f0;
                randHeight[i0 + i1 * 16] = f1;
        	}
        for(int zOffset = 0; zOffset < 16; ++zOffset)
        {
        	for(int xOffset = 0; xOffset < 16; ++xOffset)
        	{
        		float f0 = baseHeight[xOffset + zOffset * 16];
        		float f1 = randHeight[xOffset + zOffset * 16];

        		double rh = d3[xOffset + zOffset * 16];
        		rh *= 4;
        		if(rh < 0.0D)
        		{
        			rh /= 2;
        		}
        		else
        		{
        			rh += 0.05D;
        			rh /= 2;
        		}
        		rh *= 0.25D;
        		if(rh > 1.25D)
        		{
        			rh = 1.25D;
        		}
        		else if(rh < -0.5D)
        		{
        			rh = -0.5D;
        		}
        		if(f0 < 0.0F)
        		{
        			f0 *= 0.5F;
        		}
        		double height = f1 * rh + f0 * ((d1[xOffset + zOffset * 16] + 1D) * 24 + (d2[xOffset + zOffset * 16] + 1D) * 8);
        		d8[xOffset + zOffset * 16] = height;
        	}
        }
	}
	
	private double a(double ss, double es, double se, double ee, int x, int y, int size)
	{
		double a1 = ss + (es - ss) / (double) size * x;
		double a2 = se + (ee - se) / (double) size * x;
		return a1 + (a2 - a1) / (double) size * y;
	}
}