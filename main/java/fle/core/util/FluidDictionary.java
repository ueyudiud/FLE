package fle.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidDictionary extends fle.api.fluid.FluidDictionary
{
	private Map<String, List<Fluid>> fluidMap = new HashMap();
	private Map<Fluid, List<String>> dicMap = new HashMap();
	
	boolean init = true;
	
	private void init()
	{
		for(String tFluidName : FluidRegistry.getRegisteredFluids().keySet())
		{
			registerFluid(tFluidName, FluidRegistry.getRegisteredFluids().get(tFluidName));
		}
	}
	
	@Override
	public void registerFluid(String fluidName, Fluid fluid)
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

	@Override
	public List<Fluid> getFluidList(String fluidName)
	{
		return !fluidMap.containsKey(fluidName) ? new ArrayList() : new ArrayList(fluidMap.get(fluidName));
	}

	@Override
	public List<String> getFluidDictionaryName(Fluid fluid)
	{
		return !dicMap.containsKey(fluid) ? new ArrayList() : new ArrayList(dicMap.get(fluid));
	}

	@Override
	public boolean removeFluid(Fluid fluid)
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

	@Override
	public boolean matchFluid(String fluidName, FluidStack target)
	{
		if(init)
		{
			init();
			init = false;
		}
		return fluidMap.get(fluidName).contains(target.getFluid());
	}
}