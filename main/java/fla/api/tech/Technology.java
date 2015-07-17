package fla.api.tech;

public abstract class Technology
{
	public abstract String getName();
	
	public abstract TechClass getTechClassBelong();
	
	public abstract int getXPos();
	
	public abstract int getYPos();
	
	public abstract boolean canTechSelected();
	
	public abstract Page[] getPages();
}