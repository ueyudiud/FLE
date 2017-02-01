package fle.api.recipes;

import nebula.common.util.IRegisteredNameable;

public interface IRecipe extends IRegisteredNameable
{
	int getRecipeInputSize();

	IRecipeInput getInput(int index);
	
	int getRecipeOutputSize();
	
	IRecipeOutput getOutput(int index);
	
	boolean isFakeRecipe();

	boolean isValid();
}