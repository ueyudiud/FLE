package fle.core.block.machine;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import fle.api.block.IBlockBehaviour;
import fle.api.item.IBagable;
import fle.core.block.BlockSubTile;
import fle.core.init.IB;

public class ItemClayInventory1 extends ItemClayInventory implements IFluidContainerItem, IBagable
{
	public ItemClayInventory1(Block aBlock)
	{
		super(aBlock);
	}

	@Override
	public FluidStack getFluid(ItemStack container)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(container));
		if(behaviour instanceof IFluidContainerItem)
		{
			return ((IFluidContainerItem) behaviour).getFluid(container);
		}
		return null;
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(container));
		if(behaviour instanceof IFluidContainerItem)
		{
			return ((IFluidContainerItem) behaviour).getCapacity(container);
		}
		return 0;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(container));
		if(behaviour instanceof IFluidContainerItem)
		{
			return ((IFluidContainerItem) behaviour).fill(container, resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(container));
		if(behaviour instanceof IFluidContainerItem)
		{
			return ((IFluidContainerItem) behaviour).drain(container, maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public int getSize(ItemStack aStack)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(aStack));
		if(behaviour instanceof IBagable)
		{
			return ((IBagable) behaviour).getSize(aStack);
		}
		return 0;
	}

	@Override
	public ItemStack getItemContain(ItemStack aStack, int i)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(aStack));
		try
		{
			if(behaviour instanceof IBagable)
			{
				return ((IBagable) behaviour).getItemContain(aStack, i);
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setItemContain(ItemStack aStack, int i, ItemStack aInput)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(aStack));
		try
		{
			if(behaviour instanceof IBagable)
			{
				((IBagable) behaviour).setItemContain(aStack, i, aInput);
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public boolean isItemValid(ItemStack aStack, ItemStack aInput)
	{
		IBlockBehaviour<BlockSubTile> behaviour = ((BlockSubTile) IB.argil_smelted).blockBehaviors.get(getDamage(aStack));
		try
		{
			if(behaviour instanceof IBagable)
			{
				return ((IBagable) behaviour).isItemValid(aStack, aInput);
			}
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		return false;
	}
}