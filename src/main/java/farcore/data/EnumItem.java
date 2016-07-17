package farcore.data;

import net.minecraft.item.Item;

public enum EnumItem
{
	log,
	debug,
	display_fluid,

	stone_chip;

	public Item item;

	public void set(Item item)
	{
		this.item = item;
	}
}