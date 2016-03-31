package fle.api.item.behavior;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;

import farcore.interfaces.item.IItemInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BehaviorBase implements IItemInfo
{
    protected static final UUID itemDamageUUID = new UUID(471928472827199838L, 37491287194719L);
    
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
			EntityLivingBase entity)
	{
		return false;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, World entityWorld, int x, int y, int z, EntityPlayer player)
	{
		return false;
	}

	@Override
	public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
	{
		return stack;
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, EntityItem entity)
	{
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		return stack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
			float hitX, float hitY, float hitZ)
	{
		return false;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
	{
		return false;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
	{
		
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
	{
		
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int tick, boolean flag)
	{
		
	}

	@Override
	public boolean canHarvestBlock(Block block, ItemStack stack)
	{
		return false;
	}

	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 64;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 0;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return false;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack)
	{
		return null;
	}

	@Override
	public float getDigSpeed(ItemStack stack, World world, int x, int y, int z, Block block, int metadata)
	{
		return 1.0F;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.none;
	}

	@Override
	public EnumRarity getRarity(ItemStack stack)
	{
		return EnumRarity.common;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		
	}

	@Override
	public Set<String> getToolClasses(ItemStack stack)
	{
		return ImmutableSet.of();
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return 0;
	}

	@Override
	public void addAttributeModifiers(Multimap map, ItemStack stack)
	{
		
	}
}