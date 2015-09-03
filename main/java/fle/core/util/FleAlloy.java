package fle.core.util;

import java.util.Map;
import java.util.Map.Entry;

import fle.api.enums.EnumAtoms;
import fle.api.material.AlloyHelper;
import fle.api.material.IAtoms;
import fle.api.util.WeightHelper;

public class FleAlloy implements AlloyHelper
{	
	private Map<EnumAtoms, double[]> map;
	
	public FleAlloy(Map<EnumAtoms, double[]> aMap)
	{
		map = aMap;
	}
	
	@Override
	public boolean matchProportion(WeightHelper<IAtoms> aHelper)
	{
		for(Entry<EnumAtoms, double[]> entry : map.entrySet())
		{
			double c = aHelper.getContain(entry.getKey());
			if(Math.max(entry.getValue()[0], entry.getValue()[1]) < c || Math.min(entry.getValue()[0], entry.getValue()[1]) > c)
				return false;
		}
		return true;
	}
}