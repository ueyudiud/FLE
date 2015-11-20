package fle.api.item;

import fle.api.world.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBeeComb
{
	int getMaxBeeCap(ItemStack aStack);
	
	int getMaxLarvaCap(ItemStack stack);
	
	int getLarvaCount(ItemStack aStack);
	
	int getQueenCount(ItemStack aStack);
	
	int getHoneyAmount(ItemStack aStack);
	
	void saveToNBT(ItemStack aStack, int age, int honey);
	
	int getMaturationTick(ItemStack aStack);
	
	ItemStack onMaturation(ItemStack aStack, int larva, int honey);

	ItemStack deQueen(ItemStack stack);

	ItemStack putNewComb(ItemStack aStack, int queenCount, short honeyCount, short beesCount);

	int getGrowSpeed(World world, BlockPos pos, ItemStack aStack, int beesCount);
}