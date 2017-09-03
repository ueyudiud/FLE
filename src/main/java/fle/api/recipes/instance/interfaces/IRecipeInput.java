/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.api.recipes.instance.interfaces;

/**
 * The recipe input handler,
 * use to get input of recipe.<p>
 * Used by recipe from {@link fle.api.recipes.TemplateRecipeMap} predicate.
 * 
 * @author ueyudiud
 */
public interface IRecipeInput
{
	/**
	 * Get data may used in recipe predicate.
	 * @param name the recipe data tag, use to mark what type want to get.
	 * @return the recipe data.
	 * @see fle.api.recipes.instance.RecipeMaps
	 */
	<T> T getRecipeInput(String name);
}