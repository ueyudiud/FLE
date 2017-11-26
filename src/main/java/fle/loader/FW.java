/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.loader;

import static fle.api.util.DrinkableFluidHandler.registerDrinkableFluid;

import farcore.data.EnumFluid;

/**
 * Food and Drink settings.
 * 
 * @author ueyudiud
 */
public class FW
{
	public static void init()
	{
		registerDrinkableFluid(EnumFluid.water.fluid, 10, 1);
		registerDrinkableFluid(IBFS.fsJuice[0],  9, 1);//Sugarcane juice
		registerDrinkableFluid(IBFS.fsJuice[1], 12, 1);//Citrus juice
		registerDrinkableFluid(IBFS.fsJuice[2], 10, 1);//Bitter orange juice
		registerDrinkableFluid(IBFS.fsJuice[3],  8, 1);//Lemon juice
		registerDrinkableFluid(IBFS.fsJuice[4],  8, 1);//Tangerine juice
		registerDrinkableFluid(IBFS.fsJuice[5],  8, 1);//Pomelo juice
		registerDrinkableFluid(IBFS.fsJuice[6], 10, 1);//Lime juice
		registerDrinkableFluid(IBFS.fsJuice[7],  8, 1);//Orange juice
		registerDrinkableFluid(IBFS.fsJuice[8],  8, 1);// Grapefruit juice
	}
}
