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

@FunctionalInterface
public interface Judgable<T> extends Predicate<T>
{
	/** The OTB function always return <tt>true</tt>. */
	Judgable	TRUE		= arg -> true;
	/** The OTB function always return <tt>false</tt>. */
	Judgable	FALSE		= arg -> false;
	/** The OTB function to check if input are NON-NULL. */
	Judgable	NOT_NULL	= Objects::nonNull;
	/** The OTB function to check if input are NULL. */
	Judgable	NULL		= Objects::isNull;
	
	static <T> Judgable<T> fromPredicate(Predicate<? super T> predicate)
	{
		return predicate::test;
	}
	
	static <T> Judgable<T> or(Predicate<? super T>...checkers)
	{
		return t -> A.or(checkers, j -> j.test(t));
	}
	
	static <T> Judgable<T> and(Predicate<? super T>...checkers)
	{
		return t -> A.and(checkers, j -> j.test(t));
	}
	
	static <R, T extends R> Judgable<R> matchAndCast(Predicate<? super T> predicate, Class<T> cast)
	{
		return r -> cast.isInstance(r) ? predicate.test((T) r) : false;
	}
	
	/**
	 * Uses for modifiable collection checker.
	 * 
	 * @param collection
	 * @return
	 */
	static <T> Judgable<T> or(Collection<? extends Predicate<? super T>> collection)
	{
		return target -> {
			for (Predicate<? super T> checker : collection)
				if (checker.test(target))
					return true;
			return false;
		};
	}
	
	/**
	 * Uses for modifiable collection checker.
	 * 
	 * @param collection
	 * @return
	 */
	static <T> Judgable<T> and(Collection<? extends Predicate<? super T>> collection)
	{
		return target -> {
			for (Predicate<? super T> checker : collection)
				if (!checker.test(target)) return false;
			return true;
		};
	}
	
	boolean test(T t);
	
	@Deprecated
	default boolean isTrue(T target)
	{
		return test(target);
	}
	
	@Override
	default Judgable<T> or(Predicate<? super T> other)
	{
		return or(this, other);
	}
	
	@Override
	default Judgable<T> and(Predicate<? super T> other)
	{
		return and(this, other);
	}
	
	default <K> Judgable<K> from(Function<K, ? extends T> function)
	{
		return key -> test(function.apply(key));
	}
	
	default Judgable<T> not()
	{
		return new Not<>(this);
	}
	
	@Override
	default Judgable<T> negate()
	{
		return not();
	}
	
	class Nor<O> implements Judgable<O>
	{
		private final Judgable<O>[] checks;
		
		public Nor(Judgable<O>...checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean test(O target)
		{
			for (Judgable<O> tCondition : this.checks)
				if (tCondition.test(target)) return false;
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
			return obj == this ? true : (obj == null || this == null) ? false : (!(obj instanceof Nor)) ? false : Arrays.equals(checks, ((Nor) obj).checks);
		}
	}
	
	class Nand<O> implements Judgable<O>
	{
		private final Judgable<O>[] checks;
		
		public Nand(Judgable<O>...checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean test(O target)
		{
			for (Judgable<O> tCondition : this.checks)
				if (!tCondition.test(target)) return true;
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
			return obj == this ? true : (obj == null || this == null) ? false : (!(obj instanceof Nand)) ? false : Arrays.equals(checks, ((Nand) obj).checks);
		}
	}
	
	class Xor<O> implements Judgable<O>
	{
		private final Judgable<O>	check1;
		private final Judgable<O>	check2;
		
		public Xor(Judgable<O> check1, Judgable<O> check2)
		{
			this.check1 = Objects.requireNonNull(check1);
			this.check2 = Objects.requireNonNull(check2);
		}
		
		@Override
		public boolean test(O target)
		{
			return this.check1.test(target) != this.check2.test(target);
		}
		
		@Override
		public String toString()
		{
			return check1.toString() + "!=" + check2.toString();
		}
		
		@Override
		public int hashCode()
		{
			return check1.hashCode() ^ check2.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true : obj instanceof Xor && (check1.equals(((Xor) obj).check1) && check2.equals(((Xor) obj).check2)) || (check2.equals(((Xor) obj).check1) && check1.equals(((Xor) obj).check2));
		}
	}
	
	class Equal<O> implements Judgable<O>
	{
		private final Judgable<O>	check1;
		private final Judgable<O>	check2;
		
		public Equal(Judgable<O> check1, Judgable<O> check2)
		{
			this.check1 = Objects.requireNonNull(check1);
			this.check2 = Objects.requireNonNull(check2);
		}
		
		@Override
		public boolean test(O target)
		{
			return this.check1.test(target) == this.check2.test(target);
		}
		
		@Override
		public String toString()
		{
			return check1.toString() + "==" + check2.toString();
		}
		
		@Override
		public int hashCode()
		{
			return check1.hashCode() ^ check2.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true : obj instanceof Equal && (check1.equals(((Equal) obj).check1) && check2.equals(((Equal) obj).check2)) || (check1.equals(((Equal) obj).check2) && check2.equals(((Equal) obj).check1));
		}
	}
	
	class Not<O> implements Judgable<O>
	{
		private final Judgable<O>	check;
		
		public Not(Judgable<O> check)
		{
			this.check = Objects.requireNonNull(check);
		}
		
		@Override
		public boolean test(O target)
		{
			return !check.test(target);
		}
		
		@Override
		public String toString()
		{
			return "!" + check.toString();
		}
		
		@Override
		public int hashCode()
		{
			return check.hashCode() ^ -295759375;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true : obj instanceof Not && check.equals(((Not) obj).check);
		}
	}
}
