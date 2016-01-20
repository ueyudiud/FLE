package farcore.biology.plant;

import farcore.biology.IBiology;

public interface IPlant extends IBiology
{
	/**
	 * Provide a plant species.
	 */
	@Override
	IPlantSpecies getSpecie();
}