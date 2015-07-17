package fla.api.world;

public enum DampnessBand 
{
	Full_Water(1.0F),
	Wet(0.875F),
	Subwet(0.75F),
	Mid(0.6F),
	Submid(0.4F),
	Subdry(0.25F),
	Dry(0.125F),
	No_Water(0F);
	
	float rainfall;
	
	DampnessBand(float aRainfall)
	{
		this.rainfall = aRainfall;
	}
	
	public float getDampness()
	{
		return rainfall;
	}
}
