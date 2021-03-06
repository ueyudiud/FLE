/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.worldgen;

import fargen.core.FarGenConfig;
import fargen.core.worldgen.surface.FarSurfaceBiomeProvider;
import fargen.core.worldgen.surface.FarSurfaceChunkGenerator;
import nebula.base.R;
import nebula.common.LanguageManager;
import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FarWorldType extends WorldType
{
	/**
	 * The {@link net.minecraft.world.storage.WorldInfo} use <tt>"default"</tt>
	 * as default world generator if no matched world type found, so the
	 * replaced world type is suggested set a same name to old world type.
	 * 
	 * @see fargen.core.FarGen#load(net.minecraftforge.fml.common.event.FMLInitializationEvent)
	 */
	public static WorldType	DEFAULT;
	public static WorldType	FLAT	= WorldType.FLAT;
	public static WorldType	LARGE_BIOMES;
	
	public FarWorldType(int index, String name, String localName)
	{
		super(FarGenConfig.hardOverride ? name : "far_" + name);
		if (FarGenConfig.hardOverride)
		{
			WORLD_TYPES[index] = this;
			int oldIndex = getWorldTypeID();
			WORLD_TYPES[oldIndex] = null;
			R.overrideFinalField(WorldType.class, "worldTypeId", "field_82748_f", this, index, true, false);
		}
		LanguageManager.registerLocal("generator." + getName(), localName);
	}
	
	public FarWorldType(String name, String localName)
	{
		super(FarGenConfig.hardOverride ? name : "far_" + name);
		LanguageManager.registerLocal("generator." + getName(), localName);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslateName()
	{
		return LanguageManager.translateLocal(super.getTranslateName());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedInfo()
	{
		return LanguageManager.translateLocal(super.getTranslateName() + ".info");
	}
	
	@Override
	public double getHorizon(World world)
	{
		return 62.0D;
	}
	
	@Override
	public int getMinimumSpawnHeight(World world)
	{
		return this == FLAT ? 4 : world.getSeaLevel() + 1;
	}
	
	@Override
	public float getCloudHeight()
	{
		return 220F;
	}
	
	@Override
	public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
	{
		switch (world.provider.getDimension())
		{
		case 0: // The surface type.
			if (this == DEFAULT)
				return new FarSurfaceChunkGenerator(world, world.getSeed());
			// return new ChunkProviderOverworld(world, world.getSeed(),
			// world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
			else if (this == FLAT) return new ChunkProviderFlat(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
			break;
		case 1: // The nether type.
			if (this == DEFAULT) return new ChunkProviderHell(world, world.getWorldInfo().isMapFeaturesEnabled(), world.getSeed());
			break;
		case -1: // The end type.
			if (this == DEFAULT) return new ChunkProviderEnd(world, world.getWorldInfo().isMapFeaturesEnabled(), world.getSeed());
			break;
		default: // For others.
			break;
		}
		return super.getChunkGenerator(world, generatorOptions);
	}
	
	@Override
	public BiomeProvider getBiomeProvider(World world)
	{
		switch (world.provider.getDimension())
		{
		case 0:
			if (this == DEFAULT)
			{
				return new FarSurfaceBiomeProvider(world.getWorldInfo());
			}
			// return new FarSurfaceBiomeProvider(world.getWorldInfo());
			else if (this == FLAT)
			{
				FlatGeneratorInfo info = FlatGeneratorInfo.createFlatGeneratorFromString(world.getWorldInfo().getGeneratorOptions());
				return new BiomeProviderSingle(Biome.getBiome(info.getBiome(), Biomes.DEFAULT));
			}
			break;
		default:
			break;
		}
		return super.getBiomeProvider(world);
	}
}
