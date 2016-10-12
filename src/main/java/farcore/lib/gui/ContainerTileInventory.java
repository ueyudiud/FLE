package farcore.lib.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ContainerTileInventory<T extends TileEntity & IInventory> extends ContainerBase
{
	public T tile;
	protected final int[] lastTileFieldValues;
	
	public ContainerTileInventory(T tile, EntityPlayer player)
	{
		super(player);
		addOpenerSlots();
		this.tile = tile;
		lastTileFieldValues = new int[tile.getFieldCount()];
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tile);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0; i < lastTileFieldValues.length; ++i)
		{
			int value1 = tile.getField(i);
			int value2 = lastTileFieldValues[i];
			if(value1 != value2)
			{
				for(IContainerListener listener : listeners)
				{
					listener.sendProgressBarUpdate(this, i, value1);
				}
				lastTileFieldValues[i] = value1;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		tile.setField(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.isUseableByPlayer(playerIn);
	}
}