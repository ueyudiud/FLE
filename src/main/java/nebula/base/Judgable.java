/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import nebula.common.util.A;
import nebula.common.util.L;

@FunctionalInterface
public interface Judgable<T> extends Predicate<T>
{
	/** The OTB function always return <tt>true</tt>. */
	@SuppressWarnings("rawtypes")
	Judgable TRUE     = arg -> true;
	/** The OTB function always return <tt>false</tt>. */
	@SuppressWarnings("rawtypes")
	Judgable FALSE    = arg -> false;
	/** The OTB function to check if input are NON-NULL. */
	@SuppressWarnings("rawtypes")
	Judgable NOT_NULL = Objects::nonNull;
	/** The OTB function to check if input are NULL. */
	@SuppressWarnings("rawtypes")
	Judgable NULL     = Objects::isNull;
	
	static <T> Judgable<T> fromPredicate(Predicate<? super T> predicate) { return predicate::test; }
	
	static <T> Judgable<T> or(Predicate<? super T>...checkers) { return t->A.or(checkers, j->j.test(t)); }
	
	static <T> Judgable<T> and(Predicate<? super T>...checkers) { return t->A.and(checkers, j->j.test(t)); }
	
	static <R, T extends R> Judgable<R> matchAndCast(Predicate<? super T> predicate, Class<T> cast) { return r->cast.isInstance(r) ? predicate.test((T) r) : false; }
	
	/**
	 * Uses for modifiable collection checker.
	 * @param collection
	 * @return
	 */
	static <T> Judgable<T> or(Collection<? extends Predicate<? super T>> collection)
	{
		return target -> { for(Predicate<? super T> checker : collection) if(checker.test(target)) return true; return false; };
	}
	
	/**
	 * Uses for modifiable collection checker.
	 * @param collection
	 * @return
	 */
	static <T> Judgable<T> and(Collection<? extends Predicate<? super T>> collection)
	{
		return target -> { for(Predicate<? super T> checker : collection) if(!checker.test(target)) return false; return true; };
	}
	
	@Override
	default boolean test(T t)
	{
		return isTrue(t);
	}
	
	boolean isTrue(T target);
	
	@Override
	default Predicate<T> or(Predicate<? super T> other)
	{
		return or(this, other);
	}
	
	@Override
	default Predicate<T> and(Predicate<? super T> other)
	{
		return and(this, other);
	}
	
	default <K> Judgable<K> from(Function<K, T> function)
	{
		return key -> isTrue(function.apply(key));
	}
	
	default Judgable<T> not()
	{
		return target -> !isTrue(target);
	}
	
	@Override
	default Judgable<T> negate()
	{
		return not();
	}
	
	class Nor<O> implements Judgable<O>
	{
		private final Judgable<O>[] checks;
		
		public Nor(Judgable<O>... checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			for (Judgable<O> tCondition : this.checks)
				if (tCondition.isTrue(target))
					return false;
			return true;
		}
		
		@Override
		public String toString()
		{
			return "!|" + Arrays.toString(checks);
		}
		
		@Override
		public int hashCode()
		{
			return Arrays.hashCode(checks) + 63;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				(obj == null || this == null) ? false :
					(!(obj instanceof Nor)) ? false :
						Arrays.equals(checks, ((Nor) obj).checks);
		}
	}
	
	class Nand<O> implements Judgable<O>
	{
		private final Judgable<O>[] checks;
		
		public Nand(Judgable<O>... checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			for (Judgable<O> tCondition : this.checks)
				if (!tCondition.isTrue(target))
					return true;
			return false;
		}
		
		@Override
		public String toString()
		{
			return "!&" + Arrays.toString(checks);
		}
		
		@Override
		public int hashCode()
		{
			return Arrays.hashCode(checks) + 255;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				(obj == null || this == null) ? false :
					(!(obj instanceof Nand)) ? false :
						Arrays.equals(checks, ((Nand) obj).checks);
		}
	}
	
	class Xor<O> implements Judgable<O>
	{
		private final Judgable<O> check1;
		private final Judgable<O> check2;
		
		public Xor(Judgable<O> check1, Judgable<O> check2)
		{
			this.check1 = check1;
			this.check2 = check2;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			return this.check1.isTrue(target) != this.check2.isTrue(target);
		}
		
		@Override
		public String toString()
		{
			return "=" + check1.toString() + "^" + check2.toString();
		}
		
		@Override
		public int hashCode()
		{
			return check1.hashCode() + check2.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				(obj == null || this == null) ? false :
					(!(obj instanceof Xor)) ? false :
						(L.equal(((Xor) obj).check1, check1) && L.equal(((Xor) obj).check2, check2)) ||
						(L.equal(((Xor) obj).check1, check2) && L.equal(((Xor) obj).check2, check1));
		}
	}
	
	class Equal<O> implements Judgable<O>
	{
		private final Judgable<O> check1;
		private final Judgable<O> check2;
		
		public Equal(Judgable<O> check1, Judgable<O> check2)
		{
			this.check1 = check1;
			this.check2 = check2;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			return this.check1.isTrue(target) == this.check2.isTrue(target);
		}
		
		@Override
		public String toString()
		{
			return "=" + check1.toString() + "==" + check2.toString();
		}
		
		@Override
		public int hashCode()
		{
			return check1.hashCode() ^ check2.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				(obj == null || this == null) ? false :
					(!(obj instanceof Equal)) ? false :
						(L.equal(((Equal) obj).check1, check1) && L.equal(((Equal) obj).check2, check2)) ||
						(L.equal(((Equal) obj).check1, check2) && L.equal(((Equal) obj).check2, check1));
		}
	}
}