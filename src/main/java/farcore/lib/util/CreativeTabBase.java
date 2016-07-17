package farcore.lib.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class CreativeTabBase extends CreativeTabs
{
	public CreativeTabBase(String lable, String localName)
	{
		super(lable);
		LanguageManager.registerLocal(super.getTranslatedTabLabel(), localName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return LanguageManager.translateToLocal(super.getTranslatedTabLabel());
	}

	@Override
	public Item getTabIconItem()
	{
		return null;
	}

	@Override
	public abstract ItemStack getIconItemStack();
}