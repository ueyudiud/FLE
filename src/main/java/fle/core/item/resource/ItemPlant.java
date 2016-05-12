package fle.core.item.resource;

import farcore.enums.EnumBlock;
import farcore.enums.EnumItem;
import farcore.interfaces.item.IItemInfo;
import farcore.lib.substance.SubstanceWood;
import fle.api.item.ItemResource;
import fle.api.item.behavior.BehaviorBlockable;
import fle.load.BlockItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ItemPlant extends ItemResource
{
	public ItemPlant()
	{
		super("plant");
		EnumItem.plant.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "seed_oak", "Acorn", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("oak")), "seed_oak");
		addSubItem(2, "seed_spruce", "Spruce Cone", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("spruce")), "seed_spruce");
		addSubItem(3, "seed_birch", "Birch Fruit", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("birch")), "seed_birch");
		addSubItem(4, "seed_ceiba", "Ceiba Seed", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("ceiba")), "seed_ceiba");
		addSubItem(5, "seed_acacia", "Acacia Seed", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("acacia")), "seed_acacia");
		addSubItem(6, "seed_oak-black", "Dark Acorn", new BehaviorBlockable(1, BlockItems.sapling, SubstanceWood.getWoods().id("oak-black")), "seed_oak-black");
		
		addSubItem(1001, "leaves_1", "Leaves", "leaves_1");
		addSubItem(1002, "leaves_2", "Leaves", "leaves_2");

		addSubItem(2001, "branch_oak", "Oak Branch", "branch_oak");
		addSubItem(2002, "branch_spruce", "Spruce Branch", "branch_spruce");
		addSubItem(2003, "branch_birch", "Birch Branch", "branch_birch");
		addSubItem(2004, "branch_ceiba", "Ceiba Branch", "branch_ceiba");
		addSubItem(2005, "branch_acacia", "Acacia Branch", "branch_acacia");
		addSubItem(2006, "branch_oak-black", "Dark Oak Branch", "branch_oak-black");
		addSubItem(2101, "branch_bush", "Bush Branch", "branch_bush");

		addSubItem(3001, "firewood_oak", "Oak Firewood", "firewood/oak");
		addSubItem(3002, "firewood_spruce", "Spruce Firewood", "firewood/spruce");
		addSubItem(3003, "firewood_birch", "Birch Firewood", "firewood/birch");
		addSubItem(3004, "firewood_ceiba", "Ceiba Firewood", "firewood/ceiba");
		addSubItem(3005, "firewood_acacia", "Acacia Firewood", "firewood/acacia");
		addSubItem(3006, "firewood_oak-black", "Dark Oak Firewood", "firewood/oak-black");
		addSubItem(3011, "firewood_aspen", "Aspen Firewood", "firewood/aspen");
		addSubItem(3012, "firewood_morus", "Morus Firewood", "firewood/morus");
		addSubItem(3013, "firewood_willow", "Willow Firewood", "firewood/willow");
		
		addSubItem(10001, "vine", "Vine", "vine");
		addSubItem(10002, "vine_rope", "Vine Rope", "vine_rope");
	}
	
	@Override
	public void addSubItem(int id, String name, String local, String iconName)
	{
		super.addSubItem(id, name, local, iconName);
	}
	
	@Override
	public void addSubItem(int id, String name, String local, IItemInfo itemInfo, String iconName)
	{
		super.addSubItem(id, name, local, itemInfo, "fle:resource/plant/" + iconName);
	}
}