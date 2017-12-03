/*
 * copyright© 2016-2017 ueyudiud
 */
package fargen.core.biome;

import fargen.core.util.ClimaticZone;
import net.minecraft.util.math.BlockPos;

public class BiomeVoid extends BiomeBase
{
	public BiomeVoid()
	{
		super(-1, false, BiomePropertiesExtended.newProperties("void").setClimaticZone(ClimaticZone.frigid_desert).setTemperature(-1F).setRainfall(0F));
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
	}
	
	@Override
	public int getWaterColorMultiplier()
	{
		return 0x202242;
	}
	
	@Override
	public int getFoliageColorAtPos(BlockPos pos)
	{
		return 0x2B4C2C;
	}
	
	@Override
	public int getGrassColorAtPos(BlockPos pos)
	{
		return 0x3A493A;
	}
}
