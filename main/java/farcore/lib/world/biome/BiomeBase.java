package farcore.lib.world.biome;

import java.util.Random;

import farcore.enums.EnumBiome;
import farcore.enums.EnumBlock;
import farcore.interfaces.ITreeGenerator;
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
	
	public float rareMultiply = 0.2F;
	public float sedimentaryMultiply = 0.0F;
	public float metamorphismMultiply = 1.0F;
	
	public Block topBlock = Blocks.grass;
	public int topMeta = 0;
	public Block waterTop = Blocks.dirt;
	public int waterTopMeta = 0;
	public Block secondBlock = Blocks.dirt;
	public int secondMeta = 0;
	public Block fillerBlock = Blocks.gravel;
	public int fillerMeta = 0;
	
	public BD biomeDecorator = new BD();
	
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
	
	@Override
	public void decorate(World world, Random rand, int x, int z)
	{
		biomeDecorator.decorateChunk(world, rand, this, x, z);
	}
	
	public final ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z)
	{
		return getTreeGenerator(world, rand, x, z, noiseTree.func_151601_a(x * 2D, z * 2D) / 32D);
	}
	
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
	{
		return null;
	}
	
    public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        genTerrain(world, rand, blocks, metas, x, z, layer);
    }

    public void genTerrain(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
        boolean flag = true;
        int k = -1;
        int r = 0;
        int l = (int)(layer / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int i1 = x & 15;
        int j1 = z & 15;
        int k1 = blocks.length / 256;

        for (int l1 = 255; l1 >= 0; --l1)
        {
            int i2 = (j1 * 16 + i1) * k1 + l1;
            if(l1 == 0)
            {
            	blocks[i2] = Blocks.bedrock;
            }
            else if(l1 < 10)
            {
            	blocks[i2] = EnumBlock.lava.block();
            	metas[i2] = 15;
            }
            else if(l1 < 14 + rand.nextInt(2))
            {
            	blocks[i2] = Blocks.air;
            	metas[i2] = 0;
            }
            else
            {
            	Block block2 = blocks[i2];
            	if(block2 != null && block2.getMaterial() != Material.air)
            	{
            		if(block2 == Blocks.stone)
            		{
            			if(k == -1 && (r & 0x4) == 0)
            			{
            				if((r & 0x1) == 0)
            				{
            					if(l < 0)
            					{
            						blocks[i2] = Blocks.stone;
            						metas[i2] = (byte) 0;
            					}
            					else
            					{
            						if((r & 0x2) != 0)
            						{
            							blocks[i2] = waterTop;
            							metas[i2] = (byte) (waterTopMeta & 0xF);
            							k = l - 1;
            							r |= 0x1;
            						}
            						else
            						{
            							blocks[i2] = topBlock;
            							metas[i2] = (byte) (topMeta & 0xF);
            							k = l;
            							r |= 0x1;
            						}
            					}
            				}
            				else if(l1 > 112)
            				{
            					if(l < 0)
            					{
            						blocks[i2] = Blocks.stone;
            						metas[i2] = (byte) 0;
            					}
            					else
            					{
            						if((r & 0x2) != 0)
            						{
            							blocks[i2] = waterTop;
            							metas[i2] = (byte) (waterTopMeta & 0xF);
            							k = l - 1;
            							r |= 0x1;
            						}
            						else
            						{
            							blocks[i2] = topBlock;
            							metas[i2] = (byte) (topMeta & 0xF);
            							k = l;
            							r |= 0x1;
            						}
            					}
            				}
            				else
            				{
            					k = -2;
            				}
            			}
            			else if(k > 0)
            			{
            				blocks[i2] = secondBlock;
            				metas[i2] = (byte) (secondMeta & 0xF);
            				--k;
            			}
            			else if(k == 0)
            			{
            				blocks[i2] = fillerBlock;
            				metas[i2] = (byte) (fillerMeta & 0xF);
            				--k;
            			}
            			r |= 0x4;
            		}
            		else if(block2 == EnumBlock.water.block())
            		{
            			if((r & 0x2) == 0)
            			{
            				if(getFloatTemperature(x, l1, z) < 0.15F)
            				{
            					blocks[i2] = EnumBlock.ice.block();
                				metas[i2] = (byte) 0;
            				}
            				r |= 0x2;
            			}
            		}
            	}
            	else
            	{
            		r &= (~0x4);
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