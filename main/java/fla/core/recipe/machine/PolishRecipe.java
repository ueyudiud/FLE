package fla.core.recipe.machine;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import fla.api.recipe.IItemChecker;
import fla.api.recipe.IItemChecker.ItemChecker;
import fla.api.recipe.ItemState;
import fla.core.FlaItems;
import fla.core.item.ItemSub;

public class PolishRecipe
{
	private static List<PolishRecipe> list = new ArrayList();
	
	static
	{
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("branch_bush")), "p pp pp p", new ItemStack(Items.stick)));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), " h hhh h ", ItemSub.a("flint_b", 4)));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "h h      ", new ItemStack(Items.flint)));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "h  hh hhh", ItemSub.a("flint_axe")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "  h hhhhh", ItemSub.a("flint_axe")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "h hhhhhhh", ItemSub.a("flint_shovel")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "     hhhh", ItemSub.a("flint_hammer")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "phhh  h  ", ItemSub.a("flint_gaff")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "hhp  h  h", ItemSub.a("flint_gaff")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("flint_a")), "   h hh h", new ItemStack(FlaItems.flint_awl)));
		registerRecipe(new PolishRecipe(new ItemChecker(Items.flint), "   h hh h", new ItemStack(FlaItems.flint_awl)));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_a")), "h phhphhh", ItemSub.a("stone_axe")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_a")), "p hphhhhh", ItemSub.a("stone_axe")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_a")), "hphh hhhh", ItemSub.a("stone_shovel")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_a")), "      hhh", ItemSub.a("stone_hammer")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_a")), "pphp hhhh", ItemSub.a("stone_spear")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_a")), "p h     p", ItemSub.a("stone_spade_hoe")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_a")), "h p   p  ", ItemSub.a("stone_spade_hoe")));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_b")), "ppp   ppp", new ItemStack(FlaItems.whetstone)));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_b")), " h hhh h ", ItemSub.a("stone_a", 2)));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("stone_b")), "php p p p", new ItemStack(FlaItems.stone_oil_lamp)));
		registerRecipe(new PolishRecipe(new ItemChecker(ItemSub.a("limestone_a")), "mmmmmmmmm", ItemSub.a("dust_limestone")));
		
		ItemState.StateManager.register(new ItemChecker(Items.flint), ItemState.Hit);
		ItemState.StateManager.register(new ItemChecker(FlaItems.flint_awl, OreDictionary.WILDCARD_VALUE), ItemState.Crush);
		ItemState.StateManager.register(new ItemChecker(FlaItems.wooden_hammer, OreDictionary.WILDCARD_VALUE), ItemState.Hit);
		ItemState.StateManager.register(new ItemChecker(FlaItems.whetstone, OreDictionary.WILDCARD_VALUE), ItemState.Polish);
		ItemState.StateManager.register(new ItemChecker(FlaItems.flint_hammer, OreDictionary.WILDCARD_VALUE), ItemState.Main_Hit);
		ItemState.StateManager.register(new ItemChecker(FlaItems.stone_hammer, OreDictionary.WILDCARD_VALUE), ItemState.Main_Hit);
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
			if(recipe.input.match(input))
			{
				return true;
			}
		}
		return false;
	}
	
	private IItemChecker input;
	private char[] cs;
	public ItemStack output;
	
	public PolishRecipe(IItemChecker input, String str, ItemStack output)
	{
		this(input, str.toCharArray(), output);
	}
	public PolishRecipe(IItemChecker input, char[] cs, ItemStack output)
	{
		this.input = input;
		this.cs = cs;
		this.output = output.copy();
	}
	
	public boolean matchRecipe(ItemStack target, char[] input)
	{
		if(this.input.match(target))
		{
			for(int i = 0; i < input.length; ++i)
			{
				if(input[i] != cs[i]) return false;
			}
			return true;
		}
		return false;
	}
}