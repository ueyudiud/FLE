package fla.api.crop;

import net.minecraft.item.ItemStack;

public interface ICropSeed
{
	public CropCard getCrop(ItemStack stack);
}
