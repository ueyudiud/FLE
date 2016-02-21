package fle.api.util;

import flapi.util.FleValue;

public class MolCaculator
{
	public static int asMol(int stackSize)
	{
		return FleValue.ingot_mol * stackSize;
	}
	
	public static int asMol(int matterSize, int stackSize)
	{
		return FleValue.ingot_mol * stackSize / matterSize;
	}
	
	public static int asMol(int itemSize, int matterSize, int stackSize)
	{
		return itemSize * stackSize / matterSize;
	}
	
	public static enum SolutionConcentration
	{
		Pure(1.0F),
		SpectrumPure(0.984375F),
		ExperimentPure(0.96875F),
		IndustryPure(0.9375F),
		Concentrated(0.90625F),
		Enrichment(0.875F),
		Thick(0.75F),
		FeedPure(0.625F),//Just a joke. ~233
		Medium(0.3125F),
		Dilute(0.125F),
		Filtered(0.03125F),
		None(0.0F);
		
		final float contain;
		
		private SolutionConcentration(float c)
		{
			contain = c;
		}
	}
}