package fle.core.item;

import net.minecraft.item.ItemStack;
import fle.api.item.ItemFleMetaBase;

public class ItemSub extends ItemFleMetaBase
{
	public ItemSub(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		hasSubtypes = true;
	}
}