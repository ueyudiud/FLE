/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.tile;

import fle.api.recipes.IRecipeMap;
import nebula.common.tile.TEInventoryTankSingleAbstract;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class TEITSRecipe<H, C> extends TEInventoryTankSingleAbstract
{
	public static final byte
	Working = 0x1, WaitForOutput = 0x2;
	
	protected C cache;
	protected int recipeMaxTick;
	protected int recipeTick;
	
	public TEITSRecipe(int size)
	{
		super(size);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if (this.cache != null)
		{
			getRecipeMap().writeToNBT(this.cache, compound, "cache");
			compound.setInteger("recipetick", this.recipeTick);
		}
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if ((this.cache = getRecipeMap().readFromNBT(compound, "cache")) != null)
		{
			this.recipeTick = compound.getInteger("recipetick");
		}
	}
	
	@Override
	protected void initServer()
	{
		super.initServer();
		if (this.cache == null)
		{
			disable(WaitForOutput);
			disable(Working);
		}
		if (this.recipeTick != this.recipeMaxTick)
		{
			disable(WaitForOutput);
		}
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (this.cache == null)
		{
			this.cache = getRecipeMap().findRecipe(getRecipeInputHandler());
			if (this.cache != null)
			{
				onRecipeInput();
				enable(Working);
			}
		}
		if (this.cache != null)
		{
			if ((this.recipeTick += getPower()) >= this.recipeMaxTick)
			{
				if (onRecipeOutput())
				{
					onRecipeRefresh();
					disable(WaitForOutput);
				}
				else
				{
					this.recipeTick = this.recipeMaxTick;
					enable(WaitForOutput);
				}
				disable(Working);
			}
		}
	}
	
	public boolean isWorking()
	{
		return is(Working);
	}
	
	public boolean isWaitingForOutput()
	{
		return is(WaitForOutput);
	}
	
	protected abstract H getRecipeInputHandler();
	
	protected abstract IRecipeMap<?, C, H> getRecipeMap();
	
	protected abstract void onRecipeInput();
	
	protected abstract boolean onRecipeOutput();
	
	protected abstract int getPower();
	
	protected void onRecipeRefresh()
	{
		this.cache = null;
		this.recipeMaxTick = this.recipeTick = 0;
	}
	
	@Override
	public int getFieldCount()
	{
		return 2;
	}
	
	@Override
	public int getField(int id)
	{
		switch (id)
		{
		case 0 : return this.recipeTick;
		case 1 : return this.recipeMaxTick;
		default : return super.getField(id);
		}
	}
	
	public int getRecipeTick()
	{
		return this.recipeTick;
	}
	
	public int getMaxRecipeTick()
	{
		return this.recipeMaxTick;
	}
	
	@SideOnly(Side.CLIENT)
	public int getRecipeProgressBar(int length)
	{
		return this.recipeMaxTick == 0 ? 0 : (int) ((float) this.recipeTick * length / this.recipeMaxTick);
	}
	
	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
		case 0 : this.recipeTick = value; return;
		case 1 : this.recipeMaxTick = value; return;
		default : super.setField(id, value); return;
		}
	}
}