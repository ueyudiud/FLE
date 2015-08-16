package fle.api.crop;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface ICropSeed
{
	public String getCropSeedName(ItemStack stack);
	
	public CropCard getCrop(ItemStack stack);
}
