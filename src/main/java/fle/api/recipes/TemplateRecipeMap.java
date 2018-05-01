/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.api.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Nullable;

import nebula.Log;
import nebula.base.function.F;
import nebula.base.function.Judgable;
import nebula.common.nbt.INBTReaderAndWriter;
import nebula.common.util.L;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ueyudiud
 */
public class TemplateRecipeMap<H> implements IRecipeMap<TemplateRecipeMap.TemplateRecipe<H>, TemplateRecipeMap.TemplateRecipeCache<H>, H>
{
	public static <H> Builder<H> builder(String name)
	{
		return new Builder<>(name);
	}
	
	public static class Builder<H>
	{
		final String								name;
		List<TemplateRecipeMap.TemplateRecipe<H>>	list	= new ArrayList<>();
		List<TemplateRecipeCacheEntryHandler<H, ?>>	list1	= new ArrayList<>();
		
		public Builder(String name)
		{
			this.name = name;
		}
		
		public Builder<H> setList(List<TemplateRecipeMap.TemplateRecipe<H>> list)
		{
			this.list = list;
			return this;
		}
		
		public <D> Builder<H> addCacheEntry(String key, INBTReaderAndWriter<? super D, ?> nbtHandler)
		{
			return addCacheEntry(key, (Class<D>) nbtHandler.type(), nbtHandler);
		}
		
		public <D> Builder<H> addCacheEntry(String key, Class<D> type, INBTReaderAndWriter<? super D, ?> nbtHandler)
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
		String								entryName;
		Class<D>							type;
		INBTReaderAndWriter<? super D, ?>	nbtHandler;
		
		TemplateRecipeCacheEntryHandler(String name, Class<D> type, INBTReaderAndWriter<? super D, ?> nbtHandler)
		{
			this.entryName = name;
			this.type = type;
			this.nbtHandler = nbtHandler;
		}
	}
	
	public static class TemplateRecipeCache<H>
	{
		TemplateRecipeMap<H>	map;
		/**
		 * Will not stored into NBT, but it will be exist by
		 * {@link TemplateRecipeMap#findRecipe} method.
		 */
		@Nullable
		TemplateRecipe<H>		recipe;
		Object[]				storeData;
		
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
		Judgable<? super H>			judgable;
		Function<? super H, ?>[]	dataProvider;
		Object[]					customDisplayData;
		
		public TemplateRecipe(Judgable<? super H>[] judgables, Function<? super H, ?>...dataProviders)
		{
			this(F.and(judgables), dataProviders);
		}
		
		public TemplateRecipe(Judgable<? super H> judgable, Function<? super H, ?>...dataProviders)
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
	
	protected final String										name;
	protected final List<TemplateRecipeMap.TemplateRecipe<H>>	recipes;
	protected final TemplateRecipeCacheEntryHandler[]			handlers;
	
	protected TemplateRecipeMap(String name, List<TemplateRecipeMap.TemplateRecipe<H>> recipeList, TemplateRecipeCacheEntryHandler[] handlers)
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
	public TemplateRecipeCache<H> readFrom(NBTTagCompound nbt)
	{
		try
		{
			Object[] store = new Object[this.handlers.length];
			for (int i = 0; i < this.handlers.length; ++i)
			{
				store[i] = this.handlers[i].nbtHandler.readFrom(nbt, this.handlers[i].entryName);
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
	public void writeTo(TemplateRecipeCache<H> target, NBTTagCompound nbt)
	{
		try
		{
			for (int i = 0; i < this.handlers.length; ++i)
			{
				this.handlers[i].nbtHandler.writeTo(nbt, this.handlers[i].entryName, target.storeData[i]);
			}
		}
		catch (Exception exception)
		{
			Log.warn("Invalid recipe data {}", exception, target.storeData);
		}
	}
	
	@Override
	public boolean addRecipe(TemplateRecipeMap.TemplateRecipe<H> recipe)
	{
		if (recipe.dataProvider.length != this.handlers.length)
		{
			Log.error("Wrong recipe data format, get {}.", new Object[] { recipe.dataProvider });
			return false;
		}
		this.recipes.add(recipe);
		return true;
	}
	
	@Override
	public TemplateRecipeCache<H> findRecipe(H handler)
	{
		TemplateRecipeMap.TemplateRecipe recipe = L.get(this.recipes, r -> r.judgable.test(handler));
		if (recipe == null) return null;
		Object[] stores = new Object[this.handlers.length];
		for (int i = 0; i < this.handlers.length; stores[i] = recipe.dataProvider[i].apply(handler), ++i);
		return new TemplateRecipeCache<>(this, recipe, stores);
	}
	
	@Nullable
	public <V> V findRecipeValue(H handler, int id)
	{
		TemplateRecipeCache<H> cache = findRecipe(handler);
		return cache != null ? cache.get(id) : null;
	}
	
	@Override
	public Class<TemplateRecipeCache<H>> type()
	{
		return (Class) TemplateRecipeCache.class;
	}
	
	@Override
	public final List<TemplateRecipeMap.TemplateRecipe<H>> recipes()
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
		this.recipes.removeIf(r -> r.judgable.test(handler));
	}
}
