package farcore.inventory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import farcore.lib.stack.AbstractStack;
import farcore.util.U;
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
	
	protected Random rand = new Random();
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
	  
	public void writeToNBT(NBTTagCompound nbt)
	{
		writeToNBT(nbt, "items");
	}
	  
	public void writeToNBT(NBTTagCompound nbt, String tag)
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
	    nbt.setTag(tag, slots);
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
	
	public boolean decrStack(int i, AbstractStack stack, boolean process)
	{
		if(stacks[i] == null) return false;
		else
		{
			if(stack.contain(stacks[i]))
			{
				int size = stack.size(stacks[i]);
				if(stacks[i].stackSize >= size)
				{
					if(process)
					{
						if(stacks[i].stackSize > size)
						{
							stacks[i].stackSize -= size;
						}
						else
						{
							stacks[i] = null;
						}
					}
					return true;
				}
			}
			return false;
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
	
	private ItemStack[] simulate()
	{
		ItemStack[] sim = new ItemStack[stacks.length];
		for(int i = 0; i < sim.length; 
				sim[i] = ItemStack.copyItemStack(stacks[i]), ++i);
		return sim;
	}

	public boolean addStacks(int start, int end, ItemStack[] stack, boolean process)
	{
		ItemStack[] simulte = simulate();
		for(ItemStack s : stack)
		{
			if(!addStacks(simulte, start, end, s, true, true))
				return false;
		}
		if(process)
		{
			for(ItemStack s : stack)
			{
				addStacks(stacks, start, end, s, true, true);
			}
		}
		return true;
	}
	public boolean addStacks(int start, int end, ItemStack stack, boolean process)
	{
		return addStacks(stacks, start, end, stack, process, !process);
	}
	public boolean addStacks(ItemStack[] stacks, int start, int end, ItemStack stack, boolean process, boolean ignoreCheck)
	{
		if(!ignoreCheck)
		{
			ItemStack[] simulte = simulate();
			ItemStack current = stack;
			label:
			{
				for(int i = start; i < end; ++i)
				{
					if((current = addStack(simulte, i, current, 0x5)) == null)
						break label;
				}
				for(int i = start; i < end; ++i)
				{
					if((current = addStack(simulte, i, current, 0x3)) == null)
						break label;
				}
				return false;
			}
			if(process)
			{
				current = stack;
				for(int i = start; i < end; ++i)
				{
					if((current = addStack(stacks, i, current, 0x5)) == null)
						return true;
				}
				for(int i = start; i < end; ++i)
				{
					if((current = addStack(stacks, i, current, 0x3)) == null)
						return true;
				}
			}
			return true;
		}
		else
		{
			if(!process) return true;
			ItemStack current = stack;
			for(int i = start; i < end; ++i)
			{
				if((current = addStack(stacks, i, current, 0x5)) == null)
					return true;
			}
			for(int i = start; i < end; ++i)
			{
				if((current = addStack(stacks, i, current, 0x3)) == null)
					return true;
			}
			return false;
		}
	}
	
	private ItemStack addStack(ItemStack[] container, int id, ItemStack stack, int flag)
	{
		if(stack == null || stack.stackSize <= 0) return null;
		boolean process = (flag & 0x1) != 0;
		boolean addToEmpty = (flag & 0x2) != 0;
		boolean addToFull = (flag & 0x4) != 0;
		ItemStack input = stack.copy();
		if(container[id] == null)
		{
			if(addToEmpty)
			{
				if(process)
				{
					container[id] = input;
				}
				return null;
			}
			return input;
		}
		else if(addToFull)
		{
			if(U.Inventorys.areStackSimilar(stack, container[id]))
			{
				int size = container[id].stackSize + stack.stackSize;
				int max = container[id].getMaxStackSize();
				if(size > max)
				{
					int size1 = max - container[id].stackSize;
					if(process)
					{
						container[id].stackSize = max;
					}
					return input.splitStack(size1);
				}
				if(process)
				{
					container[id].stackSize = size;
				}
				return null;
			}
			return input;
		}
		return input;
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
		return tile == INSTANCE ? true : !tile.isInvalid() &&
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