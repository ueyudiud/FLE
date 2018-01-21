/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.nbt;

import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;

import nebula.base.ArrayListConditional;
import nebula.common.util.NBTs;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

/**
 * The writer and reader.
 * 
 * @author ueyudiud
 * @param <T> The reading and writing target type.
 * @see nebula.common.nbt.INBTReader
 * @see nebula.common.nbt.INBTWriter
 */
public interface INBTReaderAndWritter<T, N extends NBTBase> extends INBTReader<T, N>, INBTWriter<T, N>
{
	/**
	 * Get type of object reader and writter handled, if this method do not has
	 * any implementation, the type will get by reflection.
	 * 
	 * @return the target type.
	 */
	default Class<? super T> getTargetType()
	{
		return new TypeToken<T>(getClass())
		{
			private static final long serialVersionUID = -7802993399663477135L;
		}.getRawType();
	}
	
	/**
	 * @see nebula.common.util.NBTs#wrapAsUnorderedArrayWriterAndReader(INBTReaderAndWritter)
	 */
	default INBTReaderAndWritter<T[], NBTTagList> getArrayReaderAndWriter()
	{
		return NBTs.wrapAsUnorderedArrayWriterAndReader(this);
	}
	
	/**
	 * Create a RAW of list contain object in type this RAW handling.
	 * 
	 * @return
	 */
	default INBTReaderAndWritter<List<T>, NBTTagList> getListReaderAndWriter()
	{
		return new INBTReaderAndWritter<List<T>, NBTTagList>()
		{
			@Override
			public List<T> readFromNBT(NBTTagList tag)
			{
				List<T> list = ArrayListConditional.requireNonnull();
				for (int i = 0; i < tag.tagCount(); ++i)
				{
					list.add(INBTReaderAndWritter.this.readFromNBT((N) list.get(i)));
				}
				return new ArrayList<>(list);
			}
			
			@Override
			public NBTTagList writeToNBT(List<T> list)
			{
				NBTTagList tag = new NBTTagList();
				for (T t : list)
				{
					tag.appendTag(INBTReaderAndWritter.this.writeToNBT(t));
				}
				return tag;
			}
		};
	}
}
