package nebula.common.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import nebula.common.util.L;

@FunctionalInterface
public interface Judgable<T>
{
	Judgable TRUE     = arg -> true;
	Judgable FALSE    = arg -> false;
	Judgable NOT_NULL = arg -> arg != null;
	Judgable NULL     = arg -> arg == null;
	
	static <T> Judgable<T> or(Judgable<T>...checkers) { return or(Arrays.asList(checkers)); }
	
	static <T> Judgable<T> and(Judgable<T>...checkers) { return and(Arrays.asList(checkers)); }
	
	/**
	 * Uses for modifiable collection checker.
	 * @param collection
	 * @return
	 */
	static <T> Judgable<T> or(Collection<Judgable<T>> collection)
	{
		return target -> {
			for(Judgable<T> checker : collection) if(checker.isTrue(target)) return true; return false;
		};
	}
	
	static <T> Judgable<T> and(Collection<Judgable<T>> collection)
	{
		return target -> {
			for(Judgable<T> checker : collection) if(!checker.isTrue(target)) return false; return true;
		};
	}
	
	boolean isTrue(T target);
	
	default <K> Judgable<K> from(Function<K, T> function)
	{
		return key -> isTrue(function.apply(key));
	}
	
	default Judgable<T> not()
	{
		return target -> !isTrue(target);
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
			return "^" + check1.toString() + "~" + check2.toString();
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
			return "=" + check1.toString() + "~" + check2.toString();
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
					(!(obj instanceof Equal)) ? false :
						(L.equal(((Equal) obj).check1, check1) && L.equal(((Equal) obj).check2, check2)) ||
						(L.equal(((Equal) obj).check1, check2) && L.equal(((Equal) obj).check2, check1));
		}
	}
}