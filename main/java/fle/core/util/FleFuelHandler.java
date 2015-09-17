package fle.core.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import fle.api.FleAPI;
import fle.api.material.IAtoms.EnumCountLevel;
import fle.api.material.Matter;
import fle.api.recipe.ItemBaseStack;
import fle.api.recipe.ItemOreStack;
import fle.api.util.IFuelHandler;
import fle.core.item.ItemFleSub;

public class FleFuelHandler implements IFuelHandler
{
	@Override
	public float getFuelCalorificValue(FluidStack aStack, Matter aAirBase)
	{
		if(FleAPI.fluidDictionary.isFluidLava(aStack))
		{
			return aStack.amount * 100;
		}
		else if(FleAPI.fluidDictionary.matchFluid("oilAnimal", aStack))
		{
			return (float) ((double) aStack.amount * (2 + aAirBase.getIonContain(EnumCountLevel.Matter, Matter.mO2)) / 3);
		}
		return -1;
	}

	@Override
	public boolean getFuelRequireSmoke(FluidStack aStack, Matter aAirBase)
	{
		return false;
	}

	@Override
	public int getFuelCalorificValue(ItemStack aStack, Matter aAirBase)
	{
		if(aStack != null)
		{
			if(new ItemBaseStack(ItemFleSub.a("charred_log")).isStackEqul(aStack))
			{
				return (int) (6000000F * aAirBase.getIonContain(EnumCountLevel.Matter, Matter.mO2));
			}
			else if(new ItemOreStack("logWood").isStackEqul(aStack))
			{
				return (int) (1200000F * aAirBase.getIonContain(EnumCountLevel.Matter, Matter.mO2));
			}
			else if(new ItemOreStack("branchWood").isStackEqul(aStack) || new ItemBaseStack(ItemFleSub.a("branch_bush")).isStackEqul(aStack))
			{
				return (int) (360000F * aAirBase.getIonContain(EnumCountLevel.Matter, Matter.mO2));
			}
		}
		return 0;
	}

	@Override
	public ItemStack getFuelBurnedOutput(ItemStack aStack)
	{
		if(aStack != null)
		{
			if(new ItemBaseStack(ItemFleSub.a("charred_log")).isStackEqul(aStack))
			{
				return ItemFleSub.a("plant_ash");
			}
		}
		return null;
	}
}