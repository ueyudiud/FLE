package fle.api.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import farcore.interfaces.item.ICustomDamageItem;
import farcore.lib.recipe.ICraftingInventory;
import farcore.lib.recipe.ShapelessFleRecipe;
import farcore.lib.stack.AbstractStack;
import farcore.lib.stack.BaseStack;
import farcore.util.U;
import fle.api.item.ItemDividableFood;
import net.minecraft.item.ItemStack;

public class FoodUseCraftingRecipe extends ShapelessFleRecipe
{
	public final float perUse;

	public FoodUseCraftingRecipe(ItemStack output, float perUse, int tick, Object...objects)
	{
		super(output, tick, objects);
		this.perUse = perUse;
	}
	public FoodUseCraftingRecipe(float perUse, int tick, Object...objects)
	{
		super(tick, objects);
		this.perUse = perUse;
	}
	
	@Override
	protected boolean matchInput(ItemStack target, AbstractStack input)
	{
		if(!super.matchInput(target, input)) return false;
		if(!(target.getItem() instanceof ItemDividableFood)) return true;
		return ItemDividableFood.getFoodAmount(target) >= perUse;
	}
	
	@Override
	public void onOutputStack(ICraftingInventory inventory)
	{
		ArrayList<Entry<AbstractStack, AbstractStack>> tools = new ArrayList(Arrays.asList(this.tools));
		
		for(int i = 0; i < inventory.getToolSlotSize(); ++i)
		{
			ItemStack stack = inventory.getTool(i);
			ItemStack stack2 = inventory.getToolMatrial(i);
			if(stack != null || stack2 != null)
			{
				label:
				{
					for(int j = 0; j < tools.size(); ++j)
					{
						Entry<AbstractStack, AbstractStack> entry = tools.get(j);
						if(((entry.getKey() == null && stack == null) || entry.getKey().contain(stack)) && 
								((entry.getValue() == null && stack2 == null) || entry.getValue().contain(stack2)))
						{
							AbstractStack tool = entry.getKey();
							AbstractStack material = entry.getValue();
							if(tool != null && material != BaseStack.EMPTY)
							{
								if(tool.useContainer() || ! stack.getItem().hasContainerItem(stack))
								{
									inventory.decrStackInTool(i, tool.size(stack));
								}
								else
								{
									if(stack.getItem() instanceof ICustomDamageItem)
									{
										inventory.setToolStack(i, ((ICustomDamageItem) stack.getItem()).getCraftedItem(stack, inventory));
									}
									else
									{
										inventory.setToolStack(i, stack.getItem().getContainerItem(stack));
									}
								}
							}
							if(material != null && material != BaseStack.EMPTY)
							{
								if(material.useContainer() || ! stack2.getItem().hasContainerItem(stack2))
								{
									inventory.decrStackInToolMatrial(i, material.size(stack2));
								}
								else
								{
									inventory.setToolMatrialStack(i, stack2.getItem().getContainerItem(stack2));
								}
							}
							tools.remove(j);
							break label;
						}
					}
				}
			}
		}
		List<AbstractStack> check = new ArrayList(input);
		boolean hasOutput = outputDetect == null;
		label:
		for(int i = 0; i < inventory.getCraftingMatrixSize(); ++i)
		{
			ItemStack stack = inventory.getStackInMatrix(i);
			if(stack == null) continue;
			for(int j = 0; j < check.size(); ++j)
			{
				AbstractStack check1 = check.get(j);
				if(matchInput(stack, check1))
				{
					if(stack.getItem() instanceof ItemDividableFood)
					{
						ItemDividableFood.useAndDecrFood(stack, perUse);
						inventory.setStackInMatrix(i, U.Inventorys.valid(stack));
					}
					else
					{
						if(check1.useContainer() || !stack.getItem().hasContainerItem(stack))
						{
							inventory.decrStackInMatrix(i, check1.size(stack));
						}
						else
						{
							inventory.setStackInMatrix(i, stack.getItem().getContainerItem(stack));
						}
					}
					check.remove(j);
					continue label;
				}
			}
			if(!hasOutput)
			{
				if(outputDetect.match(stack))
				{
					inventory.setStackInMatrix(i, outputDetect.output(stack)[1]);
					hasOutput = true;
					continue label;
				}
			}
		}
	}
}