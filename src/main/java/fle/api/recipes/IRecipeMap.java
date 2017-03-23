/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.Collection;

import javax.annotation.Nullable;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.IRegisteredNameable;

/**
 * The recipe collection and matcher.
 * @author ueyudiud
 */
public interface IRecipeMap<R, C, H> extends INBTCompoundReaderAndWritter<C>, IRegisteredNameable
{
	/**
	 * Add a recipe to recipe map.
	 * @param recipe
	 * @return Return true if the recipe is adding success.
	 */
	boolean addRecipe(R recipe);
	
	/**
	 * Find recipe by crafting handler, it stored by
	 * a cache, null means no recipe matched.
	 * @param handler
	 * @return
	 */
	@Nullable C findRecipe(H handler);
	
	/**
	 * Get all recipes.
	 * @return
	 */
	Collection<R> recipes();
	
	/**
	 * Remove recipe.
	 * @param recipe
	 */
	default void removeRecipe(R recipe)
	{
		recipes().remove(recipe);
	}
	
	/**
	 * Remove recipe by match handler, the all recipe matched will be removed.
	 * @param handler
	 * @throws UnsupportedOperationException when recipe map is unmodifiable or can not remove recipe.
	 * @throws IllegalArgumentException when handler is invalid.
	 */
	default void removeRecipeByHandler(H handler)
	{
		throw new UnsupportedOperationException();
	}
}