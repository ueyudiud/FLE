/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.nbt;

import java.util.ArrayList;
import java.util.List;

import com.google.common.reflect.TypeToken;

import nebula.base.ArrayListAddWithCheck;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

/**
 * @author ueyudiud
 * @param <T> The reading and writing target type.
 */
public interface INBTReaderAndWritter<T, N extends NBTBase> extends INBTReader<T, N>, INBTWriter<T, N>
{
	default Class<? super T> getTargetType()
	{
		return new TypeToken<T>(getClass())
		{
			private static final long serialVersionUID = -7802993399663477135L;
		}.getRawType();
	}
	
	default INBTReaderAndWritter<List<T>, NBTTagList> getListReaderAndWriter()
	{
		return new INBTReaderAndWritter<List<T>, NBTTagList>()
		{
			@Override
			public List<T> readFromNBT(NBTTagList tag)
			{
				List<T> list = ArrayListAddWithCheck.requireNonnull();
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