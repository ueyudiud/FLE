package fle.api.recipes;

import java.util.Collection;

import nebula.common.nbt.INBTReaderAndWritter;

public interface IRecipeMap<R, C extends IRecipeCache<? extends R>, H> extends INBTReaderAndWritter<C>
{
	C findRecipe(H handler);

	void onRecipeInput(C cache, H handler);
	
	void addRecipe(R recipe);
	
	Collection<R> getRecipes();
}