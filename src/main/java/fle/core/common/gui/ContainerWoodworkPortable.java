/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.common.gui;

import fle.api.recipes.instance.PortableWoodworkRecipeMap.PortableWoodworkRecipe;
import fle.api.recipes.instance.RecipeMaps;
import nebula.base.A;
import nebula.common.gui.ContainerBlockPosition;
import nebula.common.gui.SlotBase;
import nebula.common.inventory.IBasicInventory;
import nebula.common.inventory.InventorySimple;
import nebula.common.inventory.InventoryWrapFactory;
import nebula.common.util.L;
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
public class ContainerWoodworkPortable extends ContainerBlockPosition
{
	private InventorySimple	inputs	= new InventorySimple(3);
	private InventorySimple	outputs	= new InventorySimple(4);
	
	private IInventory realInput;
	
	private PortableWoodworkRecipe	recipe;
	private boolean					changingFlag;
	
	public ContainerWoodworkPortable(EntityPlayer player, World world, BlockPos pos)
	{
		super(player, world, pos);
		addOpenerSlots();
		int id = this.inventorySlots.size();
		IInventory real1 = this.realInput = InventoryWrapFactory.wrap("inventory.woodwork.portable.input", this, this.inputs);
		IInventory real2 = InventoryWrapFactory.wrap("inventory.woodwork.portable.output", this, this.outputs);
		addSlotToContainer(new SlotBase(real1, 0, 22, 25));
		addSlotToContainer(new SlotBase(real1, 1, 22, 42));
		addSlotToContainer(new SlotBase(real1, 2, 49, 35));
		addOutputSlotMatrix(real2, 118, 26, 2, 2, 0, 18, 18);
		TL locationInput = new TL(id + 2), locationTools = new TL(id, id + 2);
		this.locationBag.appendTransferLocate(locationInput).appendTransferLocate(this.locationHand);
		this.locationHand.appendTransferLocate(locationInput).appendTransferLocate(locationTools).appendTransferLocate(this.locationHand);
		locationInput.appendTransferLocate(this.locationPlayer).addToList();
		locationTools.appendTransferLocate(this.locationHand).appendTransferLocate(this.locationBag).addToList();
		this.currentValue[3] = -1;
		this.currentValue[4] = -1;
		detectAndSendChanges();
	}
	
	public boolean hasRecipe()
	{
		return this.currentValue[3] >= 0 && this.currentValue[4] >= 0;
	}
	
	@SideOnly(Side.CLIENT)
	public int[] getRenderValue()
	{
		return new int[] { this.currentValue[3], this.currentValue[4] };
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		if (this.changingFlag) return;
		this.changingFlag = true;
		IBasicInventory inventory = InventoryWrapFactory.unwrap(inventoryIn);
		if (inventory == this.inputs)
		{
			if (this.recipe != null && this.recipe.match(inventory))
			{
				rerangeValue();
			}
			else
			{
				this.recipe = RecipeMaps.PORTABLE_WOODWORK.findRecipe(inventory);
				if (this.recipe != null)
				{
					rerangeValue();
				}
				else
				{
					this.currentValue[1] = this.currentValue[2] = 0;
				}
			}
			updateOutput();
		}
		this.changingFlag = false;
		super.onCraftMatrixChanged(inventoryIn);
	}
	
	private void rerangeValue()
	{
		int[] range = this.recipe.getIntScaleRange(this.inputs);
		this.currentValue[1] = range[0];
		this.currentValue[2] = range[1];
		this.currentValue[0] = L.range(this.currentValue[1], this.currentValue[2], this.currentValue[0]);
	}
	
	private void updateOutput()
	{
		if (this.recipe != null)
		{
			ItemStack[] out = A.copyToLength(this.recipe.getOutputs(this.inputs, this.currentValue[0]), 4);
			this.outputs.fromArray(out);
			int[] range = this.recipe.getDisplayNumbers(this.inputs, this.currentValue[0]);
			if (range != null)
			{
				this.currentValue[3] = range[0];
				this.currentValue[4] = range[1];
			}
			else
			{
				this.currentValue[3] = this.currentValue[4] = -1;
			}
		}
		else
		{
			this.outputs.removeAllStacks();
			this.currentValue[3] = this.currentValue[4] = -1;
		}
	}
	
	@Override
	protected int getFieldCount()
	{
		return 5;
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		switch (type)
		{
		case 0:
			if (this.recipe != null)
			{
				int i = value == 0 ? 1 : value == 1 ? -1 : value == 2 ? 5 : value == 3 ? -5 : 0;
				this.currentValue[0] = L.range(this.currentValue[1], this.currentValue[2], this.currentValue[0] + i);
				updateOutput();
				detectAndSendChanges();
			}
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
			if (this.recipe != null)
			{
				this.changingFlag = true;
				this.recipe.onOutput(this.inputs, this.currentValue[0]);
				dropOrGivePlayerPlayerItems(this.outputs);
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
		dropPlayerItems(this.inputs);
	}
}
