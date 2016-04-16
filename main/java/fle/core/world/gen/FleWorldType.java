package fle.core.world.gen;

import java.util.Arrays;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.util.U;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;

public class FleWorldType extends WorldType
{
	public static FleWorldType DEFAULT;
	public static FleWorldType FLAT;
	public static FleWorldType LARGE_BIOMES;
	
	public FleWorldType(int index, String name, String localized)
	{
		this(name, localized);
		worldTypes[index] = this;
		int oldIndex = getWorldTypeID();
		worldTypes[oldIndex] = null;
		try
		{
			U.Reflect.overrideFinalField(WorldType.class, Arrays.asList("worldTypeId", "field_82748_f"), this, index, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public FleWorldType(String name, String localized)
	{
		super(name);
		FarCoreSetup.lang.registerLocal(getTranslateName(), localized);
	}
	
	@Override
	public String getTranslateName()
	{
		return FarCore.translateToLocal(super.getTranslateName());
	}
	
	@Override
	public IChunkProvider getChunkGenerator(World world, String generatorOptions)
	{
		switch(world.provider.dimensionId)
		{
		case 0 : return //this == FLAT ? 
//				new FleSuperFlatSurfaceChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled()) : 
					new FleSurfaceChunkProvider(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
		case -1 : return new ChunkProviderHell(world, world.getSeed());
		case 1 : return new ChunkProviderEnd(world, world.getSeed());
		}
		return null;
	}
	
	@Override
	public WorldChunkManager getChunkManager(World world)
	{
		switch(world.provider.dimensionId)
		{
		case 0 : return new FleSurfaceManager(world);
		case -1 : return new WorldChunkManager(world);
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