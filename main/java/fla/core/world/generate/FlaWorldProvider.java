package fla.core.world.generate;

import fla.api.util.FlaValue;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

public class FlaWorldProvider extends WorldProvider
{
	private final WorldProviderSurface provider = new WorldProviderSurface();
	
    /**
     * creates a new world chunk manager for WorldProvider
     */
    protected void registerWorldChunkManager()
    {
    	this.worldChunkMgr = terrainType.getChunkManager(worldObj);
    }
	
	@Override
	public IChunkProvider createChunkGenerator()
	{
		return new FlaChunkProvider(worldObj, worldObj.getSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
	}
	
	@Override
	public boolean canCoordinateBeSpawn(int x, int z)
	{
		int y = worldObj.getTopSolidOrLiquidBlock(x, z) - 1;
		if(y < FlaValue.SEALEVEL || y > FlaValue.SEALEVEL + 32) return false;
		Block b = worldObj.getBlock(x, y, z);
		return b == Blocks.grass;
	}
	
	@Override
	public boolean canBlockFreeze(int x, int y, int z, boolean byWater) 
	{
        double f = FlaWorldGenerateHelper.getTempreture(worldObj.getSeed(), x, z);

        if (f > 0.15F)
        {
            return false;
        }
        else
        {
            if (y >= 0 && y < 256 && this.worldObj.getSavedLightValue(EnumSkyBlock.Block, x, y, z) < 10)
            {
                Block block = this.worldObj.getBlock(x, y, z);

                if ((block == Blocks.water || block == Blocks.flowing_water) && this.worldObj.getBlockMetadata(x, y, z) == 0)
                {
                    if (!byWater)
                    {
                        return true;
                    }

                    boolean flag1 = true;

                    if (flag1 && this.worldObj.getBlock(x - 1, y, z).getMaterial() != Material.water)
                    {
                        flag1 = false;
                    }

                    if (flag1 && this.worldObj.getBlock(x + 1, y, z).getMaterial() != Material.water)
                    {
                        flag1 = false;
                    }

                    if (flag1 && this.worldObj.getBlock(x, y, z - 1).getMaterial() != Material.water)
                    {
                        flag1 = false;
                    }

                    if (flag1 && this.worldObj.getBlock(x, y, z + 1).getMaterial() != Material.water)
                    {
                        flag1 = false;
                    }

                    if (!flag1)
                    {
                        return true;
                    }
                }
            }

            return false;
        }
	}
	
	@Override
	public float getCloudHeight()
	{
		return (float) (FlaValue.SEALEVEL + FlaValue.MAX_GENERATEHEIGHTFROMSEA);
	}

	@Override
	public String getDimensionName() 
	{
		return "FLA_DEFAULT";
	}

}
