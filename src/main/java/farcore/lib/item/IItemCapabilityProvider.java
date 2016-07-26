package farcore.lib.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IItemCapabilityProvider
{
	IItemCapabilityProvider NONE = (ItemStack stack, NBTTagCompound nbt) ->
	{
		return null;
	};

	ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt);
}
