package fle.core.cover.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import fle.api.FleAPI;
import fle.api.cover.Cover;
import fle.api.cover.CoverTile;
import fle.api.cover.IItemIOCover;
import fle.api.te.TEBase;
import fle.api.world.BlockPos;
import fle.core.cover.CoverAutoOutput;
import fle.core.energy.TransportHelper;

public class CoverTileAutoOutput extends CoverTile implements IItemIOCover
{
	final int b;
	final int l;
	int cache;

	public CoverTileAutoOutput(ForgeDirection dir, TEBase aTile, CoverAutoOutput aCover)
	{
		super(dir, aTile, aCover);
		b = aCover.loop;
		l = aCover.maxStackLimit;
	}

	@Override
	public ItemStack get()
	{
		return null;
	}

	@Override
	public boolean canDrain(BlockPos pos, ForgeDirection dir, ItemStack target)
	{
		return dir != ForgeDirection.UP;
	}

	@Override
	public boolean canFill(BlockPos pos, ForgeDirection dir, ItemStack target)
	{
		return false;
	}

	@Override
	public ItemStack drain(BlockPos pos, ForgeDirection dir, ItemStack target,
			boolean flag)
	{
		ISidedInventory inventory = (ISidedInventory) tile;
		int side = FleAPI.getIndexFromDirection(dir);
		int[] slotIDs = inventory.getAccessibleSlotsFromSide(side);
		for(int id : slotIDs)
		{
			ItemStack stack = inventory.getStackInSlot(id);
			if(stack != null)
			{
				if(!target.isItemEqual(stack)) return null;
				stack = stack.copy();
				stack.stackSize = Math.min(stack.stackSize, l);
				if(inventory.canExtractItem(id, stack, side))
				{
					return inventory.decrStackSize(id, stack.stackSize);
				}
			}
		}
		return null;
	}

	@Override
	public ItemStack drain(BlockPos pos, ForgeDirection dir, int maxSize,
			boolean flag)
	{
		ISidedInventory inventory = (ISidedInventory) tile;
		int side = FleAPI.getIndexFromDirection(dir);
		int[] slotIDs = inventory.getAccessibleSlotsFromSide(side);
		for(int id : slotIDs)
		{
			ItemStack stack = inventory.getStackInSlot(id);
			if(stack != null)
			{
				stack = stack.copy();
				stack.stackSize = Math.min(Math.min(stack.stackSize, l), maxSize);
				if(inventory.canExtractItem(id, stack, side))
				{
					return inventory.decrStackSize(id, stack.stackSize);
				}
			}
		}
		return null;
	}

	@Override
	public int fill(BlockPos pos, ForgeDirection dir, ItemStack resource,
			boolean flag)
	{
		return 0;
	}

	@Override
	public void onBlockUpdate()
	{
		if(cache++ < b)
		{
			return;
		}
		cache = 0;
		if(!(tile instanceof ISidedInventory) || dir == ForgeDirection.UP) return;
		if (tile.getBlockPos().toPos(dir).getBlockTile() instanceof ISidedInventory)
		{
			ISidedInventory from = (ISidedInventory) tile;
			for(int id = 0; id < from.getSizeInventory(); ++id)
			{
				if(from.getStackInSlot(id) != null)
				{
					int size = TransportHelper.matchAndTransferOutputItem(from.getStackInSlot(id), tile.getBlockPos().toPos(dir), dir.getOpposite());
					if(size > 0)
					{
						from.decrStackSize(id, size);
						return;
					}
				}
			}
		}
	}
	
	@Override
	public boolean onBlockActive(EntityPlayer player, double xPos, double yPos,
			double zPos)
	{
		return true;
	}
}