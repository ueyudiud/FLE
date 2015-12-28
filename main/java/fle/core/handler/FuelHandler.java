package fle.core.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import flapi.chem.base.Matter;
import flapi.chem.base.IMolecular.EnumCountLevel;
import flapi.fluid.FluidDictionary;
import flapi.recipe.stack.BaseStack;
import flapi.recipe.stack.OreStack;
import flapi.util.IFuelHandler;
import fle.core.init.Config;
import fle.core.init.IB;
import fle.core.item.ItemFleSub;

public class FuelHandler implements IFuelHandler
{
	/**
	 * getMinecraftJoulePerSmeltItem
	 * @return
	 */
	public static final int g()
	{
		return 1000000;//100W MJ.
	}
	/**
	 * getMinecraftJoulePerTick
	 * @return
	 */
	public static final int p()
	{
		return g() / 200;//0.5W MJ.
	}
	public static final int g(float per)
	{
		return (int) (g() * per);
	}
	public static final int p(float per)
	{
		return (int) (p() * per);
	}
	
	@Override
	public float getFuelCalorificValue(FluidStack aStack, Matter aAirBase)
	{
		if(FluidDictionary.isFluidLava(aStack))
		{
			return aStack.amount * 100;
		}
		else if(FluidDictionary.matchFluid("oil", aStack))
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

	private final int powerCharcoal = Config.getInteger("pCharcoal");
	private final int powerFirewood = Config.getInteger("pFirewood");
	
	@Override
	public long getFuelCalorificValue(ItemStack aStack, Matter aAirBase)
	{
		if(aStack != null)
		{
			if(new BaseStack(ItemFleSub.a("charred_log")).equal(aStack))
			{
				return (long) (powerCharcoal * 1600 / 4);
			}
			else if(new BaseStack(new ItemStack(IB.peat)).equal(aStack))
			{
				return (long) (powerCharcoal * 1600 / 6);
			}
			else if(new OreStack("logWood").equal(aStack))
			{
				return (long) (powerFirewood * 1600 / 4);
			}
			else if(new OreStack("branchWood").equal(aStack) || new BaseStack(ItemFleSub.a("branch_bush")).equal(aStack))
			{
				return (long) (powerFirewood * 1600 / 12);
			}
			else if(new BaseStack(ItemFleSub.a("tinder")).equal(aStack))
			{
				return (long) (160000);
			}
		}
		return 0;
	}

	@Override
	public ItemStack getFuelBurnedOutput(ItemStack aStack)
	{
		if(aStack != null)
		{
			if(new BaseStack(ItemFleSub.a("charred_log")).equal(aStack))
			{
				return ItemFleSub.a("plant_ash");
			}
			else if(new BaseStack(ItemFleSub.a("tinder")).equal(aStack))
			{
				return ItemFleSub.a("plant_ash");
			}
		}
		return null;
	}
}