package fle.core.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import fle.FLE;
import fle.api.FleValue;
import fle.api.recipe.ItemOreStack;
import fle.api.world.BlockPos;

public class WorldUtil
{
	/**
	 * 
	 * @param worldObj
	 * @param xCoord
	 * @param yCoord
	 * @param zCoord
	 * @return from 0D to 415D.
	 */
	public static double getWaterLevel(World worldObj, int xCoord, int yCoord, int zCoord)
	{
		double ret = 0.0D;
		BiomeGenBase base = worldObj.getBiomeGenForCoords(xCoord, zCoord);
		int i;
		int k;
		for(i = -4; i < 5; ++i)
			for(int j = 0; j < 5; ++j)
				for(k = -4; k < 5; ++k)
				{
					double distance = Math.pow((double) i, 2D) + Math.pow((double) j, 2D) + Math.pow((double) k, 2D) + 1D;
					Block block = worldObj.getBlock(xCoord + i, yCoord + j, zCoord + k);
					if(block.isAir(worldObj, xCoord + i, yCoord + j, zCoord + k)) ret += 0.2D / distance;
					if(block.getMaterial() == Material.water) ret += 1D / distance;
					if(block.getMaterial() == Material.plants) ret += 0.3D / distance;
				}
		ret -= 1024D / Math.pow((192D - (double) yCoord) + 64D, 2.0D);
		ret += base.getFloatRainfall() * 5;
		for(i = -2; i < 3; ++i)
			for(k = -2; k < 3; ++k)
				if(worldObj.getBlock(xCoord + i, yCoord - 1, zCoord + k).getMaterial() == Material.water) ret += 0.2D;
		return ret;
	}
	
	/**
	 * 
	 * @param worldObj
	 * @param xCoord
	 * @param yCoord
	 * @param zCoord
	 * @return from 0D to 1D.
	 */
	public static double getTempretureLevel(World world, int x, int y, int z)
	{
		return (double) (FLE.fle.getThermalNet().getEnvironmentTemperature(new BlockPos(world, x, y, z)) - FleValue.WATER_FREEZE_POINT) / 100;
	}
}