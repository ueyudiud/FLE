package fle.api.recipes;

public interface IRecipe
{
	int getRecipeInputSize();
	
	IRecipeInput getInput(int index);

	int getRecipeOutputSize();

	IRecipeOutput getOutput(int index);
}