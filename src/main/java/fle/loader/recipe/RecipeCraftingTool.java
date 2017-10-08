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
import fle.core.items.ItemSimpleFluidContainer;
import fle.core.items.ItemToolFar;
import fle.loader.IBF;
import nebula.base.Ety;
import nebula.common.stack.AbstractStack;
import nebula.common.stack.BaseStack;
import nebula.common.stack.OreStack;
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
		LIST.add(new Ety<>(new BaseStack(IBF.iResources.getSubItem("vine_rope")), M.vine));
		LIST.add(new Ety<>(new BaseStack(IBF.iResources.getSubItem("ramie_rope")), M.ramie_dry));
		
		addSpecialToolRecipe1("adz.rock", M.flint, IBF.iResources.getSubItem("flint_sharp"), TIE_MATCH1, "branch");
		addSpecialToolRecipe1("adz.rock", M.quartz, IBF.iResources.getSubItem("quartz_chip"), TIE_MATCH1, "branch");
		addSpecialToolRecipe1("biface", M.flint, IBF.iResources.getSubItem("flint_sharp"), IBF.iResources.getSubItem("flint_small"));
		addSpecialToolRecipe1("biface", M.obsidian, ItemMulti.createStack(M.obsidian, MC.chip_rock), ItemMulti.createStack(M.obsidian, MC.chip_rock));
		
		for (Mat material : Mat.filt(MC.FLINTY_TOOL))
		{
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "   c cc c",
					ItemTool.setMaterialToItem(IBF.iTool.getSubItem("awl"), "head", material));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "  c ccccc",
					ItemMulti.createStack(material, MC.axe_rock));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "c ccccccc",
					ItemMulti.createStack(material, MC.shovel_rock));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "     cccc",
					ItemMulti.createStack(material, MC.hard_hammer_flint));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "cc   cc c",
					ItemMulti.createStack(material, MC.spear_rock));
		}
		for (Mat material : Mat.filt(MC.ROCKY_TOOL))
		{
			if (MC.axe_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "  c ccccc",
						ItemMulti.createStack(material, MC.axe_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "c  cc ccc",
						ItemMulti.createStack(material, MC.axe_rock));
			}
			if (MC.shovel_rock.isBelongTo(material))
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "c ccccccc",
						ItemMulti.createStack(material, MC.shovel_rock));
			if (MC.hard_hammer_rock.isBelongTo(material))
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "      ppp",
						ItemMulti.createStack(material, MC.hard_hammer_rock));
			if (MC.sickle_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "  pcc ccc",
						ItemMulti.createStack(material, MC.sickle_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "p   ccccc",
						ItemMulti.createStack(material, MC.sickle_rock));
			}
			if (MC.spade_hoe_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "p c     p",
						ItemMulti.createStack(material, MC.spade_hoe_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "c p   p  ",
						ItemMulti.createStack(material, MC.spade_hoe_rock));
			}
			if (MC.spear_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "cc   cc c",
						ItemMulti.createStack(material, MC.spear_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), " ccc  c c",
						ItemMulti.createStack(material, MC.spear_rock));
			}
			if (MC.chisel_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "pccc cccc",
						ItemMulti.createStack(material, MC.chisel_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "ccpc cccc",
						ItemMulti.createStack(material, MC.chisel_rock));
			}
			if (MC.whetstone.isBelongTo(material) && MC.fragment.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "ppp   ppp",
						ItemTool.setMaterialToItem(IBF.iTool.getSubItem("whetstone"), "head", material));
			}
		}
		
		addGeneralToolRecipe("hammer.hard.flint", new BaseStack(MC.hard_hammer_flint.instance));
		addGeneralToolRecipe("shovel.rock", new BaseStack(MC.shovel_rock.instance));
		addGeneralToolRecipe("hammer.hard.rock", new BaseStack(MC.hard_hammer_rock.instance));
		addGeneralToolRecipe("spade.hoe.rock", new BaseStack(MC.spade_hoe_rock.instance));
		addGeneralToolRecipe("spear.rock", new BaseStack(MC.spear_rock.instance));
		addGeneralToolRecipe("sickle.rock", new BaseStack(MC.sickle_rock.instance));
		addGeneralToolRecipe("axe.rock", new BaseStack(MC.axe_rock.instance));
		addGeneralToolRecipe("chisel.rock", new BaseStack(MC.chisel_rock.instance));
		addGeneralNoTieToolRecipe("spinning.disk", new BaseStack(MC.spinning_disk.instance));
		//Firestarter
		RecipeAdder.addShapedRecipe(new BaseStack(IBF.iTool.getSubItem("firestarter")), " s", "ld",
				's', "stickWood", 'l', matchOfMaterial(new OreStack(MC.firewood.orePrefix), "head"),
				'd', IBF.iResources.getSubItem("dry_broadleaf"));
		//Bar Grizzly
		RecipeAdder.addShapedRecipe(new BaseStack(IBF.iTool.getSubItem("bar.grizzly")), true, "rb", "br",
				'r', IBF.iResources.getSubItem("vine_rope"),
				'b', matchOfMaterial(new OreStack(MC.branch.orePrefix), "head"));
		
		//Drain all fluids from fluid container.
		RecipeAdder.addShapelessRecipe(new ItemStack(IBF.iFluidContainer),
				new SingleInputMatch(new BaseStack(IBF.iFluidContainer), (i, o)-> {
					o.setItemDamage(i.getItemDamage());
					ItemSimpleFluidContainer.setCustomDamage(o,
							Math.min(IBF.iFluidContainer.getMaxCustomDamage(i) - 1,
									ItemSimpleFluidContainer.getCustomDamage(i) + ItemSimpleFluidContainer.getFluidAmount(i)));
				}, null));
	}
	
	private static final List<Entry<AbstractStack, Mat>> LIST = new ArrayList<>();
	private static final BiConsumer<ItemStack, ItemStack> CONSUMER1 = (input, output) -> {
		Mat material = ItemMulti.getMaterial(input);
		ItemTool.setMaterialToItem(output, "head", material);
	};
	
	private static final SingleInputMatch HANDLE_MATCH
	= new SingleInputMatch(new OreStack("stickWood"), (i, o)->
	ItemToolFar.setMaterialToItem(o, "handle", M.wood), null);
	
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
	(i, o)->
	ItemToolFar.setMaterialToItem(o, "tie", L.getFromEntries(LIST, e->e.similar(i))), null);
	
	private static SingleInputMatch matchOfMaterial(AbstractStack input, String key)
	{
		return new SingleInputMatch(input, (i, o)-> ItemTool.setMaterialToItem(o, key, ItemMulti.getMaterial(i)), null);
	}
	
	private static void addSpecialToolRecipe1(String key, Mat head, Object...inputs)
	{
		ItemStack output = IBF.iTool.getSubItem(key);
		ItemToolFar.setMaterialToItem(output, "head", head);
		RecipeAdder.addShapelessRecipe(output, inputs);
	}
	
	private static void addGeneralToolRecipe(String key, AbstractStack input)
	{
		RecipeAdder.addShapelessRecipe(IBF.iTool.getSubItem(key),
				new SingleInputMatch(input, CONSUMER1, null), TIE_MATCH1, HANDLE_MATCH);
	}
	
	private static void addGeneralNoTieToolRecipe(String key, AbstractStack input)
	{
		RecipeAdder.addShapelessRecipe(IBF.iTool.getSubItem(key),
				new SingleInputMatch(input, CONSUMER1, null), HANDLE_MATCH);
	}
}