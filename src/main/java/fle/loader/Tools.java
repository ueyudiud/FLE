package fle.loader;

import java.util.List;

import com.google.common.collect.ImmutableList;

import farcore.data.EnumToolType;
import farcore.data.MC;
import farcore.lib.item.ItemTool.ToolProp;
import farcore.lib.item.behavior.IBehavior;
import farcore.lib.item.behavior.IToolStat;
import farcore.lib.material.MatCondition;
import farcore.lib.util.IDataChecker;
import farcore.lib.util.ISubTagContainer;
import farcore.lib.util.SubTag;
import fle.core.items.behavior.BehaviorHoe;
import fle.core.items.behavior.BehaviorSpear;
import fle.core.items.behavior.BehaviorTool;
import fle.core.items.tool.ToolAxe;
import fle.core.items.tool.ToolHardHammer;
import fle.core.items.tool.ToolHoe;
import fle.core.items.tool.ToolShovel;
import fle.core.items.tool.ToolSickle;
import fle.core.items.tool.ToolSpear;

public class Tools
{
	public static void initalizeTools()
	{
		addTool(1, "adz.rock", "Adz", null, MC.adz_rock, new ToolAxe(EnumToolType.adz, 1.2F), true, true, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.adz), new BehaviorTool());
		addTool(2, "hammer.hard.flint", "Hard Hammer", null, MC.hard_hammer_flint, new ToolHardHammer(1.5F), true, true, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.hammer_digable), new BehaviorTool());
		addTool(3, "shovel.rock", "Shovel", null, MC.shovel_rock, new ToolShovel(1.8F), true, true, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.shovel), new BehaviorTool());
		addTool(4, "hammer.hard.rock", "Hard Hammer", null, MC.hard_hammer_flint, new ToolHardHammer(2.0F), true, true, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.hammer_digable), new BehaviorTool());
		addTool(5, "spade.hoe.rock", "Spade Hoe", null, MC.spade_hoe_rock, new ToolHoe(EnumToolType.spade_hoe), true, true, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.spade_hoe), new BehaviorHoe(EnumToolType.spade_hoe, 60));
		addTool(6, "spear.rock", "Spear", "Right click to throw spear out.", MC.spear_rock, new ToolSpear(), true, true, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.spear), new BehaviorSpear());
		addTool(7, "sickle.rock", "Sickle", null, MC.sickle_rock, new ToolSickle(), true, true, SubTag.ROPE, SubTag.HANDLE, ImmutableList.of(EnumToolType.sickle), new BehaviorTool());
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