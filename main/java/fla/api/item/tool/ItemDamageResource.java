package fla.api.item.tool;

public enum ItemDamageResource
{
	BreakBlock("breakBlock"),
	HitEntity("hitEntity"),
	CraftingInInventory("craftingInInventory"),
	UseItem("useItem"),
	Unknown("unknown");
	
	final String name;
	
	private ItemDamageResource(String name) 
	{
		this.name = name;
	}
}
