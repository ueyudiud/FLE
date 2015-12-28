package fle.api.te;

import java.util.Map;

import flapi.chem.base.IMolecular;

public interface IMatterContainer
{
	Map<IMolecular, Integer> getMatterContain();

	void setMatterContain(Map<IMolecular, Integer> map);
}
