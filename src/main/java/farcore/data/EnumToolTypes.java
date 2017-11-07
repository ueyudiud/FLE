/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.data;

import farcore.lib.oredict.OreStackExt;
import nebula.common.tool.EnumToolType;

/**
 * @author ueyudiud
 */
public class EnumToolTypes
{
	public static final EnumToolType BIFACE = new EnumToolTypeFC("biface", "Biface"),
			
			ADZ = new EnumToolTypeFC("adz", "Adz"), AWL = new EnumToolTypeFC("awl", "Awl"), AXE = new EnumToolTypeFC("axe", "Axe"),
			
			HAMMER_DIGABLE = new EnumToolTypeFC("hammer_digable", "DigableHammer"), HAMMER_WOODEN = new EnumToolTypeFC("wooden_hammer", "WoodenHammer"), SOFT_HAMMER = new EnumToolTypeFC("soft_hammer", "SoftHammer"),
			
			PICKAXE = new EnumToolTypeFC("pickaxe", "Pickaxe"),
			
			HOE = new EnumToolTypeFC("hoe", "Hoe"), SPADE_HOE = new EnumToolTypeFC("spade_hoe", "SpadeHoe"), SICKLE = new EnumToolTypeFC("sickle", "Sickle"),
			
			FIRESTARTER = new EnumToolTypeFC("firestarter", "Firestarter"),
			
			BAR_GRIZZLY = new EnumToolTypeFC("bar_grizzy", "BarGrizzly"),
			
			WAIST_LOOM = new EnumToolTypeFC("waist_loom", "WaistLoom"),
			
			SPINNING_TOOL = new EnumToolTypeFC("spinning_tool", "SpinningTool"),
			
			WHESTONE = new EnumToolTypeFC("whetstone", "Whetstone"),
			
			SHOVEL = new EnumToolTypeFC("shovel", "Shovel"),
			
			KNIFE = new EnumToolTypeFC("knife", "Knife"),
			
			SAW = new EnumToolTypeFC("saw", "Saw"), BOW_SAW = new EnumToolTypeFC("bow_saw", "BowSaw"),
			
			SPEAR = new EnumToolTypeFC("spear", "Spear"), DAGGER = new EnumToolTypeFC("dagger", "Dagger"), DART = new EnumToolTypeFC("dart", "Dart"), SWORD = new EnumToolTypeFC("sword", "Sword"),
			
			WHIP = new EnumToolTypeFC("whip", "Whip"),
			
			NEEDLE = new EnumToolTypeFC("needle", "Needle"),
			
			GAFF = new EnumToolTypeFC("gaff", "Gaff"),
			
			DECORTICATING_PLATE = new EnumToolTypeFC("decorticating_plate", "DecorticatingPlate"), DECORTICATING_STICK = new EnumToolTypeFC("decorticating_stick", "DecorticatingStick"),
			
			CHISEL = new EnumToolTypeFC("chisel", "Chisel"), CHISEL_CARVE = new EnumToolTypeFC("chiselCarving", "ChiselCarving"), CHISEL_POLISH = new EnumToolTypeFC("chiselPolish", "ChiselPolishing"),
			
			EXPLOSIVE = new EnumToolTypeFC("explosive", "Explosive"),
			
			DRILL = new EnumToolTypeFC("drill", "Drill"),
			
			LASER = new EnumToolTypeFC("laser", "Laser"),
			
			ROCK_CUTTER = new EnumToolTypeFC("rock_cutter", "RockCutter"),
			
			MIRROR = new EnumToolTypeFC("mirror", "Mirror"),
			
			STOCK_POT = new EnumToolTypeFC("stock_pot", "StockPot"), FRYING_PAN = new EnumToolTypeFC("frying_pan", "FryingPan"),
			
			SCREW_DRIVER = new EnumToolTypeFC("screw_driver", "ScrewDriver");
	
	private static class EnumToolTypeFC extends EnumToolType
	{
		private String className;
		
		EnumToolTypeFC(String name, String dictName)
		{
			super("farcore." + name, name, "craftingTool" + dictName, new OreStackExt("craftingTool" + dictName));
			this.className = name;
		}
		
		@Override
		public boolean isToolClass(String tool)
		{
			return this.className.equals(tool);
		}
	}
}
