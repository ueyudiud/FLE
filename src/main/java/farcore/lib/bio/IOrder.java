/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.bio;

import java.util.Collection;
import java.util.Iterator;

import javax.annotation.Nullable;

import nebula.common.nbt.INBTCompoundReaderAndWriter;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public interface IOrder<F extends IFamily<? extends S>, S extends ISpecie>
extends INBTCompoundReaderAndWriter<BioData>, Iterable<S>
{
	@Nullable F getFamily(String name);
	
	@Nullable S getSpecie(String name);
	
	Collection<? extends F> getFamilies();
	
	@Override
	default Iterator<S> iterator()
	{
		return getFamilies().stream().<S> flatMap(f -> f.getSpecies().stream()).iterator();
	}
	
	@Override
	default void writeTo(BioData data, NBTTagCompound nbt)
	{
		data.writeTo(nbt);
	}
	
	@Override
	default BioData readFrom(NBTTagCompound nbt)
	{
		BioData data = new BioData(this);
		data.readFrom(nbt);
		return data;
	}
	
	@Override
	default Class<BioData> type()
	{
		return BioData.class;
	}
}
