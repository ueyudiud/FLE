/*
 * copyright© 2016-2018 ueyudiud
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
import fle.loader.IBFS;
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
		LIST.add(new Ety<>(new BaseStack(IBFS.iResources.getSubItem("vine_rope")), M.vine));
		LIST.add(new Ety<>(new BaseStack(IBFS.iResources.getSubItem("ramie_rope")), M.ramie_dry));
		
		LIST.trimToSize();
		
		addSpecialToolRecipe1("adz.rock", M.flint, IBFS.iResources.getSubItem("flint_sharp"), TIE_MATCH1, "branch");
		addSpecialToolRecipe1("adz.rock", M.quartz, IBFS.iResources.getSubItem("quartz_chip"), TIE_MATCH1, "branch");
		addSpecialToolRecipe1("biface", M.flint, IBFS.iResources.getSubItem("flint_sharp"), IBFS.iResources.getSubItem("flint_small"));
		addSpecialToolRecipe1("biface", M.obsidian, ItemMulti.createStack(M.obsidian, MC.chip_rock), ItemMulti.createStack(M.obsidian, MC.chip_rock));
		
		for (Mat material : Mat.filt(MC.FLINTY_TOOL))
		{
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "   c cc c", ItemTool.setMaterialToItem(IBFS.iTool.getSubItem("awl"), "head", material));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "  c ccccc", ItemMulti.createStack(material, MC.axe_rock));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "c ccccccc", ItemMulti.createStack(material, MC.shovel_rock));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "     cccc", ItemMulti.createStack(material, MC.hard_hammer_flint));
			RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "cc   cc c", ItemMulti.createStack(material, MC.spear_rock));
		}
		for (Mat material : Mat.filt(MC.ROCKY_TOOL))
		{
			if (MC.axe_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "  c ccccc", ItemMulti.createStack(material, MC.axe_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "c  cc ccc", ItemMulti.createStack(material, MC.axe_rock));
			}
			if (MC.shovel_rock.isBelongTo(material)) RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "c ccccccc", ItemMulti.createStack(material, MC.shovel_rock));
			if (MC.hard_hammer_rock.isBelongTo(material)) RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "      ppp", ItemMulti.createStack(material, MC.hard_hammer_rock));
			if (MC.sickle_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "  pcc ccc", ItemMulti.createStack(material, MC.sickle_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "p   ccccc", ItemMulti.createStack(material, MC.sickle_rock));
			}
			if (MC.spade_hoe_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "p c     p", ItemMulti.createStack(material, MC.spade_hoe_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "c p   p  ", ItemMulti.createStack(material, MC.spade_hoe_rock));
			}
			if (MC.spear_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "cc   cc c", ItemMulti.createStack(material, MC.spear_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), " ccc  c c", ItemMulti.createStack(material, MC.spear_rock));
			}
			if (MC.chisel_rock.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "pccc cccc", ItemMulti.createStack(material, MC.chisel_rock));
				RecipeAdder.addPolishRecipe(new OreStack(MC.chip_rock.getOreName(material)), "ccpc cccc", ItemMulti.createStack(material, MC.chisel_rock));
			}
			if (MC.whetstone.isBelongTo(material) && MC.fragment.isBelongTo(material))
			{
				RecipeAdder.addPolishRecipe(new OreStack(MC.fragment.getOreName(material)), "ppp   ppp", ItemTool.setMaterialToItem(IBFS.iTool.getSubItem("whetstone"), "head", material));
			}
		}
		
		addToolRecipe("hammer.hard.flint", new SingleInputMatch(new BaseStack(MC.hard_hammer_flint.instance), CONSUMER1, null), new OreStack(MC.bark.orePrefix), HANDLE_MATCH, TIE_MATCH1);
		addGeneralToolRecipe("shovel.rock", new BaseStack(MC.shovel_rock.instance));
		addGeneralToolRecipe("hammer.hard.rock", new BaseStack(MC.hard_hammer_rock.instance));
		addGeneralToolRecipe("spade.hoe.rock", new BaseStack(MC.spade_hoe_rock.instance));
		addGeneralToolRecipe("spear.rock", new BaseStack(MC.spear_rock.instance));
		addGeneralToolRecipe("sickle.rock", new BaseStack(MC.sickle_rock.instance));
		addGeneralToolRecipe("axe.rock", new BaseStack(MC.axe_rock.instance));
		addGeneralToolRecipe("chisel.rock", new BaseStack(MC.chisel_rock.instance));
		addGeneralNoTieToolRecipe("spinning.disk", new BaseStack(MC.spinning_disk.instance));
		addGeneralNoTieToolRecipe("axe.metal", new BaseStack(MC.axe_metal.instance));
		addGeneralNoTieToolRecipe("shovel.metal", new BaseStack(MC.shovel_metal.instance));
		addGeneralNoTieToolRecipe("pickaxe.metal", new BaseStack(MC.pickaxe_metal.instance));
		addGeneralNoTieToolRecipe("hammer.hard.metal", new BaseStack(MC.hard_hammer_metal.instance));
		addGeneralNoTieToolRecipe("chisel.metal", new BaseStack(MC.chisel_metal.instance));
		addGeneralNoTieToolRecipe("bowsaw.metal", new BaseStack(MC.bowsaw_metal.instance));
		
		// Firestarter
		RecipeAdder.addShapedRecipe(new BaseStack(IBFS.iTool.getSubItem("firestarter")), " s", "ld", 's', "stickWood", 'l', matchOfMaterial(new OreStack(MC.firewood.orePrefix), "head"), 'd', IBFS.iResources.getSubItem("dry_broadleaf"));
		// Bar Grizzly
		RecipeAdder.addShapedRecipe(new BaseStack(IBFS.iTool.getSubItem("bar.grizzly")), true, "rb", "br", 'r', IBFS.iResources.getSubItem("vine_rope"), 'b', matchOfMaterial(new OreStack(MC.branch.orePrefix), "head"));
		
		// Drain all fluids from fluid container.
		RecipeAdder.addShapelessRecipe(new ItemStack(IBFS.iFluidContainer), new SingleInputMatch(new BaseStack(IBFS.iFluidContainer), (i, o) -> {
			o.setItemDamage(i.getItemDamage());
			ItemSimpleFluidContainer.setCustomDamage(o, Math.min(IBFS.iFluidContainer.getMaxCustomDamage(i) - 1, ItemSimpleFluidContainer.getCustomDamage(i) + ItemSimpleFluidContainer.getFluidAmount(i)));
		}, null));
	}
	
	private static final ArrayList<Entry<AbstractStack, Mat>>	LIST		= new ArrayList<>();
	private static final BiConsumer<ItemStack, ItemStack>		CONSUMER1	= (input, output) -> {
		Mat material = ItemMulti.getMaterial(input);
		ItemTool.setMaterialToItem(output, "head", material);
	};
	
	private static final SingleInputMatch HANDLE_MATCH = new SingleInputMatch(new OreStack("stickWood"), (i, o) -> ItemToolFar.setMaterialToItem(o, "handle", M.wood), null);
	
	private static final SingleInputMatch TIE_MATCH1 = new SingleInputMatch(new AbstractStack()
	{
		ArrayList<ItemStack> list;
		
		public boolean similar(ItemStack stack)
		{
			return L.contain(LIST, entry -> entry.getKey().similar(stack));
		}
		
		public int size(ItemStack stack)
		{
			return 1;
		}
		
		@Override
		public List<ItemStack> display()
		{
			if (this.list == null)
			{
				this.list = new ArrayList<>();
				LIST.forEach(entry -> this.list.addAll(entry.getKey().display()));
				this.list.trimToSize();
			}
			return this.list;
		}
	}, (i, o) -> ItemToolFar.setMaterialToItem(o, "tie", L.getFromEntries(LIST, L.toPredicate(AbstractStack::similar, i)).orElse(Mat.VOID)), null);
	
	private static SingleInputMatch matchOfMaterial(AbstractStack input, String key)
	{
		return new SingleInputMatch(input, (i, o) -> ItemTool.setMaterialToItem(o, key, ItemMulti.getMaterial(i)), null);
	}
	
	private static void addSpecialToolRecipe1(String key, Mat head, Object...inputs)
	{
		ItemStack output = IBFS.iTool.getSubItem(key);
		ItemToolFar.setMaterialToItem(output, "head", head);
		RecipeAdder.addShapelessRecipe(output, inputs);
	}
	
	private static void addToolRecipe(String key, Object...inputs)
	{
		RecipeAdder.addShapelessRecipe(IBFS.iTool.getSubItem(key), inputs);
	}
	
	private static void addGeneralToolRecipe(String key, AbstractStack input)
	{
		RecipeAdder.addShapelessRecipe(IBFS.iTool.getSubItem(key), new SingleInputMatch(input, CONSUMER1, null), TIE_MATCH1, HANDLE_MATCH);
	}
	
	private static void addGeneralNoTieToolRecipe(String key, AbstractStack input)
	{
		RecipeAdder.addShapelessRecipe(IBFS.iTool.getSubItem(key), new SingleInputMatch(input, CONSUMER1, null), HANDLE_MATCH);
	}
}
