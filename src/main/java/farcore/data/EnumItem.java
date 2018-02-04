/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.data;

import net.minecraft.item.Item;

/**
 * To get item which is added by non-multi-generated.
 * 
 * @author ueyudiud
 *
 */
public enum EnumItem
{
	log,
	debug,
	display_fluid,
	pile,
	ore_chip,
	seed,
	/** From fle. */
	misc_resource,
	crop_related,
	tool;
	
	public Item item;
	
	public void set(Item item)
	{
		this.item = item;
	}
	
	public boolean available()
	{
		return this.item != null;
	}
}
