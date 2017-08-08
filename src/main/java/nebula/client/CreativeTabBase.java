package nebula.client;

import nebula.Log;
import nebula.base.function.Appliable;
import nebula.client.util.IRenderRegister;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabBase extends CreativeTabs implements IRenderRegister
{
	private final String localName;
	private final Appliable<ItemStack> appliable;
	private ItemStack stack;
	
	public CreativeTabBase(String lable, String localName, ItemStack stack)
	{
		this(lable, localName, Appliable.to(stack));
	}
	public CreativeTabBase(String lable, String localName, Appliable<ItemStack> appliable)
	{
		super(lable);
		this.localName = localName;
		this.appliable = appliable;
		Renders.registerClientRegister(this);
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
		if(this.stack == null)
		{
			this.stack = ItemStack.copyItemStack(this.appliable.apply());
			if(this.stack == null)
			{
				this.stack = new ItemStack(Blocks.FIRE);
				Log.error("The creative tab is missing a stack for display, use fire instead.");
			}
		}
		return this.stack.copy();
	}
}