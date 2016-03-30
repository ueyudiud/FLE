package farcore.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import farcore.lib.stack.AbstractStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;

public class Inventory implements IInventory
{	
	public enum FDType
	{
		F(true, false),
		D(false, true),
		FD(true, true);
		
		boolean d;
		boolean f;
		
		FDType(boolean doFill, boolean doDrain)
		{
			d = doDrain;
			f = doFill;
		}
	}
	
	private static final TileEntity INSTANCE = new TileEntity();
	
	private TileEntity tile = INSTANCE;
	
	private final String name;
	public final ItemStack[] stacks;
	private final int limit;
	
	public Inventory(int size, String name, int stackLimit)
	{
		this.name = name;
		this.stacks = new ItemStack[size];
		this.limit = stackLimit;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		readFromNBT(nbt, "items");
	}
	  
	public void readFromNBT(NBTTagCompound nbt, String tag)
	{
		NBTTagList nbttaglist = nbt.getTagList(tag, 10);
		for (int j = 0; j < nbttaglist.tagCount(); j++)
		{
			NBTTagCompound slot = nbttaglist.getCompoundTagAt(j);
			int index = slot.getByte("slot");
			if ((index >= 0) && (index < stacks.length))
			{
				setInventorySlotContents(index, ItemStack.loadItemStackFromNBT(slot));
			}
	    }
	}
	  
	public void writeToNBT(NBTTagCompound data)
	{
		writeToNBT(data, "items");
	}
	  
	public void writeToNBT(NBTTagCompound data, String tag)
	{
		NBTTagList slots = new NBTTagList();
		for (byte index = 0; index < stacks.length; index = (byte)(index + 1))
		{
			if ((stacks[index] != null) && (stacks[index].stackSize > 0))
			{
				NBTTagCompound slot = new NBTTagCompound();
				slot.setByte("slot", index);
				stacks[index].writeToNBT(slot);
				slots.appendTag(slot);
			}
	    }
	    data.setTag(tag, slots);
	}
	  
	@Override
	public int getSizeInventory()
	{
		return stacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return stacks[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int size)
	{
		if(size <= 0) return null;
		if(stacks[i] == null) return null;
		ItemStack stack = stacks[i];
		if(stack.stackSize <= size)
		{
			stacks[i] = null;
			return stack;
		}
		else
		{
			return stack.splitStack(size);
		}
	}
	
	public int addStack(int i, ItemStack stack, boolean process)
	{
		if(stack == null || stack.stackSize == 0) return 0;
		int size = Math.min(limit, stack.getMaxStackSize());
		if(stacks[i] == null)
		{
			size = Math.min(stack.stackSize, size);
			if(process)
			{
				ItemStack stack1 = stack.copy();
				stack1.stackSize = size;
				stacks[i] = stack1;
			}
			return size;
		}
		else if(stacks[i].isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stacks[i], stack))
		{
			size = Math.min(stack.stackSize, size - stacks[i].stackSize);
			if(process)
			{
				stacks[i].stackSize += size;
			}
			return size;
		}
		return 0;
	}
	
	public boolean addStacks(int min, int max, boolean process, AbstractStack...stacks)
	{
		for(AbstractStack stack : stacks)
		{
			if(!addStack(min, max, stack, false))
			{
				return false;
			}
		}
		if(process)
		{
			for(AbstractStack stack : stacks)
			{
				addStack(min, max, stack, true);
			}
		}
		return true;
	}
	
	public boolean addStack(int min, int max, AbstractStack stack, boolean process)
	{
		if(stack == null || stack.instance() == null) return true;
		AbstractStack input = stack;
		input = addStack(min, max, input, true, false);
		if(input != null)
		{
			input = addStack(min, max, input, false, false);
		}
		if(input == null)
		{
			if(process)
			{
				input = stack;
				input = addStack(min, max, input, true, true);
				if(input != null)
				{
					input = addStack(min, max, input, false, true);
				}
			}
			return true;
		}
		return false;
	}
	
	private AbstractStack addStack(int min, int max, AbstractStack stack, boolean addToNoEmpty, boolean process)
	{
		if(stack == null || stack.instance() == null) return null;
		AbstractStack input = stack;
		for(int i = min; i < max; ++i)
		{
			if((input = $addStack(i, input, !addToNoEmpty, addToNoEmpty, true, process)) == null)
			{
				return null;
			}
		}
		return input;
	}
	
	public boolean addStack(int i, AbstractStack stack, boolean process)
	{
		if(stack == null || stack.instance() == null) return true;
		return $addStack(i, stack, true, true, false, process) == null;
	}
	
	public AbstractStack $addStack(int i, AbstractStack stack, boolean addToEmpty, boolean addToNoEmpty, boolean inputWithoutStack, boolean process)
	{
		if(stacks[i] == null)
		{
			if(!addToEmpty) return stack;
			ItemStack stack2 = stack.instance().copy();
			int size = addStack(i, stack2, false);
			if(size == stack2.stackSize)
			{
				if(process)
				{
					addStack(i, stack2, true);
				}
				return null;
			}
			else if(inputWithoutStack)
			{
				stack2.stackSize = size;
				addStack(i, stack2, process);
				return stack.split(stack2);
			}
			return stack;
		}
		else if(stack.similar(stacks[i]))
		{
			if(!addToNoEmpty) return stack;
			int size = Math.min(limit, stacks[i].getMaxStackSize());
			int size1 = (int) stack.size(stacks[i]);
			if(size1 > size - stacks[i].stackSize)
			{
				if(inputWithoutStack)
				{
					int ret = size - stacks[i].stackSize;
					if(process)
					{
						stacks[i].stackSize = size;
					}
					ItemStack add = stacks[i].copy();
					add.stackSize = ret;
					return stack.split(add);
				}
				return stack;
			}
			if(process)
			{
				stacks[i].stackSize += size1;
			}
			return null;
		}
		return stack;
	}
	
	public boolean matchShapeless(int startSlot, int endSlot, AbstractStack...inputs)
	{
		List<AbstractStack> list = new ArrayList();
		list.addAll(Arrays.asList(inputs));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			boolean flag = false;
			if(stacks[i] == null) continue;
			Iterator<AbstractStack> itr = list.iterator();
			while(itr.hasNext())
			{
				AbstractStack checker = itr.next();
				if(checker.contain(stacks[i]))
				{
					flag = true;
					list.remove(checker);
					break;
				}
			}
			if(!flag) return false;
		}
		return list.isEmpty();
	}

	public void onInputShapeless(int startSlot, int endSlot, AbstractStack...inputs) 
	{
		List<AbstractStack> list = new ArrayList();
		list.addAll(Arrays.asList(inputs));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			if(stacks[i] == null) continue;
			Iterator<AbstractStack> itr = list.iterator();
			while(itr.hasNext())
			{
				AbstractStack stack = itr.next();
				if(stack.contain(stacks[i]))
				{
					decrStackSize(i, stack.size(stacks[i]));
					list.remove(stack);
					break;
				}
			}
		}
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		ItemStack stack = stacks[i];
		stacks[i] = null;
		return stack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack stack)
	{
		stacks[i] = ItemStack.copyItemStack(stack);
	}

	@Override
	public String getInventoryName()
	{
		return name;
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return limit;
	}

	@Override
	public void markDirty()
	{
		tile.markDirty();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return tile == INSTANCE ? true : 
			player.getDistanceSq(tile.xCoord + 0.5, tile.yCoord + 0.5, tile.zCoord + 0.5) <= 64;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack stack)
	{
		return true;
	}
	
	public void setTile(TileEntity tile)
	{
		this.tile = tile;
	}
	
	public static boolean matchFluidInput(IFluidTank tank, FluidStack fluid)
	{
		if(fluid == null) return true;
		if(tank.getFluidAmount() == 0) return false;
		return !fluid.isFluidEqual(tank.getFluid()) ? false : tank.getFluid().containsFluid(fluid);
	}
	
	public boolean fillOrDrainInventoryTank(IFluidTank tank, int inputSlot, int outputSlot)
	{
		return fillOrDrainInventoryTank(tank, inputSlot, outputSlot, FDType.FD);
	}
	public boolean fillOrDrainInventoryTank(IFluidTank tank, int inputSlot, int outputSlot, FDType type)
	{
		ItemStack input = stacks[inputSlot];
		ItemStack output = stacks[outputSlot];
		if(input == null) return true;
		if(FluidContainerRegistry.isContainer(input))
		{
			ItemStack stack;
			if(FluidContainerRegistry.isEmptyContainer(input) && type.f && tank.getFluidAmount() > 0)
			{
				int i = FluidContainerRegistry.getContainerCapacity(tank.getFluid(), input);
				stack = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), input);
				if(stack == null) return false;
				if(addStack(i, stack, false) == stack.stackSize)
				{
					tank.drain(i, true);
					decrStackSize(inputSlot, 1);
					addStack(outputSlot, stack, true);
					return true;
				}
				return false;
			}
			else if(FluidContainerRegistry.isFilledContainer(input) && type.d)
			{
				FluidStack contain = FluidContainerRegistry.getFluidForFilledItem(input);
				if(tank.fill(contain, false) == contain.amount)
				{
					stack = FluidContainerRegistry.drainFluidContainer(input);
					if(stack == null)
					{
						tank.fill(contain, true);
						return true;
					}
					if(addStack(outputSlot, stack, false) == stack.stackSize)
					{
						tank.fill(contain, true);
						decrStackSize(inputSlot, 1);
						addStack(outputSlot, stack, true);
						return true;
					}
					return false;
				}
			}
		}
		else if(input.getItem() instanceof IFluidContainerItem)
		{
			IFluidContainerItem item = (IFluidContainerItem) input.getItem();
			ItemStack stack = input.copy();
			stack.stackSize = 1;
			if(item.getFluid(input) == null)
			{
				if(item.fill(stack, tank.getFluid(), false) != 0 && type.f && tank.getFluidAmount() > 0)
				{
					int i = item.fill(stack, tank.getFluid(), true);
					if(addStack(i, stack, false) == stack.stackSize)
					{
						tank.drain(i, true);
						decrStackSize(inputSlot, 1);
						addStack(outputSlot, stack, true);
						return true;
					}
					return false;
				}
			}
			else if(item.drain(stack, tank.getCapacity() - tank.getFluidAmount(), false) != null && tank.getFluidAmount() < tank.getCapacity() && type.d)
			{
				FluidStack tStack = item.drain(stack, tank.getCapacity() - tank.getFluidAmount(), true);
				if(tank.fill(tStack, false) != 0)
				{
					if(addStack(outputSlot, stack, false) == stack.stackSize)
					{
						tank.fill(tStack, true);
						decrStackSize(inputSlot, 1);
						addStack(outputSlot, stack, true);
						return true;
					}
				}
			}
			else if(tank.getFluidAmount() > 0 && item.fill(stack, tank.getFluid(), false) != 0 && type.f)
			{
				int fill = item.fill(stack, tank.getFluid().copy(), true);
				if(addStack(outputSlot, stack, false) == stack.stackSize)
				{
					tank.drain(fill, true);
					decrStackSize(inputSlot, 1);
					addStack(outputSlot, stack, true);
					return true;
				}
				return false;
			}
		}
		return false;
	}
}