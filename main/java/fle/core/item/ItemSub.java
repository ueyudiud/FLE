package fle.core.item;

import flapi.item.ItemFleMetaBase;

public class ItemSub extends ItemFleMetaBase
{
	public ItemSub(String aUnlocalized, String aUnlocalizedTooltip)
	{
		super(aUnlocalized, aUnlocalizedTooltip);
		hasSubtypes = true;
	}
}