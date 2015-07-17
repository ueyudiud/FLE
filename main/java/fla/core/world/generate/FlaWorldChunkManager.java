package fla.core.world.generate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import fla.core.world.biome.FlaBiome;

public class FlaWorldChunkManager extends WorldChunkManager
{
	protected World worldObj;

	/** The BiomeCache object for this world. */
	protected BiomeCache biomeCache;

	/** A list of biomes that the player can spawn in. */
	public static List<FlaBiome> biomesToSpawnIn = new ArrayList();

	public long seed = 0;
	
	public FlaWorldChunkManager()
	{
		super();
		biomeCache = new BiomeCache(this);
	}

	public FlaWorldChunkManager(World world)
	{
		this(world.getSeed(), world.getWorldInfo().getTerrainType());
		worldObj = world;
	}

	public FlaWorldChunkManager(long Seed, WorldType worldtype)
	{
		this();
		seed = Seed;
	}

	/**
	 * Gets the list of valid biomes for the player to spawn in.
	 */
	@Override
	public List<FlaBiome> getBiomesToSpawnIn()
	{
		return this.biomesToSpawnIn;
	}
}
