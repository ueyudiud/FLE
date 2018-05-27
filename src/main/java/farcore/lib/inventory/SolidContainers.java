/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import static nebula.common.inventory.IContainer.PROCESS;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.SubsolidStack;
import nebula.common.inventory.Containers;
import nebula.common.inventory.task.Task;

/**
 * @author ueyudiud
 */
public class SolidContainers<C extends ISolidContainer> extends Containers<C, SolidStack> implements ISolidContainers
{
	public SolidContainers(C...containers)
	{
		super(SolidStackHandler.SOLIDSTACK_HANDLER, containers);
	}
	
	public Task.TaskBTB taskInsertAllShaped(SubsolidStack[] stacks, int modifier)
	{
		final int m1 = modifier | PROCESS;
		return taskShaped(stacks, (c, m) -> c.taskIncr(m.getSubsolid(), m.amount, m1));
	}
	
	public Task.TaskBTB taskInsertAllShapeless(SubsolidStack[] stacks, int modifier)
	{
		final int m1 = modifier | PROCESS;
		return taskShapelessInsert(stacks, modifier, (c, m) -> m.of(m.amount - c.incrStack(m.getSubsolid(), m.amount, m1)));
	}
	
	public Task.TaskBTB taskExtractAllShaped(SubsolidStack[] stacks, int modifier)
	{
		final int m1 = modifier | PROCESS;
		return taskShaped(stacks, (c, m) -> c.taskDecr(m.getSubsolid(), m.amount, m1));
	}
	
	public Task.TaskBTB taskExtractAllShapeless(SubsolidStack[] stacks, int modifier)
	{
		final int m1 = modifier | PROCESS;
		return taskShapeless(stacks, modifier, (c, m) -> m.of(m.amount - c.decrStack(m.getSubsolid(), m.amount, m1)));
	}
}
