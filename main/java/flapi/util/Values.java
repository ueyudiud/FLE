package flapi.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.util.FleCreativeTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class Values 
{
	public static int FLE_BASIC_RENDER_ID;
	public static int FLE_CUSTOM_RENDER_ID;
	public static final String TEXTURE_FILE = "fle";

	public static final String mod_resource = "fle|resource";
	public static final String mod_dictionary = "fle|dictionary";
	
	/**
	 * Far Land Era creative tab.
	 */
	public static FleCreativeTab tabFLE;
	public static FleCreativeTab tabFLEResource;
	public static FleCreativeTab tabFLETools;
	public static FleCreativeTab tabFLEMachine;
	public static FleCreativeTab tabFLEDecoration;
	
	@SideOnly(Side.CLIENT)
	public static IIcon EMPTY_BLOCK_ICON;
	@SideOnly(Side.CLIENT)
	public static IIcon EMPTY_ITEM_ICON;
}