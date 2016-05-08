package fle.core.item.resource;

import farcore.enums.EnumItem;
import fle.api.item.ItemResource;
import net.minecraft.item.ItemStack;

public class ItemStoneChip extends ItemResource
{
	public ItemStoneChip()
	{
		super("stone.chip");
		EnumItem.stone_chip.set(new ItemStack(this));
		init();
	}

	private void init()
	{
		addSubItem(1, "stone", "Stone Chip", "stone");
		addSubItem(2, "andesite", "Andesite Chip", "andesite");
		addSubItem(3, "basalt", "Basalt Chip", "basalt");
		addSubItem(4, "limestone", "Lime Chip", "limestone");
		addSubItem(5, "netherrack", "Netherrack Chip", "netherrack");
		addSubItem(6, "obsidian", "Obsidian Chip", "obsidian");
		addSubItem(7, "peridotite", "Peridotite Chip", "peridotite");
		addSubItem(8, "rhyolite", "Rhyolite Chip", "rhyolite");
		addSubItem(9, "sandstone", "Sandstone Chip", "sandstone");
		addSubItem(10, "stone-compact", "Compact Stone Chip", "stone_compact");
		addSubItem(101, "flintSmall", "Flint Chip", "flint_small");
		addSubItem(102, "flintSharp", "Sharp Flint Chip", "flint_sharp");
	}
	
	@Override
	public void addSubItem(int id, String name, String local, String iconName)
	{
		super.addSubItem(id, name, local, "fle:resource/stone_chip/" + iconName);
	}
}