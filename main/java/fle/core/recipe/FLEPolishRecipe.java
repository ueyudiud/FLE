package fle.core.recipe;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import flapi.cg.RecipesTab;
import flapi.material.MaterialRock;
import flapi.recipe.CraftingState;
import flapi.recipe.IRecipeHandler;
import flapi.recipe.IRecipeHandler.MachineRecipe;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.JsonStack;
import flapi.recipe.stack.JsonStack.StackInfomation;
import flapi.util.ColorMap;
import flapi.util.io.JsonHandler;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.core.recipe.FLEPolishRecipe.PolishInfo;
import fle.core.recipe.FLEPolishRecipe.PolishRecipe;
import fle.resource.block.BlockFleRock;
import fle.tool.block.ItemOilLamp;
import fle.tool.item.ItemTool;
import fle.tool.item.ItemToolHead;

public class FLEPolishRecipe extends IRecipeHandler<PolishRecipe, PolishInfo>
{
	private static final FLEPolishRecipe instance = new FLEPolishRecipe();
	
	public static void init()
	{
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(ItemFleSub.a("branch_bush")), "p pp pp p", new ItemStack(Items.stick)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(Items.bone), "cc c c cc", ItemTool.a("bone_needle", Materials.Bone)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), " c ccc c ", ItemFleSub.a("flint_b", 4)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "c c      ", new ItemStack(Items.flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "  c ccccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "c  cc ccc", ItemToolHead.a("stone_axe", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "p cpccccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "c pccpccc", ItemToolHead.a("stone_axe", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "c ccccccc", ItemToolHead.a("stone_shovel", Materials.Flint, 5)));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "cpcc cccc", ItemToolHead.a("stone_shovel", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "     cccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "   c  ccc", ItemToolHead.a("flint_hammer", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "   c cc c", ItemTool.a("flint_awl", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), " cc cc cc", ItemToolHead.a("stone_knife", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("flint_a")), "cc cc cc ", ItemToolHead.a("stone_knife", Materials.Flint)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("chip_obsidian")), "   c cc c", ItemTool.a("flint_awl", Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("chip_obsidian")), " ccc  c c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("chip_obsidian")), "cc   cc c", ItemToolHead.a("flint_arrow", 4, Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("chip_obsidian")), " cc cc cc", ItemToolHead.a("stone_knife", Materials.Obsidian)));
		a(new PolishRecipe(RecipesTab.tabOldStoneAge, new BaseStack(ItemFleSub.a("chip_obsidian")), "cc cc cc ", ItemToolHead.a("stone_knife", Materials.Obsidian)));
		Object[][] o = {{"stone", Materials.Stone}, {"rhyolite", Materials.Rhyolite}, {"basalt", Materials.Basalt}, {"andesite", Materials.Andesite}, {"peridotite", Materials.Peridotite}};
		for(Object[] s : o)
		{
			ItemStack fragment = ItemFleSub.a("fragment_" + (String) s[0]);
			ItemStack chip = ItemFleSub.a("chip_" + (String) s[0]);
			MaterialRock material = (MaterialRock) s[1];
			Block m = BlockFleRock.a(material);
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(fragment), "pcp p p p", ItemOilLamp.a(m, 0)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(fragment), "ppp   ppp", ItemTool.a("whetstone", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(fragment), "ppp p    ", ItemTool.a("stone_decorticating_plate", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "p cpccccc", ItemToolHead.a("stone_axe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "c pccpccc", ItemToolHead.a("stone_axe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "cpcc cccc", ItemToolHead.a("stone_shovel", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "      ccc", ItemToolHead.a("stone_hammer", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "  pcc ccc", ItemToolHead.a("stone_sickle", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "p c     p", ItemToolHead.a("stone_spade_hoe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "c p   p  ", ItemToolHead.a("stone_spade_hoe", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), "cp p p pc", ItemTool.a("stone_decorticating_stick", material)));
			a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(chip), " pcp pcp ", ItemTool.a("stone_decorticating_stick", material)));
		}
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(ItemFleSub.a("chip_stone")), "ppp   ppp", ItemFleSub.a("stone_plate")));
		a(new PolishRecipe(RecipesTab.tabNewStoneAge, new BaseStack(ItemFleSub.a("chip_limestone")), "ccccccccc", ItemFleSub.a("dust_limestone")));
	}
	
	public static void postInit(JsonHandler loader)
	{
		instance.reloadRecipes(loader);
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
			if(recipe.input.equal(input))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected PolishRecipe readFromJson(PolishInfo element)
	{
		return new PolishRecipe(RecipesTab.tabClassic, element.input.toStack(), element.recipeTable.toCharArray(), element.output.getStack());
	}
	
	private FLEPolishRecipe() {}
	
	public static class PolishInfo
	{
		@Expose
		StackInfomation input;
		@Expose
		@SerializedName("map")
		String recipeTable;
		@Expose
		StackInfomation output;
	}
	
	public static class PolishRecipe extends MachineRecipe<PolishInfo>
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
		protected PolishInfo makeInfo()
		{
			PolishInfo info = new PolishInfo();
			info.input = new JsonStack(input).getInfomation();
			info.recipeTable = new String(cs);
			info.output = new JsonStack(output).getInfomation();
			return info;
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
			if(input == null) return this.input.equal(target);
			if(this.input.equal(target))
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
	
	public static class PolishRecipeKey extends RecipeKey
	{
		ItemAbstractStack stack;
		ItemStack stack1;
		String key;

		public PolishRecipeKey(ItemAbstractStack aStack, String aKey)
		{
			stack = aStack;
			key = aKey;
		}
		public PolishRecipeKey(ItemStack stack, String aKey)
		{
			if(stack != null)
				stack1 = stack.copy();
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
		protected boolean isEqual(RecipeKey keyRaw)
		{
			PolishRecipeKey key = (PolishRecipeKey) keyRaw;
			if(key.stack == null && key.stack1 == null) return false;
			if(stack == null && stack1 == null) return false;
			if(stack != null)
			{
				if(key.stack1 != null && !stack.equal(key.stack1)) return false;
				if(key.stack != null && !stack.equal(key.stack)) return false;
			}
			else if(key.stack != null)
			{
				if(stack1 != null && !key.stack.equal(stack1)) return false;
				if(stack != null && !key.stack.equal(stack)) return false;
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