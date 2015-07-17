package fla.core.init;

import fla.api.recipe.Fuel;
import fla.api.recipe.FuelOilLamp;
import fla.api.util.FlaValue;
import fla.core.FlaItems;

public class Fuels
{
	public static FuelOilLamp lipocere;
	public static FuelOilLamp fat;
	
	public static void init()
	{
		lipocere = new FuelOilLamp("Lipocere", 5.0F, FlaValue.TEXT_FILE_NAME + ":iconsets/oil");
		fat = new FuelOilLamp("Fat", 12.0F, FlaValue.TEXT_FILE_NAME + ":iconsets/oil");
		Fuel.registryFuel(lipocere);
		Fuel.registryFuel(fat);
		FlaItems.stone_oil_lamp.registerLampFuel(fat, FlaValue.TEXT_FILE_NAME + ":tools/stone/lamp/1");
		FlaItems.stone_oil_lamp.registerLampFuel(lipocere, FlaValue.TEXT_FILE_NAME + ":tools/stone/lamp/2");
	}
}