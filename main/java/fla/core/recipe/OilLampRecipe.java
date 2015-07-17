package fla.core.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import fla.api.recipe.FuelOilLamp;
import fla.api.recipe.FuelStack;
import fla.core.FlaItems;

public class OilLampRecipe extends ShapelessOreRecipe
{
	protected FuelStack<FuelOilLamp> stack;
	
	public OilLampRecipe(FuelStack<FuelOilLamp> contain, Object recipeInput)
	{
		super(new ItemStack(FlaItems.stone_oil_lamp), new Object[]{new ItemStack(FlaItems.stone_oil_lamp, 1, OreDictionary.WILDCARD_VALUE), recipeInput});
		stack = contain.copy();
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) 
	{
		ItemStack ret = super.getCraftingResult(var1);
		FlaItems.stone_oil_lamp.setLiquid(ret, stack.getFuel(), stack.getContain());
		return ret;
	}
}