package farcore.util;

public interface IDataChecker<T>
{
	public boolean isTrue(T target);

	public static class Not<T> implements IDataChecker<T>
	{
		private final IDataChecker<T> mCondition;
		
		public Not(IDataChecker<T> condition)
		{
			this.mCondition = condition;
	    }
	    
	    public boolean isTrue(T target)
	    {
	    	return !this.mCondition.isTrue(target);
	    }
	}
	
	public static class Or<T> implements IDataChecker<T>
	{
		private final IDataChecker<T>[] mConditions;
    
		public Or(IDataChecker<T>... conditions)
		{
			this.mConditions = conditions;
		}
    
		public boolean isTrue(T target)
		{
			for (IDataChecker<T> tCondition : this.mConditions) 
				if (tCondition.isTrue(target))
					return true;
			return false;
		}
	}
  
	public static class Nor<T> implements IDataChecker<T>
	{
		private final IDataChecker<T>[] mConditions;
    
		public Nor(IDataChecker<T>... conditions)
		{
			this.mConditions = conditions;
		}
    
		public boolean isTrue(T target)
		{
			for (IDataChecker<T> tCondition : this.mConditions)
				if (tCondition.isTrue(target)) 
					return false;
			return true;
		}
	}
  
	public static class And<T> implements IDataChecker<T>
	{
		private final IDataChecker<T>[] mConditions;
    
		public And(IDataChecker<T>...conditions)
		{
			this.mConditions = conditions;
		}
    
		public boolean isTrue(T target)
		{
			for (IDataChecker<T> tCondition : this.mConditions)
				if (!tCondition.isTrue(target))
					return false;
			return true;
		}
	}
  
	public static class Nand<T> implements IDataChecker<T>
	{
		private final IDataChecker<T>[] mConditions;
    
		public Nand(IDataChecker<T>...conditions)
		{
			this.mConditions = conditions;
		}

	    public boolean isTrue(T target)
	    {
	    	for (IDataChecker<T> tCondition : this.mConditions)
	    		if (!tCondition.isTrue(target))
			          return true;
	    	return false;
	    }
	}

	public static class Xor<T> implements IDataChecker<T>
	{
	    private final IDataChecker<T> mCondition1;
	    private final IDataChecker<T> mCondition2;
	    
	    public Xor(IDataChecker<T> aCondition1, IDataChecker<T> aCondition2)
	    {
	    	this.mCondition1 = aCondition1;
	    	this.mCondition2 = aCondition2;
	    }
	    
	    public boolean isTrue(T target)
	    {
	    	return this.mCondition1.isTrue(target) != this.mCondition2.isTrue(target);
	    }
	}
  
	public static class Equal<T> implements IDataChecker<T>
	{
		private final IDataChecker<T> mCondition1;
		private final IDataChecker<T> mCondition2;
    
		public Equal(IDataChecker<T> aCondition1, IDataChecker<T> aCondition2)
		{
			this.mCondition1 = aCondition1;
			this.mCondition2 = aCondition2;
		}
    
		public boolean isTrue(T target)
		{
			return this.mCondition1.isTrue(target) == this.mCondition2.isTrue(target);
		}
	}
	
	public static class Belong<T> implements IDataChecker<T>
	{
		private final IDataChecker<T> mCondition1;
		private final IDataChecker<T> mCondition2;
    
		public Belong(IDataChecker<T> aCondition1, IDataChecker<T> aCondition2)
		{
			this.mCondition1 = aCondition1;
			this.mCondition2 = aCondition2;
		}
    
		public boolean isTrue(T target)
		{
			return this.mCondition1.isTrue(target) ? true : this.mCondition2.isTrue(target);
		}
	}
}