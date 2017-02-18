/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.item.instance;

import farcore.data.EnumItem;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;

/**
 * @author ueyudiud
 */
public class ItemIngot extends ItemMulti
{
	public ItemIngot()
	{
		super(MC.ingot);
		EnumItem.ingot.set(this);
	}
}