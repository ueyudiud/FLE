package fle.api.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fle.api.recipes.ShapelessRecipeInput.ShapelessRecipeCache;
import nebula.common.base.ArrayIterator;
import net.minecraft.util.EnumActionResult;

public abstract class ShapelessRecipeInput<T, S> implements IRecipeInput<ShapelessRecipeCache, ICraftingMatrix<T>, T>
{
	public static class ShapelessRecipeCache
	{
		byte[] locate;
		
		ShapelessRecipeCache(byte[] locate)
		{
			this.locate = locate;
		}
	}
	
	protected List<S> inputs = new ArrayList();

	public ShapelessRecipeInput(Object...objects)
	{
		try
		{
			ArrayIterator<Object> itr = new ArrayIterator(objects);
			while(itr.hasNext())
			{
				inputs.add(decode(itr));
			}
		}
		catch(Exception exception)
		{
			throw new RuntimeException("Fail to decode recipe, recipes : " + Arrays.deepToString(objects), exception);
		}
	}

	protected abstract S decode(ArrayIterator<Object> itr);

	protected abstract EnumActionResult matchInput(S arg, T target);

	protected abstract void onInput(int id, S arg, ICraftingMatrix<T> matrix);

	@Override
	public InputType getInputType()
	{
		return inputs.size() == 1 ? InputType.SINGLE : InputType.SHAPELESS;
	}

	@Override
	public ShapelessRecipeCache matchInput(ICraftingMatrix<T> matrix)
	{
		EnumActionResult result;
		List<S> inputs1 = new ArrayList<S>(inputs);
		byte[] locate = new byte[matrix.getMatrixSize()];
		Arrays.fill(locate, (byte) -1);
		for(int i = 0; i < matrix.getMatrixSize(); ++i)
		{
			T target = matrix.get(i);
			if(target == null)
			{
				continue;
			}
			Iterator<S> itr1 = inputs1.iterator();
			while(itr1.hasNext())
			{
				if((result = matchInput(itr1.next(), target)) != EnumActionResult.PASS)
				{
					if(result == EnumActionResult.FAIL) return null;
					locate[i] = (byte) inputs.indexOf(target);
					itr1.remove();
					break;
				}
			}
		}
		return new ShapelessRecipeCache(locate);
	}

	@Override
	public void onInput(ICraftingMatrix<T> matrix, ShapelessRecipeCache cache)
	{
		for(int i = 0; i < matrix.getMatrixSize(); ++i)
		{
			if(cache.locate[i] != -1)
			{
				onInput(i, inputs.get(i), matrix);
			}
		}
	}

	@Override
	public boolean isValid()
	{
		for(S source : inputs)
		{
			if(!isValid(source)) return false;
		}
		return true;
	}
	
	protected abstract boolean isValid(S source);
}