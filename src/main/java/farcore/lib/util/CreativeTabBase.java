package farcore.lib.util;

import farcore.util.U;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CreativeTabBase extends CreativeTabs implements IRenderRegister
{
	private String localName;
	
	public CreativeTabBase(String lable, String localName)
	{
		super(lable);
		this.localName = localName;
		U.Mod.registerClientRegister(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
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
	@SideOnly(Side.CLIENT)
	public abstract ItemStack getIconItemStack();
}