package fle.core.util;

import java.util.Iterator;

import fle.api.plant.PlantCard;
import fle.api.plant.PlantRegister;
import fle.api.util.Register;

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
	public Iterator<PlantCard> getPlants()
	{
		return register.iterator();
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