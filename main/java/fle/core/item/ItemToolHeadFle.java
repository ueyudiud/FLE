package fle.core.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.enums.EnumItem;
import farcore.enums.EnumItem.IInfomationable;
import farcore.item.ItemSubDamagable;
import farcore.item.ItemSubTool;
import farcore.lib.substance.SubstanceHandle;
import farcore.lib.substance.SubstanceTool;
import farcore.util.U;
import fle.api.item.behavior.BehaviorBase;
import fle.core.render.ItemToolHeadRenderInfo;
import fle.load.Langs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemToolHeadFle extends ItemSubDamagable implements IInfomationable
{
	protected Map<String, ImmutableList<SubstanceTool>> validTools = new HashMap();

	public ItemToolHeadFle()
	{
		super("toolheads");
		EnumItem.tool_head.set(new ItemStack(this));
		ItemToolFle.callInit();
	}
	
	public void addSubItem(int id, String name, String local, String icon, SubstanceTool...tools)
	{
		validTools.put(name, ImmutableList.copyOf(tools));
		super.addSubItem(id, name, local, BehaviorBase.SIMPLE, new ItemToolHeadRenderInfo("fle:tools/" + icon));
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(Entry<String, ImmutableList<SubstanceTool>> entry : validTools.entrySet())
		{
			String type = entry.getKey();
			for(SubstanceTool tool : entry.getValue())
			{
				list.add(a(type, tool));
			}
		}
	}
	
	@Override
	protected int getMaxCustomDamgae(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getInteger("maxDamage");
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		int maxUse = getMaxCustomDamgae(stack);
		float lastUse = (float) maxUse - getCustomDamage(stack);
		list.add(FarCore.translateToLocal(Langs.infoToolHeadMaterial, ItemSubTool.getToolMaterial(stack).getLocalName()));
		list.add(FarCore.translateToLocal(Langs.infoToolDamage, (int) (lastUse * 100), (int) (maxUse * 100)));
		super.addInformation(stack, player, list, flag);
	}

	private ItemStack a(String type, SubstanceTool head)
	{
		return a(type, head, (int) head.maxUses, 0);
	}
	public ItemStack a(String type, SubstanceTool head, int maxDamage, int damage)
	{
		if(!register.contain(type)) return null;
		ItemStack ret = new ItemStack(this, 1, register.id(type));
		NBTTagCompound nbt = U.Inventorys.setupNBT(ret, true);
		nbt.setString("head", head.getName());
		nbt.setInteger("maxDamage", maxDamage);
		setCustomDamage(ret, damage);
		return ret;
	}

	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return a((String) objects[0], SubstanceTool.VOID_TOOL);
			}
		}
		else if(objects.length == 2)
		{
			if(objects[0] instanceof String && objects[1] instanceof SubstanceTool)
			{
				return a((String) objects[0], (SubstanceTool) objects[1]);
			}
			else if(objects[0] instanceof String && objects[1] instanceof String)
			{
				return a((String) objects[0], SubstanceTool.getSubstance((String) objects[1]));
			}
		}
		return null;
	}
}