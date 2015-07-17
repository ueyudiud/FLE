package fla.api.world;

public enum TempretureBand 
{
	High_Tem(1.0F),
	Tropic(0.875F),
	Subtropic(0.75F),
	Temprate(0.5F),
	Subfrigid(0.25F),
	Frigid(0.125F),
	Low_Tem(0.0F);
	
	float tempreture;
	
	TempretureBand(float tem)
	{
		this.tempreture = tem;
	}
	 
	public float getTempreture()
	{
		return this.tempreture;
	}
}
