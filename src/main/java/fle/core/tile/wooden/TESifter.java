/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tile.wooden;

import static fle.api.tile.TEITSRecipe.WaitForOutput;
import static fle.api.tile.TEITSRecipe.Working;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.container.SolidTank;
import fle.api.recipes.IRecipeMap;
import fle.api.recipes.TemplateRecipeMap.TemplateRecipeCache;
import fle.api.recipes.instance.RecipeMaps;
import nebula.common.tile.TEInventorySingleSlot;
import nebula.common.util.Direction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class TESifter extends TEInventorySingleSlot
{
	protected TemplateRecipeCache<SolidStack> cache;
	protected int recipeMaxTick;
	protected int recipeTick;
	protected SolidTank tank;
	
	public TESifter()
	{
		super();
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
			if ((this.recipeTick ++) >= this.recipeMaxTick)
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
	
	protected SolidStack getRecipeInputHandler()
	{
		return this.tank.getStack();
	}
	
	protected IRecipeMap<?, TemplateRecipeCache<SolidStack>, SolidStack> getRecipeMap()
	{
		return RecipeMaps.SIMPLE_SIFTER;
	}
	
	protected void onRecipeInput()
	{
		this.tank.drain(this.cache.<SolidStack>get1(0).amount, true);
	}
	
	protected boolean onRecipeOutput()
	{
		if (this.tank.insertSolid(this.cache.get(1), true) && (this.cache.get(2) == null ||
				sendItemStackTo(this.cache.get(2), Direction.D, true, true, true) == this.cache.<ItemStack>get(2).stackSize))
		{
			this.tank.insertSolid(this.cache.get(1), false);
			return true;
		}
		return false;
	}
	
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