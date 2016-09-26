package fle.api.recipes;

public interface IRecipeOutput<C, M, T>
{
	OutputType getInputType();
	
	boolean matchOutput(C cache, M matrix);
	
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