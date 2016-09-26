package fle.api.recipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import farcore.lib.collection.IteratorList;
import fle.api.recipes.ShapedRecipeInput.ShapedRecipeCache;

public abstract class ShapedRecipeInput<T, S> implements IRecipeInput<ShapedRecipeCache, ICraftingMatrix<T>, T>
{
	public static class ShapedRecipeCache
	{
		byte state;
		int ofX;
		int ofY;

		public ShapedRecipeCache(byte state, int ofX, int ofY)
		{
			this.state = state;
			this.ofX = ofX;
			this.ofY = ofY;
		}
	}
	
	//0 Standard State.
	//1 Rotate 90 degree.
	//2 Rotate 180 degree.
	//3 Rotate 270 degree.
	//4 Flip input.
	protected final int width;
	protected final int height;
	protected boolean rotateAllowed = false;
	protected boolean udMirror = false;
	protected boolean lrMirror = false;
	protected Object[][] map;
	
	public ShapedRecipeInput(Object...objects)
	{
		try
		{
			IteratorList<Object> itr = new IteratorList(objects);
			if(!itr.hasNext())
				throw new RuntimeException("The input is empty!");
			Object arg;
			arg = itr.next();
			List<String> list;
			int width = 0;
			int height = 0;
			if(arg instanceof Boolean)
			{
				lrMirror = (Boolean) arg;
				arg = itr.next();
			}
			else if(arg instanceof Boolean[])
			{
				rotateAllowed = ((Boolean[]) arg)[0];
				udMirror = ((Boolean[]) arg)[1];
				lrMirror = ((Boolean[]) arg)[2];
				arg = itr.next();
			}
			if(arg instanceof String[])
			{
				list = Arrays.asList((String[]) arg);
				height = ((String[]) arg).length;
				width = ((String[]) arg)[0].length();
				if(height > 1)
				{
					for(int i = 1; i < height; ++i)
					{
						if(width != ((String[]) arg)[i].length())
							throw new RuntimeException("The input width is not same!");
					}
				}
			}
			else
			{
				list = new ArrayList();
				if(arg instanceof String)
				{
					do
					{
						list.add((String) arg);
						if(width == 0)
						{
							width = ((String) arg).length();
						}
						else if(width != ((String) arg).length())
							throw new RuntimeException("The input width is not same!");
						++height;
						arg = itr.next();
					}
					while(arg instanceof String);
					itr.last();
				}
				else//If no map detected, use as single input normally.
				{
					//Use less checking.
					lrMirror = false;
					udMirror = false;
					rotateAllowed = false;
					this.width = 1;
					this.height = 1;
					itr.reset();
					map = new Object[][]{new Object[]{decode(itr)}};
					return;
				}
			}
			if(width == 0 || height == 0) throw new RuntimeException("Invalid map!");
			this.width = width;
			this.height = height;
			Map<Character, S> dic = new HashMap();
			while(itr.hasNext())
			{
				Character chr = (Character) itr.next();
				S source = decode(itr);
				dic.put(chr, source);
			}
			if(height == 1)
			{
				udMirror = false;
			}
			if(width == 1)
			{
				lrMirror = false;
			}
			map = new Object[height][width];
			for(int i = 0; i < height; ++i)
			{
				String m = list.get(i);
				for(int j = 0; j < width; ++j)
				{
					char chr = m.charAt(j);
					if(chr == ' ')
					{
						map[i][j] = null;
						continue;
					}
					else if(!dic.containsKey(chr)) throw new RuntimeException("No object found in dictionary!");
					map[i][j] = dic.get(chr);
				}
			}
		}
		catch (Exception exception)
		{
			throw new RuntimeException("Fail to decode recipe, recipes : " + Arrays.deepToString(objects), exception);
		}
	}

	protected abstract S decode(IteratorList<Object> itr);

	protected abstract boolean matchInput(S arg, T target);
	
	protected abstract void onInput(int x, int y, S arg, ICraftingMatrix<T> matrix);
	
	protected S getSource(int x, int y, byte state)
	{
		if((state & 0x1) != 0)
			return getSource(y, width - x - 1, (byte) (state ^ 0x1));
		if((state & 0x2) != 0)
			return getSource(width - x - 1, height - y - 1, (byte) (state ^ 0x2));
		if((state & 0x4) != 0)
			return getSource(width - x - 1, y, (byte) (state ^ 0x4));
		return (S) map[y][x];
	}
	
	@Override
	public InputType getInputType()
	{
		return height == 1 && width == 1 ? InputType.SINGLE : InputType.SHAPED;
	}
	
	@Override
	public ShapedRecipeCache matchInput(ICraftingMatrix<T> matrix)
	{
		ShapedRecipeCache cache;
		if((cache = matchInput((byte) 0, matrix)) != null)
			return cache;
		if(lrMirror && (cache = matchInput((byte) 4, matrix)) != null)
			return cache;
		if(udMirror && (cache = matchInput((byte) 5, matrix)) != null)
			return cache;
		if(lrMirror && udMirror &&
				((cache = matchInput((byte) 6, matrix)) != null) ||
				((cache = matchInput((byte) 7, matrix)) != null))
			return cache;
		if(rotateAllowed &&
				(
						(cache = matchInput((byte) 0, matrix)) != null ||
						(cache = matchInput((byte) 1, matrix)) != null ||
						(cache = matchInput((byte) 2, matrix)) != null ||
						(cache = matchInput((byte) 3, matrix)) != null))
			return cache;
		if(rotateAllowed && (lrMirror ^ udMirror) &&
				(
						(cache = matchInput((byte) 4, matrix)) != null ||
						(cache = matchInput((byte) 5, matrix)) != null ||
						(cache = matchInput((byte) 6, matrix)) != null ||
						(cache = matchInput((byte) 7, matrix)) != null))
			return cache;
		return null;
	}
	
	protected ShapedRecipeCache matchInput(byte state, ICraftingMatrix<T> matrix)
	{
		if(matrix.getHeight() < height || matrix.getWidth() < width) return null;
		for(int j = 0; j <= matrix.getHeight() - height; ++j)
		{
			for(int i = 0; i <= matrix.getWidth() - width; ++i)
			{
				if(matchInput(i, j, state, matrix))
					return new ShapedRecipeCache(state, i, j);
			}
		}
		return null;
	}

	protected boolean matchInput(int ofX, int ofY, byte state, ICraftingMatrix<T> matrix)
	{
		for(int x = 0; x < width; ++x)
		{
			for(int y = 0; y < height; ++y)
			{
				S source = getSource(x, y, state);
				T target = matrix.get(x + ofX, y + ofY);
				if(source == null && target == null)
				{
					continue;
				}
				if((source == null && target != null) ||
						!matchInput(source, target)) return false;
			}
		}
		return true;
	}
	
	@Override
	public void onInput(ICraftingMatrix<T> matrix, ShapedRecipeCache cache)
	{
		for(int i = 0; i < width; ++i)
		{
			for(int j = 0; j <height; ++j)
			{
				onInput(cache.ofX + i, cache.ofY + j, getSource(i, j, cache.state), matrix);
			}
		}
	}
}