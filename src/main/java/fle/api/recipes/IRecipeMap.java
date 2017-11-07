/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.Collection;

import javax.annotation.Nullable;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.IRegisteredNameable;

/**
 * The recipe maps are recipe collection, you can register, remove recipe by
 * recipe map.
 * <p>
 * For more detail of recipe registration and data store, see
 * {@link fle.api.recipes.instance.RecipeMaps}.
 * 
 * @author ueyudiud
 * @see fle.api.recipes.instance.RecipeMaps
 */
public interface IRecipeMap<R, C, H> extends INBTCompoundReaderAndWritter<C>, IRegisteredNameable
{
	/**
	 * Add a recipe to recipe map.
	 * 
	 * @param recipe the recipe.
	 * @return return true if the recipe is adding success.
	 */
	boolean addRecipe(R recipe);
	
	/**
	 * Find recipe by crafting handler, it stored by a cache, null means no
	 * recipe matched.
	 * 
	 * @param handler the crafting handler.
	 * @return the recipe.
	 */
	@Nullable
	C findRecipe(H handler);
	
	/**
	 * Get all recipes.
	 * <p>
	 * Use this method to remove recipe is not suggested, use
	 * {@link #removeRecipe(Object)} instead.
	 * 
	 * @return the recipe collection.
	 */
	Collection<R> recipes();
	
	/**
	 * Remove recipe from recipe map.
	 * 
	 * @param recipe the recipe to remove.
	 */
	default void removeRecipe(R recipe)
	{
		recipes().remove(recipe);
	}
	
	/**
	 * Remove recipe by match handler, the all recipe matched will be removed.
	 * 
	 * @param handler
	 * @throws UnsupportedOperationException when recipe map is unmodifiable or
	 *             can not remove recipe.
	 * @throws IllegalArgumentException when handler is invalid.
	 */
	default void removeRecipeByHandler(H handler)
	{
		throw new UnsupportedOperationException();
	}
}
