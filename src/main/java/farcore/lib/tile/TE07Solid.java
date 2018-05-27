/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tile;

import static nebula.common.inventory.IContainer.FULLY;
import static nebula.common.inventory.IContainer.PROCESS;

import java.util.Map.Entry;

import farcore.lib.container.Container04Solid;
import farcore.lib.inventory.ISolidContainer;
import farcore.lib.inventory.ISolidContainers;
import farcore.lib.solid.SolidStack;
import farcore.lib.solid.container.SolidContainerHelper;
import nebula.base.MutableIntEntry;
import nebula.common.inventory.IItemContainer;
import nebula.common.inventory.task.Task;
import nebula.common.inventory.task.Task.TaskBTB;
import nebula.common.inventory.task.TaskBuilder;
import nebula.common.tile.TE06HasGui;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class TE07Solid extends TE06HasGui<Container04Solid>
{
	protected ISolidContainers solids = ISolidContainers.EMPTY;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		this.solids.writeTo(nbt, "solids");
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		this.solids.readFrom(nbt, "solids");
	}
	
	protected Task.TaskBTB provideSolidFillOrDrainTask(int itemIn, int itemOut, int solidTarget, int maxIO, boolean doFill, boolean doDrain, boolean onlyFully)
	{
		assert itemIn != itemOut;
		IItemContainer in = this.items.getContainer(itemIn);
		IItemContainer out = this.items.getContainer(itemOut);
		ISolidContainer tar = this.solids.getContainer(solidTarget);
		return n -> {
			if (in.hasStackInContainer())
			{
				ItemStack stack = in.getStackInContainer();
				if (doDrain)
				{
					SolidContainerHelper.getSolidFromItemStack(stack);
					Entry<ItemStack, SolidStack> entry = SolidContainerHelper.drainContainer(stack, maxIO);
					if (entry != null)
					{
						TaskBTB task = TaskBuilder.builder()
								.add(in.taskDecr(stack, PROCESS))
								.add(tar.taskDecr(entry.getValue(), onlyFully ? PROCESS | FULLY : PROCESS))
								.add(out.taskIncr(entry.getKey(), PROCESS))
								.asTask();
						if (task.invoke(n))
						{
							return true;
						}
					}
				}
				if (doFill)
				{
					SolidStack stack1 = tar.extractStack(maxIO, 0);
					if (stack1 != null)
					{
						MutableIntEntry<ItemStack> entry = SolidContainerHelper.fillContainer(stack, stack1);
						if (entry != null)
						{
							Task.TaskBTB task = TaskBuilder.builder()
									.add(in.taskDecr(stack, PROCESS))
									.add(tar.taskDecr(entry.getValue(), onlyFully ? PROCESS : PROCESS))
									.add(out.taskIncr(entry.getKey(), PROCESS))
									.asTask();
							if (task.invoke(n))
							{
								return true;
							}
						}
					}
				}
			}
			return false;
		};
	}
}
