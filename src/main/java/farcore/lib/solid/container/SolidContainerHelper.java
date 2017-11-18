/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.solid.container;

import static nebula.common.inventory.InventoryHelper.FD_DRAIN;
import static nebula.common.inventory.InventoryHelper.FD_FILL_ANY;

import farcore.data.Capabilities;
import farcore.lib.solid.SolidStack;
import farcore.lib.solid.container.ISolidHandler.ISoildHandlerProperty;
import nebula.common.inventory.IBasicInventory;
import nebula.common.util.Direction;
import nebula.common.util.ItemStacks;
import net.minecraft.item.ItemStack;

/**
 * @author ueyudiud
 */
public class SolidContainerHelper
{
	public static SolidStack getSolidFromItemStack(ItemStack stack)
	{
		return null;
	}
	
	public static boolean drainFromItem(IBasicInventory inventory, int amount, int in, int out)
	{
		return false;// TODO when solid container system finished.
	}
	
	public static boolean drainOrFillTank(IBasicInventory inventory, ISolidHandlerIO io, int in, int out, byte fdType)
	{
		if (in == out)
		{
			ItemStack inputRaw = inventory.getStack(in);
			if (inputRaw != null)
			{
				ItemStack input = ItemStacks.sizeOf(inputRaw, 1);
				if (input.hasCapability(Capabilities.CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(Capabilities.CAPABILITY_SOLID, null);
					ISoildHandlerProperty property = handler.getProperty();
					if ((fdType & FD_DRAIN) != 0 && io.canExtractSolid(Direction.Q) && property.canFill())
					{
						SolidStack o1 = io.extractSolid(Integer.MAX_VALUE, Direction.Q, true);
						if (o1 != null && handler.fill(o1, false) > 0)
						{
							io.extractSolid(SolidStack.sizeOf(o1, handler.fill(o1, true)), Direction.Q, false);
							if (inputRaw.stackSize == 1 || input.stackSize == 0)
							{
								inventory.setInventorySlotContents(in, ItemStacks.valid(input));
								return true;
							}
						}
					}
					if ((fdType & FD_FILL_ANY) != 0 && property.canDrain())
					{
						SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
						if (i1 != null && io.insertSolid(i1, Direction.Q, true) != 0)
						{
							handler.drain(SolidStack.sizeOf(i1, io.insertSolid(i1, Direction.Q, false)), true);
							if (inputRaw.stackSize == 1 || input.stackSize == 0)
							{
								inventory.setInventorySlotContents(in, ItemStacks.valid(input));
								return true;
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
				if (input.hasCapability(Capabilities.CAPABILITY_SOLID, null))
				{
					ISolidHandler handler = input.getCapability(Capabilities.CAPABILITY_SOLID, null);
					ISoildHandlerProperty property = handler.getProperty();
					if ((fdType & FD_DRAIN) != 0 && io.canExtractSolid(Direction.Q) && property.canFill())
					{
						SolidStack o1 = io.extractSolid(Integer.MAX_VALUE, Direction.Q, true);
						if (o1 != null && handler.fill(o1, false) > 0)
						{
							io.extractSolid(SolidStack.sizeOf(o1, handler.fill(o1, true)), Direction.Q, false);
							if (input.stackSize == 0 || inventory.insertStack(out, input, true))
							{
								inventory.decrStackSize(in, 1);
								return true;
							}
						}
					}
					if ((fdType & FD_FILL_ANY) != 0 && property.canDrain())
					{
						SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
						if (i1 != null && io.insertSolid(i1, Direction.Q, true) != 0)
						{
							handler.drain(SolidStack.sizeOf(i1, io.insertSolid(i1, Direction.Q, false)), true);
							if (inventory.insertStack(out, input, true))
							{
								inventory.decrStackSize(in, 1);
								return true;
							}
						}
					}
				}
			}
			return false;
		}
	}
}
