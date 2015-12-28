package fle.core.util;

import java.util.Map;
import java.util.Map.Entry;

import flapi.chem.base.IMolecular;
import flapi.chem.particle.Atoms;
import flapi.collection.abs.IStackList;
import flapi.collection.abs.Stack;
import flapi.material.AlloyHelper;

public class FleAlloy implements AlloyHelper
{	
	private Map<Atoms, double[]> map;
	
	public FleAlloy(Map<Atoms, double[]> aMap)
	{
		map = aMap;
	}
	
	@Override
	public boolean matchProportion(IStackList<Stack<IMolecular>, IMolecular> aHelper)
	{
		for(Entry<Atoms, double[]> entry : map.entrySet())
		{
			double c = aHelper.scale(entry.getKey());
			if(Math.max(entry.getValue()[0], entry.getValue()[1]) < c || Math.min(entry.getValue()[0], entry.getValue()[1]) > c)
				return false;
		}
		return true;
	}
}