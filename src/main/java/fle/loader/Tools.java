package fle.loader;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.lib.item.IToolStat;
import farcore.lib.item.ItemTool.ToolProp;
import farcore.lib.material.Mat;
import farcore.lib.material.MatCondition;
import fle.core.items.behavior.BehaviorHoe;
import fle.core.items.behavior.BehaviorSpear;
import fle.core.items.behavior.BehaviorTool;
import fle.core.items.tool.ToolAxe;
import fle.core.items.tool.ToolDecorticatingPlate;
import fle.core.items.tool.ToolDecorticatingStick;
import fle.core.items.tool.ToolHardHammer;
import fle.core.items.tool.ToolHoe;
import fle.core.items.tool.ToolShovel;
import fle.core.items.tool.ToolSickle;
import fle.core.items.tool.ToolSpear;
import nebula.common.base.Judgable;
import nebula.common.item.IBehavior;
import nebula.common.tool.EnumToolType;

public class Tools
{
	public static void initalizeTools()
	{
		addTool(1, "adz.rock", "Adz", null, MC.adz_rock, new ToolAxe(EnumToolTypes.ADZ, 0.5F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.ADZ), new BehaviorTool());
		addTool(2, "hammer.hard.flint", "Hard Hammer", null, MC.hard_hammer_flint, new ToolHardHammer(0.7F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.HAMMER_DIGABLE), new BehaviorTool());
		addTool(3, "shovel.rock", "Shovel", null, MC.shovel_rock, new ToolShovel(0.9F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SHOVEL), new BehaviorTool());
		addTool(4, "hammer.hard.rock", "Hard Hammer", null, MC.hard_hammer_flint, new ToolHardHammer(0.9F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.HAMMER_DIGABLE), new BehaviorTool());
		addTool(5, "spade.hoe.rock", "Spade Hoe", null, MC.spade_hoe_rock, new ToolHoe(EnumToolTypes.SPADE_HOE), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SPADE_HOE), new BehaviorHoe(EnumToolTypes.SPADE_HOE, 60));
		addTool(6, "spear.rock", "Spear", "Right click to throw spear out.", MC.spear_rock, new ToolSpear(), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SPEAR), new BehaviorSpear());
		addTool(7, "sickle.rock", "Sickle", null, MC.sickle_rock, new ToolSickle(), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SICKLE), new BehaviorTool());
		addTool(11, "decorticating.plate", "Decorticating Plate", null, MC.decorticating_plate, new ToolDecorticatingPlate(), false, false, null, null, ImmutableList.of(EnumToolTypes.DECORTICATING_PLATE));
		addTool(12, "decorticating.stick", "Decorticating Stick", null, MC.decorticating_stick, new ToolDecorticatingStick(), false, false, null, null, ImmutableList.of(EnumToolTypes.DECORTICATING_STICK));
	}
	
	public static ToolProp addTool(int id, String name, String localName, String customToolInformation, MatCondition condition,
			IToolStat stat, boolean hasTie, boolean hasHandle, Judgable<? super Mat> filterHead,
			Judgable<? super Mat> filterTie, Judgable<? super Mat> filterHandle,
			List<EnumToolType> toolTypes, IBehavior... behaviors)
	{
		return BlocksItems.tool.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, filterHead, filterTie, filterHandle, toolTypes, behaviors);
	}
	
	public static ToolProp addTool(int id, String name, String localName, String customToolInformation, MatCondition condition,
			IToolStat stat, boolean hasTie, boolean hasHandle,
			Judgable<? super Mat> filterTie, Judgable<? super Mat> filterHandle,
			List<EnumToolType> toolTypes, IBehavior... behaviors)
	{
		return BlocksItems.tool.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, filterTie, filterHandle, toolTypes, behaviors);
	}
}