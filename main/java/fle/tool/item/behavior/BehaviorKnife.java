package fle.tool.item.behavior;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;

public class BehaviorKnife extends BehaviorTool
{
	@Override
	public boolean onItemDamageBlock(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, EntityLivingBase aEntity, World aWorld, int aX,
			int aY, int aZ)
	{
		if(aBlock.getBlockHardness(aWorld, aX, aY, aZ) > 1.0F)
		{
			item.damageItem(aStack, aEntity, EnumDamageResource.DestoryBlock, 1.0F);
		}
		else
		{
			item.damageItem(aStack, aEntity, EnumDamageResource.DestoryBlock, 0.1F);
		}
		return super.onItemDamageBlock(item, aStack, aBlock, aEntity, aWorld, aX, aY,
				aZ);
	}
	
	@Override
	public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, int aMetadata)
	{
		return aBlock.getMaterial() == Material.leaves || aBlock.getMaterial() == Material.plants || aBlock.getMaterial() == Material.vine ? 10F : super.getDigSpeed(item, aStack, aBlock, aMetadata);
	}
}