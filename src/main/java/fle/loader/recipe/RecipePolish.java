/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader.recipe;

import farcore.data.M;
import farcore.data.MC;
import farcore.data.MP;
import farcore.lib.item.ItemMulti;
import farcore.lib.material.Mat;
import fle.api.recipes.instance.PolishRecipe;
import fle.loader.IBFS;
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
		PolishRecipe.addPolishRecipe(new OreStack(ore), " c ccc c ", IBFS.iResources.getSubItem("flint_small", 4));
		ore = MC.fragment.getOreName(M.quartz);
		PolishRecipe.addPolishLevel(new OreStack(ore), 10);
		PolishRecipe.addPolishRecipe(new OreStack(ore), "c c   c c", IBFS.iResources.getSubItem("quartz_chip"));
		for (Mat material : Mat.filt(MC.ROCKY_TOOL))
		{
			OreStack stack = new OreStack(MC.chip_rock.getOreName(material));
			PolishRecipe.addPolishLevel(stack, material.getProperty(MP.property_rock).harvestLevel);
		}
		
		PolishRecipe.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(M.limestone)), "ccccccccc", ItemMulti.createStack(M.limestone, MC.pile));
		PolishRecipe.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(M.marble)), "ccccccccc", ItemMulti.createStack(M.marble, MC.pile));
		
		PolishRecipe.addPolishRecipe(new OreStack(MC.branch.orePrefix), "p pp pp p", new ItemStack(Items.STICK));
		PolishRecipe.addPolishRecipe(new OreStack(MC.branch.orePrefix), "ppp   ppp", new ItemStack(Items.STICK));
	}
}
