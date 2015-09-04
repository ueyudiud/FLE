package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import fle.api.gui.ContainerCraftable;
import fle.api.gui.SlotHolographic;
import fle.api.gui.SlotOutput;
import fle.api.net.INetEventListener;
import fle.core.te.TileEntityColdForgingPlatform;

public class ContainerColdForging extends ContainerCraftable implements INetEventListener
{	
	public ContainerColdForging(InventoryPlayer player, final TileEntityColdForgingPlatform tile)
	{
		super(player, tile, 0, 0);
		addSlotToContainer(new Slot(tile, 0, 24, 44)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return super.isItemValid(stack) && tile.canTakeStack();
			}
			
			@Override
			public boolean canTakeStack(EntityPlayer aPlayer)
			{
				return super.canTakeStack(aPlayer) && tile.canTakeStack();
			}
		});
		addSlotToContainer(new Slot(tile, 1, 41, 44)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return super.isItemValid(stack) && tile.canTakeStack();
			}
			
			@Override
			public boolean canTakeStack(EntityPlayer aPlayer)
			{
				return super.canTakeStack(aPlayer) && tile.canTakeStack();
			}
		});
		addSlotToContainer(new Slot(tile, 2, 24, 61)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return super.isItemValid(stack) && tile.canTakeStack();
			}
			
			@Override
			public boolean canTakeStack(EntityPlayer aPlayer)
			{
				return super.canTakeStack(aPlayer) && tile.canTakeStack();
			}
		});
		addSlotToContainer(new Slot(tile, 3, 41, 61)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return super.isItemValid(stack) && tile.canTakeStack();
			}
			
			@Override
			public boolean canTakeStack(EntityPlayer aPlayer)
			{
				return super.canTakeStack(aPlayer) && tile.canTakeStack();
			}
		});
		addSlotToContainer(new Slot(tile, 4, 42, 22));
		addSlotToContainer(new SlotOutput(tile, 5, 134, 42)
		{
			@Override
			public ItemStack decrStackSize(int i)
			{
				ItemStack stack = super.decrStackSize(i);
				if(stack != null) ((TileEntityColdForgingPlatform) inv).onOutput();
				return stack;
			}
		});
		locateRecipeInput = new TransLocation("input", -1);
		locateRecipeOutput = new TransLocation("output", 36, 42);
		tile.setup();
		tile.onSlotChange();
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory inventory)
	{
		super.onCraftMatrixChanged(inventory);
		if(inventory instanceof TileEntityColdForgingPlatform)
		{
			((TileEntityColdForgingPlatform) inventory).onSlotChange();
		}
	}

	@Override
	public void onReseave(byte type, Object contain)
	{
		if(type == 1)
		{
			int c = (Integer) contain;
			if(c < 9)
				((TileEntityColdForgingPlatform) inv).onToolClick(player.player, (Integer) contain);
			else if(c == 9)
				((TileEntityColdForgingPlatform) inv).switchType(0);
			else if(c == 10)
				((TileEntityColdForgingPlatform) inv).switchType(1);
			else if(c == 11)
				((TileEntityColdForgingPlatform) inv).switchType(2);
		}
	}
}