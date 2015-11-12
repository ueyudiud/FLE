package fle.core.energy;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import fle.api.soild.ISolidHandler;
import fle.api.soild.SolidStack;
import fle.api.world.BlockPos;

public class TransportHelper
{
	public static int matchOutputItem(ItemStack aStack, BlockPos target, ForgeDirection dir)
	{
		if(target == null || aStack == null) return 0;
		if(!(target.getBlockTile() instanceof ISidedInventory)) return 0;
		ISidedInventory inv = (ISidedInventory) target.getBlockTile();
		if(dir != null && dir != ForgeDirection.UNKNOWN)
		{
			int[] accessIDs = inv.getAccessibleSlotsFromSide(dir.ordinal());
			if(accessIDs == null) return 0;
			for(int slot : accessIDs)
			{
				ItemStack item = inv.getStackInSlot(slot);
				if(item == null) return Math.min(Math.min(aStack.stackSize, aStack.getMaxStackSize()), inv.getInventoryStackLimit());
				if(!(aStack.isItemEqual(item) && ItemStack.areItemStackTagsEqual(aStack, item))) continue;
				ItemStack stack = aStack.copy();
				stack.stackSize = Math.min(item.getMaxStackSize() - item.stackSize, inv.getInventoryStackLimit());
				if(inv.canInsertItem(slot, stack, slot))
				{
					return stack.stackSize;
				}
			}
			return 0;
		}
		else
		{
			for(int slot = 0; slot < inv.getSizeInventory(); ++slot)
			{
				ItemStack item = inv.getStackInSlot(slot);
				if(item == null) return Math.min(Math.min(aStack.stackSize, aStack.getMaxStackSize()), inv.getInventoryStackLimit());
				if(!(aStack.isItemEqual(item) && ItemStack.areItemStackTagsEqual(aStack, item))) continue;
				ItemStack stack = aStack.copy();
				stack.stackSize = Math.min(item.getMaxStackSize() - item.stackSize, inv.getInventoryStackLimit());
				if(inv.canInsertItem(slot, stack, slot))
				{
					return stack.stackSize;
				}
			}
			return 0;
		}
	}
	public static int matchOutputFluid(FluidStack aStack, BlockPos target, ForgeDirection dir)
	{
		if(target == null || aStack == null) return 0;
		if(!(target.getBlockTile() instanceof IFluidHandler)) return 0;
		IFluidHandler inv = (IFluidHandler) target.getBlockTile();
		dir = dir.getOpposite();
		if(dir != null && dir != ForgeDirection.UNKNOWN)
		{
			if(inv.canFill(dir, aStack.getFluid()))
			{
				return inv.fill(dir, aStack, false);
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}
	public static int matchOutputSolid(SolidStack aStack, BlockPos target, ForgeDirection dir)
	{
		if(target == null || aStack == null) return 0;
		if(!(target.getBlockTile() instanceof ISolidHandler)) return 0;
		ISolidHandler inv = (ISolidHandler) target.getBlockTile();
		dir = dir.getOpposite();
		if(dir != null && dir != ForgeDirection.UNKNOWN)
		{
			if(inv.canFillS(dir, aStack.getObj()))
			{
				return inv.fillS(dir, aStack, false);
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}
	
	public static int matchAndTransferOutputItem(ItemStack aStack, BlockPos target, ForgeDirection dir)
	{
		if(target == null || aStack == null) return 0;
		if(!(target.getBlockTile() instanceof ISidedInventory)) return 0;
		ISidedInventory inv = (ISidedInventory) target.getBlockTile();
		if(dir != null && dir != ForgeDirection.UNKNOWN)
		{
			int[] accessIDs = inv.getAccessibleSlotsFromSide(dir.ordinal());
			if(accessIDs == null) return 0;
			for(int slot : accessIDs)
			{
				ItemStack item = inv.getStackInSlot(slot);
				ItemStack stack = aStack.copy();
				if(item == null)
				{
					stack.stackSize = Math.min(Math.min(aStack.stackSize, aStack.getMaxStackSize()), inv.getInventoryStackLimit());
				}
				else
				{
					if(!(aStack.isItemEqual(item) && ItemStack.areItemStackTagsEqual(aStack, item))) continue;
					stack.stackSize = Math.min(Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit()) - item.stackSize, stack.stackSize);
				}
				if(inv.canInsertItem(slot, stack, slot))
				{
					if(item != null)
					{
						inv.getStackInSlot(slot).stackSize += stack.stackSize;
					}
					else
					{
						inv.setInventorySlotContents(slot, stack);
					}
					return stack.stackSize;
				}
			}
			return 0;
		}
		else
		{
			for(int slot = 0; slot < inv.getSizeInventory(); ++slot)
			{
				ItemStack item = inv.getStackInSlot(slot);
				ItemStack stack = aStack.copy();
				if(item == null)
				{
					stack.stackSize = Math.min(Math.min(aStack.stackSize, aStack.getMaxStackSize()), inv.getInventoryStackLimit());
				}
				else
				{
					if(!(aStack.isItemEqual(item) && ItemStack.areItemStackTagsEqual(aStack, item))) continue;
					stack.stackSize = Math.min(Math.min(item.getMaxStackSize(), inv.getInventoryStackLimit()) - item.stackSize, stack.stackSize);
				}
				if(inv.canInsertItem(slot, stack, slot))
				{
					if(item != null)
					{
						inv.getStackInSlot(slot).stackSize += stack.stackSize;
					}
					else
					{
						inv.setInventorySlotContents(slot, stack);
					}
					return stack.stackSize;
				}
			}
			return 0;
		}
	}
	public static int matchAndTransferOutputFluid(FluidStack aStack, BlockPos target, ForgeDirection dir)
	{
		if(target == null || aStack == null) return 0;
		if(!(target.getBlockTile() instanceof IFluidHandler)) return 0;
		IFluidHandler inv = (IFluidHandler) target.getBlockTile();
		dir = dir.getOpposite();
		if(dir != null && dir != ForgeDirection.UNKNOWN)
		{
			if(inv.canFill(dir, aStack.getFluid()))
			{
				return inv.fill(dir, aStack, true);
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}
	public static int matchAndTransferOutputSolid(SolidStack aStack, BlockPos target, ForgeDirection dir)
	{
		if(target == null || aStack == null) return 0;
		if(!(target.getBlockTile() instanceof ISolidHandler)) return 0;
		ISolidHandler inv = (ISolidHandler) target.getBlockTile();
		dir = dir.getOpposite();
		if(dir != null && dir != ForgeDirection.UNKNOWN)
		{
			if(inv.canFillS(dir, aStack.getObj()))
			{
				return inv.fillS(dir, aStack, true);
			}
			else
			{
				return 0;
			}
		}
		else
		{
			return 0;
		}
	}
}