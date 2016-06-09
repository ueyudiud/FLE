package fle.load;

import farcore.lib.crop.CropManager;
import fle.core.plant.crop.CropCabbage;
import fle.core.plant.crop.CropCotton;
import fle.core.plant.crop.CropFlax;
import fle.core.plant.crop.CropMillet;
import fle.core.plant.crop.CropPotato;
import fle.core.plant.crop.CropRamie;
import fle.core.plant.crop.CropReed;
import fle.core.plant.crop.CropSoybean;
import fle.core.plant.crop.CropSweetPotato;
import fle.core.plant.crop.CropWheat;

public class Crops
{
	public static void init()
	{
		CropManager.register(new CropSoybean());
		CropManager.register(new CropRamie());
		CropManager.register(new CropMillet());
		CropManager.register(new CropReed());
		CropManager.register(new CropWheat());
		CropManager.register(new CropCotton());
		CropManager.register(new CropPotato());
		CropManager.register(new CropSweetPotato());
		CropManager.register(new CropFlax());
		CropManager.register(new CropCabbage());
	}
}