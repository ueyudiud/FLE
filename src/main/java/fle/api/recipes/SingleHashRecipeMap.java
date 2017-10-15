/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.api.recipes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nebula.base.INode;
import nebula.base.Node;

/**
 * A recipe map template, use hash to
 * save and find recipe.
 * @author ueyudiud
 * @see fle.api.recipes.IRecipeMap
 * @see Object#hashCode()
 */
public abstract class SingleHashRecipeMap<R, O, H> implements IRecipeMap<R, O, H>
{
	private final Map<Integer, INode<R>> entries = new HashMap<>();
	private final List<R> recipes = new ArrayList<>();
	
	protected final String name;
	
	protected SingleHashRecipeMap(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getRegisteredName()
	{
		return this.name;
	}
	
	protected abstract int generateHashcodeFromRecipe(R recipe);
	
	protected abstract int generateHashcodeFromHandler(H handler, int index);
	
	protected abstract int hashLength(H handler);
	
	protected abstract boolean match(R recipe, H handler);
	
	protected abstract O getOutput(R recipe);
	
	@Override
	public boolean addRecipe(R recipe)
	{
		int hash = generateHashcodeFromRecipe(recipe);
		if (this.entries.containsKey(hash))
		{
			this.entries.get(hash).addLast(recipe);
		}
		else
		{
			this.entries.put(hash, Node.<R>first(recipe));
		}
		this.recipes.add(recipe);
		return true;
	}
	
	@Override
	public O findRecipe(H handler)
	{
		int l = hashLength(handler);
		int i = 0;
		int hash = generateHashcodeFromHandler(handler, 0);
		do
		{
			if (this.entries.containsKey(hash))
			{
				INode<R> node = this.entries.get(hash);
				INode<R> r = node.find(recipe->match(recipe, handler));
				if (r != null)
				{
					return getOutput(r.value());
				}
			}
		}
		while (++i < l);
		return null;
	}
	
	@Override
	public Collection<R> recipes()
	{
		return this.recipes;
	}
}