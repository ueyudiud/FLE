package flapi.item.behavior;

import java.util.List;

import flapi.item.ItemFleMetaBase;
import flapi.item.interfaces.IItemBehavior;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBehaviorStandard<T extends ItemFleMetaBase> implements IItemBehavior<T>
{
	@Override
	public boolean onLeftClickEntity(T item, ItemStack stack,
			EntityPlayer player, Entity entity) 
	{
		return false;
	}

	@Override
	public boolean onItemUse(T item, ItemStack stack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public boolean onItemUseFirst(T item, ItemStack stack,
			EntityPlayer player, World world, int x, int y, int z,
			ForgeDirection side, float xPos, float yPos, float zPos) 
	{
		return false;
	}

	@Override
	public ItemStack onItemRightClick(T item, ItemStack stack, World World, EntityPlayer player) 
	{
		return stack;
	}

	@Override
	public void getAdditionalToolTips(T item, List<String> list, ItemStack stack, boolean flag) 
	{
		
	}

	@Override
	public void onUpdate(T item, ItemStack stack,
			World world, Entity entity, int tick, boolean flag) 
	{
		
	}

	@Override
	public boolean onItemDamageBlock(T item, ItemStack stack,
			Block block, EntityLivingBase entity, World world, 
			int x, int y, int z) 
	{
		return false;
	}

	@Override
	public float getDigSpeed(T item, ItemStack stack,
			Block block, int meta) 
	{
		return 1.0F;
	}

	@Override
	public boolean canHarvestBlock(T item, Block block, int meta,
			ItemStack stack) 
	{
		return false;
	}

	@Override
	public boolean onEntityItemUpdate(T item, EntityItem entity) 
	{
		return false;
	}

	@Override
	public boolean isValidArmor(T item, ItemStack stack,
			int ammorType, Entity entity)
	{
		return false;
	}

	@Override
	public void onPlayerStoppedUsing(T item, World world,
			EntityPlayer player, int useTick, ItemStack stack)
	{
		
	}
}