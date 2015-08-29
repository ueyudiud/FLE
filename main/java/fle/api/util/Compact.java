package fle.api.util;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

import cpw.mods.fml.common.ModClassLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Compact
{
	private static Class<?> MultiItemTool;
	private static Class<?> ItemTool;
	
	public static boolean isGTTool(Item item)
	{
		try
		{
			if(MultiItemTool == null)
			{
				MultiItemTool = Class.forName("gregapi.item.MultiItemTool");
			}
			return MultiItemTool.isAssignableFrom(item.getClass()) || item.getClass() == MultiItemTool;
		}
		catch(Throwable e)
		{
			return false;
		}
	}
	
	public static boolean isFLETool(Item item)
	{
		try
		{
			if(ItemTool == null)
			{
				ItemTool = Class.forName("fle.core.item.ItemTool");
			}
			return ItemTool.isAssignableFrom(item.getClass()) || item.getClass() == ItemTool;
		}
		catch(Throwable e)
		{
			return false;
		}
	}
	
	public static void damageGTTool(ItemStack aStack, float damage)
	{
		try
		{
			if(MultiItemTool == null)
			{
				MultiItemTool = Class.forName("gregapi.item.MultiItemTool");
			}
			Method method = MultiItemTool.getMethod("doDamage", ItemStack.class, Long.class);
			method.invoke(null, aStack, (long) (damage * 100));
			return;
		}
		catch(Throwable e)
		{
			FleLog.getLogger().info("Fle fail to get item damage of gregtech.");
			return;
		}
	}
}