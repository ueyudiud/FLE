package fle.tool.item.behavior;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import fle.tool.ToolMaterialInfo;

public class BehaviorDigable extends BehaviorTool
{
	public BehaviorDigable() 
	{
		
	}
	
	@Override
	public boolean canHarvestBlock(ItemFleMetaBase item, Block aBlock,
			int aMeta, ItemStack aStack) 
	{
		return super.canHarvestBlock(item, aBlock, aMeta, aStack);
	}
	
	public boolean isBlockEffective(ItemStack aStack, Block aBlock, int aMeta)
	{
		return ForgeHooks.isToolEffective(aStack, aBlock, aMeta);
	}
	
	@Override
	public boolean onItemDamageBlock(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, EntityLivingBase aEntity, World aWorld, int aX,
			int aY, int aZ) 
	{
		if(aBlock.getBlockHardness(aWorld, aX, aY, aZ) > 0.0F)
		{
			item.damageItem(aStack, aEntity, EnumDamageResource.DestoryBlock, 1.0F);
		}
		return true;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemFleMetaBase item, ItemStack aStack,
			EntityPlayer aPlayer, Entity aEntity) 
	{
		item.damageItem(aStack, aPlayer, EnumDamageResource.HitEntity, 2);
		return super.onLeftClickEntity(item, aStack, aPlayer, aEntity);
	}
	
	@Override
	public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, int aMetadata) 
	{
		return isBlockEffective(aStack, aBlock, aMetadata) ? new ToolMaterialInfo(item.setupNBT(aStack)).getHardness() * 1.5F : super.getDigSpeed(item, aStack, aBlock, aMetadata);
	}
}