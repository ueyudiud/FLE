/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.common.gui;

import java.util.Arrays;

import fle.api.recipes.instance.RecipeMaps;
import nebula.common.gui.Container03BlockPos;
import nebula.common.gui.ItemSlotOutput;
import nebula.common.inventory.ItemContainerInventory;
import nebula.common.util.Players;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class ContainerCeramics extends Container03BlockPos
{
	public InventoryBasic inventory;
	public byte[] layers;
	
	public ContainerCeramics(EntityPlayer player, World world, BlockPos pos)
	{
		super(player, world, pos);
		this.inventory = new InventoryBasic("inventory.ceramics", false, 1);
		TransferLocation location = createLocation(1);
		this.addSlotToContainer(new ItemSlotOutput(new ItemContainerInventory(this.inventory, 0), this.inventory, 0, 130, 35));
		this.layers = new byte[10];
		Arrays.fill(this.layers, (byte) 64);
		
		this.stragtegies.add(new TS(location).addLocation(this.locationPlayer));
		this.tsPlayerBag.addLocation(this.locationHand);
		this.tsPlayerHand.addLocation(this.locationBag);
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
		this.inventory.setInventorySlotContents(0, RecipeMaps.CERAMIC.findRecipeValue(this.layers, 1));
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
			if (!this.world.isRemote && this.inventory.getStackInSlot(0) != null)
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
