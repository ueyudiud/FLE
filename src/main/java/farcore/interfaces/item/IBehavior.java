package farcore.interfaces.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IBehavior
{
	boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity);

	boolean onBlockStartBreak(ItemStack stack, World entityWorld, int x, int y, int z, EntityPlayer player);

	ItemStack onEaten(ItemStack stack, World world, EntityPlayer player);

	boolean onEntityItemUpdate(ItemStack stack, EntityItem entity);

	ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player);

	boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
			float hitY, float hitZ);

	boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX,
			float hitY, float hitZ);

	boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity);

	void onUsingTick(ItemStack stack, EntityPlayer player, int count);
	
	void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int tick);

	void onUpdate(ItemStack stack, World world, Entity entity, int tick, boolean flag);	
}