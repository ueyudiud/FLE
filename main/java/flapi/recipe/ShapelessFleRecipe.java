package flapi.recipe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import flapi.cg.RecipesTab;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.FluidContainerStack;
import flapi.recipe.stack.AbstractStack;
import flapi.recipe.stack.OreStack;

public class ShapelessFleRecipe implements IFleRecipe
{
	protected Object output = null;
    protected Set<Object> inputs = new HashSet<Object>();
    protected RecipesTab tab;

	public ShapelessFleRecipe(Object aOutput, Object...aInput) 
	{
		this(RecipesTab.tabClassic, aOutput, aInput);
	}
	public ShapelessFleRecipe(RecipesTab aTab, Object aOutput, Object...aInput) 
	{
		tab = aTab;
		try
		{
			if(aOutput != null)
			{
				if(aOutput instanceof ItemStack)
					output = ((ItemStack) aOutput).copy();
				else if(aOutput instanceof Item)
					output = new ItemStack((Item) aOutput, 1);
				else if(aOutput instanceof Block)
					output = new ItemStack((Block) aOutput, 1);
				else if(aOutput instanceof FluidStack)
					output = new SingleFillFluidRecipe(new FluidContainerStack((FluidStack) aOutput));
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
                inputs.add(new BaseStack(((ItemStack)in).copy()));
            }
            else if (in instanceof Item)
            {
                inputs.add(new BaseStack((Item)in));
            }
            else if (in instanceof Block)
            {
                inputs.add(new BaseStack((Block)in));
            }
            else if (in instanceof String)
            {
                inputs.add(new OreStack((String)in));
            }
            else if (in instanceof FluidStack)
            {
            	inputs.add(new FluidContainerStack((FluidStack) in));
            }
            else if (in instanceof AbstractStack)
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

                    if (next instanceof AbstractStack)
                    {
                        match = ((AbstractStack) next).similar(slot);
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
			output instanceof SingleInputRecipe ? 
					((SingleInputRecipe) output).getResult(null) : null;
	}
	
	public AbstractStack[] getInputs()
	{
		Set<AbstractStack> ret = new HashSet();
		for(Object obj : inputs)
		{
			if(obj instanceof AbstractStack)
				ret.add((AbstractStack) obj);
			if(obj instanceof SingleInputRecipe)
				ret.add(((SingleInputRecipe) obj).getShowStack());
		}
		return ret.toArray(new AbstractStack[ret.size()]);
	}
	
	@Override
	public RecipesTab getRecipeTab()
	{
		return tab;
	}
}