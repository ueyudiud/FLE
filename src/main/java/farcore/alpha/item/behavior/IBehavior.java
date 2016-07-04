package farcore.alpha.item.behavior;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBehavior<I extends Item>
{
	public static final IBehavior NONE = new IBehavior<Item>()
	{
		@Override
		public boolean onLeftClickEntity(Item item, ItemStack stack, EntityPlayer player, Entity entity)
		{
			return false;
		}

		@Override
		public boolean onRightClickEnity(Item item, ItemStack stack, EntityPlayer player, Entity entity)
		{
			return false;
		}

		@Override
		public boolean onItemUseFirst(Item item, ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
				int side, float hitX, float hitY, float hitZ)
		{
			return false;
		}

		@Override
		public boolean onItemUse(Item item, ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
				int side, float hitX, float hitY, float hitZ)
		{
			return false;
		}

		@Override
		public ItemStack onItemRightClick(Item item, ItemStack stack, EntityPlayer player, World world)
		{
			return null;
		}

		@Override
		public void onInventoryItemUpdate(Item item, ItemStack stack, World world, Entity entity, int tick,
				boolean flag) {	}

		@Override
		public boolean onEntityItemUpdate(Item item, ItemStack stack, EntityItem entity)
		{
			return false;
		}

		@Override
		public void onPlayerUsingTick(Item item, ItemStack stack, EntityPlayer player, int count) {	}

		@Override
		public void onPlayerStoppedUsing(Item item, ItemStack stack, EntityPlayer player, World world, int tick) {	}

		@Override
		public boolean onBlockStartBreak(Item item, ItemStack stack, EntityPlayer player, int x, int y, int z)
		{
			return false;
		}

		@Override
		public boolean onBlockDestroyed(Item item, ItemStack stack, EntityLivingBase entity, World world, Block block,
				int x, int y, int z)
		{
			return false;
		}		
	};
	
	boolean onLeftClickEntity(I item, ItemStack stack, EntityPlayer player, Entity entity);
	
	boolean onRightClickEnity(I item, ItemStack stack, EntityPlayer player, Entity entity);
	
	boolean onItemUseFirst(I item, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	
	boolean onItemUse(I item, ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ);
	
	ItemStack onItemRightClick(I item, ItemStack stack, EntityPlayer player, World world);
	
	void onInventoryItemUpdate(I item, ItemStack stack, World world, Entity entity, int tick, boolean flag);
	
	boolean onEntityItemUpdate(I item, ItemStack stack, EntityItem entity);
	
	void onPlayerUsingTick(I item, ItemStack stack, EntityPlayer player, int count);
	
	void onPlayerStoppedUsing(I item, ItemStack stack, EntityPlayer player, World world, int tick);
	
	/**
	 * Called before break a block, if return true will prevent
	 * default harvest block check.
	 * @param item
	 * @param stack
	 * @param x
	 * @param y
	 * @param z
	 * @param player
	 * @return
	 */
	boolean onBlockStartBreak(I item, ItemStack stack, EntityPlayer player, int x, int y, int z);
	
	/**
	 * Called when block is destroyed.
	 * @param stack
	 * @param world
	 * @param block
	 * @param x
	 * @param y
	 * @param z
	 * @param entity
	 * @return If destroyed block is cause by use the tool.
	 */
	boolean onBlockDestroyed(I item, ItemStack stack, EntityLivingBase entity, World world, Block block, int x, int y, int z);
}