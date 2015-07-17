package fla.api.world;

import fla.api.world.TempretureBand;

public enum ForestType 
{
	//Tropic
	Rain_Forest(TempretureBand.Tropic, 0.9875F),
	Monsoon_Forest(TempretureBand.Tropic, 0.95F),
	Dry_Tropic_Forest(TempretureBand.Tropic, 0.8F),
	Edge_Tropic_Forest(TempretureBand.Tropic, 0.75F),
	//Subtropic
	Evergreen_Boardleaf_Forest(TempretureBand.Subtropic, 0.8F),
	Evergreen_B_and_C_Forest(TempretureBand.Subtropic, 0.75F),
	Evergreen_Coniferous_Forest(TempretureBand.Subtropic, 0.7F),
	Edge_Subtropic_Forest(TempretureBand.Subtropic, 0.6F),
	//Zone
	E_and_D_Broadleaf_Forest(TempretureBand.Temprate, 0.775F),
	Deciduous_Broadleaf_Forest(TempretureBand.Temprate, 0.725F),
	Deciduous_B_and_Coniferous_Forest(TempretureBand.Temprate, 0.6875F),
	Edge_Temprate_Forest(TempretureBand.Temprate, 0.625F),
	//Subfrigid
	Coniferous_Forest(TempretureBand.Subfrigid, 0.625F),

	Deciduous_Coniferous_Forest(TempretureBand.Subfrigid, 0.5F),
	Edge_Subfrigid_Forest(TempretureBand.Subfrigid, 0.375F);
	
	double rainfall;
	TempretureBand band;
	
	ForestType(TempretureBand aBand, double aRainfall)
	{
		this.rainfall = aRainfall;
		this.band = aBand;
	}
	
	public double getRainfall()
	{
		return rainfall;
	}
	
	public TempretureBand getBand()
	{
		return band;
	}
	
	public double getTempreture()
	{
		return band.getTempreture();
	}
}