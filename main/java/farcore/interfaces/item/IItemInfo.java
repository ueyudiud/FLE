package farcore.interfaces.item;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IItemInfo extends IBehavior, IItemProperty
{
	public static class ItemInfoMix implements IItemInfo
	{
		private IBehavior behavior;
		private IItemProperty property;
		
		public ItemInfoMix(IBehavior behavior, IItemProperty property)
		{
			this.behavior = behavior;
			this.property = property;
		}

		@Override
		public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z,
				EntityLivingBase entity)
		{
			return behavior.onBlockDestroyed(stack, world, block, x, y, z, entity);
		}

		@Override
		public boolean onBlockStartBreak(ItemStack stack, World entityWorld, int x, int y, int z, EntityPlayer player)
		{
			return behavior.onBlockStartBreak(stack, entityWorld, x, y, z, player);
		}

		@Override
		public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
		{
			return behavior.onEaten(stack, world, player);
		}

		@Override
		public boolean onEntityItemUpdate(ItemStack stack, EntityItem entity)
		{
			return behavior.onEntityItemUpdate(stack, entity);
		}

		@Override
		public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
		{
			return behavior.onItemRightClick(stack, world, player);
		}

		@Override
		public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
				float hitX, float hitY, float hitZ)
		{
			return behavior.onItemUse(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}

		@Override
		public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
				float hitX, float hitY, float hitZ)
		{
			return behavior.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
		}

		@Override
		public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
		{
			return behavior.onLeftClickEntity(stack, player, entity);
		}

		@Override
		public void onUsingTick(ItemStack stack, EntityPlayer player, int count)
		{
			behavior.onUsingTick(stack, player, count);
		}

		@Override
		public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick)
		{
			behavior.onPlayerStoppedUsing(stack, world, player, tick);
		}

		@Override
		public void onUpdate(ItemStack stack, World world, Entity entity, int tick, boolean flag)
		{
			behavior.onUpdate(stack, world, entity, tick, flag);
		}

		@Override
		public boolean canHarvestBlock(Block block, ItemStack stack)
		{
			return property.canHarvestBlock(block, stack);
		}

		@Override
		public int getItemStackLimit(ItemStack stack)
		{
			return property.getItemStackLimit(stack);
		}

		@Override
		public int getMaxItemUseDuration(ItemStack stack)
		{
			return property.getMaxItemUseDuration(stack);
		}

		@Override
		public boolean hasContainerItem(ItemStack stack)
		{
			return property.hasContainerItem(stack);
		}

		@Override
		public ItemStack getContainerItem(ItemStack stack)
		{
			return property.getContainerItem(stack);
		}

		@Override
		public float getDigSpeed(ItemStack stack, World world, int x, int y, int z, Block block, int metadata)
		{
			return property.getDigSpeed(stack, world, x, y, z, block, metadata);
		}

		@Override
		public EnumAction getItemUseAction(ItemStack stack)
		{
			return property.getItemUseAction(stack);
		}

		@Override
		public EnumRarity getRarity(ItemStack stack)
		{
			return property.getRarity(stack);
		}

		@Override
		public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
		{
			property.addInformation(stack, player, list, flag);
		}
		
		@Override
		public Set<String> getToolClasses(ItemStack stack)
		{
			return property.getToolClasses(stack);
		}
		
		@Override
		public int getHarvestLevel(ItemStack stack, String toolClass)
		{
			return property.getHarvestLevel(stack, toolClass);
		}

		@Override
		public void addAttributeModifiers(Multimap map, ItemStack stack)
		{
			property.addAttributeModifiers(map, stack);
		}
	}
}