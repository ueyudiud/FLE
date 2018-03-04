/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.gui.delegate;

import java.util.function.Predicate;

import nebula.common.block.delegated.ITileDelegateTE;
import nebula.common.gui.ContainerBase;
import nebula.common.gui.ContainerTileInventory;
import nebula.common.gui.FluidSlot;
import nebula.common.tile.delegate.TEDelegate;
import nebula.common.util.L;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class ContainerDelegate<T extends TEDelegate & IInventory> extends ContainerTileInventory<T>
{
	protected final ITileDelegateTE.ITDTE_HasGui<?, T, ?, ?> delegate;
	
	public ContainerDelegate(T tile, EntityPlayer player, ITileDelegateTE.ITDTE_HasGui<?, T, ?, ?> delegate)
	{
		super(tile, player);
		this.delegate = delegate;
		delegate.addContainerSlots(tile, player);
	}
	
	@Override
	protected ItemStack onToolClick(ItemStack tool, IInventory inventoryBelong, int index)
	{
		return this.delegate.onToolClick(this.tile, L.castAny(this), tool, inventoryBelong, index);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		this.delegate.onContainerClosed(this.tile, L.castAny(this), playerIn);
		super.onContainerClosed(playerIn);
	}
	
	@Override
	public void onRecieveGUIAction(byte type, long value)
	{
		this.delegate.onRecieveGUIAction(this.tile, L.castAny(this), type, value);
	}
	
	public int getItemSlotCount()
	{
		return this.inventorySlots.size();
	}
	
	public int getFluidSlotCount()
	{
		return this.fluidSlots.size();
	}
	
	public ContainerBase.TL createTransferLocation(int id)
	{
		return new TL(id);
	}
	
	public ContainerBase.TL createTransferLocation(int from, int to)
	{
		return new TL(from, to);
	}
	
	public ContainerBase.TL createTransferLocation(int from, int to, boolean flipped)
	{
		return new TL(from, to, flipped);
	}
	
	public ContainerBase.TL createTransferLocation(int from, int to, boolean flipped, Predicate<ItemStack> predicate)
	{
		return new TL(from, to, flipped)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return predicate.test(stack);
			}
		};
	}
	
	public ContainerBase.TL getBagTransferLocation()
	{
		return this.locationBag;
	}
	
	public ContainerBase.TL getHandTransferLocation()
	{
		return this.locationHand;
	}
	
	public ContainerBase.TL getPlayerTransferLocation()
	{
		return this.locationPlayer;
	}
	
	@Override
	public Slot addSlotToContainer(Slot slotIn)
	{
		return super.addSlotToContainer(slotIn);
	}
	
	@Override
	public FluidSlot addSlotToContainer(FluidSlot slot)
	{
		return super.addSlotToContainer(slot);
	}
	
	@Override
	public void addOpenerBagSlots()
	{
		super.addOpenerBagSlots();
	}
	
	@Override
	public void addOpenerBagSlots(int x, int y)
	{
		super.addOpenerBagSlots(x, y);
	}
	
	@Override
	public void addOpenerHandSlots()
	{
		super.addOpenerHandSlots();
	}
	
	@Override
	public void addOpenerHandSlots(int x, int y)
	{
		super.addOpenerHandSlots(x, y);
	}
	
	@Override
	public void addOpenerSlots()
	{
		super.addOpenerSlots();
	}
	
	@Override
	public void addOutputSlotMatrix(IInventory inventory, int x, int y, int widthSlot, int heightSlot, int offSlot, int spacingU, int spacingV)
	{
		super.addOutputSlotMatrix(inventory, x, y, widthSlot, heightSlot, offSlot, spacingU, spacingV);
	}
	
	@Override
	public void addStandardSlotMatrix(IInventory inventory, int x, int y, int widthSlot, int heightSlot, int offSlot, int spacingU, int spacingV)
	{
		super.addStandardSlotMatrix(inventory, x, y, widthSlot, heightSlot, offSlot, spacingU, spacingV);
	}
}
