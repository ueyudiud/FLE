/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import nebula.base.function.SequenceConsumer;

/**
 * The array parsing helper.
 * <p>
 * It takes some useful methods.
 * 
 * @author ueyudiud
 */
public class ObjArrayParseHelper implements Spliterator<Object>
{
	/** The read offset. */
	private int			off;
	/** The object array. */
	private Object[]	array;
	
	public static <K, V> Map<K, V> newImmutableMap(Object...objects)
	{
		return create(objects).toMap();
	}
	
	public static <E> List<E> newImmutableList(E...objects)
	{
		return create(objects).toList();
	}
	
	public static <E> ArrayList<E> newArrayList(E...objects)
	{
		return create(objects).toArrayList();
	}
	
	public static ObjArrayParseHelper create(Object...objects)
	{
		return new ObjArrayParseHelper(objects);
	}
	
	private ObjArrayParseHelper(Object[] array)
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
		if (!hasNext(classes.length)) return false;
		for (int i = 0; i < classes.length; ++i)
		{
			if (!classes[i].isInstance(this.array[i + this.off])) return false;
		}
		return true;
	}
	
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
		for (; l < r && clazz.isInstance(this.array[this.off + l]); ++l)
			;
		R[] array = (R[]) Array.newInstance(clazz, l);
		if (l == 0) return array;
		System.arraycopy(this.array, this.off, array, 0, l);
		this.off += l;
		return array;
	}
	
	public boolean readOrSkip(boolean def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Boolean ? ((Boolean) this.array[this.off++]).booleanValue() : def;
	}
	
	public int readOrSkip(int def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Integer ? ((Integer) this.array[this.off++]).intValue() : def;
	}
	
	public long readOrSkip(long def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Long || this.array[this.off] instanceof Integer ? ((Number) this.array[this.off++]).longValue() : def;
	}
	
	public float readOrSkip(float def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Number ? ((Number) this.array[this.off++]).floatValue() : def;
	}
	
	public double readOrSkip(double def)
	{
		return !hasNext() ? def : this.array[this.off] instanceof Number ? ((Double) this.array[this.off++]).doubleValue() : def;
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
		while (hasNext())
			consumer.accept(read());
	}
	
	public <I> void readToEnd(SequenceConsumer<I> consumer)
	{
		while (hasNext())
			consumer.accept(this.off, read());
	}
	
	/**
	 * Read elements to end of array.
	 * 
	 * @param consumer the consumer to accept elements.
	 * @return Return true if array is fully read.
	 */
	public <I1, I2> boolean readToEnd(@Nonnull BiConsumer<I1, I2> consumer)
	{
		while (hasNext(2))
			consumer.accept(read(), read());
		return this.array.length == this.off;
	}
	
	public <K, V> Entry<K, V> readEntry()
	{
		return new Ety<>(read(), read());
	}
	
	public <K, T, V> Entry<K, T> readEntry(@Nonnull Function<V, T> function)
	{
		return new Ety<>(read(), function.apply(read()));
	}
	
	public <K, V> boolean readEntryToEnd(@Nonnull Consumer<Entry<K, V>> consumer)
	{
		return readToEnd((K k, V v) -> consumer.accept(new Ety<>(k, v)));
	}
	
	public <R> Stack<R> readStack()
	{
		if (hasNext(2) && (this.array[this.off + 1] instanceof Integer || this.array[this.off + 1] instanceof Long))
		{
			return new Stack<>(read(), this.<Number> read().longValue());
		}
		return new Stack<>(read());
	}
	
	public <R, I> Stack<R> readStack(@Nonnull Function<I, R> function)
	{
		if (hasNext(2) && (this.array[this.off + 1] instanceof Integer || this.array[this.off + 1] instanceof Long))
		{
			return new Stack<>(function.apply(read()), this.<Number> read().longValue());
		}
		return new Stack<>(function.apply(read()));
	}
	
	public <R> void readStackToEnd(@Nonnull Consumer<Stack<R>> consumer)
	{
		while (hasNext())
			consumer.accept(readStack());
	}
	
	public <R, I> void readStackToEnd(@Nonnull Function<I, R> function, @Nonnull Consumer<Stack<R>> consumer)
	{
		while (hasNext())
			consumer.accept(readStack(function));
	}
	
	public <C, R> C collect(@Nonnull Function<R, C> start, @Nonnull BiConsumer<C, R> consumer)
	{
		if (!hasNext())
			return null;
		else
		{
			C s = start.apply(read());
			while (hasNext())
				consumer.accept(s, read());
			return s;
		}
	}
	
	public <C, R> C collect(@Nonnull Supplier<C> start, @Nonnull BiConsumer<C, R> consumer)
	{
		return collect(start.get(), consumer);
	}
	
	public <C, R> C collect(@Nonnull C start, @Nonnull BiConsumer<C, R> consumer)
	{
		while (hasNext())
			consumer.accept(start, read());
		return start;
	}
	
	public <C, R> C connect(@Nonnull Function<R, C> start, @Nonnull BiFunction<? super C, ? super R, ? extends C> function)
	{
		if (!hasNext())
			return null;
		else
		{
			C s = start.apply(read());
			while (hasNext())
				s = function.apply(s, read());
			return s;
		}
	}
	
	public <C, R> C connect(@Nonnull Supplier<C> start, @Nonnull BiFunction<? super C, ? super R, ? extends C> function)
	{
		return connect(start.get(), function);
	}
	
	public <C, R> C connect(@Nonnull C start, @Nonnull BiFunction<? super C, ? super R, ? extends C> function)
	{
		while (hasNext())
			start = function.apply(start, read());
		return start;
	}
	
	/**
	 * Copies remained values to a immutable list.
	 * 
	 * @return the immutable list.
	 */
	public <E> List<E> toList()
	{
		return (List<E>) ImmutableList.copyOf(remainArray());
	}
	
	public <E> ArrayList<E> toArrayList()
	{
		return new ArrayList<>(new ArrayListArgument<>(remainArray()));
	}
	
	public <E> Set<E> toSet()
	{
		return (Set<E>) ImmutableSet.copyOf(remainArray());
	}
	
	public <K, V> Map<K, V> toMap() throws IllegalArgumentException
	{
		ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
		if (!readToEnd((BiConsumer<K, V>) builder::put)) throw new IllegalArgumentException();
		return builder.build();
	}
	
	/**
	 * Format string with remain array by
	 * {@link String#format(String, Object...)}.
	 * 
	 * @param key the string to format.
	 * @return the formatted string.
	 * @see String#format(String, Object...)
	 */
	public String format(String key)
	{
		return String.format(key, remainArray());
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
	
	@Override
	public String toString()
	{
		return Arrays.toString(this.array);
	}
	
	@Override
	public boolean tryAdvance(Consumer<? super Object> action)
	{
		if (hasNext())
		{
			accept1(action);
			return true;
		}
		return false;
	}
	
	@Override
	public Spliterator<Object> trySplit()
	{
		return null;
	}
	
	@Override
	public long estimateSize()
	{
		return this.array.length - this.off;
	}
	
	@Override
	public int characteristics()
	{
		return SIZED;
	}
}
