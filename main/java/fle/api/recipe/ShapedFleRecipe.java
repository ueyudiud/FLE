package fle.api.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fle.api.cg.RecipesTab;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class ShapedFleRecipe implements IFleRecipe
{
    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
	protected int width;
	protected int height;
	protected int targetID = -1;
	protected boolean mirrored = false;
	private Object output;
	protected ItemAbstractStack[] inputs;
	private RecipesTab tab;

	public ShapedFleRecipe(Object aOutput, Object...aRecipe) 
	{
		this(RecipesTab.tabClassic, aOutput, aRecipe);
	}
	public ShapedFleRecipe(RecipesTab aTab, Object aOutput, Object...aRecipe) 
	{
		tab = aTab;
		try
		{
			if(aOutput != null)
			{
				if(aOutput instanceof ItemStack)
					setOutput(((ItemStack) aOutput).copy());
				else if(aOutput instanceof Item)
					setOutput(new ItemStack((Item) aOutput, 1));
				else if(aOutput instanceof Block)
					setOutput(new ItemStack((Block) aOutput, 1));
				else if(aOutput instanceof FluidStack)
					setOutput(new SingleFillFluidRecipe(new ItemFluidContainerStack(false, (FluidStack) aOutput)));
				else if(aOutput instanceof SingleInputRecipe)
					setOutput(aOutput);
				else throw new NullPointerException();
			}
			else throw new NullPointerException();
	        setup(aRecipe);
		}
		catch(Throwable e)
		{
            String ret = "Invalid shaped fle recipe: ";
            for (Object tmp : aRecipe)
            {
                ret += tmp + ", ";
            }
            ret += output;
			e.printStackTrace();
			throw new RuntimeException(ret);
		}
	}
	private void setup(Object...aRecipe) 
	{
	    String shape = "";
	    int idx = 0;

        if (aRecipe[idx] instanceof Integer)
        {
            targetID = (Integer)aRecipe[idx];
            ++idx;
        }
        
        if (aRecipe[idx] instanceof Boolean)
        {
            mirrored = (Boolean)aRecipe[idx];
            if (aRecipe[idx+1] instanceof Object[])
            {
                aRecipe = (Object[])aRecipe[idx + 1];
            }
            else
            {
                ++idx;
            }
        }

        if (aRecipe[idx] instanceof String[])
        {
            String[] parts = ((String[])aRecipe[idx++]);

            for (String s : parts)
            {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        }
        else
        {
            while (aRecipe[idx] instanceof String)
            {
                String s = (String)aRecipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        if (width * height != shape.length())
        {
            throw new RuntimeException();
        }

        HashMap<Character, ItemAbstractStack> itemMap = new HashMap();

        for (; idx < aRecipe.length; idx += 2)
        {
            Character chr = (Character)aRecipe[idx];
            Object in = aRecipe[idx + 1];

            if (in instanceof ItemStack)
            {
                itemMap.put(chr, new ItemBaseStack((ItemStack) in));
            }
            else if (in instanceof Item)
            {
                itemMap.put(chr, new ItemBaseStack((Item) in));
            }
            else if (in instanceof Block)
            {
                itemMap.put(chr, new ItemBaseStack((Block) in));
            }
            else if (in instanceof String)
            {
                itemMap.put(chr, new ItemOreStack((String) in));
            }
            else if (in instanceof FluidStack)
            {
            	boolean flag = true;
            	if(aRecipe[idx + 2] instanceof Boolean)
            	{
            		flag = ((Boolean) aRecipe[idx + 2]).booleanValue();
            		++idx;
            	}
                itemMap.put(chr, new ItemFluidContainerStack(flag, (FluidStack) in));
            }
            else if (in instanceof ItemAbstractStack)
            {
            	itemMap.put(chr, (ItemAbstractStack) in);
            }
            else throw new NullPointerException();
        }

        inputs = new ItemAbstractStack[width * height];
        int x = 0;
        for (char chr : shape.toCharArray())
        {
            inputs[x++] = itemMap.get(chr);
        }
	}

	@Override
	public boolean matches(InventoryCrafting aInv, World aWorld) 
	{
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
        {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
            {
                if (checkMatch(aInv, x, y, false))
                {
                    return true;
                }

                if (mirrored && checkMatch(aInv, x, y, true))
                {
                    return true;
                }
            }
        }
        return false;
	}

	private boolean checkMatch(InventoryCrafting aInv, int startX, int startY, boolean mirror) 
	{
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                ItemAbstractStack target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = inputs[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = inputs[subX + subY * width];
                    }
                }

                ItemStack slot = aInv.getStackInRowAndColumn(x, y);

                if (target == null && slot == null)
                	continue;
                else if((target == null && slot != null) || (target != null && slot == null))
                	return false;
                else if(!target.isStackEqul(slot))
                	return false;
            }
        }

        return true;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting aInv) 
	{
		if(output instanceof ItemStack) return ((ItemStack) output).copy();
		else if(output instanceof SingleInputRecipe)
		{
	        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++)
	        {
	            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y)
	            {
	                if (checkMatch(aInv, x, y, false))
	                {
	                    return ((SingleInputRecipe) output).getResult(aInv.getStackInRowAndColumn(x + targetID % width, y + targetID % height));
	                }

	                if (mirrored && checkMatch(aInv, x, y, true))
	                {
	                    return ((SingleInputRecipe) output).getResult(aInv.getStackInRowAndColumn(x + MAX_CRAFT_GRID_WIDTH - targetID % width - 1, y + MAX_CRAFT_GRID_HEIGHT - targetID % height - 1));
	                }
	            }
	        }
		}
		return null;
	}

	@Override
	public int getRecipeSize() 
	{
		return inputs.length;
	}

	@Override
	public ItemStack getRecipeOutput() 
	{
		return output instanceof ItemStack ? (ItemStack) output : 
			output instanceof SingleInputRecipe ? ((SingleFillFluidRecipe) output).getResult(null) : null;
	}
	
	public int getXSize()
	{
		return width;
	}
	
	public int getYSize()
	{
		return height;
	}
	
	public ItemAbstractStack[] getInputs()
	{
		return inputs;
	}
	
	private void setOutput(Object output)
	{
		this.output = output;
	}
	
	@Override
	public RecipesTab getRecipeTab()
	{
		return tab;
	}
}