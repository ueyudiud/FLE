package flapi.plant;

public abstract class PlantRegister
{
	public abstract void registerPlant(PlantCard crop);
	
	public abstract PlantCard getPlantFromName(String name);
	
	public abstract Iterable<PlantCard> getPlants();

	public abstract int getPlantID(PlantCard crop);
}