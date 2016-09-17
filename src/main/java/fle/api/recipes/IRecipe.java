package fle.api.recipes;

import farcore.lib.util.IRegisteredNameable;

public interface IRecipe extends IRegisteredNameable
{
	int getRecipeInputSize();

	IRecipeInput getInput(int index);
	
	int getRecipeOutputSize();
	
	IRecipeOutput getOutput(int index);
}