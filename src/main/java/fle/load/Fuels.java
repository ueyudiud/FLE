package fle.load;

import farcore.enums.EnumItem;
import farcore.lib.stack.OreStack;
import fle.api.fuel.FuelHandler;
import fle.api.fuel.IFuelManager;
import fle.api.fuel.IFuelValue;
import net.minecraft.item.ItemStack;
import scala.annotation.varargs;

public class Fuels implements IFuelManager
{
	public static void init()
	{
		FuelHandler.addFuelManager(new Fuels());
	}
	
	private OreStack firewood = new OreStack("firewood");
	private IFuelValue a = make(1600000, 4000F, 680, 350, null);

	@Override
	public IFuelValue getFuelValue(ItemStack fuel)
	{
		if(firewood.similar(fuel))
		{
			return a;
		}
		return null;
	}
	
	private IFuelValue make(int energy, float power, float maxTemp, float minBurnPoint, ItemStack output)
	{
		return new IFuelValue(){
			public ItemStack getOutput(ItemStack stack) {return output;}
			public float getMinBurnPoint(ItemStack stack) {return minBurnPoint;}
			public float getMaxTempreture(ItemStack stack) {return maxTemp;}
			public float getMaxPower(ItemStack stack, float tempreture) {return power;}
			public int getEnergyCurrent(ItemStack stack) {return energy;}
		};
	}
}