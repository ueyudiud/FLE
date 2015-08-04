package fle.core.recipe.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import fle.api.item.ITreeLog;
import fle.api.recipe.ShapedFleRecipe;

public class TreeCuttingRecipe extends ShapedFleRecipe
{
	public TreeCuttingRecipe() 
	{
		super(new ItemStack(Blocks.log), new Object[]{"y", "x", 'x', "logFLE", 'y', "craftingToolAxe"});
	}

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1)
    {
    	return getResult(var1).copy(); 
    }
    
    ItemStack getResult(InventoryCrafting inv)
    {
    	for(int i = 0; i < inv.getSizeInventory(); ++i)
    	{
    		if(inv.getStackInSlot(i) != null)
    		{
    			ItemStack stack = inv.getStackInSlot(i);
    			if (stack.getItem() instanceof ITreeLog) 
    			{
					ITreeLog log = (ITreeLog) stack.getItem();
					return log.getLogDrop(stack);
				}
    		}
    	}
    	return super.getCraftingResult(inv);
    }
}