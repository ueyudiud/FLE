package farcore.data;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

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
	soft_hammer("SoftHammer"),
	
	pickaxe("Pickaxe"),
	
	shovel("Shovel"),
	spade_hoe("SpadeHoe"),
	sickle("Sickle"),
	
	firestarter("Firestarter"),
	
	bar_grizzly("BarGrizzly"),
	
	waist_loom("WaistLoom"),
	
	spinning_tool("SpinningTool"),
	
	whetstone("Whetstone"),
	
	saw("Saw"),
	bow_saw("BowSaw"),
	
	spear("Spear"),
	dagger("Dagger"),
	dart("Dart"),
	sword("Sword"),

	whip("Whip"),
	
	needle("Needle"),
	
	gaff("Gaff"),
	
	decorticating_plate("DecorticatingPlate"),
	decorticating_stick("DecorticatingStick"),
	
	chisel("Chisel"),
	
	explosive("Explosive"),
	
	drill("Drill"),
	
	laser("Laser"),
	
	rock_cutter("RockCutter"),
	
	mirror("Mirror"),
	
	stock_pot("StockPot"),
	frying_pan("FryingPan");
	
	public static final List<EnumToolType> HAND_USABLE_TOOL = ImmutableList.of(hand);
	
	Set<String> toolClass = ImmutableSet.of(name());
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
	
	public Set<String> getToolClasses()
	{
		return toolClass;
	}
	
	public boolean isToolClass(String tool)
	{
		return toolClass.contains(tool);
	}
}