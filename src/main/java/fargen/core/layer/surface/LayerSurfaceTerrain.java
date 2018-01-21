/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.layer.surface;

import fargen.api.terrain.Terrain;
import fargen.api.terrain.TerrainSimple;
import fargen.core.layer.abstracts.LayerReplace;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceTerrain extends LayerReplace
{
	// Terrain(fake) biomes.
	public static final Terrain	_deep_ocean;
	public static final Terrain	_swamp;
	public static final Terrain	_plains;
	public static final Terrain	_hills;
	public static final Terrain	_montain_edge;
	public static final Terrain	_montain;
	public static final Terrain	_high_plains;
	public static final Terrain	_beach1;
	public static final Terrain	_beach2;
	public static final Terrain	_lake;
	public static final Terrain	_river;
	public static final Terrain	_ocean;
	public static final Terrain	_high_plains_edge;
	
	public static final Terrain[] TERRAIN_TABLE;
	
	static
	{
		_deep_ocean			= new TerrainSimple( 0,-0.8F, 0.4F);
		_ocean				= new TerrainSimple( 1,-0.6F, 0.3F);
		_swamp				= new TerrainSimple( 2, 0.02F,0.1F);
		_plains				= new TerrainSimple( 3, 0.1F, 0.2F);
		_hills				= new TerrainSimple( 4, 0.4F, 0.5F);
		_montain_edge		= new TerrainSimple( 5, 0.2F, 0.9F);
		_montain			= new TerrainSimple( 6, 0.7F, 1.6F);
		_high_plains		= new TerrainSimple( 7, 0.5F, 0.1F);
		_beach1				= new TerrainSimple( 8, 0.05F,0.04F);
		_beach2				= new TerrainSimple( 9, 0.1F, 0.2F);
		_river				= new TerrainSimple(10,-0.3F, 0.2F);
		_lake				= new TerrainSimple(11,-0.4F, 0.1F);
		_high_plains_edge	= new TerrainSimple(12, 0.3F, 0.35F);
		
		TERRAIN_TABLE = new Terrain[] { _deep_ocean, _ocean, _swamp, _plains, _hills, _montain_edge, _montain, _high_plains, _beach1, _beach2, _river, _lake, _high_plains_edge };
	}
	
	private Terrain[] terrains = { _plains, _high_plains, _hills, _montain };
	
	public LayerSurfaceTerrain(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int id)
	{
		if (id != 0)
		{
			initChunkSeed(x, y);
			return this.terrains[nextInt(this.terrains.length)].id;
		}
		return id;
	}
}
