package flapi.fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flapi.material.IChemCondition.EnumPH;

public abstract class FluidDictionary 
{
	private static Map<String, List<Fluid>> fluidMap = new HashMap();
	private static Map<Fluid, List<String>> dicMap = new HashMap();
	private static Map<String, EnumPH> phMap = new HashMap();
	
	static boolean init = true;
	
	private static void init()
	{
		for(String tFluidName : FluidRegistry.getRegisteredFluids().keySet())
		{
			registerFluid(tFluidName, FluidRegistry.getRegisteredFluids().get(tFluidName));
		}
	}
	
	public static void registerFluid(String fluidName, Fluid fluid)
	{
		if(!fluidMap.containsKey(fluidName))
		{
			fluidMap.put(fluidName, new ArrayList());
		}
		fluidMap.get(fluidName).add(fluid);
		if(!dicMap.containsKey(fluid))
		{
			dicMap.put(fluid, new ArrayList());
		}
		dicMap.get(fluid).add(fluidName);
	}

	public static List<Fluid> getFluidList(String fluidName)
	{
		return !fluidMap.containsKey(fluidName) ? new ArrayList() : new ArrayList(fluidMap.get(fluidName));
	}
	
	public static List<String> getFluidDictionaryName(Fluid fluid)
	{
		return !dicMap.containsKey(fluid) ? new ArrayList() : new ArrayList(dicMap.get(fluid));
	}

	public static boolean removeFluid(Fluid fluid)
	{
		List<String> tList = dicMap.get(fluid);
		if(fluid != null)
		{
			for(String tDicName : tList)
			{
				fluidMap.get(tDicName).remove(fluid);
			}
			dicMap.remove(fluid);
			return true;
		}
		return false;
	}

	public static boolean matchFluid(String fluidName, FluidStack target)
	{
		if(init)
		{
			init();
			init = false;
		}
		return fluidMap.get(fluidName).contains(target.getFluid());
	}

	public static void registerFluidPH(Fluid fluid, EnumPH ph)
	{
		phMap.put(fluid.getName(), ph);
	}

	public static EnumPH getFluidPH(Fluid fluid)
	{
		return phMap.containsKey(fluid.getName()) ? phMap.get(fluid.getName()) : EnumPH.Water;
	}

	public static boolean isFluidWater(FluidStack aStack)
	{
		return matchFluid("water", aStack);
	}
	
	public static boolean isFluidLava(FluidStack aStack)
	{
		return matchFluid("lava", aStack);
	}
	
	public static boolean isFluidSteam(FluidStack aStack)
	{
		return matchFluid("steam", aStack);
	}
}