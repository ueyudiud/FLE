package fle.api.recipes;

public interface ICraftingMatrix<T>
{
	default int getMatrixSize()
	{
		return getWidth() * getHeight();
	}

	int getWidth();

	default int getHeight()
	{
		return 1;
	}
	
	T get(int index);

	default T get(int x, int y)
	{
		return x < 0 || y < 0 || x >= getWidth() || y >= getHeight() ?
				null : get(y * getWidth() + x);
	}
	
	boolean set(int index, T target);

	default boolean set(int x, int y, T target)
	{
		if(x < 0 || y < 0 || x >= getWidth() || y >= getHeight()) return false;
		return set(y * getWidth() + x, target);
	}
}