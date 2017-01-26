package farcore.lib.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import farcore.util.L;

@FunctionalInterface
public interface IDataChecker<T>
{
	IDataChecker TRUE     = arg -> true;
	IDataChecker FALSE    = arg -> false;
	IDataChecker NOT_NULL = arg -> arg != null;
	IDataChecker NULL     = arg -> arg == null;
	
	/**
	 * Uses for modifiable collection checker.
	 * @param collection
	 * @return
	 */
	static <T> IDataChecker<T> or(Collection<IDataChecker<T>> collection)
	{
		return target -> {
			for(IDataChecker<T> checker : collection) if(checker.isTrue(target)) return true; return false;
		};
	}
	
	boolean isTrue(T target);
	
	default <K> IDataChecker<K> from(Function<K, T> function)
	{
		return key -> isTrue(function.apply(key));
	}
	
	default IDataChecker<T> not()
	{
		return new Not(this);
	}
	
	class Not<O> implements IDataChecker<O>
	{
		private final IDataChecker<O> check;
		
		public Not(IDataChecker<O> check)
		{
			this.check = check;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			return !this.check.isTrue(target);
		}
		
		@Override
		public String toString()
		{
			return "!" + check.toString();
		}
		
		@Override
		public int hashCode()
		{
			return check.hashCode() ^ 0xFFFFFFFF;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				(obj == null || this == null) ? false :
					(!(obj instanceof Not)) ? false :
						L.equal(((Not) obj).check, check);
		}
	}
	
	class Or<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public Or(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			for (IDataChecker<O> tCondition : this.checks)
				if (tCondition.isTrue(target))
					return true;
			return false;
		}
		
		@Override
		public String toString()
		{
			return "|" + Arrays.toString(checks);
		}
		
		@Override
		public int hashCode()
		{
			return Arrays.hashCode(checks) + 31;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				(obj == null || this == null) ? false :
					(!(obj instanceof Or)) ? false :
						Arrays.equals(checks, ((Or) obj).checks);
		}
	}
	
	class Nor<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public Nor(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			for (IDataChecker<O> tCondition : this.checks)
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
	
	class And<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public And(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			for (IDataChecker<O> tCondition : this.checks)
				if (!tCondition.isTrue(target))
					return false;
			return true;
		}
		
		@Override
		public String toString()
		{
			return "&" + Arrays.toString(checks);
		}
		
		@Override
		public int hashCode()
		{
			return Arrays.hashCode(checks) + 127;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			return obj == this ? true :
				(obj == null || this == null) ? false :
					(!(obj instanceof And)) ? false :
						Arrays.equals(checks, ((And) obj).checks);
		}
	}
	
	class Nand<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public Nand(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
		@Override
		public boolean isTrue(O target)
		{
			for (IDataChecker<O> tCondition : this.checks)
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
	
	class Xor<O> implements IDataChecker<O>
	{
		private final IDataChecker<O> check1;
		private final IDataChecker<O> check2;
		
		public Xor(IDataChecker<O> check1, IDataChecker<O> check2)
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
	
	class Equal<O> implements IDataChecker<O>
	{
		private final IDataChecker<O> check1;
		private final IDataChecker<O> check2;
		
		public Equal(IDataChecker<O> check1, IDataChecker<O> check2)
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