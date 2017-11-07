/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.base.function;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author ueyudiud
 */
class ApplicableCached<T> implements Applicable<T>
{
	Supplier<? extends T>	supplier;
	Optional<T>				cache;
	
	ApplicableCached(Supplier<? extends T> supplier)
	{
		this.supplier = supplier;
	}
	
	private void apply$1()
	{
		if (this.cache == null)
		{
			this.cache = Optional.ofNullable(this.supplier.get());
		}
	}
	
	@Override
	public T apply()
	{
		apply$1();
		return this.cache.isPresent() ? this.cache.get() : null;
	}
	
	@Override
	public Optional<T> applyOptional()
	{
		apply$1();
		return this.cache;
	}
}
