/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.Collection;

import javax.annotation.Nullable;

import nebula.common.nbt.INBTCompoundReaderAndWritter;
import nebula.common.util.IRegisteredNameable;

/**
 * The recipe map.
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
}