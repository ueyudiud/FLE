package fle.core.item.resource;

import farcore.enums.EnumItem;
import farcore.interfaces.item.IItemInfo;
import fle.api.item.ItemResource;
import net.minecraft.item.ItemStack;

public class ItemStoneProduction extends ItemResource
{
	public ItemStoneProduction()
	{
		super("stone.production");
		EnumItem.stone_production.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "stone_plate", "Stone Plate", "stone_plate");
	}
	
	@Override
	public void addSubItem(int id, String name, String local, String iconName)
	{
		super.addSubItem(id, name, local, iconName);
	}
	
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/stone/" + iconName);
	}
}