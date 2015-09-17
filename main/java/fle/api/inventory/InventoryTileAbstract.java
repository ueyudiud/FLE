package fle.api.inventory;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.item.ItemEvent;
import fle.api.event.FLEInventoryUpdateEvent;
import fle.api.event.FLETankUpdateEvent;
import fle.api.fluid.IUpdatableFluid;
import fle.api.item.IUpdatableItem;
import fle.api.te.IFluidTanks;
import fle.api.te.TEInventory;

public abstract class InventoryTileAbstract<T extends TEInventory> extends InventoryTileBase<T>
{
	String customName;
	final String inventoryName;
	
	public InventoryTileAbstract(String name, int i)
	{
		super(i);
		inventoryName = name;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(customName != null)
			nbt.setString("customInventoryName", customName);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if(nbt.hasKey("customInventoryName"))
			customName = nbt.getString("customInventoryName");
	}
	
	public void updateStackInSlot(T tile)
	{
		Event evt = new FLEInventoryUpdateEvent(tile, this);
		MinecraftForge.EVENT_BUS.post(evt);
		if(evt.isCanceled())
		{
			return;
		}
		for(int i = 0; i < stacks.length; ++i)
		{
			if(stacks[i] != null)
			{
				if(stacks[i].getItem() instanceof IUpdatableItem)
				{
					((IUpdatableItem) stacks[i].getItem()).onInventoryItemUpdate(stacks[i], tile.getBlockPos(), this);
				}
			}
		}
	}
	
	public void updateFluidTank(T tile)
	{
		if(this instanceof IFluidTanks)
		{
			IFluidTanks tank = (IFluidTanks) this;
			Event evt = new FLETankUpdateEvent(tile, tank);
			MinecraftForge.EVENT_BUS.post(evt);
			
			if(evt.isCanceled())
			{
				return;
			}
			
			for(int i = 0; i < tank.getSizeTank(); ++i)
			{
				if(tank.getFluidStackInTank(i) != null)
					if (tank.getFluidStackInTank(i).getFluid() instanceof IUpdatableFluid)
					{
						IUpdatableFluid fluid = (IUpdatableFluid) tank.getFluidStackInTank(i).getFluid();
						fluid.onTankFluidUpdate(tank.getTank(i), tile);
					}
			}
		}
	}

	@Override
	public String getInventoryName()
	{
		return hasCustomInventoryName() ? customName : inventoryName;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return customName != null && customName != "";
	}
	
	public void setCustomInventoryName(String name)
	{
		customName = name;
	}
}