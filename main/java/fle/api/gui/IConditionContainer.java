package fle.api.gui;

import java.util.Collection;

import fle.api.util.IDataChecker;

public interface IConditionContainer 
{
	public Collection<GuiCondition> get();
	
	public void add(GuiCondition tag);

	public boolean contain(GuiCondition tag);
	
	public void remove(GuiCondition tag);
}