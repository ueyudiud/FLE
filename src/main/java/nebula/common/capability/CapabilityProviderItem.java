/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ueyudiud
 */
public class CapabilityProviderItem implements ICapabilityProvider
{
	protected ItemStack stack;
	
	protected CapabilityProviderItem()
	{
	}
	
	public void setItem(ItemStack stack)
	{
		this.stack = stack;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return CapabilityHelper.getCapabilityType(capability).isInstance(this) && facing == null;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		try
		{
			return (T) this;
		}
		catch (ClassCastException exception)
		{
			return null;
		}
	}
}
