/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client;

import java.util.List;

import nebula.base.function.Applicable;
import nebula.client.util.IRenderRegister;
import nebula.common.LanguageManager;
import nebula.common.util.Game;
import nebula.common.util.ItemStacks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabBase extends CreativeTabs implements IRenderRegister
{
	private final String				localName;
	private final Applicable<ItemStack>	appliable;
	private ItemStack					stack;
	
	public CreativeTabBase(String lable, String localName, ItemStack stack)
	{
		this(lable, localName, Applicable.to(stack));
	}
	
	public CreativeTabBase(String lable, String localName, Applicable<ItemStack> appliable)
	{
		super(lable);
		this.localName = localName;
		this.appliable = appliable;
		Game.registerClientRegister(this);
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
		if (this.stack == null)
		{
			this.stack = this.appliable.applyOptional()
					.map(ItemStacks.COPY_ITEMSTACK).orElse(new ItemStack(Blocks.FIRE));
		}
		return this.stack.copy();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void displayAllRelevantItems(List<ItemStack> list)
	{
		for (Item item : Item.REGISTRY)
		{
			if (item == null)
			{
				continue;
			}
			for (CreativeTabs tab : item.getCreativeTabs())
			{
				if (tab == this)
				{
					item.getSubItems(item, this, list);
					break;
				}
			}
		}
	}
	
	@Override
	public boolean hasSearchBar()
	{
		return false;
	}
}
