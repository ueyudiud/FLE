package farcore.util;

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
	}
}