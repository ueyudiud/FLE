package farcore.block;

import farcore.biology.plant.ICoverablePlantSpecies;
import farcore.biology.plant.PlantCover;
import farcore.util.IUnlocalized;

public enum EnumDirtState implements IUnlocalized
{
	dirt, grass, mycelium, moss, farmland;
	
	public String getUnlocalized()
	{
		return "state." + name();
	}
	
	public ICoverablePlantSpecies getPlant()
	{
		return PlantCover.getSpecies(this);
	}
}