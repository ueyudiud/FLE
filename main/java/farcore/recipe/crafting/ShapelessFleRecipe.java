package farcore.recipe.crafting;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import farcore.recipe.stack.IItemStackMatcher;
import farcore.recipe.stack.IS;
import farcore.recipe.stack.OS;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * Shapeless recipe.<br>
 * Implements listener during output stack.
 * @author ueyudiud
 * @see farcore.recipe.ISingleInputRecipe
 */
public class ShapelessFleRecipe implements IRecipe, farcore.recipe.IRecipe, IRecipeListener
{
	/** Output of recipe. */
	public final ItemStack output;
	/** The inputs list. */
    public final List<ISingleInputRecipe> inputs = new ArrayList();

    public ShapelessFleRecipe(Object...objects)
    {
    	try
		{
    		int i = 0;
    		ItemStack o;
    		//Get output.
    		if(objects[i] instanceof ItemStack)
    			o = ((ItemStack) objects[i++]).copy();
			else if(objects[i] instanceof Item)
				o = new ItemStack((Item) objects[i++], 1);
			else if(objects[i] instanceof Block)
				o = new ItemStack((Block) objects[i++], 1);
			else throw new RuntimeException();
    		output = o;
    		if(objects[i] instanceof Object[])
    		{
    			objects = (Object[]) objects[i];
    			i = 0;
    		}
            for (; i < objects.length;)
            {
            	Object in = objects[i++];
                if (in instanceof ItemStack)
                {
                    inputs.add(
                    		new SingleMatcherRecipe(
                    				new IS(((ItemStack)in).getItem(), 
                    						1, ((ItemStack) in).getItemDamage())));
                }
                else if (in instanceof Item)
                {
                    inputs.add(new SingleMatcherRecipe(
            				new IS((Item) in)));
                }
                else if (in instanceof Block)
                {
                    inputs.add(new SingleMatcherRecipe(
            				new IS((Block) in)));
                }
                else if (in instanceof String)
                {
                    inputs.add(new SingleMatcherRecipe(
            				new OS((String) in)));
                }
                else if (in instanceof IItemStackMatcher)
                {
                    inputs.add(new SingleMatcherRecipe(
            				(IItemStackMatcher) in));
                }
//                else if (in instanceof FluidStack)
//                {
//                	inputs.add(new FluidContainerStack((FluidStack) in));
//                }
//                else if (in instanceof ItemAbstractStack)
//                {
//                	inputs.add(in);
//                }
                else if (in instanceof ISingleInputRecipe)
                {
                	inputs.add((ISingleInputRecipe) in);
                }
                else throw new NullPointerException();
            }
            if(output == null)
            	throw new NullPointerException();
        }
		catch(Exception e)
		{
			e.printStackTrace();
	        String ret = "Invalid shapeless fle recipe: ";
	        for (Object tmp : objects)
	        {
	            ret += tmp + ", ";
	        }
	        throw new RuntimeException(ret);
		}
	}
    
	@Override
	public boolean matches(InventoryCrafting crafting, World world)
	{
        ArrayList<ISingleInputRecipe> required = new ArrayList(inputs);
        
        for (int x = 0; x < crafting.getSizeInventory(); x++)
        {
            ItemStack slot = crafting.getStackInSlot(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator<ISingleInputRecipe> req = required.iterator();

                while (req.hasNext())
                {
                    ISingleInputRecipe next = req.next();

                    if (next.match(slot))
                    {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting crafting)
	{
		return output.copy();
	}

	@Override
	public int getRecipeSize()
	{
		return inputs.size();
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return output.copy();
	}

	@Override
	public void onCrafting(InventoryCrafting crafting, EntityPlayer player)
	{
        ArrayList<ISingleInputRecipe> required = new ArrayList(inputs);
        
        label:
        for (int x = 0; x < crafting.getSizeInventory(); x++)
        {
            ItemStack slot = crafting.getStackInSlot(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator<ISingleInputRecipe> req = required.iterator();

                while (req.hasNext())
                {
                    ISingleInputRecipe next = req.next();

                    if (next.match(slot))
                    {
                        slot.stackSize++;
                    	crafting.setInventorySlotContents(x, next.output(slot));
                    	continue label;
                    }
                }
            }
        }
	}
}