package farcore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.render.RenderHandler;
import farcore.lib.util.LanguageManager;
import farcore.network.Network;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class FarCore
{
	public static final String ID = "farcore";

	public static final String OVERRIDE_ID = "faroverride";

	public static Network network;
	@SideOnly(Side.CLIENT)
	public static IIcon voidBlockIcon;
	@SideOnly(Side.CLIENT)
	public static IIcon voidItemIcon;

	public static CreativeTabs tabResourceBlock;
	public static CreativeTabs tabResourceItem;
	public static CreativeTabs tabTool;
	public static CreativeTabs tabMachine;
	public static CreativeTabs tabMaterial;

	/**
	 * The handler of block without rendering in inventory.
	 */
	@SideOnly(Side.CLIENT)
	public static RenderHandler handlerA;
	/**
	 * The handler of block with rendering in inventory.
	 */
	@SideOnly(Side.CLIENT)
	public static RenderHandler handlerB;

	public static String translateToLocal(String unlocal, Object...objects)
	{
		return LanguageManager.translateToLocal(unlocal, objects);
	}
}