package fle.core.util;

import java.util.Arrays;

import fle.core.handler.WorldHandler;
import fle.core.world.climate.Climate;
import fle.core.world.climate.EnumClimate;
import fle.core.world.climate.IClimateHandler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ClimateManager
{
	public static Climate getClimateAt(World world, int x, int z)
	{
		return WorldHandler.getClimateAt(world, x, z);
	}
	
	public static Climate getGenClimateAt(World world, int x, int z)
	{
		if(world.getWorldChunkManager() instanceof IClimateHandler)
		{
			return ((IClimateHandler) world.getWorldChunkManager()).getClimateAt(x, z);
		}
		return EnumClimate.ocean.climate();
	}
	
	public static Climate[] getGenClimatesAt(Climate[] climates, World world, int x, int z, int w, int h)
	{
		if(world.getWorldChunkManager() instanceof IClimateHandler)
		{
			return ((IClimateHandler) world.getWorldChunkManager()).getClimateAt(climates, x, z, w, h, false);
		}
		if(climates == null || climates.length < w * h)
		{
			climates = new Climate[w * h];
		}
		Arrays.fill(climates, EnumClimate.ocean.climate());
		return climates;
	}
}