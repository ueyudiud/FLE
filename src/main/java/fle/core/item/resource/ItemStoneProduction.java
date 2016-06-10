package fle.core.item.resource;

import farcore.enums.EnumItem;
import farcore.interfaces.item.IItemInfo;
import fle.api.item.ItemResource;
import fle.core.item.behavior.BehaviorArgilBall;
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
		addSubItem(2, "argil_plate_unsmelted", "Unsmelted Argil Plate", "argil_plate_unsmelted");
		
		addSubItem(1001, "lime_dust", "Limestone Dust", "lime_dust");
		addSubItem(1002, "sand_dust", "Sand Dust", "sand_dust");
		
		addSubItem(2001, "argil_ball", "Argil Ball", new BehaviorArgilBall(), "argil_ball");

		addSubItem(3001, "argil_brick_unsmelted", "Unsmelted Argil Brick", "argil_brick_unsmelted");
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