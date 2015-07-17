package fla.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ITreeLog 
{
	public int getLogLength(ItemStack itemstack);
	
	public ItemStack getLogDrop(ItemStack itemstack);
	
	public ItemStack getBarkDrop(ItemStack itemstack);

	public ItemStack createStandardLog(int meta, int length);
	
	public ItemStack createStandardLog(Block treeeLog, int meta, int length);
}
