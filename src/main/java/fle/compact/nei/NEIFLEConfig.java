package fle.compact.nei;

import codechicken.nei.api.IConfigureNEI;

public class NEIFLEConfig implements IConfigureNEI
{
	@Override
	public void loadConfig()
	{
		new FarCraftingHandler();
		new PolishRecipeHandler();
	}

	@Override
	public String getName()
	{
		return "FLE NEI Plugin";
	}

	@Override
	public String getVersion()
	{
		return "0.1";
	}
}