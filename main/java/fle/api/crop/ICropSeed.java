package fle.api.crop;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public interface ICropSeed
{
	public String getCropSeedName();
	
	public CropCard getCrop(ItemStack stack);
	
	public IIcon getIconFromCropCard();
}
