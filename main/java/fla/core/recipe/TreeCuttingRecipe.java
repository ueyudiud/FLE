package fla.core.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import fla.api.item.ITreeLog;

public class TreeCuttingRecipe extends ShapedOreRecipe
{
	public TreeCuttingRecipe(ItemStack result, Object input) 
	{
		super(result, new Object[]{"y", "x", 'x', input, 'y', "craftingToolAxe"});
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
