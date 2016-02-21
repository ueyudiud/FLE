package fle.core.world.dim;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import fle.core.util.noise.NoiseBase;
import fle.core.util.noise.NoisePerlin;
import fle.core.util.noise.NoiseSmooth;
import fle.resource.world.FleCrystalGen;
import fle.resource.world.FleLiquidGen;

public class FLENetherChunkProvider extends ChunkProviderHell
{
	private final World worldObj;
	protected NoiseBase slowsandGravelNoiseGen;
	protected NoiseBase netherrackExculsivityNoiseGen;
	private double[] slowsandNoise;
	private double[] gravelNoise;
	private double[] netherrackExclusivityNoise;
	protected Random hellRNG;
	
	public FLENetherChunkProvider(World aWorld, long seed)
	{
		super(aWorld, seed);
		worldObj = aWorld;
		slowsandGravelNoiseGen = new NoiseSmooth(32D, new NoisePerlin(seed * 27414179L + 14194157L, 8));
		netherrackExculsivityNoiseGen = new NoiseSmooth(32D, new NoisePerlin(seed * 151851953L + 151795157L, 8));
		hellRNG = new Random(seed);
	}
	
	public void replaceBiomeBlocks(int x, int z, Block[] blocks, byte[] meta, BiomeGenBase[] biomes)
    {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, x, z, blocks, meta, biomes, this.worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        byte b0 = 64;
        //double d0 = 0.03125D;
        slowsandNoise = slowsandGravelNoiseGen.noise(slowsandNoise, x * 16, z * 16, 16, 16);
        gravelNoise = slowsandGravelNoiseGen.noise(gravelNoise, x * 16, 109, z * 16, 16, 1, 16);
        netherrackExclusivityNoise = netherrackExculsivityNoiseGen.noise(netherrackExclusivityNoise, x * 16, z * 16, 16, 16);

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                boolean flag = slowsandNoise[k + l * 16] + hellRNG.nextDouble() * 0.2D > 0.0D;
                boolean flag1 = gravelNoise[k + l * 16] + hellRNG.nextDouble() * 0.2D > 0.0D;
                int i1 = (int)(netherrackExclusivityNoise[k + l * 16] / 3.0D + 3.0D + hellRNG.nextDouble() * 0.25D);
                int j1 = -1;
                Block block = Blocks.netherrack;
                Block block1 = Blocks.netherrack;

                for (int k1 = 127; k1 >= 0; --k1)
                {
                    int l1 = (l * 16 + k) * 128 + k1;

                    if (k1 < 127 - this.hellRNG.nextInt(5) && k1 > 0 + this.hellRNG.nextInt(5))
                    {
                        Block block2 = blocks[l1];

                        if (block2 != null && block2.getMaterial() != Material.air)
                        {
                            if (block2 == Blocks.netherrack)
                            {
                                if (j1 == -1)
                                {
                                    if (i1 <= 0)
                                    {
                                        block = null;
                                        block1 = Blocks.netherrack;
                                    }
                                    else if (k1 >= b0 - 4 && k1 <= b0 + 1)
                                    {
                                        block = Blocks.netherrack;
                                        block1 = Blocks.netherrack;

                                        if (flag1)
                                        {
                                            block = Blocks.gravel;
                                            block1 = Blocks.netherrack;
                                        }

                                        if (flag)
                                        {
                                            block = Blocks.soul_sand;
                                            block1 = Blocks.soul_sand;
                                        }
                                    }

                                    if (k1 < b0 && (block == null || block.getMaterial() == Material.air))
                                    {
                                        block = Blocks.lava;
                                    }

                                    j1 = i1;

                                    if (k1 >= b0 - 1)
                                    {
                                        blocks[l1] = block;
                                    }
                                    else
                                    {
                                        blocks[l1] = block1;
                                    }
                                }
                                else if (j1 > 0)
                                {
                                    --j1;
                                    blocks[l1] = block1;
                                }
                            }
                        }
                        else
                        {
                            j1 = -1;
                        }
                    }
                    else
                    {
                        blocks[l1] = Blocks.bedrock;
                    }
                }
            }
        }

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                BiomeGenBase biomegenbase = biomes[l + k * 16];
                biomegenbase.genTerrainBlocks(worldObj, hellRNG, blocks, meta, x * 16 + k, z * 16 + l, 0);
            }
        }
    }

	private FleCrystalGen gen1 = new FleCrystalGen();
	private FleLiquidGen gen2 = new FleLiquidGen(Blocks.netherrack, Blocks.lava, Blocks.netherrack, null);
	
	@Override
	public void populate(IChunkProvider provider, int x, int z)
	{
		super.populate(provider, x, z);

		float temp = worldObj.provider.getBiomeGenForCoords(x * 16, z * 16).getFloatTemperature(x * 16, 64, z * 16);
		if(temp < 7F)
		{
			if(hellRNG.nextBoolean())
				gen1.generate(worldObj, hellRNG, x * 16, hellRNG.nextInt(100) + 10, z * 16);
		}
		else if(temp > 10F)
		{
			if(hellRNG.nextBoolean())
				gen2.generate(worldObj, hellRNG, x * 16, hellRNG.nextInt(hellRNG.nextInt(80) + 20) + 10, z * 16);
		}
	}
}