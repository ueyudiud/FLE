package fle.resource;

import flapi.collection.Register;
import flapi.plant.PlantCard;
import flapi.plant.PlantRegister;

public class FlePlantRegister extends PlantRegister
{
	private final Register<PlantCard> register = new Register<PlantCard>();

	@Override
	public void registerPlant(PlantCard plant)
	{
		if(plant == null) throw new NullPointerException();
		register.register(plant, plant.getPlantName());
	}

	@Override
	public PlantCard getPlantFromName(String name)
	{
		return register.get(name);
	}

	@Override
	public Iterable<PlantCard> getPlants()
	{
		return register;
	}

	@Override
	public int getPlantID(PlantCard crop)
	{
		return register.serial(crop);
	}

	public PlantCard getPlant(int plantID)
	{
		return register.get(plantID);
	}

	public int getPlantSize()
	{
		return register.size();
	}
}