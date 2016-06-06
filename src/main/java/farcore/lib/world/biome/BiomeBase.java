package farcore.lib.world.biome;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumBiome;
import farcore.enums.EnumBlock;
import farcore.interfaces.ICustomTempGenerate;
import farcore.interfaces.ITreeGenerator;
import farcore.util.FleLog;
import farcore.util.noise.NoiseBasic;
import farcore.util.noise.NoiseCoherent;
import farcore.util.noise.NoisePerlin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.terraingen.BiomeEvent.GetGrassColor;

public class BiomeBase extends BiomeGenBase
{
	private static final BiomeBase[] biomeList = new BiomeBase[256];
	
	protected static final NoiseBasic customTreeNoise = new NoisePerlin(481945937195L, 6, 1.8D, 2.4D, 2D);
	protected static final NoiseBasic customPlantNoise = new NoisePerlin(39571194729417L, 5, 3.2D, 2D, 2D);
	
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
		rainfall = 1.0F;
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
		return getTreeGenerator(world, rand, x, z, customTreeNoise.noise(x, 0, z));
	}
	
	protected ITreeGenerator getTreeGenerator(World world, Random rand, int x, int z, double treeNoise)
	{
		return null;
	}
	
	public float getTemperature(World world, int x, int y, int z)
	{
		float temp = temperature;
		if(world.getWorldChunkManager() instanceof ICustomTempGenerate)
		{
			temp = ((ICustomTempGenerate) world.getWorldChunkManager()).getBaseTemperature(x, z);
		}
		if(y > 128)
		{
			temp -= (y - 128) * 0.00028F;
		}
		return temp;
	}

    public float getRainfall(World world, int x, int y, int z)
    {
    	float rain = rainfall;
    	if(world.getWorldChunkManager() instanceof ICustomTempGenerate)
    	{
    		rain = ((ICustomTempGenerate) world.getWorldChunkManager()).getBaseRainfall(x, z);
    	}
		return rain;
	}
	
	@Override
	public boolean isHighHumidity()
	{
		return rainfall >= 1.25F;
	}
	
    public final void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer)
    {
    	genTerrainBlocks(world, rand, blocks, metas, x, z, layer, getTemperature(world, x, 0, z), getRainfall(world, x, 0, z));
    }
	
    public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double layer, float temp, float rainfall)
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
            				if(getTemperature(world, x, l1, z) < 0.15F)
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
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int x, int y, int z)
    {
        double d0 = (double)MathHelper.clamp_float(getTemperature(Minecraft.getMinecraft().theWorld, x, y, z), 0.0F, 1.0F);
        double d1 = (double)MathHelper.clamp_float(getRainfall(Minecraft.getMinecraft().theWorld, x, y, z), 0.0F, 1.0F);
        return getModdedBiomeGrassColor(ColorizerGrass.getGrassColor(d0, d1));
    }

	/**
     * Provides the basic foliage color based on the biome temperature and rainfall
     */
    @SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int x, int y, int z)
    {
        double d0 = (double)MathHelper.clamp_float(getTemperature(Minecraft.getMinecraft().theWorld, x, y, z), 0.0F, 1.0F);
        double d1 = (double)MathHelper.clamp_float(getRainfall(Minecraft.getMinecraft().theWorld, x, y, z), 0.0F, 1.0F);
        return getModdedBiomeFoliageColor(ColorizerFoliage.getFoliageColor(d0, d1));
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