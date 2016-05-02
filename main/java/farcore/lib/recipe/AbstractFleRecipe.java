package farcore.lib.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map.Entry;

import farcore.interfaces.item.ICustomDamageItem;
import farcore.lib.collection.Ety;
import farcore.lib.stack.AbstractStack;
import farcore.util.U;
import net.minecraft.item.ItemStack;

public abstract class AbstractFleRecipe implements IFleRecipe
{
	public ItemStack output;
	public int tick;
	public Entry<AbstractStack, AbstractStack>[] tools;
	
	protected static AbstractFleRecipe wrap(AbstractFleRecipe recipe, ItemStack output, int tick, Entry...tools)
	{
		recipe.output = ItemStack.copyItemStack(output);
		recipe.tick = tick;
		recipe.tools = new Entry[tools.length];
		for(int i = 0; i < tools.length; ++i)
		{
			AbstractStack tool = U.Inventorys.asStack(tools[i].getKey(), true);
			AbstractStack material = U.Inventorys.asStack(tools[i].getValue(), true);
			recipe.tools[i] = new Ety(tool, material);
		}
		return recipe;
	}
	
	protected AbstractFleRecipe(){	}
	
	@Override
	public boolean matchRecipe(ICraftingInventoryMatching inventory)
	{
		if(inventory.getToolSlotSize() < tools.length) return false;
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
							tools.remove(j);
							break label;
						}
					}
					return false;
				}
			}
		}
		return tools.isEmpty();
	}
	
	@Override
	public int getRecipeTick(ICraftingInventoryMatching inventory)
	{
		return tick;
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
							if(tool != null)
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
							if(material != null)
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
	}
	
	@Override
	public ItemStack getOutput(ICraftingInventoryMatching inventory)
	{
		return output.copy();
	}
}