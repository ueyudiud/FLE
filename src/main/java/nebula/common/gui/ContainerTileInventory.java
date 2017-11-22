/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.gui;

import java.util.ArrayList;
import java.util.List;

import nebula.common.network.packet.PacketContainerDataUpdateAll;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The container class include a tile entity implemented <code>Inventory</code>.
 * <p>
 * 
 * @author ueyudiud
 *
 * @param <T> the tile entity type.
 */
public abstract class ContainerTileInventory<T extends TileEntity & IInventory> extends ContainerBase
{
	public T tile;
	
	protected class InventoryContainerDataHandler<C extends ContainerBase> extends ContainerBase.BaseContainerDataHandler<C>
	{
		protected int[] values;
		
		@Override
		public void addListener(IContainerListener listener)
		{
			super.addListener(listener);
			if (!(listener instanceof EntityPlayerMP))
			{
				listener.sendAllWindowProperties(ContainerTileInventory.this, ContainerTileInventory.this.tile);
			}
		}
		
		@Override
		protected PacketContainerDataUpdateAll getAllDataPacket()
		{
			if (this.stacks1 == null)
			{
				this.stacks1 = new ItemStack[ContainerTileInventory.this.inventorySlots.size()];
				this.stacks2 = new FluidStack[ContainerTileInventory.this.fluidSlots.size()];
				this.values = new int[ContainerTileInventory.this.tile.getFieldCount()];
			}
			List<Object[]> list = new ArrayList<>(3);
			if (this.stacks1.length != 0)
				list.add(concat(ContainerDataHandlerManager.BS_IS, this.stacks1));
			if (this.stacks2.length != 0)
				list.add(concat(ContainerDataHandlerManager.BS_FS, this.stacks2));
			if (this.values.length != 0)
			{
				Integer[] value = new Integer[this.values.length];
				for (int i = 0; i < value.length; value[i] = this.values[i], ++i);
				list.add(concat(ContainerDataHandlerManager.BS_INT, value));
			}
			return new PacketContainerDataUpdateAll(ContainerTileInventory.this, list.toArray(new Object[list.size()][]));
		}
		
		@Override
		public void detectAndSendChanges()
		{
			if (this.values == null)
			{
				this.values = new int[ContainerTileInventory.this.tile.getFieldCount()];
			}
			for (int i = 0; i < this.values.length; ++i)
			{
				int value1 = ContainerTileInventory.this.tile.getField(i);
				int value2 = this.values[i];
				if (value1 != value2)
				{
					for (IContainerListener listener : ContainerTileInventory.this.listeners)
					{
						listener.sendProgressBarUpdate(ContainerTileInventory.this, i, value1);
					}
					this.values[i] = value1;
				}
			}
			super.detectAndSendChanges();
		}
	}
	
	/**
	 * Include opener slots and auto generated fields cache.
	 * 
	 * @param tile
	 * @param player
	 */
	public ContainerTileInventory(T tile, EntityPlayer player)
	{
		super(player);
		addOpenerSlots();
		this.tile = tile;
	}
	
	@Override
	protected IContainerDataHandler createHandler()
	{
		return new InventoryContainerDataHandler<>();
	}
	
	public T getTileEntity()
	{
		return this.tile;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		this.tile.setField(id, data);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.tile.isUsableByPlayer(playerIn);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		if (!this.isClosed)
		{
			this.tile.closeInventory(playerIn);
		}
		super.onContainerClosed(playerIn);
	}
}
