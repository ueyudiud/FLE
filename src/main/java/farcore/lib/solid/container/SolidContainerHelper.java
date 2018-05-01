/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.solid.container;

import static farcore.data.Capabilities.CAPABILITY_SOLID;

import java.util.Map.Entry;

import javax.annotation.Nullable;

import farcore.lib.inventory.ISolidHandler;
import farcore.lib.solid.SolidStack;
import farcore.lib.solid.container.SolidContainerManager.SolidContainerData;
import nebula.base.Ety;
import nebula.base.MutableIntEntry;
import nebula.common.inventory.ContainerTask;
import nebula.common.inventory.ContainerTask.TasksBuilder;
import nebula.common.inventory.IItemContainer;
import nebula.common.stack.IS;
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
	
	public static boolean drainFromItem(IItemContainer in, IItemContainer out, int amount)
	{
		if (in == out)
		{
			if (in.hasStackInContainer())
			{
				ItemStack input = in.getStackInContainer();
				if (input.hasCapability(CAPABILITY_SOLID, null) && input.stackSize == 1)
				{
					ISolidHandler handler = input.getCapability(CAPABILITY_SOLID, null);
					SolidStack i1 = handler.drain(Integer.MAX_VALUE, false);
					handler.drain(i1, true);
					if (input.stackSize == 0)
					{
						in.refresh();
						return true;
					}
				}
				else
				{
					SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
					if (data != null)
					{
						TasksBuilder builder = ContainerTask.builder();
						builder.add(in.taskDecr(data.filled, IItemContainer.PROCESS));
						builder.add(in.taskIncr(ItemStack.copyItemStack(data.empty), IItemContainer.PROCESS));
						return builder.build().getAsBoolean();
					}
				}
			}
			return false;
		}
		else
		{
			if (in.hasStackInContainer())
			{
				ItemStack input = IS.copy(in.getStackInContainer(), 1);
				if (input.hasCapability(CAPABILITY_SOLID, null))
				{
					ItemStack output = input.copy();
					ISolidHandler handler = output.getCapability(CAPABILITY_SOLID, null);
					SolidStack i1 = handler.drain(amount, true);
					if (i1 != null && i1.amount == amount)
					{
						TasksBuilder builder = ContainerTask.builder();
						builder.add(in.taskDecr(1, IItemContainer.PROCESS));
						builder.add(out.taskIncr(output, IItemContainer.PROCESS));
						return builder.build().getAsBoolean();
					}
				}
				else
				{
					SolidContainerData data = SolidContainerManager.getDrainedContainer(input);
					if (data != null)
					{
						TasksBuilder builder = ContainerTask.builder();
						builder.add(in.taskDecr(data.filled, IItemContainer.PROCESS));
						builder.add(out.taskIncr(data.empty, IItemContainer.PROCESS));
						return builder.build().getAsBoolean();
					}
				}
			}
			return false;
		}
	}
	
	public static @Nullable MutableIntEntry<ItemStack> fillContainer(ItemStack stack, SolidStack resource)
	{
		if (stack.hasCapability(CAPABILITY_SOLID, null))
		{
			stack = stack.copy();
			ISolidHandler handler = stack.getCapability(CAPABILITY_SOLID, null);
			int amount = handler.fill(resource, true);
			if (amount > 0)
			{
				return new MutableIntEntry<>(stack, amount);
			}
		}
		SolidContainerData data = SolidContainerManager.getFilledContainer(stack, resource);
		if (data != null)
		{
			return new MutableIntEntry<>(data.filled.copy(), data.contain.amount);
		}
		return null;
	}
	
	public static @Nullable Entry<ItemStack, SolidStack> drainContainer(ItemStack stack, int amount)
	{
		if (stack.hasCapability(CAPABILITY_SOLID, null))
		{
			stack = stack.copy();
			ISolidHandler handler = stack.getCapability(CAPABILITY_SOLID, null);
			SolidStack stack2 = handler.drain(amount, true);
			return new Ety(ItemStacks.valid(stack), stack2);
		}
		SolidContainerData data = SolidContainerManager.getDrainedContainer(stack);
		if (data != null && amount <= data.contain.amount)
		{
			return new Ety(IS.copy(data.empty), data.contain.copy());
		}
		return null;
	}
}
