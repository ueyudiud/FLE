package fle.core.item.behavior;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.item.IItemBehaviour;
import fle.api.item.ItemFleMetaBase;

public class BehaviorBase implements IItemBehaviour<ItemFleMetaBase>
{
	@Override
	public boolean onLeftClickEntity(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, Entity entity) 
	{
		return false;
	}

	@Override
	public boolean onItemUse(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemFleMetaBase item, ItemStack itemstack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemFleMetaBase item,	ItemStack itemstack, World paramWorld, EntityPlayer player) 
	{
		return itemstack;
	}

	@Override
	public void getAdditionalToolTips(ItemFleMetaBase item,	List<String> list, ItemStack itemstack) 
	{
		
	}

	@Override
	public void onUpdate(ItemFleMetaBase item, ItemStack itemstack,
			World world, Entity entity, int tick, boolean flag) 
	{
		
	}

	@Override
	public boolean canDispense(ItemFleMetaBase item, IBlockSource source,
			ItemStack itemstack) 
	{
		return false;
	}

	@Override
	public ItemStack onDispense(ItemFleMetaBase item, IBlockSource source,
			ItemStack itemstack) 
	{
		return itemstack;
	}

	@Override
	public boolean onItemDamageBlock(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, EntityLivingBase aEntity, World aWorld, int aX,
			int aY, int aZ) 
	{
		return false;
	}

	@Override
	public float getDigSpeed(ItemFleMetaBase item, ItemStack aStack,
			Block aBlock, int aMetadata) 
	{
		return 0.5F;
	}

	@Override
	public boolean canHarvestBlock(ItemFleMetaBase item, Block aBlock, int aMeta,
			ItemStack aStack) 
	{
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(ItemFleMetaBase item, EntityItem aItem) 
	{
		return false;
	}
}