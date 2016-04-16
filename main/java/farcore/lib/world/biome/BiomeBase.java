package farcore.lib.world.biome;

import java.util.Random;

import farcore.enums.EnumBiome;
import farcore.enums.EnumBlock;
import farcore.util.FleLog;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.NoiseGeneratorPerlin;

public class BiomeBase extends BiomeGenBase
{
	private static final NoiseGeneratorPerlin noiseTree = new NoiseGeneratorPerlin(new Random(3719371912701L), 6);
	private static final NoiseGeneratorPerlin noisePlantI = new NoiseGeneratorPerlin(new Random(371937191273L), 4);
	private static final NoiseGeneratorPerlin noisePlantII = new NoiseGeneratorPerlin(new Random(291849181907L), 4);
	private static final NoiseGeneratorPerlin noisePlantIII = new NoiseGeneratorPerlin(new Random(4918596729179L), 4);
	private static final BiomeBase[] biomeList = new BiomeBase[256];
	
	public BiomeBase(int id, boolean register)
	{
		super(id, register);
		if(register)
		{
			biomeList[id] = this;
		}
	}

	public BiomeBase(int id)
	{
		this(id, true);
	}
	
    public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        genTerrain(world, rand, blocks, metas, x, z, layer);
    }

    public void genTerrain(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        boolean flag = true;
        Block block = this.topBlock;
        byte b0 = (byte)(this.field_150604_aj & 255);
        Block block1 = this.fillerBlock;
        int k = -1;
        int l = (int)(layer / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int i1 = x & 15;
        int j1 = z & 15;
        int k1 = blocks.length / 256;

        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (j1 * 16 + i1) * k1 + l1;

            if (l1 <= 0 + rand.nextInt(5))
            {
                blocks[i2] = Blocks.bedrock;
            }
            else
            {
                Block block2 = blocks[i2];

                if (block2 != null && block2.getMaterial() != Material.air)
                {
                    if (block2 == Blocks.stone)
                    {
                        if (k == -1)
                        {
                            if (l <= 0)
                            {
                                block = null;
                                b0 = 0;
                                block1 = Blocks.stone;
                            }
                            else if (l1 >= 120 && l1 <= 128)
                            {
                                block = topBlock;
                                b0 = (byte)(field_150604_aj & 255);
                                block1 = fillerBlock;
                            }

                            if (l1 < 127 && (block == null || block.getMaterial() == Material.air))
                            {
                                if (getFloatTemperature(x, l1, z) < 0.15F)
                                {
                                    block = EnumBlock.ice.block();
                                    b0 = 0;
                                }
                                else
                                {
                                    block = EnumBlock.water.block();
                                    b0 = 15;
                                }
                            }

                            k = l;

                            if (l1 >= 62)
                            {
                                blocks[i2] = block;
                                metas[i2] = b0;
                            }
                            else if (l1 < 56 - l)
                            {
                                block = null;
                                block1 = Blocks.stone;
                                blocks[i2] = Blocks.gravel;
                            }
                            else
                            {
                                blocks[i2] = block1;
                            }
                        }
                        else if (k > 0)
                        {
                            --k;
                            blocks[i2] = block1;

                            if (k == 0 && block1 == Blocks.sand)
                            {
                                k = rand.nextInt(4) + Math.max(0, l1 - 127);
                                block1 = Blocks.sandstone;
                            }
                        }
                    }
                }
                else
                {
                    k = -1;
                }
            }
        }
    }

    /**
     * return the biome specified by biomeID, or 0 (ocean) if out of bounds
     */
    public static BiomeBase getBiome(int idx)
    {
        if (idx >= 0 && idx <= biomeList.length && biomeList[idx] != null)
        {
            return biomeList[idx];
        }
        else
        {
            FleLog.getCoreLogger().warn("Biome ID is out of bounds: " + idx + ", use defaulting (Ocean)");
            return EnumBiome.ocean.biome();
        }
    }
}