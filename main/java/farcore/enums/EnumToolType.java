package farcore.enums;

import farcore.lib.stack.OreStack;

public enum EnumToolType 
{
	adz("Adz"),
	axe("Axe"),
	hammer_digable("DigableHammer"),
	hammer_digable_basic("DigableHammerBasic"),
	pickaxe("Pickaxe"),
	shovel("Shovel");
	
	String name;
	OreStack stack;
	
	EnumToolType(String name)
	{
		stack = new OreStack(this.name = ("craftingTool" + name));
	}
	
	public OreStack stack()
	{
		return stack;
	}
	
	public String ore()
	{
		return name;
	}
}