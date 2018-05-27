/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.inventory;

import javax.annotation.ParametersAreNonnullByDefault;

import farcore.lib.solid.SolidStack;
import farcore.lib.solid.SubsolidStack;
import nebula.common.inventory.IContainers;
import nebula.common.inventory.task.Task;
import nebula.common.nbt.INBTSelfCompoundReaderAndWriter;
import nebula.common.nbt.NBTs;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
@ParametersAreNonnullByDefault
public interface ISolidContainers extends IContainers<SolidStack>, INBTSelfCompoundReaderAndWriter
{
	ISolidContainers EMPTY = new SolidContainersEmpty();
	
	@Override
	ISolidContainer[] getContainers();
	
	@Override
	ISolidContainer getContainer(int index);
	
	@Override
	default void writeTo(NBTTagCompound nbt)
	{
		NBTs.set(nbt, "data", getContainers());
	}
	
	@Override
	default NBTTagCompound writeTo(NBTTagCompound nbt, String key)
	{
		NBTs.set(nbt, key, getContainers());
		return nbt;
	}
	
	@Override
	default void readFrom(NBTTagCompound nbt)
	{
		NBTs.get(nbt, "data", getContainers());
	}
	
	@Override
	default void readFrom(NBTTagCompound nbt, String key)
	{
		NBTs.get(nbt, key, getContainers());
	}
	
	Task.TaskBTB taskInsertAllShaped(SubsolidStack[] stacks, int modifier);
	
	Task.TaskBTB taskInsertAllShapeless(SubsolidStack[] stacks, int modifier);
	
	Task.TaskBTB taskExtractAllShaped(SubsolidStack[] stacks, int modifier);
	
	Task.TaskBTB taskExtractAllShapeless(SubsolidStack[] stacks, int modifier);
}
