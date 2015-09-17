package fle.core.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import fle.api.cg.RecipesTab;
import fle.api.recipe.CraftingState;
import fle.api.recipe.IRecipeHandler;
import fle.api.recipe.IRecipeHandler.MachineRecipe;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.ItemBaseStack;
import fle.api.util.ColorMap;
import fle.core.block.ItemOilLamp;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.item.ItemTool;
import fle.core.item.ItemToolHead;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;

public class FLEPolishRecipe extends IRecipeHandler<PolishRecipe>
{
	private static final FLEPolishRecipe instance = new FLEPolishRecipe();
	
	static
	{
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("branch_bush")), "p pp pp p", new ItemStack(Items.stick)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), " c ccc c ", ItemFleSub.a("flint_b", 4)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c c      ", new ItemStack(Items.flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "  c ccccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c  cc ccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "p cpccccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c pccpccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "c ccccccc", ItemToolHead.a("stone_shovel", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "cpcc cccc", ItemToolHead.a("stone_shovel", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "     cccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "   c  ccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("flint_a")), "   c cc c", ItemTool.a("flint_awl", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), "   c cc c", ItemTool.a("flint_awl", Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new ItemBaseStack(ItemFleSub.a("chip_obsidian")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_b")), "pcp p p p", ItemOilLamp.a(Blocks.stone, 0)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_b")), "ppp   ppp", ItemTool.a("whetstone", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "ppp   ppp", ItemFleSub.a("stone_plate")));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "p cpccccc", ItemToolHead.a("stone_axe", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "c pccpccc", ItemToolHead.a("stone_axe", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "cpcc cccc", ItemToolHead.a("stone_shovel", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "      ccc", ItemToolHead.a("stone_hammer", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "  pcc ccc", ItemToolHead.a("stone_sickle", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "p c     p", ItemToolHead.a("stone_spade_hoe", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "c p   p  ", ItemToolHead.a("stone_spade_hoe", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_b")), "ppp p    ", ItemTool.a("stone_decorticating_plate", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), "cp p p pc", ItemTool.a("stone_decorticating_stick", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("stone_a")), " pcp pcp ", ItemTool.a("stone_decorticating_stick", Materials.Stone)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new ItemBaseStack(ItemFleSub.a("limestone")), "ccccccccc", ItemFleSub.a("dust_limestone")));
	}

	public static FLEPolishRecipe getInstance()
	{
		return instance;
	}
	
	public static void a(PolishRecipe recipe)
	{
		instance.registerRecipe(recipe);
	}
	
	public static boolean canPolish(ItemStack input) 
	{
		for(PolishRecipe recipe : instance.recipeSet)
		{
			if(recipe.input.isStackEqul(input))
			{
				return true;
			}
		}
		return false;
	}
	
	private FLEPolishRecipe() {}
	
	public static class PolishRecipe implements MachineRecipe
	{
		PolishRecipeKey key;
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
		
		@Override
		public RecipeKey getRecipeKey()
		{
			if(key == null)
			{
				key = new PolishRecipeKey(input, new String(cs));
			}
			return key;
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
	
	public static class PolishRecipeKey implements RecipeKey
	{
		ItemAbstractStack stack;
		ItemStack stack1;
		String key;

		public PolishRecipeKey(ItemAbstractStack aStack, String aKey)
		{
			stack = aStack;
			key = aKey;
		}
		public PolishRecipeKey(ItemStack aStack, String aKey)
		{
			if(stack1 != null)
				stack1 = aStack.copy();
			key = aKey;
		}
		
		@Override
		public int hashCode()
		{
			int code = 1;
			code = code * 31 + key.hashCode();
			return code;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(!(obj instanceof PolishRecipeKey)) return false;
			PolishRecipeKey key = (PolishRecipeKey) obj;
			if(key.stack == null && key.stack1 == null) return false;
			if(stack != null)
			{
				if(key.stack1 != null && !stack.isStackEqul(key.stack1)) return false;
				if(key.stack != null && !stack.isStackEqul(key.stack)) return false;
			}
			else if(key.stack != null)
			{
				if(stack1 != null && !key.stack.isStackEqul(stack1)) return false;
				if(stack != null && !key.stack.isStackEqul(stack)) return false;
			}
			if(!key.key.equals(this.key)) return false;
			return true;
		}

		@Override
		public String toString()
		{
			try
			{
				return "recipe.input:" + stack.toString() + "." + key;
			}
			catch(Throwable e)
			{
				return "null";
			}
		}
	}
}