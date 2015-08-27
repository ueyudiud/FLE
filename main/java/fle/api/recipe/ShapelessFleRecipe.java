package fle.api.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ShapelessFleRecipe implements IRecipe
{
    private Object output = null;
    private ArrayList<Object> inputs = new ArrayList<Object>();
    
	public ShapelessFleRecipe(Object aOutput, Object...aInput) 
	{
		try
		{
			if(aOutput != null)
			{
				if(aOutput instanceof ItemStack)
					output = ((ItemStack) aOutput).copy();
				else if(aOutput instanceof Item)
					output = new ItemStack((Item) aOutput, 1, OreDictionary.WILDCARD_VALUE);
				else if(aOutput instanceof Block)
					output = new ItemStack((Block) aOutput, 1, OreDictionary.WILDCARD_VALUE);
				else if(aOutput instanceof FluidStack)
					output = new SingleFillFluidRecipe(new ItemFluidContainerStack(false, (FluidStack) aOutput));
				else if(aOutput instanceof SingleInputRecipe)
					output = aOutput;
			}
			setup(aInput);
        }
		catch(Throwable e)
		{
			e.printStackTrace();
	        String ret = "Invalid shapeless fle recipe: ";
	        for (Object tmp : aInput)
	        {
	            ret += tmp + ", ";
	        }
	        ret += output;
	        throw new RuntimeException(ret);
		}
	}
	private void setup(Object...aRecipe) 
	{
        for (Object in : aRecipe)
        {
            if (in instanceof ItemStack)
            {
                inputs.add(new ItemBaseStack(((ItemStack)in).copy()));
            }
            else if (in instanceof Item)
            {
                inputs.add(new ItemBaseStack((Item)in));
            }
            else if (in instanceof Block)
            {
                inputs.add(new ItemBaseStack((Block)in));
            }
            else if (in instanceof String)
            {
                inputs.add(new ItemOreStack((String)in));
            }
            else if (in instanceof FluidStack)
            {
            	inputs.add(new ItemFluidContainerStack(true, (FluidStack) in));
            }
            else if (in instanceof ItemAbstractStack)
            {
            	inputs.add(in);
            }
            else if (in instanceof SingleInputRecipe)
            {
            	inputs.add(in);
            	if(output == null)
            		output = (SingleInputRecipe) in;
            	else throw new NullPointerException();
            }
            else throw new NullPointerException();
        }
        if(output == null)
        	throw new NullPointerException();
	}

	@Override
    public boolean matches(InventoryCrafting aInv, World aWorld)
    {
        ArrayList<Object> required = new ArrayList<Object>(inputs);
        boolean flag = !(output instanceof SingleInputRecipe);
        
        for (int x = 0; x < aInv.getSizeInventory(); x++)
        {
            ItemStack slot = aInv.getStackInSlot(x);

            if (slot != null)
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                if(!flag)
                {
                	if(((SingleInputRecipe) output).match(slot.copy()))
                		flag = true;
                }
                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemAbstractStack)
                    {
                        match = ((ItemAbstractStack) next).isStackEqul(slot);
                    }
                    else if (next instanceof SingleInputRecipe)
                    {
                    	match = ((SingleInputRecipe) next).match(slot);
                    }

                    if (match)
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

        return required.isEmpty() && flag;
    }

	@Override
	public ItemStack getCraftingResult(InventoryCrafting aInv)
	{
		if(output instanceof ItemStack) return ((ItemStack) output).copy();
		else if(output instanceof SingleInputRecipe)
			for(int i = 0; i < aInv.getSizeInventory(); ++i)
			{
				if(aInv.getStackInSlot(i) != null)
					if(((SingleInputRecipe) output).match(aInv.getStackInSlot(i).copy()))
						return ((SingleInputRecipe) output).getResult(aInv.getStackInSlot(i).copy());
			}
		return null;
	}

	@Override
	public int getRecipeSize() 
	{
		return inputs.size();
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return output instanceof ItemStack ? ((ItemStack) output).copy() :
			output instanceof SingleInputRecipe ? ((SingleInputRecipe) output).getResult(null) : null;
	}
	
	public ItemAbstractStack[] getInputs()
	{
		List<ItemAbstractStack> ret = new ArrayList();
		for(Object obj : inputs)
		{
			if(obj instanceof ItemAbstractStack)
				ret.add((ItemAbstractStack) obj);
		}
		return ret.toArray(new ItemAbstractStack[ret.size()]);
	}
}