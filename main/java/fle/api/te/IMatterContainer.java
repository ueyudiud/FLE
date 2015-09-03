package fle.api.te;

import java.util.Map;

import fle.api.material.IAtoms;

public interface IMatterContainer
{
	Map<IAtoms, Integer> getMatterContain();

	void setMatterContain(Map<IAtoms, Integer> map);
}
