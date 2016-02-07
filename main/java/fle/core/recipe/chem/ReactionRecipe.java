package fle.core.recipe.chem;

import java.util.Map;

import farcore.chem.AbstractReactionRecipe;
import farcore.chem.IReactionRecipe;
import farcore.chem.IReactionSystem;
import farcore.substance.Substance;

public class ReactionRecipe extends AbstractReactionRecipe
{
	private Map<Substance, Integer> input;
	private Map<Substance, Integer> product;
	long t1 = Long.MAX_VALUE;
	long t2 = 0;
	long p1 = Long.MAX_VALUE;
	long p2 = 0;
	int PH1 = Integer.MAX_VALUE;
	int PH2 = Integer.MIN_VALUE;
	long energyUse = 0;
	
	public ReactionRecipe(String name, Map<Substance, Integer> input, Map<Substance, Integer> output)
	{
		super(name);
		this.input = input;
		this.product = output;
	}
	
	public ReactionRecipe setEnergyUse(long energyUse)
	{
		this.energyUse = energyUse;
		return this;
	}
	
	public ReactionRecipe setTemperature(long max, long min)
	{
		t1 = max;
		t2 = min;
		return this;
	}
	
	public ReactionRecipe setTemperature(long min)
	{
		t2 = min;
		return this;
	}
	
	public ReactionRecipe setPressure(long max)
	{
		p1 = max;
		return this;
	}
	
	public ReactionRecipe setPressure(long max, long min)
	{
		p1 = max;
		p2 = min;
		return this;
	}
	
	public ReactionRecipe setPH(int max, int min)
	{
		PH1 = max;
		PH2 = min;
		return this;
	}
	
	public ReactionRecipe setPH(int max)
	{
		PH1 = max;
		return this;
	}

	@Override
	public long getMaxTemp()
	{
		return t1;
	}

	@Override
	public long getMinTemp()
	{
		return t2;
	}

	@Override
	public long getMaxPressure()
	{
		return p1;
	}

	@Override
	public long getMinPressure()
	{
		return p2;
	}

	@Override
	public int getMaxPH()
	{
		return PH1;
	}

	@Override
	public int getMinPH()
	{
		return PH2;
	}

	@Override
	public Map<Substance, Integer> material()
	{
		return input;
	}

	@Override
	public Map<Substance, Integer> product()
	{
		return product;
	}

	@Override
	public float reactionCoefficient(IReactionSystem system)
	{
		return 1.0F;
	}

	@Override
	public long energyChange()
	{
		return energyUse;
	}
}