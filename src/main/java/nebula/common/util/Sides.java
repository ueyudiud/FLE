/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author ueyudiud
 */
public final class Sides
{
	private Sides()
	{
	}
	
	public static boolean isClient()
	{
		return FMLCommonHandler.instance().getSide().isClient();
	}
	
	public static boolean isServer()
	{
		return FMLCommonHandler.instance().getSide().isServer();
	}
	
	public static boolean isSimulating()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isServer();
	}
}
