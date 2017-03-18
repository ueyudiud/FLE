/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import static fle.api.recipes.SingleInputMatch.toolUse;
import static fle.api.recipes.instance.RecipeAdder.addShapelessRecipe;

import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyTree;
import fle.api.recipes.instance.RecipeMaps;
import fle.core.recipe.RecipePortableWoodwork1;
import fle.core.recipe.RecipePortableWoodwork2;
import fle.loader.BlocksItems;

/**
 * @author ueyudiud
 */
public class RecipeResource1
{
	public static void init()
	{
		addShapelessRecipe(BlocksItems.miscResources.getSubItem("ramie_rope"), BlocksItems.miscResources.getSubItem("dry_ramie_fiber"), 4);
		addShapelessRecipe(BlocksItems.miscResources.getSubItem("ramie_rope_bundle"), BlocksItems.miscResources.getSubItem("ramie_rope"), 4);
		addShapelessRecipe(BlocksItems.miscResources.getSubItem("ramie_rope", 4), BlocksItems.miscResources.getSubItem("ramie_rope_bundle"));
		
		for (PropertyTree tree : Mat.filtAndGet(SubTags.TREE, MP.property_tree))
		{
			addShapelessRecipe(ItemMulti.createStack(tree.material(), MC.firewood, 2), MC.log.getOreName(tree.material()), toolUse(EnumToolTypes.AXE, tree.hardness / 20.0F + 0.25F));
		}
		
		RecipeMaps.PORTABLE_WOODWORK.addRecipe(new RecipePortableWoodwork1());
		RecipeMaps.PORTABLE_WOODWORK.addRecipe(new RecipePortableWoodwork2());
	}
}