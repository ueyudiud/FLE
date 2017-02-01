package fle.loader;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumToolTypes;
import farcore.data.MC;
import farcore.data.SubTags;
import farcore.lib.item.IToolStat;
import farcore.lib.item.ItemTool.ToolProp;
import farcore.lib.material.MatCondition;
import fle.core.items.behavior.BehaviorHoe;
import fle.core.items.behavior.BehaviorSpear;
import fle.core.items.behavior.BehaviorTool;
import fle.core.items.tool.ToolAxe;
import fle.core.items.tool.ToolHardHammer;
import fle.core.items.tool.ToolHoe;
import fle.core.items.tool.ToolShovel;
import fle.core.items.tool.ToolSickle;
import fle.core.items.tool.ToolSpear;
import nebula.common.data.EnumToolType;
import nebula.common.item.IBehavior;
import nebula.common.util.IDataChecker;
import nebula.common.util.ISubTagContainer;

public class Tools
{
	public static void initalizeTools()
	{
		addTool(1, "adz.rock", "Adz", null, MC.adz_rock, new ToolAxe(EnumToolTypes.ADZ, 1.2F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.ADZ), new BehaviorTool());
		addTool(2, "hammer.hard.flint", "Hard Hammer", null, MC.hard_hammer_flint, new ToolHardHammer(1.5F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.HAMMER_DIGABLE), new BehaviorTool());
		addTool(3, "shovel.rock", "Shovel", null, MC.shovel_rock, new ToolShovel(1.8F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SHOVEL), new BehaviorTool());
		addTool(4, "hammer.hard.rock", "Hard Hammer", null, MC.hard_hammer_flint, new ToolHardHammer(2.0F), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.HAMMER_DIGABLE), new BehaviorTool());
		addTool(5, "spade.hoe.rock", "Spade Hoe", null, MC.spade_hoe_rock, new ToolHoe(EnumToolTypes.SPADE_HOE), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SPADE_HOE), new BehaviorHoe(EnumToolTypes.SPADE_HOE, 60));
		addTool(6, "spear.rock", "Spear", "Right click to throw spear out.", MC.spear_rock, new ToolSpear(), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SPEAR), new BehaviorSpear());
		addTool(7, "sickle.rock", "Sickle", null, MC.sickle_rock, new ToolSickle(), true, true, SubTags.ROPE, SubTags.HANDLE, ImmutableList.of(EnumToolTypes.SICKLE), new BehaviorTool());
	}
	
	public static ToolProp addTool(int id, String name, String localName, String customToolInformation, MatCondition condition,
			IToolStat stat, boolean hasTie, boolean hasHandle, IDataChecker<? extends ISubTagContainer> filterHead,
			IDataChecker<? extends ISubTagContainer> filterTie, IDataChecker<? extends ISubTagContainer> filterHandle,
			List<EnumToolType> toolTypes, IBehavior... behaviors)
	{
		return BlocksItems.tool.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, filterHead, filterTie, filterHandle, toolTypes, behaviors);
	}
	
	public static ToolProp addTool(int id, String name, String localName, String customToolInformation, MatCondition condition,
			IToolStat stat, boolean hasTie, boolean hasHandle,
			IDataChecker<? extends ISubTagContainer> filterTie, IDataChecker<? extends ISubTagContainer> filterHandle,
			List<EnumToolType> toolTypes, IBehavior... behaviors)
	{
		return BlocksItems.tool.addSubItem(id, name, localName, customToolInformation, condition, stat, hasTie, hasHandle, filterTie, filterHandle, toolTypes, behaviors);
	}
}