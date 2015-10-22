package fle.api.tech;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.Event;

public abstract class Technology
{
	public abstract String getName();
	
	public abstract ITechTag getTechClassBelong();
	
	public abstract boolean canTechSelected();
	
	public abstract LearningType[] getLearningTypes();

	public abstract boolean canBeLearned(PlayerTechInfo info, LearningType type, Event evt);
	
	public static abstract class LearningCheck
	{
		protected List<Technology> parentTech = new ArrayList(); 
		
		public void addParentTechnology(Technology tech)
		{
			parentTech.add(tech);
		}
		
		public boolean canBeLearned(PlayerTechInfo info, LearningType type, Event evt)
		{
			if(parentTech.isEmpty()) return true;
			for(Technology tech : parentTech)
			{
				if(!info.isPlayerKnowTech(tech)) return false;
			}
			return true;
		}
	}
	
	public static enum LearningType
	{
		Break,
		Hearvest,
		Hurt,
		Hit,
		Drop,
		Use,
		Crafting,
		Get, 
		Pickup;
	}
}