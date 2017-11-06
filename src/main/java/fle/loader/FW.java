/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader;

import farcore.data.EnumFluid;
import fle.api.util.DrinkableFluidHandler;

/**
 * Food and Drink settings.
 * @author ueyudiud
 */
public class FW
{
	public static void init()
	{
		DrinkableFluidHandler.registerDrinkableFluid(EnumFluid.water.fluid, 10, 1);
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[0], 9, 1);//Sugarcane juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[1], 12, 1);//Citrus juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[2], 10, 1);//Bitter orange juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[3], 8, 1);//Lemon juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[4], 8, 1);//Tangerine juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[5], 8, 1);//Pomelo juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[6], 10, 1);//Lime juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[7], 8, 1);//Orange juice
		DrinkableFluidHandler.registerDrinkableFluid(IBFS.fsJuice[8], 8, 1);//Grapefruit juice
	}
}