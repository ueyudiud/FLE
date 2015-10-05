package fle.core.recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.oredict.OreDictionary;
import fle.api.FleValue;
import fle.api.recipe.ItemAbstractStack;
import fle.api.soild.ISolidContainerItem;
import fle.api.soild.ISolidHandler;
import fle.api.soild.SolidRegistry;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import static fle.core.recipe.RecipeHelper.FDType.*;

public class RecipeHelper 
{
	public static boolean fillOrDrainInventoryTank(IInventory inv, SolidTank tank, int inputSlot, int outputSlot)
	{
		return fillOrDrainInventoryTank(inv, tank, inputSlot, outputSlot, FD);
	}
	public static boolean fillOrDrainInventoryTank(IInventory inv, IFluidTank tank, int inputSlot, int outputSlot)
	{
		return fillOrDrainInventoryTank(inv, tank, inputSlot, outputSlot, FD);
	}
	public static boolean fillOrDrainInventoryTank(IInventory inv, SolidTank tank, int inputSlot, int outputSlot, FDType type)
	{
		ItemStack input = inv.getStackInSlot(inputSlot);
		ItemStack output = inv.getStackInSlot(outputSlot);
		if(input == null) return true;
		if(SolidRegistry.isContainer(input))
		{
			ItemStack stack;
			if(SolidRegistry.isEmptyContainer(input) && type.f && tank.size() > 0)
			{
				int i = SolidRegistry.getContainerCapacity(tank.getStack(), input);
				stack = SolidRegistry.fillSolidContainer(tank.getStack(), input);
				if(stack == null) return true;
				if(output == null)
				{
					tank.drain(i, true);
					inv.decrStackSize(inputSlot, 1);
					inv.setInventorySlotContents(outputSlot, stack);
					return true;
				}
				else if(matchOutput(inv, outputSlot, stack))
				{
					tank.drain(i, true);
					inv.decrStackSize(inputSlot, 1);
					inv.getStackInSlot(outputSlot).stackSize++;
					return true;
				}
				return false;
			}
			else if(SolidRegistry.isFilledContainer(input) && type.d)
			{
				SolidStack contain = SolidRegistry.getSolidForFilledItem(input);
				if(tank.fill(contain, false) != 0 && tank.size() + contain.getSize() <= tank.getCapcity())
				{
					stack = SolidRegistry.drainSolidContainer(input);
					if(output == null)
					{
						tank.fill(contain, true);
						inv.decrStackSize(inputSlot, 1);
						inv.setInventorySlotContents(outputSlot, stack);
						return true;
					}
					else if(matchOutput(inv, outputSlot, stack))
					{
						tank.fill(contain, true);
						inv.decrStackSize(inputSlot, 1);
						inv.getStackInSlot(outputSlot).stackSize++;
						return true;
					}
					return false;
				}
			}
		}
		else if(input.getItem() instanceof ISolidContainerItem)
		{
			ISolidContainerItem item = (ISolidContainerItem) input.getItem();
			ItemStack aStack = input.copy();
			aStack.stackSize = 1;
			if(item.getSolid(input) == null && type.f && tank.size() > 0)
			{
				if(item.canFill(aStack, tank.get()))
				{
					if(item.fill(aStack, tank.getStack(), false) != 0)
					{
						int i = item.fill(aStack, tank.getStack(), true);
						if(output == null)
						{
							tank.drain(i, true);
							inv.decrStackSize(inputSlot, 1);
							inv.setInventorySlotContents(outputSlot, aStack);
							return true;
						}
						else if(matchOutput(inv, outputSlot, aStack))
						{
							tank.drain(i, true);
							inv.decrStackSize(inputSlot, 1);
							inv.getStackInSlot(outputSlot).stackSize++;
							return true;
						}
						return false;
					}
				}
			}
			else if(item.drain(aStack, tank.getCapcity() - tank.size(), false) != null && tank.size() < tank.getCapcity() && type.d)
			{
				if(item.canDrain(aStack, item.getSolid(aStack).getObj()))
				{
					SolidStack tStack = item.drain(aStack, tank.getCapcity() - tank.size(), true);
					if(tank.fill(tStack, false) != 0)
					{
						if(output == null)
						{
							tank.fill(tStack, true);
							inv.decrStackSize(inputSlot, 1);
							inv.setInventorySlotContents(outputSlot, aStack);
							return true;
						}
						else if(matchOutput(inv, outputSlot, aStack))
						{
							tank.fill(tStack, true);
							inv.decrStackSize(inputSlot, 1);
							inv.getStackInSlot(outputSlot).stackSize++;
							return true;
						}
					}
				}
			}
			else if(tank.size() > 0 && item.fill(aStack, tank.getStack(), false) != 0 && type.f)
			{
				int fill = item.fill(aStack, tank.getStack().copy(), true);
				if(output == null)
				{
					tank.drain(fill, true);
					inv.decrStackSize(inputSlot, 1);
					inv.setInventorySlotContents(outputSlot, aStack);
					return true;
				}
				else if(matchOutput(inv, outputSlot, aStack))
				{
					tank.drain(fill, true);
					inv.decrStackSize(inputSlot, 1);
					inv.getStackInSlot(outputSlot).stackSize++;
					return true;
				}
				return false;
			}
		}
		return false;
	}
	public static boolean fillOrDrainInventoryTank(IInventory inv, IFluidTank tank, int inputSlot, int outputSlot, FDType type)
	{
		ItemStack input = inv.getStackInSlot(inputSlot);
		ItemStack output = inv.getStackInSlot(outputSlot);
		if(input == null) return true;
		if(FluidContainerRegistry.isContainer(input))
		{
			ItemStack stack;
			if(FluidContainerRegistry.isEmptyContainer(input) && type.f && tank.getFluidAmount() > 0)
			{
				int i = FluidContainerRegistry.getContainerCapacity(tank.getFluid(), input);
				stack = FluidContainerRegistry.fillFluidContainer(tank.getFluid(), input);
				if(stack == null) return true;
				if(output == null)
				{
					tank.drain(i, true);
					inv.decrStackSize(inputSlot, 1);
					inv.setInventorySlotContents(outputSlot, stack);
					return true;
				}
				else if(matchOutput(inv, outputSlot, stack))
				{
					tank.drain(i, true);
					inv.decrStackSize(inputSlot, 1);
					inv.getStackInSlot(outputSlot).stackSize++;
					return true;
				}
				return false;
			}
			else if(FluidContainerRegistry.isFilledContainer(input) && type.d)
			{
				FluidStack contain = FluidContainerRegistry.getFluidForFilledItem(input);
				if(tank.fill(contain, false) != 0 && tank.getFluidAmount() + contain.amount <= tank.getCapacity())
				{
					stack = FluidContainerRegistry.drainFluidContainer(input);
					if(output == null)
					{
						tank.fill(contain, true);
						inv.decrStackSize(inputSlot, 1);
						inv.setInventorySlotContents(outputSlot, stack);
						return true;
					}
					else if(matchOutput(inv, outputSlot, stack))
					{
						tank.fill(contain, true);
						inv.decrStackSize(inputSlot, 1);
						inv.getStackInSlot(outputSlot).stackSize++;
						return true;
					}
					return false;
				}
			}
		}
		else if(input.getItem() instanceof IFluidContainerItem)
		{
			IFluidContainerItem item = (IFluidContainerItem) input.getItem();
			ItemStack aStack = input.copy();
			aStack.stackSize = 1;
			if(item.getFluid(input) == null)
			{
				if(item.fill(aStack, tank.getFluid(), false) != 0 && type.f && tank.getFluidAmount() > 0)
				{
					int i = item.fill(aStack, tank.getFluid(), true);
					if(output == null)
					{
						tank.drain(i, true);
						inv.decrStackSize(inputSlot, 1);
						inv.setInventorySlotContents(outputSlot, aStack);
						return true;
					}
					else if(matchOutput(inv, outputSlot, aStack))
					{
						tank.drain(i, true);
						inv.decrStackSize(inputSlot, 1);
						inv.getStackInSlot(outputSlot).stackSize++;
						return true;
					}
					return false;
				}
			}
			else if(item.drain(aStack, tank.getCapacity() - tank.getFluidAmount(), false) != null && tank.getFluidAmount() < tank.getCapacity() && type.d)
			{
				FluidStack tStack = item.drain(aStack, tank.getCapacity() - tank.getFluidAmount(), true);
				if(tank.fill(tStack, false) != 0)
				{
					if(output == null)
					{
						tank.fill(tStack, true);
						inv.decrStackSize(inputSlot, 1);
						inv.setInventorySlotContents(outputSlot, aStack);
						return true;
					}
					else if(matchOutput(inv, outputSlot, aStack))
					{
						tank.fill(tStack, true);
						inv.decrStackSize(inputSlot, 1);
						inv.getStackInSlot(outputSlot).stackSize++;
						return true;
					}
				}
			}
			else if(tank.getFluidAmount() > 0 && item.fill(aStack, tank.getFluid(), false) != 0 && type.f)
			{
				int fill = item.fill(aStack, tank.getFluid().copy(), true);
				if(output == null)
				{
					tank.drain(fill, true);
					inv.decrStackSize(inputSlot, 1);
					inv.setInventorySlotContents(outputSlot, aStack);
					return true;
				}
				else if(matchOutput(inv, outputSlot, aStack))
				{
					tank.drain(fill, true);
					inv.decrStackSize(inputSlot, 1);
					inv.getStackInSlot(outputSlot).stackSize++;
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
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
	
	public static boolean matchItemStack(IInventory inv, int matchSlot, ItemAbstractStack ic)
	{
		return inv.getStackInSlot(matchSlot) != null ? ic.isStackEqul(inv.getStackInSlot(matchSlot)) : ic == null;
	}
	public static boolean matchShapedInventory(IInventory inv, int startSlot, int endSlot, ItemAbstractStack[] ic)
	{
		int uStartSlot = endSlot - ic.length;
		if(uStartSlot < 0) throw new RuntimeException();
		for(int sMove = 0; sMove <= uStartSlot; ++sMove)
		{
			boolean flag = true;
			for(int i = startSlot + sMove; i < startSlot + sMove + ic.length; ++i)
			{
				if(!matchItemStack(inv, i, ic[i - startSlot - sMove]))
				{
					flag = false;
					break;
				}
			}
			if(flag) return true;
		}
		return false;
	}
	public static boolean matchShapelessInventory(IInventory inv, int startSlot, int endSlot, ItemAbstractStack[] ic)
	{
		List<ItemAbstractStack> list = new ArrayList();
		list.addAll(Arrays.asList(ic));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			boolean flag = false;
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null) continue;
			Iterator<ItemAbstractStack> itr = list.iterator();
			while(itr.hasNext())
			{
				ItemAbstractStack checker = itr.next();
				if(checker.isStackEqul(stack))
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

	public static boolean matchOutput(IInventory inv, int outputSlot, ItemStack output)
	{
		return inv.getStackInSlot(outputSlot) == null ? true : 
			output == null ? true : 
				ItemStack.areItemStackTagsEqual(inv.getStackInSlot(outputSlot), output) && 
				output.isItemEqual(inv.getStackInSlot(outputSlot)) && 
				inv.getStackInSlot(outputSlot).stackSize + output.stackSize <= Math.min(output.getMaxStackSize(), inv.getInventoryStackLimit());
	}
	public static boolean matchOutput(IInventory inv, int startSlot, int endSlot, ItemStack output)
	{
		if(output == null) return true;
		ItemStack stack = output.copy();
		for(int i = startSlot; i < endSlot; ++i)
		{
			int a = addIn(false, inv, i, stack);
			stack.stackSize -= a;
			if(stack.stackSize == 0) return true;
		}
		return stack.stackSize == 0;
	}
	public static boolean matchOutput(IInventory inv, int startSlot, int endSlot, ItemStack[] output)
	{
		FakeInventory inv1 = new FakeInventory(inv);
		for(int i = 0; i < output.length; ++i)
		{
			if(output[i] == null) continue;
			ItemStack stack = output[i].copy();
			for(int j = startSlot; j < endSlot; ++j)
			{
				int a = addIn(true, inv1, j, stack);
				stack.stackSize -= a;
				if(stack.stackSize == 0) break;
			}
			if(stack.stackSize != 0) return false;
		}
		return true;
	}
	private static int addIn(boolean doAdd, IInventory inv, int outputSlot, ItemStack output)
	{
		if(!doAdd)
		{
			return inv.getStackInSlot(outputSlot) == null ? output.stackSize : ItemStack.areItemStackTagsEqual(inv.getStackInSlot(outputSlot), output) && output.isItemEqual(inv.getStackInSlot(outputSlot)) ? Math.min(output.stackSize, output.getMaxStackSize() - inv.getStackInSlot(outputSlot).stackSize) : 0;
		}
		else if(inv.getStackInSlot(outputSlot) == null) 
		{
			inv.setInventorySlotContents(outputSlot, output);
			return output.stackSize;
		}
		else if(ItemStack.areItemStackTagsEqual(inv.getStackInSlot(outputSlot), output) && output.isItemEqual(inv.getStackInSlot(outputSlot)))
		{
			int add = Math.min(output.stackSize, output.getMaxStackSize() - inv.getStackInSlot(outputSlot).stackSize);
			inv.getStackInSlot(outputSlot).stackSize += add;
			return add;
		}
		else
		{
			return 0;
		}
	}

	public static void onInputItemStack(EntityPlayer aPlayer)
	{
		if(aPlayer.capabilities.isCreativeMode) return;
		--aPlayer.getCurrentEquippedItem().stackSize;
		if(aPlayer.getCurrentEquippedItem().stackSize <= 0)
		{
			aPlayer.destroyCurrentEquippedItem();
		}
	}
	public static void onInputItemStack(IInventory inv, int slotSlot) 
	{
		inv.decrStackSize(slotSlot, 1);
	}
	public static void onInputShapedInventory(IInventory inv, int startSlot, int endSlot, ItemAbstractStack[] ic) 
	{
		int uStartSlot = endSlot - ic.length;
		for(int sMove = 0; sMove < uStartSlot; ++sMove)
		{
			boolean flag = true;
			for(int i = startSlot + sMove; i < startSlot + sMove + uStartSlot; ++i)
			{
				if(!matchItemStack(inv, i, ic[i]))
				{
					flag = false;
					break;
				}
			}
			if(flag)
			{
				for(int i = startSlot + sMove; i < startSlot + sMove + uStartSlot; ++i)
				{
					inv.decrStackSize(i, 1);
				}
				break;
			}
		}
	}
	public static void onInputShaelessInventory(IInventory inv, int startSlot, int endSlot, ItemAbstractStack[] ic) 
	{
		List<ItemAbstractStack> list = new ArrayList();
		list.addAll(Arrays.asList(ic));
		
		for(int i = startSlot; i < endSlot; ++i)
		{
			boolean flag = false;
			ItemStack stack = inv.getStackInSlot(i);
			if(stack == null) continue;
			Iterator<ItemAbstractStack> itr = list.iterator();
			while(itr.hasNext())
			{
				ItemAbstractStack checker = itr.next();
				if(checker.isStackEqul(stack))
				{
					flag = true;
					inv.decrStackSize(i, 1);
					list.remove(checker);
					break;
				}
			}
		}
	}
	
	public static void onOutputItemStack(IInventory inv, int outputSlot, ItemStack output)
	{
		if(output == null) return;
		if(inv.getStackInSlot(outputSlot) == null) inv.setInventorySlotContents(outputSlot, output.copy());
		else inv.getStackInSlot(outputSlot).stackSize += output.stackSize;
	}
	public static void onOutputShapelessStack(IInventory inv, int startSlot, int endSlot, ItemStack output)
	{
		if(output == null) return;
		ItemStack stack = output.copy();
		for(int i = startSlot; i < endSlot; ++i)
		{
			int a = addIn(true, inv, i, stack);
			stack.stackSize -= a;
			if(stack.stackSize == 0) return;
		}
	}
	public static void onOutputShapelessStacks(IInventory inv, int startSlot, int endSlot, ItemStack[] output)
	{
		for(int i = 0; i < output.length; ++i)
			onOutputShapelessStack(inv, startSlot, endSlot, output[i]);
	}

	public static boolean matchOutFluidStack(IFluidTank tank, FluidStack stack) 
	{
		return tank.getFluid() == null || stack == null ? true : 
			tank.getCapacity() - tank.getFluidAmount() < stack.amount ? false :
				tank.getFluid().getFluid() == stack.getFluid() ? true : mixFluid(tank.getFluid(), stack) != null;
	}

	public static boolean matchOutSolidStack(SolidTank tank, SolidStack stack) 
	{
		return tank.getStack() == null || stack == null ? true : 
			tank.getCapcity() - tank.size() < stack.getSize() ? false :
				SolidStack.areStackEquls(tank.getStack(), stack);
	}
	
	public static FluidStack mixFluid(FluidStack stack1, FluidStack stack2)
	{
		return null;
	}
	
	public static void onOutputSolidStack(SolidTank tank, SolidStack stack)
	{
		if(stack == null) return;
		else if(tank.getStack() == null)
		{
			tank.fill(stack, true);
		}
		else if(tank.getStack().isStackEqul(stack))
		{
			tank.fill(stack, true);
		}
	}

	public static void onOutputFluidStack(IFluidTank tank, FluidStack stack) 
	{
		if(stack == null)
		{
			return;
		}
		else if(tank.getFluid() == null)
		{
			tank.fill(stack, true);
		}
		else if(tank.getFluid().isFluidEqual(stack))
		{
			tank.fill(stack, true);
		}
		else
		{
			FluidStack out = mixFluid(tank.getFluid(), stack);
			if(out != null)
			{
				tank.drain(tank.getCapacity(), true);
				tank.fill(out, true);
			}
		}
	}
	
	public static class FakeCraftingInventory extends InventoryCrafting
	{
		private final ItemStack[] itemstacks;
		private int xSize;
		private int ySize;
		
		public static FakeCraftingInventory init(Object...recipe)
		{
	        String shape = "";
	        int idx = 0;
	        int xSize = 0;
	        int ySize = 0;
	        ItemStack[] itemstacks;

	        if (recipe[idx] instanceof String[])
	        {
	            String[] parts = ((String[])recipe[idx++]);

	            for (String s : parts)
	            {
	                xSize = s.length();
	                shape += s;
	            }

	            ySize = parts.length;
	        }
	        else
	        {
	            while (recipe[idx] instanceof String)
	            {
	                String s = (String)recipe[idx++];
	                shape += s;
	                xSize = s.length();
	                ySize++;
	            }
	        }

	        if (xSize * ySize != shape.length())
	        {
	            String ret = "Invalid shaped fake inventory: ";
	            for (Object tmp :  recipe)
	            {
	                ret += tmp + ", ";
	            }
	            throw new RuntimeException(ret);
	        }

	        HashMap<Character, ItemStack> itemMap = new HashMap();

	        for (; idx < recipe.length; idx += 2)
	        {
	            Character chr = (Character)recipe[idx];
	            Object in = recipe[idx + 1];

	            if (in instanceof ItemStack)
	            {
	                itemMap.put(chr, ((ItemStack)in).copy());
	            }
	            else if (in instanceof Item)
	            {
	                itemMap.put(chr, new ItemStack((Item)in));
	            }
	            else if (in instanceof Block)
	            {
	                itemMap.put(chr, new ItemStack((Block)in));
	            }
	            else if (in instanceof String)
	            {
	            	if(!OreDictionary.getOres((String)in).isEmpty())
	            	{
	            		ItemStack stack = OreDictionary.getOres((String)in).get(0);
	            		if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
	            			stack.setItemDamage(0);
	            		itemMap.put(chr, stack);
	            	}
	            	else
	            	{
	            		itemMap.put(chr, new ItemStack(Blocks.air));
	            	}
	            }
	            else
	            {
	                String ret = "Invalid shaped ore recipe: ";
	                for (Object tmp :  recipe)
	                {
	                    ret += tmp + ", ";
	                }
	                throw new RuntimeException(ret);
	            }
	        }

	        itemstacks = new ItemStack[xSize * ySize];
	        int x = 0;
	        for (char chr : shape.toCharArray())
	        {
	            itemstacks[x++] = itemMap.get(chr);
	        }

			return new FakeCraftingInventory(xSize, ySize, itemstacks);
		}
		private FakeCraftingInventory(int xSize, int ySize, ItemStack...stacks)
		{
			super(null, xSize, ySize);
			this.itemstacks = stacks;
			this.xSize = xSize;
			this.ySize = ySize;
		}

	    public ItemStack getStackInRowAndColumn(int x, int y)
	    {
	        if (x >= 0 && x < xSize && y >= 0 && y < ySize)
	        {
	            int k = x + y * xSize;
	            return this.getStackInSlot(k);
	        }
	        else
	        {
	            return null;
	        }
	    }

		public int getSizeInventory() {return itemstacks.length;}

		public ItemStack getStackInSlot(int i) {return i >= itemstacks.length || i < 0 ? null : itemstacks[i];}

		public ItemStack decrStackSize(int i, int size) 
		{
			if(itemstacks[i] == null) return null;
			ItemStack ret = itemstacks[i].copy();
			int a = ret.stackSize;
			itemstacks[i].stackSize -= size;
			if(itemstacks[i].stackSize < 1) itemstacks[i] = null;
			ret.stackSize = Math.min(size, ret.stackSize);
			return ret;
		}

		public ItemStack getStackInSlotOnClosing(int i) {return decrStackSize(i, getInventoryStackLimit());}

		public void setInventorySlotContents(int i, ItemStack itemstack) 
		{
			if(itemstack != null) itemstacks[i] = itemstack.copy();
		}

		public String getInventoryName() {return null;}
		public boolean hasCustomInventoryName() {return false;}
		public int getInventoryStackLimit() {return FleValue.MAX_STACK_SIZE;}
		public void markDirty() {}
		public boolean isUseableByPlayer(EntityPlayer player) {return true;}
		public void openInventory() {}
		public void closeInventory() {}
		public boolean isItemValidForSlot(int i, ItemStack stack) { return true;}
	}
	
	private static class FakeInventory implements IInventory
	{
		private final ItemStack[] itemstacks;
		private FakeInventory(ItemStack[] stacks) 
		{
			this.itemstacks = stacks;
		}
		private FakeInventory(IInventory inventory) 
		{
			this.itemstacks = new ItemStack[inventory.getSizeInventory()];
		}

		public int getSizeInventory() {return itemstacks.length;}

		public ItemStack getStackInSlot(int i) {return itemstacks[i];}

		public ItemStack decrStackSize(int i, int size) 
		{
			if(itemstacks[i] == null) return null;
			ItemStack ret = itemstacks[i].copy();
			int a = ret.stackSize;
			itemstacks[i].stackSize -= size;
			if(itemstacks[i].stackSize < 1) itemstacks[i] = null;
			ret.stackSize = Math.min(size, ret.stackSize);
			return ret;
		}

		public ItemStack getStackInSlotOnClosing(int i) {return decrStackSize(i, getInventoryStackLimit());}

		public void setInventorySlotContents(int i, ItemStack itemstack) 
		{
			if(itemstack != null) itemstacks[i] = itemstack.copy();
		}

		public String getInventoryName() {return null;}
		public boolean hasCustomInventoryName() {return false;}
		public int getInventoryStackLimit() {return FleValue.MAX_STACK_SIZE;}
		public void markDirty() {}
		public boolean isUseableByPlayer(EntityPlayer player) {return true;}
		public void openInventory() {}
		public void closeInventory() {}
		public boolean isItemValidForSlot(int i, ItemStack stack) { return true;}	
	}
}