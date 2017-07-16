/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.loader.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.ItemTool;
import farcore.lib.material.Mat;
import fle.api.recipes.SingleInputMatch;
import fle.api.recipes.instance.RecipeAdder;
import fle.core.items.ItemToolFar;
import fle.loader.BlocksItems;
import nebula.base.Ety;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.util.L;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class RecipeCraftingTool
{
	public static void init()
	{
		LIST.add(new Ety<>(new BaseStack(Items.STRING), M.spider_silk));
		LIST.add(new Ety<>(new BaseStack(BlocksItems.miscResources.getSubItem("vine_rope")), M.vine));
		LIST.add(new Ety<>(new BaseStack(BlocksItems.miscResources.getSubItem("ramie_rope")), M.ramie_dry));
		
		addSpecialToolRecipe1("adz.rock", M.flint, BlocksItems.miscResources.getSubItem("flint_sharp"), TIE_MATCH1, "branch");
		addSpecialToolRecipe1("adz.rock", M.quartz, BlocksItems.miscResources.getSubItem("quartz_chip"), TIE_MATCH1, "branch");
		addSpecialToolRecipe1("biface", M.flint, BlocksItems.miscResources.getSubItem("flint_sharp"), BlocksItems.miscResources.getSubItem("flint_small"));
		addSpecialToolRecipe1("biface", M.obsidian, ItemMulti.createStack(M.obsidian, MC.chip_rock), ItemMulti.createStack(M.obsidian, MC.chip_rock));
		
		addGeneralToolRecipe("hammer.hard.flint", new BaseStack(MC.hard_hammer_flint.instance));
		addGeneralToolRecipe("shovel.rock", new BaseStack(MC.shovel_rock.instance));
		addGeneralToolRecipe("hammer.hard.rock", new BaseStack(MC.hard_hammer_rock.instance));
		addGeneralToolRecipe("spade.hoe.rock", new BaseStack(MC.spade_hoe_rock.instance));
		addGeneralToolRecipe("spear.rock", new BaseStack(MC.spear_rock.instance));
		addGeneralToolRecipe("sickle.rock", new BaseStack(MC.sickle_rock.instance));
		addGeneralToolRecipe("axe.rock", new BaseStack(MC.axe_rock.instance));
	}
	
	private static final List<Entry<AbstractStack, Mat>> LIST = new ArrayList<>();
	private static final BiConsumer<ItemStack, ItemStack> CONSUMER1 = (input, output) -> {
		Mat material = ItemMulti.getMaterial(input);
		ItemTool.setMaterialToItem(output, "head", material);
	};
	
	private static final SingleInputMatch TIE_MATCH1
	= new SingleInputMatch(new AbstractStack()
	{
		List<ItemStack> list;
		
		public boolean similar(ItemStack stack) { return L.contain(LIST, entry->entry.getKey().similar(stack)); }
		
		public int size(ItemStack stack) { return 1; }
		
		@Override
		public List<ItemStack> display()
		{
			if (this.list == null)
			{
				this.list = new ArrayList<>();
				LIST.forEach(entry->this.list.addAll(entry.getKey().display()));
			}
			return this.list;
		}
	},
	(input, output)-> {
		Entry<AbstractStack, Mat> entry1 = L.get(LIST, e->e.getKey().similar(input));
		if (entry1 != null)
		{
			ItemToolFar.setMaterialToItem(output, "tie", entry1.getValue());
		}
	}, null);
	
	private static void addSpecialToolRecipe1(String key, Mat head, Object...inputs)
	{
		ItemStack output = BlocksItems.tool.getSubItem(key);
		ItemToolFar.setMaterialToItem(output, "head", head);
		RecipeAdder.addShapelessRecipe(output, inputs);
	}
	
	private static void addGeneralToolRecipe(String key, AbstractStack input)
	{
		RecipeAdder.addShapelessRecipe(BlocksItems.tool.getSubItem(key),
				new SingleInputMatch(input, CONSUMER1, null), TIE_MATCH1, "stickWood");
	}
}