/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.loader;

import farcore.data.EnumPhysicalDamageType;
import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.lib.item.IToolStat;
import farcore.lib.item.ItemTool.ToolProp;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import farcore.lib.skill.SkillAbstract;
import fle.core.items.ItemToolHead;
import fle.core.items.behavior.BehaviorBarGrizzly;
import fle.core.items.behavior.BehaviorChiselHelpingSilkTouch;
import fle.core.items.behavior.BehaviorFirestarter;
import fle.core.items.behavior.BehaviorHoe;
import fle.core.items.behavior.BehaviorModeSwitching;
import fle.core.items.behavior.BehaviorPolishable;
import fle.core.items.behavior.BehaviorSpear;
import fle.core.items.behavior.BehaviorTool;
import fle.core.items.behavior.BehaviorWoodworkable;
import fle.core.items.tool.ToolAwl;
import fle.core.items.tool.ToolAxe;
import fle.core.items.tool.ToolBarGrizzly;
import fle.core.items.tool.ToolBiface;
import fle.core.items.tool.ToolChisel;
import fle.core.items.tool.ToolDecorticatingPlate;
import fle.core.items.tool.ToolDecorticatingStick;
import fle.core.items.tool.ToolFirestarter;
import fle.core.items.tool.ToolHardHammer;
import fle.core.items.tool.ToolHoe;
import fle.core.items.tool.ToolPickaxe;
import fle.core.items.tool.ToolShovel;
import fle.core.items.tool.ToolSickle;
import fle.core.items.tool.ToolSpear;
import fle.core.items.tool.ToolSpinningDisk;
import fle.core.items.tool.ToolWhetstone;
import nebula.base.function.Judgable;
import nebula.common.item.IBehavior;

public class Tools
{
	public static void initalizeTools()
	{
		EnumPhysicalDamageType.HIT.setSkill(new SkillAbstract("damage.hit", "Hit")
		{
		}.setExpInfo(100, 4.0F, 0.8F));
		EnumPhysicalDamageType.CUT.setSkill(new SkillAbstract("damage.cutting", "Cutting")
		{
		}.setExpInfo(100, 4.0F, 0.8F));
		EnumPhysicalDamageType.PUNCTURE.setSkill(new SkillAbstract("damage.puncture", "Puncture")
		{
		}.setExpInfo(100, 4.0F, 0.8F));
		EnumPhysicalDamageType.SMASH.setSkill(new SkillAbstract("damage.smash", "Smash")
		{
		}.setExpInfo(100, 4.0F, 0.8F));
		
		new ItemToolHead(MC.hard_hammer_flint);
		new ItemToolHead(MC.hard_hammer_rock);
		new ItemToolHead(MC.hard_hammer_metal);
		new ItemToolHead(MC.shovel_rock);
		new ItemToolHead(MC.shovel_metal);
		new ItemToolHead(MC.spade_hoe_rock);
		new ItemToolHead(MC.spear_rock);
		new ItemToolHead(MC.sickle_rock);
		new ItemToolHead(MC.axe_rock);
		new ItemToolHead(MC.axe_metal);
		new ItemToolHead(MC.pickaxe_metal);
		new ItemToolHead(MC.spinning_disk);
		new ItemToolHead(MC.chisel_rock);
		new ItemToolHead(MC.chisel_metal);
		new ItemToolHead(MC.bowsaw_metal);
		
		addTool(1, "adz.rock", "Adz", "Click top of a log to open woodwork table.", MC.adz_rock, new ToolAxe(EnumToolTypes.ADZ, 0.5F), true, true, SubTags.ROPE, SubTags.HANDLE, new BehaviorWoodworkable());
		addTool(2, "hammer.hard.flint", "Hammer", null, MC.hard_hammer_flint, new ToolHardHammer(0.7F), true, true, SubTags.ROPE, SubTags.HANDLE);
		addTool(3, "shovel.rock", "Shovel", null, MC.shovel_rock, new ToolShovel(0.9F), true, true, SubTags.ROPE, SubTags.HANDLE);
		addTool(4, "hammer.hard.rock", "Hammer", null, MC.hard_hammer_rock, new ToolHardHammer(0.9F), true, true, SubTags.ROPE, SubTags.HANDLE, new BehaviorPolishable(EnumToolTypes.AWL, 0, 0.5F, 'c', ' '));
		addTool(5, "spade.hoe.rock", "Spade Hoe", null, MC.spade_hoe_rock, new ToolHoe(EnumToolTypes.SPADE_HOE), true, true, SubTags.ROPE, SubTags.HANDLE, new BehaviorHoe(EnumToolTypes.SPADE_HOE, 60));
		addTool(6, "spear.rock", "Spear", "Right click to throw spear out.", MC.spear_rock, new ToolSpear(), true, true, SubTags.ROPE, SubTags.HANDLE, new BehaviorSpear());
		addTool(7, "sickle.rock", "Sickle", null, MC.sickle_rock, new ToolSickle(), true, true, SubTags.ROPE, SubTags.HANDLE, new BehaviorTool());
		addTool(8, "firestarter", "Firestarter", null, MC.firestarter, new ToolFirestarter(), false, false, null, null, new BehaviorFirestarter());
		addTool(9, "awl", "Awl", null, MC.awl, new ToolAwl(), false, false, null, null, new BehaviorPolishable(EnumToolTypes.AWL, 3, 1.0F, 'c', ' ', 'p'));
		addTool(10, "axe.rock", "Axe", null, MC.axe_rock, new ToolAxe(EnumToolTypes.AXE, 0.75F), true, true, SubTags.ROPE, SubTags.HANDLE, new BehaviorWoodworkable());
		addTool(11, "decorticating.plate", "Decorticating Plate", null, MC.decorticating_plate, new ToolDecorticatingPlate(), false, false, null, null);
		addTool(12, "decorticating.stick", "Decorticating Stick", null, MC.decorticating_stick, new ToolDecorticatingStick(), false, false, null, null);
		addTool(13, "whetstone", "Whetstone", null, MC.whetstone, new ToolWhetstone(), false, false, null, null, new BehaviorPolishable(EnumToolTypes.WHESTONE, 4, 0.75F, 'p', ' '));
		addTool(14, "biface", "Biface", "Multi-purpose tool", MC.biface, new ToolBiface(), false, false, null, null, new BehaviorPolishable(EnumToolTypes.BIFACE, 2, 1.25F, 'c', ' ', 'p'), new BehaviorWoodworkable());
		addTool(15, "bar.grizzly", "Bar Grizzly", null, MC.bar_grizzly, new ToolBarGrizzly(), false, false, null, null, new BehaviorBarGrizzly());
		addTool(16, "spinning.disk", "Spinning Tool", null, MC.spinning_disk, new ToolSpinningDisk(), false, true, null, SubTags.HANDLE);
		addTool(17, "chisel.rock", "Chisel", null, MC.chisel_rock, new ToolChisel(), true, true, SubTags.ROPE, SubTags.HANDLE, new BehaviorModeSwitching("chisel", "carve", "polish"));
		addTool(18, "axe.metal", "Axe", null, MC.axe_metal, new ToolAxe(EnumToolTypes.AXE, 1.0F), false, true, null, SubTags.HANDLE, new BehaviorWoodworkable());
		addTool(19, "shovel.metal", "Shovel", null, MC.shovel_metal, new ToolShovel(1.0F), false, true, null, SubTags.HANDLE);
		addTool(20, "pickaxe.metal", "Pickaxe", null, MC.pickaxe_metal, new ToolPickaxe(), false, true, null, SubTags.HANDLE);
		addTool(21, "hammer.hard.metal", "Hammer", null, MC.hard_hammer_metal, new ToolHardHammer(1.0F), false, true, null, SubTags.HANDLE, new BehaviorChiselHelpingSilkTouch());
		addTool(22, "chisel.metal", "Chisel", null, MC.chisel_metal, new ToolChisel(), false, true, null, SubTags.HANDLE, new BehaviorModeSwitching("chisel", "carve", "polish"));
		addTool(23, "bowsaw.metal", "Bowsaw", null, MC.bowsaw_metal, new ToolAxe(EnumToolTypes.BOW_SAW, 0.7F), false, true, null, SubTags.HANDLE, new BehaviorWoodworkable());
	}
	
	public static ToolProp addTool(int id, String name, String localName, String customToolInformation, MatCondition condition, IToolStat stat, boolean hasTie, boolean hasHandle, Judgable<? super Mat> filterHead, Judgable<? super Mat> filterTie, Judgable<? super Mat> filterHandle,
			IBehavior...behaviors)
	{
		return IBFS.iTool.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, filterHead, filterTie, filterHandle, behaviors);
	}
	
	public static ToolProp addTool(int id, String name, String localName, String customToolInformation, MatCondition condition, IToolStat stat, boolean hasTie, boolean hasHandle, Judgable<? super Mat> filterTie, Judgable<? super Mat> filterHandle, IBehavior...behaviors)
	{
		return IBFS.iTool.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, condition, filterTie, filterHandle, behaviors);
	}
}
