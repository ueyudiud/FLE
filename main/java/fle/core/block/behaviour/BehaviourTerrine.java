package fle.core.block.behaviour;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.FleValue;
import fle.api.block.IDebugableBlock;
import fle.api.item.IBagable;
import fle.api.material.MatterDictionary;
import fle.api.te.IFluidTanks;
import fle.core.block.BlockSubTile;
import fle.core.gui.ContainerTerrine;
import fle.core.gui.GuiTerrine;
import fle.core.te.argil.TileEntityTerrine;

public class BehaviourTerrine extends BehaviourTile implements IDebugableBlock, IFluidContainerItem, IBagable
{
	public BehaviourTerrine()
	{
		super(TileEntityTerrine.class);
	}

	@Override
	public Container openContainer(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new ContainerTerrine(aPlayer.inventory, (TileEntityTerrine) aWorld.getTileEntity(x, y, z));
	}

	@Override
	public GuiContainer openGui(World aWorld, int x, int y, int z,
			EntityPlayer aPlayer)
	{
		return new GuiTerrine(aPlayer, (TileEntityTerrine) aWorld.getTileEntity(x, y, z));
	}
	
	@Override
	public void getAdditionalToolTips(BlockSubTile block, List<String> list,
			ItemStack aStack)
	{
		super.getAdditionalToolTips(block, list, aStack);
		list.add("Item : ");
		for(int i = 0; i < getSize(aStack); ++i)
		{
			ItemStack stack = getItemContain(aStack, i);
			if(stack != null)
				list.add(String.format("%sx%d", stack.getDisplayName(), stack.stackSize));
		}
		list.add("Fluid : ");
		FluidStack stack = getFluid(aStack);
		if(stack != null)
			list.add(String.format("%sx%dL", stack.getLocalizedName(), stack.amount));
	}
	
	@Override
	public void onBlockPlacedBy(BlockSubTile block, World aWorld, int x, int y,
			int z, ItemStack aStack, EntityLivingBase aEntity)
	{
		TileEntityTerrine tile = (TileEntityTerrine) aWorld.getTileEntity(x, y, z);
		if(tile != null)
		{
			for(int i = 0; i < 2; ++i)
				tile.setInventorySlotContents(i, getItemContain(aStack, i));
			tile.setFluidStackInTank(0, getFluid(aStack));
		}
	}
	
	@Override
	public void addInfomationToList(World aWorld, int x, int y, int z, List aList)
	{
		TileEntityTerrine tile = (TileEntityTerrine) aWorld.getTileEntity(x, y, z);
		if(tile != null)
		{
			if(tile.mode == 1 && tile.getProgress() >= 0)
			{
				aList.add("=====================Progress===================");
				aList.add("Smelted Completed : " + FleValue.format_progress.format_c(tile.getProgress()));
			}
		}
	}
	
	@Override
	public ArrayList<ItemStack> getHarvestDrop(BlockSubTile block,
			World aWorld, int aMeta, TileEntity aTile, int aFotune)
	{
		if (aTile instanceof TileEntityTerrine) 
		{
			TileEntityTerrine tile = (TileEntityTerrine) aTile;
			ArrayList<ItemStack> ret = new ArrayList();
			ItemStack stack = new ItemStack(block, 1, aMeta);
			if(tile.getFluid() != null)
			{
				fill(stack, tile.getFluid(), true);
			}
			for(int i = 0; i < 2; ++i)
				if(tile.getStackInSlot(i) != null)
					setItemContain(stack, i, tile.getStackInSlot(i));
			ret.add(stack);
			return ret;
		}
		return super.getHarvestDrop(block, aWorld, aMeta, aTile, aFotune);
	}

	@Override
	public int getSize(ItemStack aStack)
	{
		return 2;
	}

	@Override
	public ItemStack getItemContain(ItemStack aStack, int i)
	{
		return ItemStack.loadItemStackFromNBT(setupNBT(aStack).getCompoundTag("Slot_" + i));
	}

	@Override
	public void setItemContain(ItemStack aStack, int i, ItemStack aInput)
	{
		setupNBT(aStack).setTag("Slot_" + i, aInput.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public boolean isItemValid(ItemStack aStack, ItemStack aInput)
	{
		return getFluid(aStack) == null ? true : false;
	}

	@Override
	public FluidStack getFluid(ItemStack container)
	{
		return FluidStack.loadFluidStackFromNBT(setupNBT(container).getCompoundTag("Fluid"));
	}

	@Override
	public int getCapacity(ItemStack container)
	{
		return 3000;
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill)
	{
		FluidTank aStack = new FluidTank(getFluid(container), getCapacity(container));
		int ret = aStack.fill(resource, doFill);
		if(doFill)
		{
			setupNBT(container).removeTag("Fluid");
			if(aStack.getFluid() != null)
				setupNBT(container).setTag("Fluid", aStack.writeToNBT(new NBTTagCompound()));
		}
		return ret;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
	{
		FluidTank aStack = new FluidTank(getFluid(container), getCapacity(container));
		FluidStack ret = aStack.drain(maxDrain, doDrain);
		if(doDrain)
		{
			setupNBT(container).removeTag("Fluid");
			if(aStack.getFluid() != null)
				setupNBT(container).setTag("Fluid", aStack.writeToNBT(new NBTTagCompound()));
		}
		return ret;
	}
}