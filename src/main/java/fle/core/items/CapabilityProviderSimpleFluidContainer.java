/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.items;

import fle.core.items.ItemSimpleFluidContainer.FluidContainerProperty;
import nebula.common.capability.CapabilityProviderItem;
import nebula.common.nbt.INBTCompoundReaderAndWriter;
import nebula.common.util.FluidStacks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * @author ueyudiud
 */
public class CapabilityProviderSimpleFluidContainer extends CapabilityProviderItem implements INBTCompoundReaderAndWriter<CapabilityProviderSimpleFluidContainer>, IFluidHandler
{
	private final FluidContainerProperty	property;
	private FluidStack						stack;
	private int								damage;
	
	public CapabilityProviderSimpleFluidContainer(FluidContainerProperty property)
	{
		this.property = property;
	}
	
	public void setFluid(FluidStack stack)
	{
		this.stack = stack;
	}
	
	public FluidStack getFluid()
	{
		return this.stack;
	}
	
	public int getDamage()
	{
		return this.damage;
	}
	
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	
	@Override
	public CapabilityProviderSimpleFluidContainer readFrom(NBTTagCompound nbt)
	{
		if (nbt.hasKey("fluid"))
		{
			this.stack = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("fluid"));
		}
		this.damage = nbt.getInteger("damage");
		return this;
	}
	
	@Override
	public void writeTo(CapabilityProviderSimpleFluidContainer target, NBTTagCompound nbt)
	{
		if (this.stack != null)
		{
			nbt.setTag("fluid", this.stack.writeToNBT(new NBTTagCompound()));
		}
		nbt.setInteger("damage", this.damage);
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return new IFluidTankProperties[] { new FluidTankProperties(this.stack, this.property.capacity, this.property.enableToFill, this.property.enableToDrain) };
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if (resource == null || !isItemUsable()) return 0;
		if (this.stack == null)
		{
			int amount = Math.min(resource.amount, this.property.capacity);
			if (doFill)
			{
				this.stack = FluidStacks.sizeOf(resource, amount);
			}
			return amount;
		}
		else if (!this.stack.isFluidEqual(resource))
			return 0;
		else if (this.stack.amount == this.property.capacity) return 0;
		int result = Math.min(this.property.capacity - this.stack.amount, resource.amount);
		if (doFill)
		{
			this.stack.amount += result;
		}
		return result;
	}
	
	private boolean isItemUsable()
	{
		return this.damage < this.property.durbility;
	}
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if (resource == null) return null;
		if (this.stack != null && this.stack.isFluidEqual(resource)) return drain(resource.amount, doDrain);
		return null;
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (maxDrain == 0) return null;
		if (this.stack == null) return null;
		int amount = Math.min(maxDrain, this.stack.amount);
		FluidStack result = FluidStacks.sizeOf(this.stack, amount);
		if (doDrain)
		{
			this.stack.amount -= amount;
			this.stack = this.stack.amount == 0 ? null : this.stack;
			int max = this.property.durbility;
			if ((this.damage += amount) == max && this.stack.amount == 0)
			{
				super.stack.stackSize--;
			}
		}
		return result;
	}
	
	public boolean canUse()
	{
		return this.damage < this.property.durbility;
	}
}
