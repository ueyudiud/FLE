package fle.core.item.resource;

import farcore.enums.EnumItem;
import fle.api.item.ItemResource;
import net.minecraft.item.ItemStack;

public class ItemStoneFragment extends ItemResource
{
	public ItemStoneFragment()
	{
		super("stone.fragment");
		EnumItem.stone_fragment.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "stone", "Stone Fragment", "stone");
		addSubItem(2, "andesite", "Andesite Fragment", "andesite");
		addSubItem(3, "basalt", "Basale Fragment", "basalt");
		addSubItem(4, "limestone", "Lime Fragment", "limestone");
		addSubItem(5, "netherrack", "Netherrack Fragment", "netherrack");
		addSubItem(6, "obsidian", "Obsidian Fragment", "obsidian");
		addSubItem(7, "peridotite", "Peridotite Fragment", "peridotite");
		addSubItem(8, "rhyolite", "Rhyolite Fragment", "rhyolite");
		addSubItem(9, "sandstone", "Sandstone Fragment", "sandstone");
		addSubItem(10, "stone-compact", "Compact Stone Fragment", "stone_compact");
		addSubItem(101, "flint", "Flint Fragment", "flint");
	}
	
	@Override
	public void addSubItem(int id, String name, String local, String iconName)
	{
		super.addSubItem(id, name, local, "fle:resource/stone_fragment/" + iconName);
	}
}