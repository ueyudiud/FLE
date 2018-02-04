/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.data;

import net.minecraft.world.biome.Biome.TempCategory;

/**
 * Temperature category, better than Minecraft one.
 * 
 * @author ueyudiud
 *
 */
public enum EnumTempCategory
{
	TROPICAL(TempCategory.WARM), SUBTROPICAL(TempCategory.WARM), TEMPERATE(TempCategory.MEDIUM), SUBFRIGID(TempCategory.COLD), FRIGID(TempCategory.COLD), OCEAN(TempCategory.OCEAN);
	
	EnumTempCategory(TempCategory category)
	{
		this.category = category;
	}
	
	public final TempCategory category;
}
