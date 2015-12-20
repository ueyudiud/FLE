package fle.core.world.dim;

import java.util.Arrays;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import flapi.FleAPI;
import fle.core.util.Util;

public class FLEWorldType extends WorldType
{
	public static FLEWorldType DEFAULT;
	public static FLEWorldType FLAT;
	public static FLEWorldType LARGE_BIOMES;
	
	public FLEWorldType(int index, String name, String localized)
	{
		this(name, localized);
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
	public FLEWorldType(String name, String localized)
	{
		super(name);
		FleAPI.langManager.registerLocal(super.getTranslateName(), localized);
	}
	
	@Override
	public String getTranslateName()
	{
		return FleAPI.langManager.translateToLocal(super.getTranslateName(), new Object[0]);
	}
	
	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions)
	{
		switch(world.provider.dimensionId)
		{
		case 0 : return this == FLAT ? new FLESuperFlatSurfaceChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled()) : 
			new FLESurfaceChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
		case -1 : return new FLENetherChunkProvider(world, world.getSeed());
		case 1 : return new ChunkProviderEnd(world, world.getSeed());
		}
		return null;
	}
	
	@Override
	public WorldChunkManager getChunkManager(World world)
	{
		switch(world.provider.dimensionId)
		{
		case 0 : return new FLESurfaceManager(world);
		case -1 : return new FLENetherManager(world);
		case 1 : return new WorldChunkManager(world);
		}
		return null;
	}
	
	@Override
	public int getMinimumSpawnHeight(World world)
	{
		return world.provider.isHellWorld ? 64 : 128;
	}
	
	@Override
	public double getHorizon(World world)
	{
		return 127.0D;
	}
}