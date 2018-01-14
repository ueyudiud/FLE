/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid.container;

import static farcore.data.Capabilities.CAPABILITY_SOLID;
import static nebula.common.inventory.InventoryHelper.FD_DRAIN;
import static nebula.common.inventory.InventoryHelper.FD_FILL_ANY;
import static nebula.common.inventory.InventoryHelper.FD_FILL_ONLYFULL;

import javax.annotation.Nullable;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.container.ISolidHandler.ISoildHandlerProperty;
import farcore.lib.solid.container.SolidContainerManager.SolidContainerData;
import nebula.common.inventory.IBasicInventory;
import nebula.common.util.Direction;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class SolidContainerHelper
{
	public static SolidStack getSolidFromItemStack(@Nullable ItemStack stack)
	{
		if (stack == null) return null;
		if (stack.hasCapability(CAPABILITY_SOLID, null))
		{
			return stack.getCapability(CAPABILITY_SOLID, null).getProperty().getContents()[0];
		}
		SolidContainerManager.SolidContainerData data = SolidContainerManager.getDrainedContainer(stack);
		return data != null ? data.contain : null;
	}
	
	public static boolean drainFromItem(IBasicInventory inventory, int amount, int in, int out)
	{
		if (in == out)
		{
			ItemStack inputRaw = inventory.getStack(in);
			if (inputRaw != null)
			{
				ItemStack input = ItemStacks.sizeOf(inputRaw, 1);
				if (input.hasCapability(CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(CAPABILITY_SOLID, null);
					SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
					handler.drain(i1, true);
					if (input.stackSize == 0)
					{
						inventory.setSlotContents(in, ItemStacks.valid(input));
						return true;
					}
				}
				else if (inputRaw.stackSize == 1)
				{
					SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
					if (data != null)
					{
						inventory.setSlotContents(in, data.empty);
						return true;
					}
				}
			}
			return false;
		}
		else
		{
			ItemStack inputRaw = inventory.getStack(in);
			if (inputRaw != null)
			{
				ItemStack input = ItemStacks.sizeOf(inputRaw, 1);
				if (input.hasCapability(CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(CAPABILITY_SOLID, null);
					SolidStack i1 = handler.drain(amount, false);
					if (i1 != null && i1.amount == amount && inventory.instItem(out, input, true))
					{
						inventory.decrStackSize(in, 1);
						return true;
					}
				}
				else
				{
					SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
					if (data != null)
					{
						if (inventory.instItem(out, data.empty, true))
						{
							inventory.decrItem(in, 1, true);
							return true;
						}
					}
				}
			}
			return false;
		}
	}
	
	public static boolean drainOrFillTank(IBasicInventory inventory, SolidTank tank, int in, int out, byte fdType)
	{
		if ((fdType & FD_FILL_ANY) == 0 && tank.getSolidAmount() == 0)
		{
			return false;
		}
		if (in == out)
		{
			ItemStack inputRaw = inventory.getStack(in);
			if (inputRaw != null)
			{
				ItemStack input = ItemStacks.sizeOf(inputRaw, 1);
				if (input.hasCapability(CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(CAPABILITY_SOLID, null);
					ISoildHandlerProperty property = handler.getProperty();
					if ((fdType & FD_DRAIN) != 0 && tank.canDrain() && property.canFill())
					{
						SolidStack o1 = tank.drain(Integer.MAX_VALUE, false);
						if (o1 != null && handler.fill(o1, false) > 0)
						{
							tank.drain(SolidStack.sizeOf(o1, handler.fill(o1, true)), true);
							if (inputRaw.stackSize == 1 || input.stackSize == 0)
							{
								inventory.setSlotContents(in, ItemStacks.valid(input));
								return true;
							}
						}
					}
					if ((fdType & FD_FILL_ANY) != 0 && property.canDrain())
					{
						SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
						if (i1 != null && tank.canFill(i1))
						{
							int i;
							if ((fdType & FD_FILL_ONLYFULL) != 0)
							{
								handler.drain(i1, true);
								if ((inputRaw.stackSize == 1 || input.stackSize == 0) &&
										tank.insertSolid(i1, false))
								{
									inventory.setSlotContents(in, ItemStacks.valid(input));
									return true;
								}
							}
							else if (/*(fdType & FD_FILL_SIMPLE) != 0 &&*/(i = tank.fill(i1, false)) != 0)
							{
								SolidStack i2 = SolidStack.sizeOf(i1, i);
								handler.drain(i2, true);
								if (inputRaw.stackSize == 1 || input.stackSize == 0)
								{
									tank.fill(i2, true);
									inventory.setSlotContents(in, ItemStacks.valid(input));
									return true;
								}
							}
						}
					}
				}
				else if (inputRaw.stackSize == 1)
				{
					if ((fdType & FD_DRAIN) != 0)
					{
						SolidContainerData data = SolidContainerManager.getFilledContainer(input, tank.drain(Integer.MAX_VALUE, false));
						if (data != null)
						{
							inventory.setSlotContents(in, data.filled);
							tank.drain(data.contain, true);
							return true;
						}
					}
					else if ((fdType & FD_FILL_ANY) != 0)
					{
						SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
						if (data != null)
						{
							if ((fdType & FD_FILL_ONLYFULL) != 0)
							{
								if (tank.insertSolid(data.contain, false))
								{
									inventory.setSlotContents(in, data.empty);
									return true;
								}
							}
							else
							{
								if (tank.fill(data.contain, true) != 0)
								{
									inventory.setSlotContents(in, data.empty);
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
		else
		{
			ItemStack inputRaw = inventory.getStack(in);
			if (inputRaw != null)
			{
				ItemStack input = ItemStacks.sizeOf(inputRaw, 1);
				if (input.hasCapability(CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(CAPABILITY_SOLID, null);
					ISoildHandlerProperty property = handler.getProperty();
					if ((fdType & FD_DRAIN) != 0 && tank.canDrain() && property.canFill())
					{
						SolidStack o1 = tank.drain(Integer.MAX_VALUE, false);
						if (o1 != null && handler.fill(o1, false) > 0)
						{
							SolidStack o2 = SolidStack.sizeOf(o1, handler.fill(o1, true));
							if (input.stackSize == 0 || inventory.instItem(out, input, true))
							{
								tank.drain(o2, true);
								inventory.decrStackSize(in, 1);
								return true;
							}
						}
					}
					if ((fdType & FD_FILL_ANY) != 0 && property.canDrain())
					{
						SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
						if (i1 != null && tank.fill(i1, false) != 0)
						{
							SolidStack i2 = SolidStack.sizeOf(i1, tank.fill(i1, false));
							handler.drain(i2, true);
							if (inventory.instItem(out, input, true))
							{
								tank.fill(i2, true);
								inventory.decrStackSize(in, 1);
								return true;
							}
						}
					}
				}
				else
				{
					if ((fdType & FD_DRAIN) != 0)
					{
						SolidContainerData data = SolidContainerManager.getFilledContainer(input, tank.drain(Integer.MAX_VALUE, false));
						if (data != null && inventory.instItem(out, data.filled, true))
						{
							inventory.decrItem(in, 1, true);
							tank.drain(data.contain, true);
							return true;
						}
					}
					else if ((fdType & FD_FILL_ANY) != 0)
					{
						SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
						if (data != null)
						{
							if ((fdType & FD_FILL_ONLYFULL) != 0 && inventory.instItem(out, data.empty, false)
									&& tank.insertSolid(data.contain, false))
							{
								inventory.decrItem(in, 1, true);
								return true;
							}
							else
							{
								if (tank.fill(data.contain, false) > 0 && inventory.instItem(out, data.empty, true))
								{
									tank.fill(data.contain, true);
									inventory.decrItem(in, 1, true);
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
	}
	
	public static boolean drainOrFillTank(IBasicInventory inventory, ISolidHandlerIO io, Direction direction, int in, int out, byte fdType)
	{
		if (in == out)
		{
			ItemStack inputRaw = inventory.getStack(in);
			if (inputRaw != null)
			{
				ItemStack input = ItemStacks.sizeOf(inputRaw, 1);
				if (input.hasCapability(CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(CAPABILITY_SOLID, null);
					ISoildHandlerProperty property = handler.getProperty();
					if ((fdType & FD_DRAIN) != 0 && io.canExtractSolid(direction) && property.canFill())
					{
						SolidStack o1 = io.extractSolid(Integer.MAX_VALUE, direction, true);
						if (o1 != null && handler.fill(o1, false) > 0)
						{
							io.extractSolid(SolidStack.sizeOf(o1, handler.fill(o1, true)), direction, false);
							if (inputRaw.stackSize == 1 || input.stackSize == 0)
							{
								inventory.setSlotContents(in, ItemStacks.valid(input));
								return true;
							}
						}
					}
					if ((fdType & FD_FILL_ANY) != 0 && property.canDrain())
					{
						SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
						if (i1 != null && io.insertSolid(i1, direction, true) != 0)
						{
							handler.drain(SolidStack.sizeOf(i1, io.insertSolid(i1, direction, false)), true);
							if (inputRaw.stackSize == 1 || input.stackSize == 0)
							{
								inventory.setSlotContents(in, ItemStacks.valid(input));
								return true;
							}
						}
					}
				}
				else if (inputRaw.stackSize == 1)
				{
					if ((fdType & FD_DRAIN) != 0)
					{
						SolidContainerData data = SolidContainerManager.getFilledContainer(input, io.extractSolid(Integer.MAX_VALUE, direction, true));
						if (data != null)
						{
							inventory.setSlotContents(in, data.filled);
							io.extractSolid(data.contain, direction, false);
							return true;
						}
					}
					else if ((fdType & FD_FILL_ANY) != 0)
					{
						SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
						if (data != null && io.insertSolid(data.contain, direction, true) == data.contain.amount)
						{
							inventory.setSlotContents(in, data.empty);
							io.insertSolid(data.contain, direction, false);
							return true;
						}
					}
				}
			}
			return false;
		}
		else
		{
			ItemStack inputRaw = inventory.getStack(in);
			if (inputRaw != null)
			{
				ItemStack input = ItemStacks.sizeOf(inputRaw, 1);
				if (input.hasCapability(CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(CAPABILITY_SOLID, null);
					ISoildHandlerProperty property = handler.getProperty();
					if ((fdType & FD_DRAIN) != 0 && io.canExtractSolid(direction) && property.canFill())
					{
						SolidStack o1 = io.extractSolid(Integer.MAX_VALUE, direction, true);
						if (o1 != null && handler.fill(o1, false) > 0)
						{
							io.extractSolid(SolidStack.sizeOf(o1, handler.fill(o1, true)), direction, false);
							if (input.stackSize == 0 || inventory.instItem(out, input, true))
							{
								inventory.decrStackSize(in, 1);
								return true;
							}
						}
					}
					if ((fdType & FD_FILL_ANY) != 0 && property.canDrain())
					{
						SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
						if (i1 != null && io.insertSolid(i1, direction, true) != 0)
						{
							handler.drain(SolidStack.sizeOf(i1, io.insertSolid(i1, direction, false)), true);
							if (inventory.instItem(out, input, true))
							{
								inventory.decrStackSize(in, 1);
								return true;
							}
						}
					}
				}
				else
				{
					if ((fdType & FD_DRAIN) != 0)
					{
						SolidContainerData data = SolidContainerManager.getFilledContainer(input, io.extractSolid(Integer.MAX_VALUE, direction, true));
						if (data != null && inventory.instItem(out, data.filled, true))
						{
							inventory.decrItem(in, 1, true);
							io.extractSolid(data.contain, direction, false);
							return true;
						}
					}
					else if ((fdType & FD_FILL_ANY) != 0)
					{
						SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
						if (data != null)
						{
							if ((fdType & FD_FILL_ONLYFULL) != 0)
							{
								if (io.insertSolid(data.contain, direction, true) == data.contain.amount &&
										inventory.instItem(out, data.empty, true))
								{
									inventory.decrItem(in, 1, true);
									io.insertSolid(data.contain, direction, false);
									return true;
								}
							}
							else
							{
								if (io.insertSolid(data.contain, direction, true) > 0 &&
										inventory.instItem(out, data.empty, true))
								{
									inventory.decrItem(in, 1, true);
									io.insertSolid(data.contain, direction, false);
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
	}
}
