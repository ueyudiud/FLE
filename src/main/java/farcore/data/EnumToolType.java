package farcore.data;

import farcore.lib.stack.OreStack;
import net.minecraft.item.ItemStack;

public enum EnumToolType
{
	hand("Hand"),//The player hand current, do not register tool with this.
	adz("Adz"),
	awl("Awl"),
	axe("Axe"),
	hammer_digable("DigableHammer"),
	hammer_digable_basic("DigableHammerBasic"),
	wooden_hammer("WoodenHammer"),
	pickaxe("Pickaxe"),
	shovel("Shovel"),
	firestarter("Firestarter"),
	bar_grizzly("BarGrizzly"),
	whetstone("Whetstone"),
	knife("Knife"),
	decorticating_plate("DecorticatingPlate"),
	decorticating_stick("DecorticatingStick");
	
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
	
	public boolean match(ItemStack stack)
	{
		return stack != null && stack().similar(stack);
	}
}