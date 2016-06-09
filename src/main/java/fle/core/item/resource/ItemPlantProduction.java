package fle.core.item.resource;

import farcore.enums.EnumItem;
import farcore.interfaces.item.IItemInfo;
import farcore.lib.substance.SubstanceWood;
import fle.api.item.ItemResource;
import fle.api.item.behavior.BehaviorBlockable;
import fle.core.item.behavior.BehaviorFirewood;
import fle.load.BlockItems;
import net.minecraft.item.ItemStack;

public class ItemPlantProduction extends ItemResource
{
	public ItemPlantProduction()
	{
		super("plant.production");
		EnumItem.plant_production.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "vine_rope", "Vine Rope", "vine_rope");
		addSubItem(1001, "leaves_dry", "Dry Leaves", "dry_leaves");
		addSubItem(1002, "dry_ramie_fiber", "Dry Ramie Fiber", "dry_ramie_fiber");
		addSubItem(1003, "ramie_rope", "Ramie Rope", "ramie_rope");
		addSubItem(1004, "ramie_bundle", "Ramie Rope Bundle", "ramie_rope_bundle");
	}
	
	@Override
	public void addSubItem(int id, String name, String local, String iconName)
	{
		super.addSubItem(id, name, local, iconName);
	}
	
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/plant/other/" + iconName);
	}
}