package farcore.chem;

import java.util.Map;

import farcore.substance.Substance;

public abstract class AbstractReactionRecipe implements IReactionRecipe
{
	final String name;
	
	public AbstractReactionRecipe(String name)
	{
		this.name = name;
	}

	@Override
	public String name()
	{
		return name;
	}
}