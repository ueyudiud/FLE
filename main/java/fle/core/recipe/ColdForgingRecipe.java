package fle.core.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import flapi.recipe.CraftingState;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.recipe.stack.OreStack;
import fle.core.init.Materials;
import fle.core.item.ItemFleSub;
import fle.tool.item.ItemToolHead;

public class ColdForgingRecipe
{
	public static ItemStack getResult(IInventory aInput, String aStates)
	{
		for(ColdForgingRecipe recipe : list)
		{
			if(recipe.match(aInput, aStates)) return recipe.getOutput();
		}
		return null;
	}
	
	public static List<ColdForgingRecipe> getRecipes()
	{
		return list;
	}
	
	private static List<ColdForgingRecipe> list = new ArrayList();

	private static Map<A, Character> forgingMap = new HashMap();
	private static Map<Character, A> forgingMap1 = new HashMap();
	
	static
	{
		registerRecipe(new ColdForgingRecipe(new ItemAbstractStack[]{new OreStack("ingotCopper"), new OreStack("ingotCopper")}, 
				" q     n ", ItemFleSub.a("ingot_double_cu")));
		registerRecipe(new ColdForgingRecipe(new ItemAbstractStack[]{new OreStack("ingotCopper"), new OreStack("ingotCopper"), new OreStack("ingotCopper")}, 
				"o  o! !ss", ItemToolHead.a("metal_axe", Materials.Copper)));
		registerRecipe(new ColdForgingRecipe(new ItemAbstractStack[]{new OreStack("ingotCopper"), new OreStack("ingotCopper"), new OreStack("ingotCopper")}, 
				"   nnnsss", ItemToolHead.a("metal_pickaxe", Materials.Copper)));
		registerRecipe(new ColdForgingRecipe(new ItemAbstractStack[]{new OreStack("ingotCopper")}, 
				"o r1n4!s@", ItemToolHead.a("metal_shovel", Materials.Copper)));
		registerRecipe(new ColdForgingRecipe(new ItemAbstractStack[]{new OreStack("ingotCopper"), new OreStack("ingotCopper")}, 
				" 3*1 r!n4", ItemToolHead.a("metal_chisel", Materials.Copper)));
		a(' ', 0, 0);
		a('n', 1, 0);
		a('o', 0, 1);
		a('q', -1, 0);
		a('r', 0, -1);
		a('s', 2, 0);
		a('t', 0, 2);
		a('u', -2, 0);
		a('v', 0, -2);
		a('w', 3, 0);
		a('x', 0, 3);
		a('y', -3, 0);
		a('z', 0, -3);
		a('1', 1, 1);
		a('2', -1, 1);
		a('3', -1, -1);
		a('4', 1, -1);
		a('!', 2, 1);
		a('@', 2, -1);
		a('#', 1, 2);
		a('$', -1, 2);
		a('%', -2, -1);
		a('^', -2, 1);
		a('&', 1, -2);
		a('*', -1, -2);
	}
	
	private static void a(char chr, int...is)
	{
		forgingMap.put(new A(is[0], is[1]), chr);
		forgingMap1.put(chr, new A(is[0], is[1]));
	}
	
	public static char getState(int[] cs)
	{
		return forgingMap.containsKey(new A(cs[0], cs[1])) ? forgingMap.get(new A(cs[0], cs[1])) : 'c';
	}
	
	public static char getForgingMapOutputChar(char input1, char input2)
	{
		if(input1 == ' ') return input2;
		if(input2 == ' ') return input1;
		if(!forgingMap1.containsKey(input1) || !forgingMap1.containsKey(input2)) return 'c';
		A a = forgingMap1.get(input1);
		A b = forgingMap1.get(input2);
		A cs = new A(a.a + b.a, a.b + b.b);
		return forgingMap.containsKey(cs) ? forgingMap.get(cs) : 'c';
	}
	
	private static class A
	{
		int a;
		int b;
		
		public A(int a, int b)
		{
			this.a = a;
			this.b = b;
		}
		
		@Override
		public int hashCode()
		{
			return (a + 1832) * 31 + b;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj instanceof A ? ((A) obj).a == a && ((A) obj).b == b : false;
		}
	}
	
	public static void registerRecipe(ColdForgingRecipe recipe)
	{
		list.add(recipe);
	}
	
	ItemAbstractStack[] stack;
	CraftingState[] map;
	ItemStack output;
	
	public ColdForgingRecipe(ItemAbstractStack[] aStack, String aMap, ItemStack aOutput)
	{
		stack = aStack;
		map = CraftingState.getStates(aMap);
		output = aOutput.copy();
	}
	
	public boolean match(IInventory aInput, String aStates)
	{
		if(!matchInput(aInput)) return false;
		if(map.length != aStates.length()) return false;
		for(int i = 0; i < map.length; ++i)
		{
			if(map[i] != CraftingState.getState(aStates.charAt(i))) return false;
		}
		return true;
	}
	
	private boolean matchInput(IInventory aInput)
	{
		return RecipeHelper.matchShapedInventory(aInput, 0, 4, stack);
	}
	
	public ItemStack getOutput()
	{
		return output.copy();
	}
	
	public CraftingState[] getRecipeMap()
	{
		return map.clone();
	}

	public ItemAbstractStack[] getRecipeInputs()
	{
		return stack;
	}
}