/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.compat.jei;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Iterators;

import farcore.lib.solid.SolidStack;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.util.color.ColorGetter;
import nebula.common.util.Strings;

/**
 * @author ueyudiud
 */
public class SolidStackHelper implements IIngredientHelper<SolidStack>
{
	@Override
	public List<SolidStack> expandSubtypes(List<SolidStack> ingredients)
	{
		return ingredients;
	}
	
	@Override
	public SolidStack getMatch(Iterable<SolidStack> ingredients, SolidStack ingredientToMatch)
	{
		for (SolidStack stack : ingredients)
		{
			if (ingredientToMatch.getSolid() == stack.getSolid())
			{
				return stack;
			}
		}
		return null;
	}
	
	@Override
	public String getDisplayName(SolidStack ingredient)
	{
		return ingredient.getSolid().getLocalizedName();
	}
	
	@Override
	public String getUniqueId(SolidStack ingredient)
	{
		return ingredient.getRegistryName();
	}
	
	@Override
	public String getWildcardId(SolidStack ingredient)
	{
		return getUniqueId(ingredient);
	}
	
	@Override
	public String getModId(SolidStack ingredient)
	{
		return Strings.splitFirst(ingredient.getRegistryName(), ':')[0];
	}
	
	@Override
	public Iterable<Color> getColors(SolidStack ingredient)
	{
		try
		{
			return ColorGetter.getColors(ingredient.getSolid().getIcon(ingredient), ingredient.getSolid().getColor(ingredient), 2);
		}
		catch (Exception exception)
		{
			return Iterators::emptyIterator;
		}
	}
	
	@Override
	public String getErrorInfo(SolidStack ingredient)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(ingredient.getRegistryName()).append('x').append(ingredient.amount);
		if (ingredient.tag != null)
		{
			builder.append('(').append("nbt: ").append(ingredient.tag).append(')');
		}
		return builder.toString();
	}
}
