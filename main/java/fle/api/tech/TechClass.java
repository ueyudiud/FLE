package fle.api.tech;

import java.util.ArrayList;
import java.util.List;

public class TechClass
{
	private String name;
	private List<Technology> techList = new ArrayList();
	
	public TechClass(String name)
	{
		this.name = name;
	}
	
	public String getTechClassName()
	{
		return name;
	}
	
	public void setTechList(Technology t)
	{
		techList.add(t);
	}
	
	public List<Technology> getTechList()
	{
		return techList;
	}
}
