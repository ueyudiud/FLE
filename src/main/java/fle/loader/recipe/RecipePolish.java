/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import farcore.data.M;
import farcore.data.MC;
import farcore.data.MP;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import fle.api.recipes.instance.PolishRecipe;
import fle.loader.IBF;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.OreStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipePolish
{
	public static void init()
	{
		String ore = MC.fragment.getOreName(M.flint);
		PolishRecipe.addPolishLevel(new OreStack(ore), 7);
		PolishRecipe.addPolishRecipe(new OreStack(ore), "c c      ", new ItemStack(Items.FLINT));
		PolishRecipe.addPolishRecipe(new OreStack(ore), " c ccc c ", IBF.miscResources.getSubItem("flint_small", 4));
		addFlintRecipe(new OreStack(ore), M.flint);
		ore = MC.fragment.getOreName(M.quartz);
		PolishRecipe.addPolishLevel(new OreStack(ore), 10);
		addFlintRecipe(new OreStack(ore), M.quartz);
		PolishRecipe.addPolishRecipe(new OreStack(ore), "c c   c c", IBF.miscResources.getSubItem("quartz_chip"));
		addFlintRecipe(new OreStack(MC.chip_rock.getOreName(M.obsidian)), M.obsidian);
		for (Mat material : Mat.filt(MC.ROCKY))
		{
			OreStack stack = new OreStack(MC.chip_rock.getOreName(material));
			PolishRecipe.addPolishLevel(stack, material.getProperty(MP.property_rock).harvestLevel);
			addPolishedStoneRecipe(stack, material);
		}
	}
	
	private static void addFlintRecipe(AbstractStack input, Mat material)
	{
		ItemStack stack1 = IBF.tool.getSubItem("awl");
		ItemTool.setMaterialToItem(stack1, "head", material);
		PolishRecipe.addPolishRecipe(input, "   c cc c", stack1);
		stack1 = ItemMulti.createStack(material, MC.hard_hammer_flint);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "     cccc", stack1);
			PolishRecipe.addPolishRecipe(input, "   c  ccc", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.axe_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "  c ccccc", stack1);
			PolishRecipe.addPolishRecipe(input, "c  cc ccc", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.spear_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, " ccc  c c", stack1);
			PolishRecipe.addPolishRecipe(input, "cc   cc c", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.shovel_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "c ccccccc", stack1);
		}
	}
	
	private static void addPolishedStoneRecipe(AbstractStack input, Mat material)
	{
		ItemStack stack1;
		stack1 = ItemMulti.createStack(material, MC.hard_hammer_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "      ccc", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.shovel_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "cpcc cccc", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.spear_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, " pcp  c p", stack1);
			PolishRecipe.addPolishRecipe(input, "cp   pp c", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.axe_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "  c pcpcc", stack1);
			PolishRecipe.addPolishRecipe(input, "c  cp ccp", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.spade_hoe_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "p c     p", stack1);
			PolishRecipe.addPolishRecipe(input, "c p   p  ", stack1);
		}
		stack1 = ItemMulti.createStack(material, MC.sickle_rock);
		if (stack1 != null)
		{
			PolishRecipe.addPolishRecipe(input, "  pcc ccc", stack1);
			PolishRecipe.addPolishRecipe(input, "p   ccccc", stack1);
		}
		stack1 = IBF.tool.getSubItem("whetstone");
		ItemTool.setMaterialToItem(stack1, "head", material);
		PolishRecipe.addPolishRecipe(input, "ppp   ppp", stack1);
		PolishRecipe.addPolishRecipe(input, "ppp   ppp", stack1);
	}
}