/*
 * copyright 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import static fle.api.recipes.SingleInputMatch.toolUse;
import static fle.api.recipes.instance.RecipeAdder.addShapedRecipe;
import static fle.api.recipes.instance.RecipeAdder.addShapelessRecipe;

import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.MP;
import farcore.data.SubTags;
import farcore.lib.block.behavior.RockBehavior;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import farcore.lib.material.prop.PropertyWood;
import fle.api.recipes.instance.RecipeMaps;
import fle.core.recipe.RecipePortableWoodwork1;
import fle.core.recipe.RecipePortableWoodwork2;
import fle.loader.IBFS;
import nebula.common.stack.BaseStack;
import net.minecraft.init.Items;

/**
 * @author ueyudiud
 */
public class RecipeResource1
{
	public static void init()
	{
		addShapelessRecipe(IBFS.iResources.getSubItem("vine_rope"), IBFS.iCropRelated.getSubItem("vine"), 4);
		addShapelessRecipe(IBFS.iResources.getSubItem("ramie_rope"), IBFS.iResources.getSubItem("dry_ramie_fiber"), 4);
		addShapelessRecipe(IBFS.iResources.getSubItem("ramie_rope_bundle"), IBFS.iResources.getSubItem("ramie_rope"), 4);
		addShapelessRecipe(IBFS.iResources.getSubItem("ramie_rope", 4), IBFS.iResources.getSubItem("ramie_rope_bundle"));
		addShapelessRecipe(IBFS.iResources.getSubItem("crushed_bone"), Items.BONE, toolUse(EnumToolTypes.HAMMER_DIGABLE, 0.2F));
		addShapelessRecipe(IBFS.iResources.getSubItem("tinder"), IBFS.iResources.getSubItem("dry_broadleaf"), 3, toolUse(EnumToolTypes.FIRESTARTER, 0.5F));
		
		for (RockBehavior<?> rock : Mat.filtAndGet(MC.fragment, MP.property_rock))
		{
			addShapelessRecipe(ItemMulti.createStack(rock.material, MC.chip_rock, 2), toolUse(EnumToolTypes.HAMMER_DIGABLE, 0.25F + 0.125F * rock.hardness), ItemMulti.createStack(rock.material, MC.fragment));
			addShapelessRecipe(ItemMulti.createStack(rock.material, MC.chip_rock, 2), toolUse(EnumToolTypes.PICKAXE, 0.25F + 0.125F * rock.hardness), ItemMulti.createStack(rock.material, MC.fragment));
		}
		
		for (PropertyWood tree : Mat.filtAndGet(SubTags.WOOD, MP.property_wood))
		{
			addShapelessRecipe(ItemMulti.createStack(tree.material, MC.firewood, 2), MC.log.getOreName(tree.material), toolUse(EnumToolTypes.AXE, tree.hardness / 20.0F + 0.25F));
			if (tree.plank != null)
			{
				addShapedRecipe(new BaseStack(tree.plank), "xx", "xx", 'x', MC.plank.getOreName(tree.material));
			}
		}
		
		for (Mat material : Mat.filt(SubTags.DUST, true))
		{
			addShapedRecipe(ItemMulti.createStack(material, MC.dust_small, 4), "x ", 'x', MC.dust.getOreName(material));
			addShapedRecipe(ItemMulti.createStack(material, MC.dust_tiny, 9), " x", 'x', MC.dust.getOreName(material));
			addShapelessRecipe(ItemMulti.createStack(material, MC.dust), MC.dust_small.getOreName(material), 4);
			addShapelessRecipe(ItemMulti.createStack(material, MC.dust), MC.dust_tiny.getOreName(material), 9);
		}
		
		RecipeMaps.PORTABLE_WOODWORK.addRecipe(new RecipePortableWoodwork1());
		RecipeMaps.PORTABLE_WOODWORK.addRecipe(new RecipePortableWoodwork2());
	}
}
