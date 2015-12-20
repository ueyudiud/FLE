package fle.core.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import flapi.util.FleValue;
import fle.core.init.IB;

public class FleCreativeTab extends CreativeTabs
{
	public FleCreativeTab(String lable)
	{
		super(lable);
		FleValue.tabFLE = this;
	}

	@Override
	public Item getTabIconItem()
	{
		return IB.debug;
	}
}