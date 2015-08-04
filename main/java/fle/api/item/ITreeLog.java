package fle.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public interface ITreeLog
{
	boolean isLog(ItemStack aStack);
	
	ItemStack createStandardLog(Block block, int blockMetadata, int log);

	ItemStack getLogDrop(ItemStack stack);
}
