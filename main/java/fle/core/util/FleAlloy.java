package fle.core.util;

import java.util.Map;
import java.util.Map.Entry;

import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.enums.EnumAtoms;
import flapi.material.AlloyHelper;
import flapi.material.IMolecular;

public class FleAlloy implements AlloyHelper
{	
	private Map<EnumAtoms, double[]> map;
	
	public FleAlloy(Map<EnumAtoms, double[]> aMap)
	{
		map = aMap;
	}
	
	@Override
	public boolean matchProportion(IStackList<Stack<IMolecular>, IMolecular> aHelper)
	{
		for(Entry<EnumAtoms, double[]> entry : map.entrySet())
		{
			double c = aHelper.scale(entry.getKey());
			if(Math.max(entry.getValue()[0], entry.getValue()[1]) < c || Math.min(entry.getValue()[0], entry.getValue()[1]) > c)
				return false;
		}
		return true;
	}
}