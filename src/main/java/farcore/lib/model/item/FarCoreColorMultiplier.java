/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.model.item;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 * @since 1.5
 */
@SideOnly(Side.CLIENT)
public class FarCoreColorMultiplier
{
	private static final Map<ResourceLocation, ToIntFunction<ItemStack>> BUILTIN_COLOR_MULTIPLIERS = new HashMap();
	
	public static final Map<ResourceLocation, ToIntFunction<ItemStack>> STORED_FUNCTION = new HashMap();
	
	static void cleanCache()
	{
		STORED_FUNCTION.clear();
	}
	
	public static void registerColorMultiplier(ResourceLocation location, ToIntFunction<ItemStack> function)
	{
		BUILTIN_COLOR_MULTIPLIERS.put(location, function);
	}
	
	public static ToIntFunction<ItemStack> getColorMultiplier(ResourceLocation location)
	{
		if(BUILTIN_COLOR_MULTIPLIERS.containsKey(location))
		{
			return BUILTIN_COLOR_MULTIPLIERS.get(location);
		}
		return FarCoreItemModelLoader.NORMAL_MULTIPLIER;
	}
	
	/**
	 * Wrap model cache to a color applier for ItemColors, which use to
	 * apply color for render.
	 * @param cache
	 * @return
	 */
	static IItemColor createColorMultiplier(FarCoreItemModelCache cache)
	{
		final ToIntFunction<ItemStack>[] list = new ToIntFunction[cache.caches.length];
		boolean flag = false;
		for(int i = 0; i < list.length; ++i)
		{
			if((list[i] = cache.caches[i].colorMultiplier) != FarCoreItemModelLoader.NORMAL_MULTIPLIER)
				flag = true;
		}
		return flag ? (stack, tindex) -> list[tindex].applyAsInt(stack) : null;
	}
}