/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance;

import fle.api.recipes.TemplateRecipeMap;
import fle.api.recipes.instance.interfaces.IPolishRecipeHandler;
import nebula.common.data.NBTLSs;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeMaps
{
	//NAME        : fle.polishing
	//NBT ENTRY   : output(ItemStack)
	//DISPLAYMENTS: input(AbstractStack), map(char[]), output(ItemStack)
	public static final
	TemplateRecipeMap<IPolishRecipeHandler> POLISHING =
	TemplateRecipeMap.<IPolishRecipeHandler>builder("fle.polishing").addCacheEntry("output", ItemStack.class, NBTLSs.RW_ITEMSTACK).build();
}