/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client;

import java.util.HashMap;
import java.util.Map;

import nebula.Log;
import nebula.client.render.IIconLoader;
import nebula.client.render.IIconRegister;
import nebula.client.render.IItemCustomRender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public final class NebulaRenderHandler implements IIconLoader
{
	public static final NebulaRenderHandler INSTNACE = new NebulaRenderHandler();
	public static boolean itemRenderingFlag = false;
	
	public static boolean isItemRendering()
	{
		return itemRenderingFlag;
	}
	
	public static void registerRender(Item item, IItemCustomRender render)
	{
		if (INSTNACE.renders.containsKey(item))
		{
			Log.warn("The {} has already has a custom render.", item);
			return;
		}
		INSTNACE.renders.put(item, render);
	}
	
	private final Map<Item, IItemCustomRender> renders = new HashMap<>();
	
	private NebulaRenderHandler()
	{
		NebulaTextureHandler.addIconLoader(this);
	}
	
	public IItemCustomRender getRender(ItemStack stack)
	{
		if (stack.getItem() == null) return null;
		return this.renders.get(stack.getItem());
	}
	
	@Override
	public void registerIcon(IIconRegister register)
	{
		this.renders.values().forEach(l->l.registerIcon(register));
	}
}