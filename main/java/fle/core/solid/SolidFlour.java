package fle.core.solid;

import flapi.solid.Solid;

public class SolidFlour extends Solid
{
	public SolidFlour(String name, String localizedName)
	{
		super(name, localizedName);
		setType(SolidState.Dust);
	}	
}