package farcore.lib.util;

import farcore.util.Appliable;
import farcore.util.U;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabBase extends CreativeTabs implements IRenderRegister
{
	private final String localName;
	private final Appliable<ItemStack> appliable;
	
	public CreativeTabBase(String lable, String localName, Appliable<ItemStack> appliable)
	{
		super(lable);
		this.localName = localName;
		this.appliable = appliable;
		U.Mod.registerClientRegister(this);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		LanguageManager.registerLocal(super.getTranslatedTabLabel(), this.localName);
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
	public final ItemStack getIconItemStack()
	{
		return this.appliable.apply();
	}
}