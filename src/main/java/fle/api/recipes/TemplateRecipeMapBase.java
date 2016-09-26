package fle.api.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.nbt.NBTTagCompound;

public abstract class TemplateRecipeMapBase<R extends IRecipe, C extends IRecipeCache<R>, H> implements IRecipeMap<R, C, H>
{
	private static final List<TemplateRecipeMapBase> RECIPEMAP = new ArrayList();

	public static void reloadRecipeMaps()
	{
		for(TemplateRecipeMapBase map : RECIPEMAP)
		{
			map.reloadMap();
		}
	}
	
	private List<R> recipeUnbaked = new ArrayList();
	private Map<String, R> recipemap;
	private Map<R, String> nameToRecipe;
	private Collection<R> recipeUnmodify = Collections.unmodifiableCollection(recipeUnbaked);

	public TemplateRecipeMapBase()
	{
		RECIPEMAP.add(this);
	}

	protected void reloadMap()
	{
		ImmutableMap.Builder<String, R> builder1 = ImmutableMap.builder();
		ImmutableMap.Builder<R, String> builder2 = ImmutableMap.builder();
		for(R recipe : recipeUnbaked)
		{
			if(recipe.isValid() && !recipe.isFakeRecipe())
			{
				builder1.put(recipe.getRegisteredName(), recipe);
				builder2.put(recipe, recipe.getRegisteredName());
			}
		}
		recipemap = builder1.build();
		nameToRecipe = builder2.build();
	}
	
	protected abstract C readConfigFromNBT(NBTTagCompound nbt, R recipe);

	protected abstract void writeConfigToNBT(NBTTagCompound nbt, C cache);

	@Override
	public C readFromNBT(NBTTagCompound nbt)
	{
		R recipe = recipemap.get(nbt.getString("name"));
		if(recipe != null)
			return readConfigFromNBT(nbt, recipe);
		return null;
	}
	
	@Override
	public void writeToNBT(C recipecache, NBTTagCompound nbt)
	{
		if(recipecache == null || recipecache.recipe() == null)
			return;
		nbt.setString("name", nameToRecipe.get(recipecache.recipe()));
		writeConfigToNBT(nbt, recipecache);
	}

	@Override
	public C findRecipe(H handler)
	{
		C cache;
		for(R recipe : recipeUnmodify)
		{
			if((cache = matchMatrixs(recipe, handler)) != null)
				return cache;
		}
		return null;
	}

	protected abstract C matchMatrixs(R recipe, H handler);
	
	@Override
	public void addRecipe(R recipe)
	{
		recipeUnbaked.add(recipe);
	}

	@Override
	public Collection<R> getRecipes()
	{
		return recipeUnmodify;
	}
}