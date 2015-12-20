package flapi.te.interfaces;

import java.util.Map;

import flapi.material.IMolecular;

public interface IMatterContainer
{
	Map<IMolecular, Integer> getMatterContain();

	void setMatterContain(Map<IMolecular, Integer> map);
}
