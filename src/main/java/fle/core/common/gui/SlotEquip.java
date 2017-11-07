/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.common.gui;

import static net.minecraft.inventory.EntityEquipmentSlot.CHEST;
import static net.minecraft.inventory.EntityEquipmentSlot.FEET;
import static net.minecraft.inventory.EntityEquipmentSlot.HEAD;
import static net.minecraft.inventory.EntityEquipmentSlot.LEGS;

import javax.annotation.Nullable;

import nebula.common.gui.SlotBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class SlotEquip extends SlotBase
{
	protected static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = { FEET, LEGS, CHEST, HEAD };
	
	protected final int				index;
	protected final EntityPlayer	player;
	
	public SlotEquip(InventoryPlayer inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, 36 + index, xPosition, yPosition);
		this.player = inventoryIn.player;
		this.index = index;
	}
	
	public final EntityEquipmentSlot getSlotType()
	{
		return VALID_EQUIPMENT_SLOTS[this.index];
	}
	
	/**
	 * Returns the maximum stack size for a given slot (usually the same as
	 * getInventoryStackLimit(), but 1 in the case of armor slots)
	 */
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
	
	@Override
	public boolean isItemValid(@Nullable ItemStack stack)
	{
		return stack != null && stack.getItem().isValidArmor(stack, VALID_EQUIPMENT_SLOTS[this.index], this.player);
	}
	
	@Override
	@Nullable
	@SideOnly(Side.CLIENT)
	public String getSlotTexture()
	{
		return ItemArmor.EMPTY_SLOT_NAMES[this.index];
	}
}
