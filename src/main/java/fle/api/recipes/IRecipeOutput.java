package fle.api.recipes;

public interface IRecipeOutput<M extends ICraftingMatrix<T>, T>
{
	OutputType getInputType();

	boolean matchOutput(M matrix);

	void onOutput(M matrix);
	
	public static enum OutputType
	{
		SINGLE,
		SHAPELESS,
		SHAPED,
		SPECIAL,
		MODIFY;
	}
}