package fle.api.plant;

import java.util.Iterator;

public abstract class PlantRegister
{
	public abstract void registerPlant(PlantCard crop);
	
	public abstract PlantCard getPlantFromName(String name);
	
	public abstract Iterator<PlantCard> getPlants();

	public abstract int getPlantID(PlantCard crop);
}