package fle.core.world.dim;

import java.util.Arrays;
import java.util.Random;

import cpw.mods.fml.common.eventhandler.Event.Result;
import fle.core.util.noise.NoiseMix;
import fle.core.util.noise.NoisePerlin;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
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

public class FLESuperFlatSurfaceChunkProvider extends ChunkProviderGenerate
{
	private Random rand;
	private World worldObj;
	private WorldType worldType;
	private BiomeGenBase[] biomesForGeneration;
	private Block[] idsBig;
	private byte[] metaBig;
    private MapGenBase caveGenerator = new MapGenCaves();
    private MapGenBase ravineGenerator = new MapGenRavine();
	
	public FLESuperFlatSurfaceChunkProvider(World aWorld, long aSeed,
			boolean flag)
	{
		super(aWorld, aSeed, flag);
		worldObj = aWorld;
		rand = new Random(aSeed);
		idsBig = new Block[65536];
		metaBig = new byte[65536];
	}
	
	@Override
	public Chunk provideChunk(int x, int z)
	{
		Arrays.fill(idsBig, null);
		Arrays.fill(metaBig, (byte) 0); 
        rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
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

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                BiomeGenBase biomegenbase = aBiome[l + k * 16];
                biomegenbase.genTerrainBlocks(worldObj, rand, aBlock, aMeta, x * 16 + k, z * 16 + l, 4.0F);
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
        		int idOffset = i << 12 | j << 8;
        		for(int k = b0; k >= 0; --k)
        		{
            		idsBig[idOffset + k] = Blocks.stone;
        		}
        	}
        }
	}
}