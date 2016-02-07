package farcore.chem;

import java.util.ArrayList;
import java.util.List;

import farcore.substance.SStack;
import fle.machine.tileentity.StackHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class RecipeCache
{
	public final IReactionRecipe recipe;
	public final List<SStack> cacheOutput;
	private final long maxTick;
	private long tick;
	
	public RecipeCache(IReactionRecipe recipe, List<SStack> output, long tick)
	{
		this.recipe = recipe;
		this.cacheOutput = output;
		this.maxTick = tick;
	}
	
	public static RecipeCache readFromNBT(NBTTagCompound nbt)
	{
		IReactionRecipe recipe = 
				StandardReactionHandler.recipes.get(nbt.getString("recipe"));
		NBTTagList list = nbt.getTagList("output", 10);
		List<SStack> l = new ArrayList();
		for(int i = 0; i < list.tagCount(); ++i)
		{
			SStack stack = SStack.readFromNBT(list.getCompoundTagAt(i));
			if(stack != null)
				l.add(stack);
		}
		long tickM = nbt.getLong("tickMax");
		long tick = nbt.getLong("tick");
		if(recipe == null || l.isEmpty()) return null;
		RecipeCache cache = new RecipeCache(recipe, l, tickM);
		cache.tick = tick;
		return cache;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("recipe", recipe.name());
		NBTTagList list = new NBTTagList();
		for(SStack stack : cacheOutput)
		{
			list.appendTag(stack.writeToNBT(new NBTTagCompound()));
		}
		nbt.setTag("output", list);
		nbt.setLong("tickMax", maxTick);
		nbt.setLong("tick", tick);
		return nbt;
	}
	
	public void update(IReactionSystem system, long tick)
	{
		if(this.tick == -1) return;
		this.tick += tick;
		if(this.tick >= maxTick)
		{
			for(SStack stack : cacheOutput)
			{
				system.add(stack.getSubstance(), stack.part, stack.size);
			}
			this.tick = -1;
		}
	}
	
	public boolean isFinal()
	{
		return tick == -1;
	}
	
	public long getMaxTick()
	{
		return maxTick;
	}
	
	public long getTick()
	{
		return tick;
	}
}