package fle.core.item.behavior;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import fle.api.item.ItemFleMetaBase;

public class BehaviorKnife extends BehaviorTool
{
	@Override
	public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, int aMetadata)
	{
		return aBlock.getMaterial() == Material.leaves || aBlock.getMaterial() == Material.plants || aBlock.getMaterial() == Material.vine ? 2.5F : super.getDigSpeed(item, aStack, aBlock, aMetadata);
	}
}