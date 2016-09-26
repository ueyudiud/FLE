package fle.api.recipes;

import fle.api.recipes.TemplateRecipeMap.RecipeCacheWraper;

public abstract class TemplateRecipeMap<R extends IRecipe> extends TemplateRecipeMapBase<R, RecipeCacheWraper<R>, ICraftingHandler>
{
	public static class RecipeCacheWraper<R extends IRecipe> implements IRecipeCache<R>
	{
		R recipe;
		Object[] caches;

		public RecipeCacheWraper(R recipe, Object...caches)
		{
			this.recipe = recipe;
			this.caches = caches;
		}

		@Override
		public R recipe()
		{
			return recipe;
		}
	}

	protected final int[][] matchingLocates;
	
	public TemplateRecipeMap(int[][] locates)
	{
		this.matchingLocates = locates;
	}
	
	@Override
	protected RecipeCacheWraper matchMatrixs(R recipe, ICraftingHandler handler)
	{
		Object[] caches = new Object[matchingLocates.length];
		for(int i = 0; i < matchingLocates.length; ++i)
		{
			int[] locate = matchingLocates[i];
			Object cache = recipe.getInput(locate[0]).matchInput(handler.getInputMatrix(locate[1]));
			if(cache != null)
			{
				caches[i] = cache;
			}
			else
				return null;
		}
		return new RecipeCacheWraper(recipe, caches);
	}

	@Override
	public void onRecipeInput(RecipeCacheWraper<R> wraper, ICraftingHandler handler)
	{
		R recipe = wraper.recipe;
		Object[] inputCaches = wraper.caches;
		for(int i = 0; i < recipe.getRecipeInputSize(); ++i)
		{
			recipe.getInput(i).onInput(handler.getInputMatrix(i), inputCaches[i]);
		}
	}
}