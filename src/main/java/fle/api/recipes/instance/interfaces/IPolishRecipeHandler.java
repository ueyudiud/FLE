/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes.instance.interfaces;

import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public interface IPolishRecipeHandler
{
	ItemStack getPolishingInput();
	
	char[] getPolishingMatrix();
}