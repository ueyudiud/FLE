package farcore.lib.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import farcore.interfaces.item.ICustomDamageItem;
import farcore.lib.stack.AbstractStack;
import farcore.util.U;
import net.minecraft.item.ItemStack;

public class ShapelessFleRecipe extends AbstractFleRecipe
{
	public ISingleInputRecipe outputDetect;
	public List<AbstractStack> input;

	public ShapelessFleRecipe(int tick, Object...objects)
	{
		this(null, tick, objects);
	}
	public ShapelessFleRecipe(ItemStack output, int tick, Object...objects)
	{
		try
		{
			int i = 0;
			Entry[] entries = new Entry[0];
			if(objects[i] instanceof Entry[])
			{
				entries = (Entry[]) objects[i++];
			}
			else
			{
				List<Entry> list = new ArrayList();
				while(objects[i] instanceof Entry)
				{
					list.add((Entry) objects[i++]);
				}
				entries = list.toArray(entries);
			}
			wrap(this, output, tick, entries);
			input = new ArrayList();
			for(; i < objects.length; ++i)
			{
				if(objects[i] instanceof ISingleInputRecipe)
				{
					ISingleInputRecipe recipe = (ISingleInputRecipe) objects[i];
					if(outputDetect != null) throw new IllegalArgumentException("The recipe can only contain one input check!");
					outputDetect = recipe;
					this.output = recipe.instance();
				}
				else
				{
					input.add(U.Inventorys.asStack(objects[i], true));
				}
			}
			if(output == null && outputDetect == null)
				throw new IllegalArgumentException("The recipe should has an output!");
		}
		catch(Throwable throwable)
		{
            String ret = "Invalid shapeless recipe: ";
            for (Object tmp : objects)
            {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret, throwable);
		}
	}
	
	protected boolean matchInput(ItemStack target, AbstractStack input)
	{
		return input.contain(target);
	}

	@Override
	public boolean matchRecipe(ICraftingInventoryMatching inventory)
	{
		if(!super.matchRecipe(inventory)) return false;
		List<AbstractStack> check = new ArrayList(input);
		boolean hasOutput = outputDetect == null;
		label:
		for(int i = 0; i < inventory.getCraftingMatrixSize(); ++i)
		{
			ItemStack stack = inventory.getStackInMatrix(i);
			if(stack == null) continue;
			for(int j = 0; j < check.size(); ++j)
			{
				if(matchInput(stack, check.get(j)))
				{
					check.remove(j);
					continue label;
				}
			}
			if(!hasOutput)
			{
				if(outputDetect.match(stack))
				{
					hasOutput = true;
					continue label;
				}
			}
			return false;
		}
		return check.isEmpty() && hasOutput;
	}

	@Override
	public void onOutputStack(ICraftingInventory inventory)
	{
		super.onOutputStack(inventory);
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
//					if(stack.getItem() instanceof ICustomDamageItem)
//					{
//						inventory.setStackInMatrix(i, ((ICustomDamageItem) stack.getItem()).getCraftedItem(stack, inventory));
//					}
//					else 
						if(check1.useContainer() || !stack.getItem().hasContainerItem(stack))
					{
						inventory.decrStackInMatrix(i, check1.size(stack));
					}
					else
					{
						inventory.setStackInMatrix(i, stack.getItem().getContainerItem(stack));
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
	
	@Override
	public ItemStack getOutput(ICraftingInventoryMatching inventory)
	{
		if(outputDetect != null)
		{
			for(int i = 0; i < inventory.getCraftingMatrixSize(); ++i)
			{
				ItemStack stack = inventory.getStackInMatrix(i);
				if(outputDetect.match(stack))
				{
					return outputDetect.output(stack)[0];
				}
			}
		}
		return super.getOutput(inventory);
	}
}