package flapi.gui;

import java.util.Collection;

public interface IConditionContainer 
{
	public Collection<GuiCondition> get();
	
	public void add(GuiCondition tag);

	public boolean contain(GuiCondition tag);
	
	public void remove(GuiCondition tag);
}