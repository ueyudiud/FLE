package farcore.util;

import java.util.Arrays;

public interface IDataChecker<T>
{
	boolean isTrue(T target);

	public static class Not<O> implements IDataChecker<O>
	{
		private final IDataChecker<O> check;
		
		public Not(IDataChecker<O> check)
		{
			this.check = check;
		}
		
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
						U.Lang.equal(((Not) obj).check, check);
		}
	}
	
	public static class Or<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public Or(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
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
	
	public static class Nor<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public Nor(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
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
	
	public static class And<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public And(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
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
	
	public static class Nand<O> implements IDataChecker<O>
	{
		private final IDataChecker<O>[] checks;
		
		public Nand(IDataChecker<O>... checks)
		{
			this.checks = checks;
		}
		
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
	
	public static class Xor<O> implements IDataChecker<O>
	{
		private final IDataChecker<O> check1;
		private final IDataChecker<O> check2;
		
		public Xor(IDataChecker<O> check1, IDataChecker<O> check2)
		{
			this.check1 = check1;
			this.check2 = check2;
		}
		
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
						(U.Lang.equal(((Xor) obj).check1, check1) && U.Lang.equal(((Xor) obj).check2, check2)) ||
						(U.Lang.equal(((Xor) obj).check1, check2) && U.Lang.equal(((Xor) obj).check2, check1));
		}
	}
	
	public static class Equal<O> implements IDataChecker<O>
	{
		private final IDataChecker<O> check1;
		private final IDataChecker<O> check2;
		
		public Equal(IDataChecker<O> check1, IDataChecker<O> check2)
		{
			this.check1 = check1;
			this.check2 = check2;
		}
		
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
						(U.Lang.equal(((Equal) obj).check1, check1) && U.Lang.equal(((Equal) obj).check2, check2)) ||
						(U.Lang.equal(((Equal) obj).check1, check2) && U.Lang.equal(((Equal) obj).check2, check1));
		}
	}
}