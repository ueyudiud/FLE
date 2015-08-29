package fle.core.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fle.api.recipe.CraftingState;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.ColorMap;
import fle.cg.RecipesTab;
import fle.core.block.ItemOilLamp;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;

public class PolishRecipe
{
	private static List<PolishRecipe> list = new ArrayList();
	
	public static List<PolishRecipe> getRecipeList()
	{
		return list;
	}
	
	static
	{
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("branch_bush")), "p pp pp p", new ItemStack(Items.stick)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), " c ccc c ", ItemFleSub.a("flint_b", 4)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c c      ", new ItemStack(Items.flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "  c ccccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c  cc ccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "p cpccccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c pccpccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c ccccccc", ItemToolHead.a("stone_shovel", Materials.Flint, 5)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "cpcc cccc", ItemToolHead.a("stone_shovel", Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "     cccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "   c  ccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "   c cc c", ItemTool.a("flint_awl", Materials.Flint)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), "   c cc c", ItemTool.a("flint_awl", Materials.Obsidian)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		registerRecipe(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_b")), "pcp p p p", ItemOilLamp.a(Blocks.stone, 0)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_b")), "ppp   ppp", ItemTool.a("whetstone", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "ppp   ppp", ItemFleSub.a("stone_plate")));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "p cpccccc", ItemToolHead.a("stone_axe", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "c pccpccc", ItemToolHead.a("stone_axe", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "cpcc cccc", ItemToolHead.a("stone_shovel", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "      ccc", ItemToolHead.a("stone_hammer", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "      ccc", ItemToolHead.a("stone_hammer", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "  pcc ccc", ItemToolHead.a("stone_sickle", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "p c     p", ItemToolHead.a("stone_spade_hoe", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "c p   p  ", ItemToolHead.a("stone_spade_hoe", Materials.Stone)));
		registerRecipe(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("limestone")), "ccccccccc", ItemFleSub.a("dust_limestone")));
	}
	
	public static void registerRecipe(PolishRecipe recipe)
	{
		list.add(recipe);
	}
	
	public static ItemStack getRecipeResult(ItemStack input, char[] cs)
	{
		for(PolishRecipe recipe : list)
		{
			if(recipe.matchRecipe(input, cs))
			{
				return recipe.output.copy();
			}
		}
		return null;
	}
	
	public static boolean canPolish(ItemStack input) 
	{
		for(PolishRecipe recipe : list)
		{
			if(recipe.matchRecipe(input, null))
			{
				return true;
			}
		}
		return false;
	}
	
	RecipesTab tab;
	private ItemAbstractStack input;
	private char[] cs;
	public ItemStack output;
	
	public PolishRecipe(RecipesTab tab, ItemAbstractStack input, String str, ItemStack output)
	{
		this(tab, input, str.toCharArray(), output);
	}
	public PolishRecipe(RecipesTab tab, ItemAbstractStack input, char[] cs, ItemStack output)
	{
		this.tab = tab;
		this.input = input;
		this.cs = cs;
		this.output = output.copy();
	}
	public PolishRecipe(ItemAbstractStack input, ColorMap aMap, int xStart, int yStart, ItemStack output)
	{
		this.input = input;
		this.output = output;
		char cs[] = new char[9];
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				cs[i + j * 3] = CraftingState.getState(aMap.getColorFromCrood(xStart + i, yStart + j)).getCharIndex();
		this.cs = cs;
	}
	
	public boolean matchRecipe(ItemStack target, char[] input)
	{
		if(input == null) return this.input.isStackEqul(target);
		if(this.input.isStackEqul(target))
		{
			for(int i = 0; i < input.length; ++i)
			{
				if(input[i] != cs[i]) return false;
			}
			return true;
		}
		return false;
	}
	
	public char[] getRecipeMap()
	{
		return cs;
	}
	
	public ItemAbstractStack getinput()
	{
		return input;
	}
	
	public RecipesTab getTab()
	{
		return tab;
	}
}