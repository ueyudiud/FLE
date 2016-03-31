package fle.api.util;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.IIcon;

public class V
{
	public static boolean debug;
	
	@SideOnly(Side.CLIENT)
	public static IIcon voidBlockIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon voidItemIcon;
}