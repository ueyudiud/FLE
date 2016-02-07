package farcore.util;

import farcore.FarCore;
import fle.core.init.IBF;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public abstract class FleCreativeTab extends CreativeTabs implements IUnlocalized
{
	public FleCreativeTab(String lable, String localized)
	{
		super(lable);
		FarCore.lang.registerLocal(this, localized);
	}
	
	public FleCreativeTab registerLocal(String locale, String localized)
	{
		FarCore.lang.registerLocal(locale, this, localized);
		return this;
	}
	
	@Override
	public String getUnlocalized()
	{
		return super.getTranslatedTabLabel();
	}
	
	@Override
	public String getTabLabel()
	{
		return super.getTabLabel();
	}
	
	@Override
	public String getTranslatedTabLabel()
	{
		return FarCore.lang.translateToLocal(this);
	}

	public abstract Item getTabIconItem();
}