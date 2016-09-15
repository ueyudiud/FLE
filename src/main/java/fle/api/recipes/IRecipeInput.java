package fle.api.recipes;

public interface IRecipeInput<M extends ICraftingMatrix<T>, T>
{
	InputType getInputType();
	
	boolean matchInput(M matrix);
	
	void onInput(M matrix);

	public static enum InputType
	{
		SINGLE,
		SHAPELESS,
		SHAPED,
		SPECIAL;
	}
}