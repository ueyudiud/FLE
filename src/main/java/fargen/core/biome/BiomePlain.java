/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.biome;

import fargen.core.biome.decorator.SimpleBiomeDecorator;

/**
 * @author ueyudiud
 */
public class BiomePlain extends BiomeBase
{
	public BiomePlain(int id, BiomePropertiesExtended properties)
	{
		super(id, properties);
		((SimpleBiomeDecorator) this.decorator).grassPerChunk = 2;
	}
}
