/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.nbt;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 * @param <T> The reading and writing target type.
 */
public interface INBTCompoundReaderAndWritter<T> extends INBTCompoundReader<T>, INBTCompoundWriter<T>, INBTReaderAndWritter<T, NBTTagCompound>
{
}
