/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.capability;

import java.lang.reflect.ParameterizedType;

import com.google.common.reflect.TypeToken;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
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
	
	private static <T> Class<T> getType(IStorage<T> storage)
	{
		return (Class<T>)
				TypeToken.of(((ParameterizedType) TypeToken.of(storage.getClass()).getSupertype(IStorage.class).getType())
						.getActualTypeArguments()[0]).getRawType();
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return getType(capability.getStorage()).isInstance(this) && facing == null;
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