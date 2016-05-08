package fle.api.item;

import java.util.List;

import farcore.enums.EnumItem.IInfomationable;
import farcore.interfaces.IItemIconInfo;
import farcore.interfaces.item.IItemInfo;
import farcore.item.ItemSubBehavior;
import farcore.lib.render.item.ItemRenderInfoSimple;
import fle.api.item.behavior.BehaviorBase;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemResource extends ItemSubBehavior implements IInfomationable
{
	protected ItemResource(String unlocalized)
	{
		super(unlocalized);
	}
	protected ItemResource(String unlocalized, String unlocalizedTooltip)
	{
		super(unlocalized, unlocalizedTooltip);
	}
	protected ItemResource(String modid, String unlocalized, String unlocalizedTooltip)
	{
		super(modid, unlocalized, unlocalizedTooltip);
	}
	
	public void addSubItem(int id, String name, String local, String iconName)
	{
		addSubItem(id, name, local, BehaviorBase.SIMPLE, iconName);
	}
	
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName)
	{
		super.addSubItem(id, name, local, itemInfo, new ItemRenderInfoSimple(iconName));
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		for(String name : register.names())
		{
			list.add(new ItemStack(item, 1, register.id(name)));
		}
	}
	
	@Override
	public ItemStack provide(int size, Object... objects)
	{
		if(objects.length == 1)
		{
			if(objects[0] instanceof String)
			{
				return new ItemStack(this, size, register.id((String) objects[0]));
			}
			else if(objects[0] instanceof Number)
			{
				return new ItemStack(this, size, ((Number) objects[0]).intValue());
			}
		}
		return null;
	}
}