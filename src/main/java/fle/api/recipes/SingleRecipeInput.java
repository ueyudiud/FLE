package fle.api.recipes;

import fle.api.recipes.SingleRecipeInput.SingleRecipeInputCache;

public abstract class SingleRecipeInput<S, M extends ICraftingMatrix<T>, T> implements IRecipeInput<SingleRecipeInputCache, M, T>
{
	public static class SingleRecipeInputCache
	{
		int id;

		public SingleRecipeInputCache(int id)
		{
			this.id = id;
		}
	}

	protected final S source;

	public SingleRecipeInput(Object...objects)
	{
		source = decode(objects);
	}

	@Override
	public final InputType getInputType()
	{
		return InputType.SINGLE;
	}
	
	protected abstract S decode(Object...objects);
	
	protected abstract boolean match(T target);
	
	protected abstract void onInput(int id, M matrix);

	@Override
	public final SingleRecipeInputCache matchInput(M matrix)
	{
		int id = -1;
		for(int i = 0; i < matrix.getMatrixSize(); ++i)
		{
			T target = matrix.get(i);
			if(target != null)
			{
				if(id != -1) return null;
				if(!match(target)) return null;
				id = i;
			}
		}
		return id != -1 ? new SingleRecipeInputCache(id) : null;
	}

	@Override
	public final void onInput(M matrix, SingleRecipeInputCache cache)
	{
		onInput(cache.id, matrix);
	}
}