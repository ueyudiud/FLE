package farcore.data;

import net.minecraft.item.Item;

public enum EnumItem
{
	log,
	debug,
	display_fluid,
	stone_chip,
	ore_chip,
	seed,
	stone_fragment,
	nugget,
	/** From fle. */
	tool;

	public Item item;

	public void set(Item item)
	{
		this.item = item;
	}
}