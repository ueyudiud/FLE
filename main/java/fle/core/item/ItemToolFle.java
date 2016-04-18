package fle.core.item;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import farcore.enums.EnumItem;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemSubTool;
import farcore.lib.render.item.ItemRenderInfoSimple;
import farcore.lib.substance.SubstanceTool;
import farcore.util.SubTag;
import farcore.util.U;
import fle.api.item.behavior.BehaviorAxe;
import fle.api.item.behavior.BehaviorFireStarter;
import fle.api.item.behavior.BehaviorShovel;
import fle.api.item.behavior.BehaviorStoneHammer;
import fle.api.item.behavior.BehaviorWoodHammer;
import fle.core.render.ItemToolCustomInfo;
import fle.core.render.ItemToolRenderInfo;
import fle.load.Substances;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemToolFle extends ItemSubTool
{
	public ItemToolFle()
	{
		super("tools");
		EnumItem.tool.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "rough_stone_adz", new BehaviorAxe(1F), "axe/stone_rough", false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone), SubstanceTool.class));
		addSubItem(3, "wood_hammer", new BehaviorWoodHammer(), "hammer/wood", true, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_wood), SubstanceTool.class));
		addSubItem(4, "flint_hammer", new BehaviorStoneHammer(), "hammer/flint", false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_flint), SubstanceTool.class));
		addSubItem(5, "firestarter", new BehaviorFireStarter(0.4F), new ItemToolCustomInfo("fle:tools/firestarter", "raw_wood_fire", "wood_fire"), 
				new SubstanceTool("raw_wood_fire").setMaxUses(12), new SubstanceTool("wood_fire").setMaxUses(32));
		addSubItem(11, "stone_axe", new BehaviorAxe(2F), "axe/stone", false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone_real), SubstanceTool.class));
		addSubItem(12, "stone_shovel", new BehaviorShovel(1F), "shovel/stone", false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone_real), SubstanceTool.class));
		addSubItem(13, "stone_hammer", new BehaviorStoneHammer(), "hammer/stone", false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone_real), SubstanceTool.class));
	}
	
	public void addSubItem(int id, String name, IItemInfo itemInfo, String iconName, boolean useSingleIcon, SubstanceTool...tools)
	{
		validTools.put(name, ImmutableList.copyOf(tools));
		if(useSingleIcon)
		{
			super.addSubItem(id, name, itemInfo, new ItemRenderInfoSimple("fle:tools/" + iconName));
		}
		else
		{
			super.addSubItem(id, name, itemInfo, new ItemToolRenderInfo("fle:tools/" + iconName));
		}
	}
	
	public void addSubItem(int id, String name, IItemInfo itemInfo, IItemIconInfo iconInfo, SubstanceTool...tools)
	{
		validTools.put(name, ImmutableList.copyOf(tools));
		super.addSubItem(id, name, itemInfo, iconInfo);
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		super.addInformation(stack, player, list, flag);
	}
}