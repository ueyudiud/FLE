package fle.api.tech;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.IIcon;

public abstract class ITechTag
{
	private String name;
	private List<Technology> techList = new ArrayList();
	
	public ITechTag(String name)
	{
		this.name = name;
	}
	
	public String getTechTagName()
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
	
	public abstract boolean isTechEnable(PlayerTechInfo aInfo);
	
	public abstract IIcon getIconForTag();
}