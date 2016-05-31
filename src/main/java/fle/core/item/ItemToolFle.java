package fle.core.item;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.enums.EnumDamageResource;
import farcore.enums.EnumItem;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.ICustomDamageBehavior;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemSubTool;
import farcore.lib.recipe.ICraftingInventory;
import farcore.lib.render.item.ItemRenderInfoSimple;
import farcore.lib.substance.SubstanceTool;
import farcore.util.SubTag;
import farcore.util.U;
import fle.api.item.behavior.BehaviorAwl;
import fle.api.item.behavior.BehaviorAxe;
import fle.api.item.behavior.BehaviorBarGrizzly;
import fle.api.item.behavior.BehaviorBase;
import fle.api.item.behavior.BehaviorFireStarter;
import fle.api.item.behavior.BehaviorKnife;
import fle.api.item.behavior.BehaviorShovel;
import fle.api.item.behavior.BehaviorStoneHammer;
import fle.api.item.behavior.BehaviorWhetstone;
import fle.api.item.behavior.BehaviorWoodHammer;
import fle.core.render.ItemToolCustomInfo;
import fle.core.render.ItemToolRenderInfo;
import fle.load.Substances;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemToolFle extends ItemSubTool
{
	public static void callInit()
	{
		if(EnumItem.tool.initialised() && EnumItem.tool_head.initialised())
		{
			((ItemToolFle) EnumItem.tool.item()).init();
		}
	}
	
	public ItemToolFle()
	{
		super("tools");
		EnumItem.tool.set(new ItemStack(this));
		callInit();
	}

	private void init()
	{
		addSubItem(1, "rough_stone_adz", "Rough Stone Adz", new BehaviorAxe(1F), "axe/stone_rough", false, false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone), SubstanceTool.class));
		addSubItem(2, "flint_awl", "Flint Awl", new BehaviorAwl(), "awl", false, false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_flint), SubstanceTool.class));
		addSubItem(3, "wood_hammer", "Wooden Hammer", new BehaviorWoodHammer(), "hammer/wood", true, false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_wood), SubstanceTool.class));
		addSubItem(4, "flint_hammer", "Flint Hammer", new BehaviorStoneHammer(), "hammer/flint", false, true, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_flint), SubstanceTool.class));
		addSubItem(5, "firestarter", "Firestarter", new BehaviorFireStarter(0.4F), new ItemToolCustomInfo("fle:tools/firestarter", "raw_wood_fire", "wood_fire"),
				new SubstanceTool("raw_wood_fire").setMaxUses(12), new SubstanceTool("wood_fire").setMaxUses(32));
		addSubItem(6, "bar_grizzly", "Bar Grizzly", new BehaviorBarGrizzly(), new ItemToolCustomInfo("fle:tools/bar_grizzly", "simple_bar_grizzly"), 
				new SubstanceTool("simple_bar_grizzly").setMaxUses(128));
		addSubItem(7, "flint_knife", "Flint Knife", new BehaviorKnife(), "knife/flint", false, true, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_flint), SubstanceTool.class));
		addSubItem(11, "stone_axe", "Stone Axe", new BehaviorAxe(2F), "axe/stone", false, true, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone_real), SubstanceTool.class));
		addSubItem(12, "stone_shovel", "Stone Shovel", new BehaviorShovel(1F), "shovel/stone", false, true, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone), SubstanceTool.class));
		addSubItem(13, "stone_hammer", "Stone Hammer", new BehaviorStoneHammer(), "hammer/stone", false, true, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone_real), SubstanceTool.class));
		addSubItem(14, "whetstone", "Whetstone", new BehaviorWhetstone(), "whetstone", false, false, U.Lang.cast(SubstanceTool.getSubstances(SubTag.TOOL_stone_real), SubstanceTool.class));
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName, boolean useSingleIcon, boolean hasHead, SubstanceTool...tools)
	{
		validTools.put(name, ImmutableList.copyOf(tools));
		if(useSingleIcon)
		{
			super.addSubItem(id, name, local, itemInfo, new ItemRenderInfoSimple("fle:tools/" + iconName));
		}
		else
		{
			super.addSubItem(id, name, local, itemInfo, new ItemToolRenderInfo("fle:tools/" + iconName));
		}
		if(hasHead)
		{
			((ItemToolHeadFle) EnumItem.tool_head.item()).addSubItem(id, name, local, iconName, tools);
		}
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, IItemIconInfo iconInfo, SubstanceTool...tools)
	{
		validTools.put(name, ImmutableList.copyOf(tools));
		super.addSubItem(id, name, local, itemInfo, iconInfo);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		super.addInformation(stack, player, list, flag);
	}
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return 1;
	}
	
	@Override
	public ItemStack getCraftedItem(ItemStack stack, ICraftingInventory crafting)
	{
		IItemInfo info = register.get(getDamage(stack));
		if(info instanceof ICustomDamageBehavior)
		{
			return ((ICustomDamageBehavior) info).getCraftedItem(stack, crafting);
		}
		else
		{
			damangeItem(stack, 1F, null, EnumDamageResource.CRAFT);
		}
		return U.Inventorys.valid(stack);
	}
}