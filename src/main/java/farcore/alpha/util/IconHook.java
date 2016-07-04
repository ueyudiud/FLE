package farcore.alpha.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.alpha.interfaces.INamedIconRegister;
import farcore.alpha.interfaces.IRegisteredNameable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

@SideOnly(Side.CLIENT)
public class IconHook implements INamedIconRegister
{
	public static final IconHook instance = new IconHook();
	
	private IconHook(){	}
	
	final Map<String, IIcon> iconMap = new HashMap();

	IIconRegister register;
	
	static LinkedList<String> list = new LinkedList();
	
	public static void set(IIconRegister register)
	{
		instance.register = register;
	}
	
	public static void unset()
	{
		instance.register = null;
	}
	
	public static void push(IRegisteredNameable arg)
	{
		push(arg.getRegisteredName());
	}
	
	public static void push(String arg)
	{
		if(list.isEmpty())
		{
			list.add(arg);
		}
		else
		{
			list.addLast(list.getLast() + "." + arg);
		}
	}
	
	public static void pop()
	{
		if(!list.isEmpty())
		{
			list.removeLast();
		}
	}
	
	public static void register(IRegisteredNameable arg, String name, String textureName, IIconRegister register)
	{
		instance.iconMap.put(name == null ? arg.getRegisteredName() : arg.getRegisteredName() + "." + name, register.registerIcon(textureName));
	}
	
	public static IIcon get(IRegisteredNameable arg, String name)
	{
		return name == null ? instance.iconMap.get(arg.getRegisteredName()) :
				instance.iconMap.get(arg.getRegisteredName() + "." + name);
	}
	
	private static String get()
	{
		return list.getLast();
	}
	
	@Override
	public IIcon registerIcon(String name, String textureName)
	{
		if(register == null)
		{
			throw new RuntimeException("The hook is not registering icon.");
		}
		IIcon icon;
		instance.iconMap.put(name == null ? get() : get() + "." + name, icon = register.registerIcon(textureName));
		return icon;
	}

	@Override
	public IIcon getIconFromName(String name)
	{
		return name == null ? instance.iconMap.get(get()) :
			instance.iconMap.get(get() + "." + name);
	}
}