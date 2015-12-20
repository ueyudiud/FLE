package fle.tool.item.behavior;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import flapi.enums.EnumDamageResource;
import flapi.item.ItemFleMetaBase;
import flapi.util.FleValue;

public class BehaviorChisel extends BehaviorTool
{
	@Override
	public boolean onItemDamageBlock(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, EntityLivingBase aEntity, World aWorld, int aX,
			int aY, int aZ)
	{
		item.damageItem(aStack, aEntity, EnumDamageResource.DestoryBlock, 2F);
		return true;
	}
	
	@Override
	public void getAdditionalToolTips(ItemFleMetaBase item, List<String> list,
			ItemStack itemstack, boolean flag)
	{
		if(flag)
		{
			list.add("You need put a hammer in bag to assist chisel to harvest block.");
		}
		else
		{
			list.add(FleValue.info_shift);
		}
	}
}