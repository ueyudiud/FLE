package fla.api.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import fla.api.world.BlockPos;

public interface IPlaceableItem 
{
	public int canItemPlace(ItemStack stack);
	
	public ItemStack setPlacedBlock(EntityPlayer player);

	public ItemStack setPlacedBlock(ItemStack stack);
}
