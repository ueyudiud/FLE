package fle.core.world.dim;

import java.util.Arrays;

import fle.core.util.Util;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;

public class FLEWorldType extends WorldType
{
	public static FLEWorldType DEFAULT;
	
	public FLEWorldType(int index, String name)
	{
		super(name);
		worldTypes[index] = this;
		int oldIndex = getWorldTypeID();
		worldTypes[oldIndex] = null;
		try
		{
			Util.overrideFinalField(WorldType.class, Arrays.asList("worldTypeId", "field_82748_f"), this, index, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public FLEWorldType(String name)
	{
		super(name);
	}
	
	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions)
	{
		return new FLESurfaceChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
	}
	
	@Override
	public WorldChunkManager getChunkManager(World world)
	{
		return new FLESurfaceManager(world);
	}
	
	@Override
	public int getMinimumSpawnHeight(World world)
	{
		return 129;
	}
	
	@Override
	public double getHorizon(World world)
	{
		return 128;
	}
}