/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import nebula.Log;
import nebula.common.base.Judgable;
import nebula.common.nbt.INBTReaderAndWritter;
import nebula.common.util.L;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class TemplateRecipeMap<H> implements IRecipeMap<TemplateRecipeMap.TemplateRecipe, TemplateRecipeMap.TemplateRecipeCache<H>, H>
{
	public static <H> Builder<H> builder(String name)
	{
		return new Builder<>(name);
	}
	
	public static class Builder<H>
	{
		final String name;
		List<TemplateRecipeMap.TemplateRecipe> list = new ArrayList<>();
		List<TemplateRecipeCacheEntryHandler<H, ?>> list1 = new ArrayList<>();
		
		public Builder(String name)
		{
			this.name = name;
		}
		
		public Builder<H> setList(List<TemplateRecipeMap.TemplateRecipe> list)
		{
			this.list = list;
			return this;
		}
		
		public <D> Builder<H> addCacheEntry(String key, INBTReaderAndWritter<? super D, ?> nbtHandler)
		{
			return addCacheEntry(key, (Class<D>) nbtHandler.getTargetType(), nbtHandler);
		}
		
		public <D> Builder<H> addCacheEntry(String key, Class<D> type, INBTReaderAndWritter<? super D, ?> nbtHandler)
		{
			this.list1.add(new TemplateRecipeCacheEntryHandler<>(key, type, nbtHandler));
			return this;
		}
		
		public TemplateRecipeMap<H> build()
		{
			return new TemplateRecipeMap<>(this.name, this.list, L.cast(this.list1, TemplateRecipeCacheEntryHandler.class));
		}
	}
	
	protected static class TemplateRecipeCacheEntryHandler<H, D>
	{
		String entryName;
		Class<D> type;
		INBTReaderAndWritter<? super D, ?> nbtHandler;
		
		TemplateRecipeCacheEntryHandler(String name, Class<D> type, INBTReaderAndWritter<? super D, ?> nbtHandler)
		{
			this.entryName = name;
			this.type = type;
			this.nbtHandler = nbtHandler;
		}
	}
	
	public static class TemplateRecipeCache<H>
	{
		TemplateRecipeMap<H> map;
		/**
		 * Will not stored into NBT, but it will be exist by {@link TemplateRecipeMap#findRecipe} method.
		 */
		@Nullable
		TemplateRecipe<H> recipe;
		Object[] storeData;
		
		TemplateRecipeCache(TemplateRecipeMap<H> map, TemplateRecipe<H> recipe, Object...datas)
		{
			this.map = map;
			this.recipe = recipe;
			this.storeData = datas;
		}
		
		public <T> T get(int i)
		{
			return (T) this.storeData[i];
		}
		
		public <T> T get1(int i)
		{
			return (T) this.recipe.customDisplayData[i];
		}
	}
	
	public static class TemplateRecipe<H>
	{
		Judgable<H> judgable;
		Function<H, ?>[] dataProvider;
		Object[] customDisplayData;
		
		public TemplateRecipe(Judgable<H>[] judgables, Function<H, ?>...dataProviders)
		{
			this(Judgable.and(judgables), dataProviders);
		}
		public TemplateRecipe(Judgable<H> judgable, Function<H, ?>...dataProviders)
		{
			this.judgable = judgable;
			this.dataProvider = dataProviders;
		}
		
		public TemplateRecipe<H> setData(Object...objects)
		{
			this.customDisplayData = objects;
			return this;
		}
		
		public <T> T get(int i)
		{
			return (T) this.customDisplayData[i];
		}
	}
	
	protected final String name;
	protected final List<TemplateRecipeMap.TemplateRecipe> recipes;
	protected final TemplateRecipeCacheEntryHandler[] handlers;
	
	protected TemplateRecipeMap(String name, List<TemplateRecipeMap.TemplateRecipe> recipeList, TemplateRecipeCacheEntryHandler[] handlers)
	{
		this.name = name;
		this.recipes = recipeList;
		this.handlers = handlers;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	@Override
	public TemplateRecipeCache<H> readFromNBT(NBTTagCompound nbt)
	{
		try
		{
			Object[] store = new Object[this.handlers.length];
			for (int i = 0; i < this.handlers.length; ++i)
			{
				store[i] = this.handlers[i].nbtHandler.readFromNBT(nbt, this.handlers[i].entryName);
			}
			return new TemplateRecipeCache<>(this, null, store);
		}
		catch (Exception exception)
		{
			Log.warn("Invalid nbt {} for recipe {}", exception, nbt, this.name);
		}
		return null;
	}
	
	@Override
	public void writeToNBT(TemplateRecipeCache<H> target, NBTTagCompound nbt)
	{
		try
		{
			for (int i = 0; i < this.handlers.length; ++i)
			{
				this.handlers[i].nbtHandler.writeToNBT(target.storeData[i], nbt, this.handlers[i].entryName);
			}
		}
		catch (Exception exception)
		{
			Log.warn("Invalid recipe data {}", exception, target.storeData);
		}
	}
	
	@Override
	public boolean addRecipe(TemplateRecipeMap.TemplateRecipe recipe)
	{
		if (recipe.dataProvider.length != this.handlers.length)
		{
			return false;
		}
		this.recipes.add(recipe);
		return true;
	}
	
	@Override
	public TemplateRecipeCache<H> findRecipe(H handler)
	{
		TemplateRecipeMap.TemplateRecipe recipe = L.get(this.recipes, r->r.judgable.isTrue(handler));
		if (recipe == null) return null;
		Object[] stores = new Object[this.handlers.length];
		for (int i = 0; i < this.handlers.length; stores[i] = recipe.dataProvider[i].apply(handler), ++i);
		return new TemplateRecipeCache<>(this, recipe, stores);
	}
	
	@Override
	public Class<TemplateRecipeCache> getTargetType()
	{
		return TemplateRecipeCache.class;
	}
	
	@Override
	public final List<TemplateRecipeMap.TemplateRecipe> recipes()
	{
		return this.recipes;
	}
	
	@Override
	public void removeRecipe(TemplateRecipe recipe)
	{
		this.recipes.remove(recipe);
	}
	
	@Override
	public void removeRecipeByHandler(H handler)
	{
		this.recipes.removeIf(r->r.judgable.isTrue(handler));
	}
}