package farcore.lib.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

@FunctionalInterface
public interface IItemCapabilityProvider
{
	IItemCapabilityProvider NONE = (stack, nbt) -> null;
	
	ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt);
}
