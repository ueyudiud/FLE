package fle.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import fle.api.te.IObjectInWorld;
import fle.api.world.BlockPos;

public class ContainerWithTile<T extends TileEntity> extends ContainerBase implements IObjectInWorld
{
	protected T inventoryTile;
	
	public ContainerWithTile(EntityPlayer aPlayer, T aTile) 
	{
		super(aPlayer);
		inventoryTile = aTile;
	}
	public ContainerWithTile(InventoryPlayer aPlayer, T aTile) 
	{
		super(aPlayer);
		inventoryTile = aTile;
	}
	
	@Override
	public World getWorldObj() 
	{
		return inventoryTile.getWorldObj();
	}
	
	@Override
	public BlockPos getBlockPos() 
	{
		return new BlockPos(inventoryTile.getWorldObj(), inventoryTile.xCoord, inventoryTile.yCoord, inventoryTile.zCoord);
	}
	
	@Override
	public boolean openGUI() 
	{
		return false;
	}
}