package fle.api.recipe.smelting;

import farcore.interfaces.energy.thermal.IThermalItem;
import farcore.lib.stack.AbstractStack;
import fle.api.item.ItemDividableFood;
import fle.api.recipe.smelting.SmeltingRecipes.SmeltingRecipe;
import fle.api.util.TemperatureHandler;
import net.minecraft.item.ItemStack;

public class DiviableFoodSmeltingRecipe extends SmeltingRecipe
{
	public DiviableFoodSmeltingRecipe(AbstractStack input, int energy, int temp1, ItemStack output1) 
	{
		super(input, energy, temp1, output1);
	}
	public DiviableFoodSmeltingRecipe(AbstractStack input, int energy, int temp1, ItemStack output1, int temp2,
			ItemStack output2)
	{
		super(input, energy, temp1, output1, temp2, output2);
	}
	
	@Override
	public ItemStack getOutput(ItemStack stack)
	{
		if(minTemp2 < 0)
		{
			ItemStack output = output1.copy();
			ItemDividableFood.setFoodAmount(output, ItemDividableFood.getFoodAmount(stack));
			return output;
		}
		float temp;
		if(stack.getItem() instanceof IThermalItem)
		{
			temp = ((IThermalItem) stack.getItem()).getTemperature(stack);
		}
		else
		{
			temp = TemperatureHandler.getTemperature(stack);
		}
		if(temp >= minTemp2)
		{
			return ItemStack.copyItemStack(output2);
		}

		ItemStack output = output1.copy();
		ItemDividableFood.setFoodAmount(output, ItemDividableFood.getFoodAmount(stack));
		return output;
	}
}