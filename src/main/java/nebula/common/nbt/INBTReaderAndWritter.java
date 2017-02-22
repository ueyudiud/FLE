/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.nbt;

import com.google.common.reflect.TypeToken;

import net.minecraft.nbt.NBTBase;

/**
 * @author ueyudiud
 * @param <T> The reading and writing target type.
 */
public interface INBTReaderAndWritter<T, N extends NBTBase> extends INBTReader<T, N>, INBTWriter<T, N>
{
	default Class<? super T> getTargetType()
	{
		return new TypeToken<T>(getClass()){}.getRawType();
	}
}