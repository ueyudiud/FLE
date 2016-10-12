package fle.api.recipes;

public interface IRecipeInput<C, M, T>
{
	InputType getInputType();
	
	C matchInput(M matrix);
	
	void onInput(M matrix, C cache);

	boolean isValid();

	public static enum InputType
	{
		SINGLE,
		SHAPELESS,
		SHAPED,
		SPECIAL;
	}
}