/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.common.gui;

import fle.api.recipes.instance.RecipeMaps;
import nebula.common.gui.ContainerBlockPosition;
import nebula.common.gui.SlotOutput;
import nebula.common.inventory.InventorySimple;
import nebula.common.inventory.InventoryWrapFactory;
import nebula.common.util.A;
import nebula.common.util.Players;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class ContainerCeramics extends ContainerBlockPosition
{
	public InventorySimple inventory;
	public byte[] layers;
	
	public ContainerCeramics(EntityPlayer player, World world, BlockPos pos)
	{
		super(player, world, pos);
		addOpenerSlots();
		int id = this.inventorySlots.size();
		this.inventory = new InventorySimple(1);
		IInventory inv = InventoryWrapFactory.wrap("inventory.ceramics", this, this.inventory);
		this.addSlotToContainer(new SlotOutput(inv, 0, 130, 35));
		this.layers = A.fillByteArray(10, (byte) 64);
		
		new TL(id).appendTransferLocate(this.locationPlayer).addToList();
		this.locationPlayer.appendTransferLocate(this.locationHand);
		this.locationHand.appendTransferLocate(this.locationPlayer);
	}
	
	private void onCrafting(int id)
	{
		if (id < 5)
		{
			this.layers[id] = (byte) Math.max(this.layers[id] - 3, 0);
			this.layers[id + 5] = (byte) Math.min(this.layers[id + 5] + 1, 64);
		}
		else
		{
			this.layers[id] = (byte) Math.max(this.layers[id] - 3, 0);
			this.layers[id - 5] = (byte) Math.min(this.layers[id - 5] + 1, 64);
		}
		this.inventory.setSlotContents(0, RecipeMaps.CERAMIC.findRecipeValue(this.layers, 1));
		detectAndSendChanges();
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		if (type == (byte) 1)
		{
			onCrafting((int) value);
		}
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if (slotId >= 0 && getSlot(slotId).isHere(this.inventory, 0))
		{
			if (!this.world.isRemote && this.inventory.hasStackInSlot(0))
			{
				Players.giveOrDrop(player, this.inventory.removeStackFromSlot(0));
				player.closeScreen();
				player.openContainer.detectAndSendChanges();
			}
			return null;
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
}
