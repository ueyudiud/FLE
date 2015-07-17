package fla.core.tech;

import fla.api.tech.ITechManager;
import fla.api.tech.TechClass;
import fla.api.tech.Technology;
import fla.api.util.Registry;

public class TechManager extends ITechManager
{
	private final Registry<Technology> register1 = new Registry();
	private final Registry<TechClass> register2 = new Registry();
	
	public void registerTech(Technology tech)
	{
		register1.register(tech, tech.getName());
		tech.getTechClassBelong().setTechList(tech);
	}

	public void registerTechClass(TechClass clazz)
	{
		register2.register(clazz, clazz.getTechClassName());
	}
}