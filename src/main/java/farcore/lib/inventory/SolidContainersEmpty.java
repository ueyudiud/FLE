/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.SubsolidStack;
import nebula.common.inventory.ContainersEmpty;
import nebula.common.inventory.task.Task;
import nebula.common.inventory.task.Task.TaskBTB;

/**
 * @author ueyudiud
 */
final class SolidContainersEmpty extends ContainersEmpty<SolidStack> implements ISolidContainers
{
	@Override
	public ISolidContainer getContainer(int index)
	{
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public ISolidContainer[] getContainers()
	{
		return new ISolidContainer[0];
	}
	
	private boolean access(Object[] values) { return values.length == 0; }
	private Task.TaskBTB task(Object[] values) { return access(values) ? Task.pass() : Task.fail(); }
	
	@Override
	public TaskBTB taskInsertAllShaped(SubsolidStack[] stacks, int modifier)
	{
		return task(stacks);
	}
	
	@Override
	public TaskBTB taskInsertAllShapeless(SubsolidStack[] stacks, int modifier)
	{
		return task(stacks);
	}
	
	@Override
	public TaskBTB taskExtractAllShaped(SubsolidStack[] stacks, int modifier)
	{
		return task(stacks);
	}
	
	@Override
	public TaskBTB taskExtractAllShapeless(SubsolidStack[] stacks, int modifier)
	{
		return task(stacks);
	}
}
