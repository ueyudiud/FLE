package flapi.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import flapi.world.BlockPos;
import fle.api.te.IObjectInWorld;

public class ContainerWithTile<T extends TileEntity> extends ContainerBase implements IObjectInWorld
{
	protected T inventoryTile;
	
	public ContainerWithTile(EntityPlayer aPlayer, T aTile) 
	{
		super(aPlayer.inventory);
		inventoryTile = aTile;
	}
	public ContainerWithTile(InventoryPlayer aPlayer, T aTile) 
	{
		super(aPlayer);
		inventoryTile = aTile;
	}
	
	@Override
	public BlockPos getBlockPos() 
	{
		return new BlockPos(inventoryTile.getWorldObj(), inventoryTile.xCoord, inventoryTile.yCoord, inventoryTile.zCoord);
	}
	
	@Override
	public boolean transferStackInSlot(Slot slot, ItemStack baseItemStack,
			ItemStack itemstack, int locate) 
	{
		return false;
	}
}