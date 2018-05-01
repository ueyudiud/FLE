/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.tile;

import java.io.IOException;

import fle.api.recipes.IRecipeMap;
import nebula.common.NebulaSynchronizationHandler;
import nebula.common.network.PacketBufferExt;
import nebula.common.tile.INetworkedSyncTile;
import nebula.common.tile.TE04Synchronization;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class TERecipe<H, C> extends TE04Synchronization implements INetworkedSyncTile
{
	public static final byte Working = 1, WaitForOutput = 2;
	
	protected C		cache;
	private int		lastRecipeMaxTick, lastRecipeTick;
	protected int	recipeMaxTick, recipeTick;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);
		if (this.cache != null)
		{
			getRecipeMap().writeTo(compound, "cache", this.cache);
			compound.setInteger("recipetick", this.recipeTick);
			compound.setInteger("recipemaxtick", this.recipeMaxTick);
		}
		return compound;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		if ((this.cache = getRecipeMap().readFrom(compound, "cache")) != null)
		{
			this.recipeTick = compound.getInteger("recipetick");
			this.recipeMaxTick = compound.getInteger("recipemaxtick");
		}
	}
	
	protected void sendTicking(int type)
	{
		if (this.lastRecipeMaxTick != this.recipeMaxTick)
		{
			this.lastRecipeMaxTick = this.recipeMaxTick;
			type |= 1;
		}
		if (this.lastRecipeTick != this.recipeTick)
		{
			this.lastRecipeTick = this.recipeTick;
			type |= 2;
		}
		NebulaSynchronizationHandler.markTileEntityForUpdate(this, type);
	}
	
	@Override
	public void writeNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		if ((type & 1) != 0)
		{
			buf.writeInt(this.recipeMaxTick);
		}
		if ((type & 2) != 0)
		{
			buf.writeInt(this.recipeTick);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void readNetworkData(int type, PacketBufferExt buf) throws IOException
	{
		if ((type & 1) != 0)
		{
			this.recipeMaxTick = buf.readInt();
		}
		if ((type & 2) != 0)
		{
			this.recipeTick = buf.readInt();
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
	
	protected boolean isInputEnabled()
	{
		return true;
	}
	
	@Override
	protected void updateServer()
	{
		super.updateServer();
		if (this.cache == null && isInputEnabled())
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
					markDirty();
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
}
