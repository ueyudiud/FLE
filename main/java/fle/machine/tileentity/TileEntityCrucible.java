package fle.machine.tileentity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.chem.EnumReactionSystemType;
import farcore.chem.IReactionRecipe;
import farcore.chem.IReactionSystem;
import farcore.chem.RecipeCache;
import farcore.chem.StandardReactionHandler;
import farcore.nbt.NBTLoad;
import farcore.nbt.NBTSave;
import farcore.substance.SStack;
import farcore.substance.Substance;
import farcore.tile.TEUpdatable;
import farcore.util.Part;
import farcore.util.Vs;
import flapi.FleAPI;

public class TileEntityCrucible extends TEUpdatable implements IReactionSystem
{
	@NBTLoad(name = "stacks")
	@NBTSave(name = "stacks")
	public StackHandler stacks = new StackHandler();
	
	@Override
	public void init()
	{
		
	}

	@Override
	public void onClientUpdate()
	{
		;
	}
	
	int buf = 0;

	@Override
	public void update(long tick, long tick1)
	{
		StandardReactionHandler.updateReaction(this);
		Iterator<RecipeCache> cache = ImmutableList.copyOf(stacks.caches).iterator();
		while(cache.hasNext())
		{
			RecipeCache cache2 = cache.next();
			cache2.update(this, tick1);
			if(cache2.isFinal())
				stacks.caches.remove(cache2);
		}
		if(buf++ == 10)
		{
			buf = 0;
			syncNBT();
		}
	}

	@Override
	public EnumReactionSystemType getType()
	{
		return EnumReactionSystemType.OPEN;
	}
	
	@Override
	public List<SStack> getEnvironment()
	{
		return FleAPI.getEnvironment(worldObj.provider.dimensionId);
	}

	@Override
	public List<SStack> getContain()
	{
		return stacks.stacks;
	}

	@Override
	public void markReactionCache(RecipeCache cache)
	{
		stacks.caches.add(cache);
	}

	@Override
	public int getPH()
	{
		return 700;
	}

	@Override
	public long getTemperature()
	{
		return Vs.water_freeze_point + 2000;
	}

	@Override
	public long getPressure()
	{
		return 101325;
	}

	@Override
	public void remove(Substance substance, int size)
	{
		stacks.remove(substance, size);
	}

	@Override
	public void remove(Substance substance, Part part, int size)
	{
		stacks.remove(substance, part.getBasePart(), size);
	}

	@Override
	public void add(Substance substance, Part part, int size)
	{
		stacks.add(substance, part.getBasePart(), size);
	}

	@Override
	public double releaseEnergy(double amount, boolean process)
	{
		return amount;
	}

	@Override
	public double absorbEnergy(double amount, boolean process)
	{
		return amount;
	}
}