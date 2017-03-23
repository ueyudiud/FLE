/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.layer.surface;

import fargen.core.biome.BiomeBase;
import fargen.core.biome.BiomeBase.BiomePropertiesExtended;
import fargen.core.layer.abstracts.LayerReplace;
import fargen.core.util.ClimaticZone;
import net.minecraft.world.gen.layer.GenLayer;

/**
 * @author ueyudiud
 */
public class LayerSurfaceBiome extends LayerReplace
{
	//Terrain(fake) biomes.
	public static final BiomeBase _deep_ocean;
	public static final BiomeBase _swamp;
	public static final BiomeBase _plains;
	public static final BiomeBase _hills;
	public static final BiomeBase _montain_edge;
	public static final BiomeBase _montain;
	public static final BiomeBase _high_plains;
	public static final BiomeBase _beach1;
	public static final BiomeBase _beach2;
	public static final BiomeBase _lake;
	public static final BiomeBase _river;
	public static final BiomeBase _ocean;
	
	public static final BiomeBase[] BIOME_TABLE;
	
	static
	{
		_deep_ocean		= new BiomeBase(0, false, BiomePropertiesExtended.newProperties("deep_ocean").setClimaticZone(ClimaticZone.ocean_temperate).setHeight(-1.5F, 1.0E-3F));
		_ocean			= new BiomeBase(1, false, BiomePropertiesExtended.newProperties("ocean").setClimaticZone(ClimaticZone.ocean_temperate).setHeight(-0.9F, 1.0E-3F));
		_swamp			= new BiomeBase(2, false, BiomePropertiesExtended.newProperties("swamp").setClimaticZone(ClimaticZone.subfrigid_plain).setHeight(-0.1F, 0.1F));
		_plains			= new BiomeBase(3, false, BiomePropertiesExtended.newProperties("plains").setClimaticZone(ClimaticZone.temperate_plain).setHeight(0.1F, 0.16F));
		_hills			= new BiomeBase(4, false, BiomePropertiesExtended.newProperties("hills").setClimaticZone(ClimaticZone.temperate_plain).setHeight(0.3F, 0.7F));
		_montain_edge	= new BiomeBase(5, false, BiomePropertiesExtended.newProperties("montain_edge").setClimaticZone(ClimaticZone.temperate_plain).setHeight(0.2F, 0.9F));
		_montain		= new BiomeBase(6, false, BiomePropertiesExtended.newProperties("montain").setClimaticZone(ClimaticZone.temperate_plain).setHeight(0.7F, 1.6F));
		_high_plains	= new BiomeBase(7, false, BiomePropertiesExtended.newProperties("high_plains").setClimaticZone(ClimaticZone.temperate_plain).setHeight(0.4F, 0.5F));
		_beach1			= new BiomeBase(8, false, BiomePropertiesExtended.newProperties("beach").setClimaticZone(ClimaticZone.temperate_plain).setHeight(0.01F, 0.02F));
		_beach2			= new BiomeBase(9, false, BiomePropertiesExtended.newProperties("beach2").setClimaticZone(ClimaticZone.temperate_plain).setHeight(0.01F, 0.05F));
		_river			= new BiomeBase(10, false, BiomePropertiesExtended.newProperties("river").setClimaticZone(ClimaticZone.temperate_plain).setHeight(-0.5F, -0.3F));
		_lake			= new BiomeBase(11, false, BiomePropertiesExtended.newProperties("lake").setClimaticZone(ClimaticZone.temperate_plain).setHeight(-0.5F, -0.05F));
		
		BIOME_TABLE = new BiomeBase[]{_deep_ocean, _ocean, _swamp, _plains, _hills, _montain_edge, _montain, _high_plains, _beach1, _beach2, _river, _lake};
	}
	
	private BiomeBase[] biomes = {_plains, _high_plains, _hills, _montain};
	
	public LayerSurfaceBiome(long seed, GenLayer layer)
	{
		super(seed, layer);
	}
	
	@Override
	protected int getValue(int x, int y, int id)
	{
		if (id != 0)
		{
			initChunkSeed(x, y);
			return this.biomes[nextInt(this.biomes.length)].biomeID;
		}
		return id;
	}
	
	protected boolean isOceanic(int id)
	{
		return id == LayerSurfaceBiome._ocean.biomeID || id == LayerSurfaceBiome._deep_ocean.biomeID;
	}
}