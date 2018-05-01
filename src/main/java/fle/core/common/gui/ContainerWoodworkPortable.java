/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.common.gui;

import nebula.common.gui.Container03BlockPos;
import nebula.common.gui.ItemSlot;
import nebula.common.inventory.InventorySimple;
import nebula.common.util.TileEntities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ContainerWoodworkPortable extends Container03BlockPos
{
	private InventoryWoodenPortableInput	inputs	= new InventoryWoodenPortableInput(this);
	InventorySimple							outputs	= new InventorySimple("inventory.woodwork.portable.output", false, 4);
	
	private IInventory realInput;
	
	private boolean					changingFlag;
	
	public ContainerWoodworkPortable(EntityPlayer player, World world, BlockPos pos)
	{
		super(player, world, pos);
		TransferLocation locationTools = createLocation(2);
		addSlotToContainer(new ItemSlot(this.inputs.containers.getContainer(0), this.inputs, 0, 22, 25));
		addSlotToContainer(new ItemSlot(this.inputs.containers.getContainer(1), this.inputs, 1, 22, 42));
		TransferLocation locationInput = createLocation(1);
		addSlotToContainer(new ItemSlot(this.inputs.containers.getContainer(2), this.inputs, 2, 49, 35));
		addOutputSlots(this.outputs.containers.getContainers(), this.outputs, 0, 2, 2, 118, 26, 18, 18);
		this.tsPlayerBag.addLocation(locationInput).addLocation(this.locationHand);
		this.tsPlayerHand.addLocation(locationInput).addLocation(locationTools).addLocation(this.locationHand);
		this.stragtegies.add(new TS(locationInput).addLocation(this.locationPlayer));
		this.stragtegies.add(new TS(locationTools).addLocation(this.locationHand).addLocation(this.locationBag));
		detectAndSendChanges();
	}
	
	public boolean hasRecipe()
	{
		return this.inputs.left >= 0 && this.inputs.right >= 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int[] getRenderValue()
	{
		return new int[] { this.inputs.left, this.inputs.right };
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		if (this.changingFlag) return;
		this.changingFlag = true;
		if (inventoryIn == this.inputs)
		{
			this.inputs.onCraftMatrixChanged();
		}
		this.changingFlag = false;
		super.onCraftMatrixChanged(inventoryIn);
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		switch (type)
		{
		case 0:
			this.inputs.onOptionChanged((int) value);
			break;
		default:
			super.onRecieveGUIAction(type, value);
		}
	}
	
	@Override
	public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
	{
		if (slotId >= 39)
		{
			if (this.inputs.recipe != null)
			{
				this.changingFlag = true;
				this.inputs.onOutput();
				TileEntities.dropItemStacks(this.world, this.pos, this.outputs);
				this.changingFlag = false;
				onCraftMatrixChanged(this.realInput);
				return null;
			}
		}
		return super.slotClick(slotId, dragType, clickTypeIn, player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		TileEntities.dropItemStacks(this.world, this.pos, this.inputs);
	}
}
