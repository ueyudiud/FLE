package nebula.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

@FunctionalInterface
public interface IItemCapabilityProvider
{
	ICapabilityProvider PROVIDER = new ICapabilityProvider()
	{
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return false;
		}
		
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return null;
		}
	};
	IItemCapabilityProvider NONE = (stack, nbt) -> PROVIDER;
	
	ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt);
}