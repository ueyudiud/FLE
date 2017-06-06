/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.base;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import nebula.common.util.A;

/**
 * @author ueyudiud
 */
public class ObjArrayParseHelper
{
	/** The read offset. */
	private int off;
	/** The object array. */
	private Object[] array;
	
	public static ObjArrayParseHelper create(Object...objects)
	{
		return new ObjArrayParseHelper(objects);
	}
	
	ObjArrayParseHelper(Object[] array)
	{
		this.array = array;
	}
	
	public boolean hasNext()
	{
		return this.off < this.array.length;
	}
	
	public boolean hasNext(int len)
	{
		return this.off + len - 1 < this.array.length;
	}
	
	public boolean match(Class<?> clazz)
	{
		return hasNext() && clazz.isInstance(this.array[this.off]);
	}
	
	public boolean match(Class<?>...classes)
	{
		if (!hasNext(classes.length))
			return false;
		for (int i = 0; i < classes.length; ++i)
		{
			if (!classes[i].isInstance(this.array[i + this.off]))
				return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public <I> boolean match(Predicate<I> predicate)
	{
		return predicate.test((I) this.array[this.off]);
	}
	
	public <R> R[] readArrayOrCompact(Class<R> clazz)
	{
		if (match(Array.newInstance(clazz, 0).getClass()))
		{
			return read();
		}
		else
		{
			return readArray(clazz);
		}
	}
	
	public <R> R[] readArray(Class<R> clazz)
	{
		if (this.off >= this.array.length) return (R[]) Array.newInstance(clazz, 0);
		int r = this.array.length - this.off;
		int l = 0;
		for (; l < r && clazz.isInstance(this.array[this.off + l]); ++l);
		R[] array = (R[]) Array.newInstance(clazz, l);
		if (l == 0) return array;
		System.arraycopy(this.array, this.off, array, 0, l);
		this.off += l;
		return array;
	}
	
	public boolean readOrSkip(boolean def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Boolean ?
				((Boolean) this.array[this.off++]).booleanValue() : def;
	}
	
	public int readOrSkip(int def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Integer ?
				((Integer) this.array[this.off++]).intValue() : def;
	}
	
	public long readOrSkip(long def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Long ||
				this.array[this.off] instanceof Integer?
						((Number) this.array[this.off++]).longValue() : def;
	}
	
	public float readOrSkip(float def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Number ?
				((Number) this.array[this.off++]).floatValue() : def;
	}
	
	public double readOrSkip(double def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Number ?
				((Double) this.array[this.off++]).doubleValue() : def;
	}
	
	public <T> T readOrSkip(Class<T> clazz)
	{
		return readOrSkip(clazz, null);
	}
	
	public <T> T readOrSkip(Class<T> clazz, T def)
	{
		return !hasNext() ? def : clazz.isInstance(this.array[this.off]) ? read() : def;
	}
	
	public <R> R read()
	{
		return (R) this.array[this.off++];
	}
	
	public <R, I> R read1(Function<I, R> function)
	{
		return function.apply(read());
	}
	
	public <R, I1, I2> R read2(BiFunction<I1, I2, R> function)
	{
		return function.apply(read(), read());
	}
	
	public <I> void accept1(Consumer<I> consumer)
	{
		consumer.accept(read());
	}
	
	public <I1, I2> void accept2(BiConsumer<I1, I2> consumer)
	{
		consumer.accept(read(), read());
	}
	
	public <I> void readToEnd(Consumer<I> consumer)
	{
		while (hasNext()) consumer.accept(read());
	}
	
	/**
	 * 
	 * @param consumer
	 * @return Return true if array is fully read.
	 */
	public <I1, I2> boolean readToEnd(BiConsumer<I1, I2> consumer)
	{
		while (hasNext(2)) consumer.accept(read(), read());
		return this.array.length == this.off;
	}
	
	public <K, V> Entry<K, V> readEntry()
	{
		return new Ety<>(read(), read());
	}
	
	public <K, T, V> Entry<K, T> readEntry(Function<V, T> function)
	{
		return new Ety<>(read(), function.apply(read()));
	}
	
	public <K, V> boolean readEntryToEnd(Consumer<Entry<K, V>> consumer)
	{
		return readToEnd((K k, V v)-> consumer.accept(new Ety<>(k, v)));
	}
	
	public <R> Stack<R> readStack()
	{
		if (hasNext(2) && (this.array[this.off + 1] instanceof Integer ||
				this.array[this.off + 1] instanceof Long))
		{
			return new Stack<>(read(), this.<Number>read().longValue());
		}
		return new Stack<>(read());
	}
	
	public <R, I> Stack<R> readStack(Function<I, R> function)
	{
		if (hasNext(2) && (this.array[this.off + 1] instanceof Integer ||
				this.array[this.off + 1] instanceof Long))
		{
			return new Stack<>(function.apply(read()), this.<Number>read().longValue());
		}
		return new Stack<>(function.apply(read()));
	}
	
	public <R> void readStackToEnd(Consumer<Stack<R>> consumer)
	{
		while (hasNext()) consumer.accept(readStack());
	}
	
	public <R, I> void readStackToEnd(Function<I, R> function, Consumer<Stack<R>> consumer)
	{
		while (hasNext()) consumer.accept(readStack(function));
	}
	
	public <E> List<E> toList()
	{
		return (List<E>) Arrays.asList(remainArray());
	}
	
	public <E> Set<E> toSet()
	{
		return (Set<E>) ImmutableSet.copyOf(remainArray());
	}
	
	public <K, V> Map<K, V> toMap() throws IllegalArgumentException
	{
		ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
		if (!readToEnd((BiConsumer<K, V>) builder::put))
			throw new IllegalArgumentException();
		return builder.build();
	}
	
	public Object[] remainArray()
	{
		return this.off == 0 ? this.array : A.sublist(this.array, this.off);
	}
	
	public Object[] array()
	{
		return this.array;
	}
	
	public void reset()
	{
		this.off = 0;
	}
}