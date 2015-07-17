package fla.api.tech;

public interface ITechnologyInventory
{
	public TechClass getSelectTechClass();
	
	public int getXRow();
	
	public int getYRow();
	
	public int getXSelect();
	
	public int getYSelect();
	
	public Technology getTechnology(int xRow, int yRow);
	
	public Technology getTechnologyWithHide(int xRow, int yRow);
}
