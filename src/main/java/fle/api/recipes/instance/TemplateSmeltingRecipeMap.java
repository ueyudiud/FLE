package fle.api.recipes.instance;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import farcore.lib.stack.AbstractStack;
import farcore.util.U;
import fle.api.recipes.IRecipe;
import fle.api.recipes.IRecipeCache;
import fle.api.recipes.IRecipeInput;
import fle.api.recipes.IRecipeMap;
import fle.api.recipes.IRecipeOutput;
import fle.api.recipes.instance.TemplateSmeltingRecipeMap.SmeltingEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;

/**
 * This is a instance of recipe map,
 * use for vanilla furnace.
 * @author ueyudiud
 *
 */
public class TemplateSmeltingRecipeMap implements IRecipeMap<SmeltingEntry, SmeltingEntry, TileEntityFurnace>
{
	/**
	 * The smelting recipes.<br>
	 * Because of furnace only have single input slot,
	 * make it has cache and recipe uses.
	 * @author ueyudiud
	 *
	 */
	public static class SmeltingEntry implements IRecipe, IRecipeCache<SmeltingEntry>
	{
		public AbstractStack input;
		public AbstractStack output;
		public float exp;
		
		private IRecipeInput<SmeltingEntry, TileEntityFurnace, ItemStack> ri;
		private IRecipeOutput<SmeltingEntry, TileEntityFurnace, ItemStack> ro;

		public SmeltingEntry(AbstractStack input, float exp, AbstractStack output)
		{
			this.input = input;
			this.output = output;
			this.exp = exp;
		}

		@Override
		public final String getRegisteredName()
		{
			return input.toString() + "->" + output.toString();
		}
		
		@Override
		public SmeltingEntry recipe()
		{
			return this;
		}
		
		@Override
		public int getRecipeInputSize()
		{
			return 1;
		}
		
		@Override
		public IRecipeInput getInput(int index)
		{
			if(ri == null)
			{
				ri = new IRecipeInput<SmeltingEntry, TileEntityFurnace, ItemStack>()
				{
					@Override
					public InputType getInputType()
					{
						return InputType.SINGLE;
					}
					
					@Override
					public SmeltingEntry matchInput(TileEntityFurnace matrix)
					{
						return input.contain(matrix.getStackInSlot(0)) ? SmeltingEntry.this : null;
					}
					
					@Override
					public void onInput(TileEntityFurnace matrix, SmeltingEntry cache)
					{
						matrix.decrStackSize(0, input.size(matrix.getStackInSlot(0)));
					}
				};
			}
			return ri;
		}
		
		@Override
		public int getRecipeOutputSize()
		{
			return 1;
		}
		
		@Override
		public IRecipeOutput getOutput(int index)
		{
			if(ro == null)
			{
				ro = new IRecipeOutput<SmeltingEntry, TileEntityFurnace, ItemStack>()
				{
					@Override
					public OutputType getInputType()
					{
						return OutputType.SINGLE;
					}
					
					@Override
					public boolean matchOutput(SmeltingEntry cache, TileEntityFurnace matrix)
					{
						return U.TileEntities.matchOutput(output, matrix.getStackInSlot(2), matrix.getInventoryStackLimit());
					}
					
					@Override
					public void onOutput(TileEntityFurnace matrix)
					{
						U.TileEntities.insertStack(output, matrix, 2);
					}
				};
			}
			return ro;
		}
		
		@Override
		public boolean isFakeRecipe()
		{
			return false;
		}
		
		@Override
		public boolean isValid()
		{
			return input.valid() && output.valid();
		}
	}
	
	private final Map<String, SmeltingEntry> smeltings = new HashMap();

	@Override
	public SmeltingEntry readFromNBT(NBTTagCompound nbt)
	{
		return smeltings.get(nbt.getString("recipe"));
	}
	
	@Override
	public void writeToNBT(SmeltingEntry target, NBTTagCompound nbt)
	{
		nbt.setString("recipe", target.getRegisteredName());
	}
	
	@Override
	public SmeltingEntry findRecipe(TileEntityFurnace handler)
	{
		for(SmeltingEntry entry : smeltings.values())
		{
			if(entry.ri.matchInput(handler) != null)
				return entry;
		}
		return null;
	}
	
	@Override
	public void onRecipeInput(SmeltingEntry cache, TileEntityFurnace handler)
	{
		cache.ri.onInput(handler, cache);
	}
	
	@Override
	public void addRecipe(SmeltingEntry recipe)
	{
		smeltings.put(recipe.getRegisteredName(), recipe);
	}
	
	@Override
	public Collection<SmeltingEntry> getRecipes()
	{
		return Collections.unmodifiableCollection(smeltings.values());
	}
}