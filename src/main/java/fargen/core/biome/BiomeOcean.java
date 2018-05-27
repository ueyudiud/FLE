/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.biome;

import fargen.core.biome.decorator.SimpleBiomeDecorator;

/**
 * @author ueyudiud
 */
public class BiomeOcean extends BiomeBase
{
	public BiomeOcean(int id, BiomePropertiesExtended properties)
	{
		super(id, properties);
		((SimpleBiomeDecorator) this.decorator).grassPerChunk = 0;
		((SimpleBiomeDecorator) this.decorator).enableStoneChipGen = false;
	}
}
