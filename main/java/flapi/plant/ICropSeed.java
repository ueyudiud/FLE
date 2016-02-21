package flapi.plant;

import net.minecraft.item.ItemStack;

public interface ICropSeed
{
	public String getCropSeedName(ItemStack stack);
	
	public CropCard getCrop(ItemStack stack);
}
