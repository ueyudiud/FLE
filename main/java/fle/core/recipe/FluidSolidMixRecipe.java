package fle.core.recipe;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.inventory.IInventory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import fle.api.FleValue;
import fle.api.material.IFluidChemInfo;
import fle.api.soild.Solid;
import fle.api.soild.SolidRegistry;
import fle.api.soild.SolidStack;
import fle.api.soild.SolidTank;
import fle.api.util.IChemCondition;
import fle.api.util.IChemCondition.EnumOxide;
import fle.api.util.IChemCondition.EnumPH;
import fle.core.init.IB;

public class FluidSolidMixRecipe
{
	private static final Set<FluidSolidMixRecipe> set = new HashSet();

	static
	{
		registerRecipe(new FluidSolidMixRecipe(FluidRegistry.WATER, 125F, IB.limestone, 0.8F, IB.lime_mortar));
		registerRecipe(new FluidSolidMixRecipe(FluidRegistry.WATER, 5F, IB.plant_ash, 0.9F, IB.plant_ash_mortar));
		registerRecipe(new FluidSolidMixRecipe(FluidRegistry.WATER, 2.3148148F, IB.brown_sugar, 0.2F, IB.brown_sugar_aqua));
		registerRecipe(new FluidSolidMixRecipe(FluidRegistry.WATER, 6F, IB.millet_c, 0.5F, IB.millet_dextrin).set(FleValue.WATER_FREEZE_POINT + 120));
		registerRecipe(new FluidSolidMixRecipe(FluidRegistry.WATER, 6F, IB.wheat_c, 0.5F, IB.wheat_dextrin).set(FleValue.WATER_FREEZE_POINT + 120));
		registerRecipe(new FluidSolidMixRecipe(FluidRegistry.WATER, 8F, IB.millet, 0.6F, IB.millet_dextrin).set(FleValue.WATER_FREEZE_POINT + 105));
		registerRecipe(new FluidSolidMixRecipe(FluidRegistry.WATER, 8F, IB.wheat, 0.6F, IB.wheat_dextrin).set(FleValue.WATER_FREEZE_POINT + 105));
	}
	
	public static void registerRecipe(FluidSolidMixRecipe recipe)
	{
		set.add(recipe);
	}
	
	public static final Iterable<FluidSolidMixRecipe> getRecipes()
	{
		return set;
	}
	
	public static void mixSolidAndFluid(IFluidTank fluidTank, SolidTank solidTank)
	{
		mixSolidAndFluid(fluidTank, solidTank, creatDefaultCC(fluidTank.getFluid()));
	}
	public static void mixSolidAndFluid(IFluidTank fluidTank, SolidTank solidTank, IChemCondition cc)
	{
		if(fluidTank.getFluidAmount() <= 0 || solidTank.size() <= 0) return;
		for(FluidSolidMixRecipe recipe : set)
		{
			if(recipe.match(fluidTank.getFluid().getFluid(), solidTank.get(), cc))
			{
				int amount = fluidTank.getFluidAmount();
				int decrSize = recipe.getMaxSolidUse(amount, solidTank.size());
				solidTank.drain(decrSize, true);
				fluidTank.drain(fluidTank.getCapacity(), true);
				fluidTank.fill(recipe.getResult(amount, decrSize), true);
			}
		}
	}
	public static void mixSolidAndFluid(IFluidTank fluidTank, IInventory inv, int slotID)
	{
		mixSolidAndFluid(fluidTank, inv, slotID, creatDefaultCC(fluidTank.getFluid()));
	}
	public static void mixSolidAndFluid(IFluidTank fluidTank, IInventory inv, int slotID, IChemCondition cc)
	{
		if(fluidTank.getFluidAmount() <= 0 || inv.getStackInSlot(slotID) == null) return;
		SolidStack tStack = SolidRegistry.getSolidForFilledItem(inv.getStackInSlot(slotID));
		if(tStack == null) return;
		int size = tStack.getSize();
		tStack.setSize(size * inv.getStackInSlot(slotID).stackSize);
		for(FluidSolidMixRecipe recipe : set)
		{
			if(recipe.match(fluidTank.getFluid().getFluid(), tStack.getObj(), cc))
			{
				int amount = fluidTank.getFluidAmount();
				int decrSize = recipe.getMaxSolidUse(amount, tStack.getSize());
				inv.decrStackSize(slotID, (int) Math.ceil((double) decrSize / size));
				fluidTank.drain(fluidTank.getCapacity(), true);
				fluidTank.fill(recipe.getResult(amount, decrSize), true);
			}
		}
	}
	
	private static IChemCondition creatDefaultCC(final FluidStack fluid)
	{
		return new IChemCondition() 
		{
			public int getTemperature()	    {return fluid == null ? FleValue.WATER_FREEZE_POINT + 25 : fluid.getFluid().getTemperature(fluid);}
			public EnumPH getPHLevel()  	{return fluid == null ? EnumPH.Water : fluid.getFluid() instanceof IFluidChemInfo ? ((IFluidChemInfo) fluid.getFluid()).getFluidPH(fluid) : EnumPH.Water;}
			public EnumOxide getOxideLevel(){return fluid == null ? EnumOxide.Default : fluid.getFluid() instanceof IFluidChemInfo ? ((IFluidChemInfo) fluid.getFluid()).getFluidOxide(fluid) : EnumOxide.Default;}
		};
	}
	
	public Fluid fluid;
	public float perStack;
	public Solid solid;
	public float perOutput;
	public Fluid output;
	public int maxPH = EnumPH.values().length;
	public int minPH = 0;
	public int maxOxideLevel = EnumOxide.values().length;
	public int minOxideLevel = 0;
	public int minFunctionPoint = -1;
	public int maxFunctionPoint = Integer.MAX_VALUE;

	public FluidSolidMixRecipe(Fluid fluid, Solid solid, Fluid output)
	{
		this(fluid, 1.0F, solid, output);
	}
	public FluidSolidMixRecipe(Fluid fluid, float perFluid, Solid solid, Fluid output)
	{
		this(fluid, perFluid, solid, 1.0F, output);
	}
	public FluidSolidMixRecipe(Fluid fluid, Solid solid, float perOutput, Fluid output)
	{
		this(fluid, 1.0F, solid, perOutput, output);
	}
	public FluidSolidMixRecipe(Fluid fluid, float perFluid, Solid solid, float perOutput, Fluid output)
	{
		this.fluid = fluid;
		perStack = perFluid;
		this.solid = solid;
		this.perOutput = perOutput;
		this.output = output;
	}
	
	public FluidSolidMixRecipe set(EnumPH ph1, EnumPH ph2, EnumOxide o1, EnumOxide o2, int minPoint)
	{
		return set(ph1, ph2, o1, o2, minPoint, Integer.MAX_VALUE);
	}
	public FluidSolidMixRecipe set(EnumPH ph1, EnumPH ph2, EnumOxide o1, EnumOxide o2, int minPoint, int maxPoint)
	{
		maxPH = Math.max(ph1.ordinal(), ph2.ordinal());
		minPH = Math.min(ph1.ordinal(), ph2.ordinal());
		maxOxideLevel = Math.max(o1.ordinal(), o2.ordinal());
		minOxideLevel = Math.min(o1.ordinal(), o2.ordinal());
		maxFunctionPoint = maxPoint;
		minFunctionPoint = minPoint;
		return this;
	}
	public FluidSolidMixRecipe set(int minTemp)
	{
		minFunctionPoint = minTemp;
		return this;
	}
	
	public boolean match(Fluid aFluid, Solid aSolid, IChemCondition cc)
	{
		return fluid == aFluid && solid == aSolid &&
				minPH <= cc.getPHLevel().ordinal() && maxPH >= cc.getPHLevel().ordinal() &&
				minOxideLevel <= cc.getOxideLevel().ordinal() && maxOxideLevel >= cc.getOxideLevel().ordinal() &&
				minFunctionPoint <= cc.getTemperature() && maxFunctionPoint >= cc.getTemperature();
	}
	
	public FluidStack getResult(int inputAmount, int inputSize)
	{
		return new FluidStack(output, (int) Math.min(inputAmount * perOutput, inputSize * perStack * perOutput));
	}
	
	public int getMaxSolidUse(int inputAmount, int inputSize)
	{
		return Math.min((int) Math.ceil(inputAmount / perStack), inputSize);
	}
}