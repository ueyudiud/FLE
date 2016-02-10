package flapi.item;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCore;
import farcore.render.item.ItemTextureHandler;
import flapi.item.interfaces.IItemBehavior;
import flapi.util.Values;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemFleMetaStandard extends ItemFleMetaBase
{
	protected final Map<String, ItemTextureHandler> icons = new HashMap();
	
	protected ItemFleMetaStandard(String unlocalized)
	{
		super(unlocalized);
	}
	public final ItemFleMetaBase addSubItem1(int metaValue, String tag, ItemTextureHandler handler, IItemBehavior<ItemFleMetaBase> behaviour)
	{
		icons.put(tag, handler);
		return addSubItem(metaValue, tag, behaviour);
	}
	public final ItemFleMetaBase addSubItem1(int metaValue, String tag, String localized, ItemTextureHandler handler, IItemBehavior<ItemFleMetaBase> behaviour)
	{
		icons.put(tag, handler);
		return addSubItem(metaValue, tag, localized, behaviour);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register) 
	{
		for(ItemTextureHandler handler : icons.values())
		{
			handler.onIconRegister(register);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass)
	{
		try
		{
			return icons.get(itemBehaviors.name(getDamage(stack))).getIcon(pass);
		}
		catch(Exception exception)
		{
			return Values.EMPTY_ITEM_ICON;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderPasses(int metadata)
	{
		return itemBehaviors.contain(metadata) ? icons.get(itemBehaviors.name(metadata)).getRenderPass() : 1;
	}
}