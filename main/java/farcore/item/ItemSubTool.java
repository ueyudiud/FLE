package farcore.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import farcore.FarCore;
import farcore.enums.EnumItem.IInfomationable;
import farcore.lib.substance.SubstanceHandle;
import farcore.lib.substance.SubstanceTool;
import farcore.util.U;
import fle.load.Langs;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemSubTool extends ItemSubDamagable implements IInfomationable
{
	protected Map<String, ImmutableList<SubstanceTool>> validTools = new HashMap();

	protected ItemSubTool(String unlocalized)
	{
		super(unlocalized);
		hasSubtypes = true;
	}
	protected ItemSubTool(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
		hasSubtypes = true;
	}
	protected ItemSubTool(String modid, String unlocalized, String unlocalizedTooltip) 
	{
		super(modid, unlocalized, unlocalizedTooltip);
		hasSubtypes = true;
	}
	
	@Override
	public boolean isFull3D()
	{
		return true;
	}
	
	@Override
	public boolean isDamageable()
	{
		return false;
	}
	
	@Override
	public boolean isItemTool(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag)
	{
		int maxUse = getMaxCustomDamgae(stack);
		int lastUse = maxUse - getCustomDamage(stack);
		list.add(FarCore.translateToLocal(Langs.infoToolHeadMaterial, getToolMaterial(stack).getLocalName()));
		list.add(FarCore.translateToLocal(Langs.infoToolHandleMaterial, getHandleMaterial(stack).getLocalName()));
		list.add(FarCore.translateToLocal(Langs.infoToolDamage, lastUse, maxUse));
		super.addInformation(stack, player, list, flag);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(Entry<String, ImmutableList<SubstanceTool>> entry : validTools.entrySet())
		{
			String type = entry.getKey();
			for(SubstanceTool tool : entry.getValue())
			{
				list.add(a(type, tool, SubstanceHandle.VOID_TOOL));
			}
		}
	}
	
	@Override
	protected int getMaxCustomDamgae(ItemStack stack)
	{
		return U.Inventorys.setupNBT(stack, false).getInteger("maxDamage");
	}
	
	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		if(!getToolClasses(stack).contains(toolClass)) return -1;
		return getToolMaterial(stack).harvestLevel;
	}

	public static SubstanceTool getToolMaterial(ItemStack stack)
	{
		return SubstanceTool.getSubstance(U.Inventorys.setupNBT(stack, false).getString("head"));
	}
	public static SubstanceHandle getHandleMaterial(ItemStack stack)
	{
		return SubstanceHandle.getSubstance(U.Inventorys.setupNBT(stack, false).getString("handle"));
	}

	private ItemStack a(String type, SubstanceTool head, SubstanceHandle handle)
	{
		return a(type, head, handle, (int) (head.maxUses * handle.usesMul));
	}
	private ItemStack a(String type, SubstanceTool head, SubstanceHandle handle, int maxDamage)
	{
		return a(type, head, handle, maxDamage, 0);
	}
	private ItemStack a(String type, SubstanceTool head, SubstanceHandle handle, int maxDamage, int damage)
	{
		if(!register.contain(type)) return null;
		ItemStack ret = new ItemStack(this, 1, register.id(type));
		NBTTagCompound nbt = U.Inventorys.setupNBT(ret, true);
		nbt.setString("head", head.getName());
		nbt.setString("handle", handle.getName());
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
				return a((String) objects[0], SubstanceTool.VOID_TOOL, SubstanceHandle.VOID_TOOL);
			}
		}
		else if(objects.length == 3)
		{
			if(objects[0] instanceof String && objects[1] instanceof SubstanceTool && objects[2] instanceof SubstanceHandle)
			{
				return a((String) objects[0], (SubstanceTool) objects[1], (SubstanceHandle) objects[2]);
			}
			else if(objects[0] instanceof String && objects[1] instanceof String && objects[2] instanceof String)
			{
				return a((String) objects[0], SubstanceTool.getSubstance((String) objects[1]), SubstanceHandle.getSubstance((String) objects[2]));
			}
		}
		return null;
	}
}