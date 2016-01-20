package flapi.util;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FleValue 
{
	/**
	 * The locate of condition icon collection, see {@link fle.api.gui.GuiCondition}.
	 * Use renderEngine to bind this location when used in GUI.
	 */
	@SideOnly(Side.CLIENT)
	public static final ResourceLocation conditionLocate = new ResourceLocation("textures/atlas/condition.png");
	
	public static final String TEXTURE_FILE = "fle";
	public static final String VOID_ICON_FILE = "void";

	public static final int TILE_GUI_CHANNEL = 0x10;
	public static final int COVER_GUI_CHANNEL = 0x20;
	
	public static final int MAX_STACK_SIZE = 64;

	//Removed, see flapi.util.Direction;
	//public static final Direction[] MACHINE_ROTATION = ;
	//public static final int[][] MACHINE_FACING = ;
	
	public static final int is[] = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, -2147483648};
	public static final int CAPACITY[] = {250, 500, 1000, 2000, 3000, 4000, 5000, 7500, 10000, 12000, 16000, 24000, 30000, 36000, 40000, 48000, 64000, 80000, 128000, 160000};

	public static int FLE_NOINV_RENDER_ID;
	public static int FLE_RENDER_ID;

	@SideOnly(Side.CLIENT)
	public static CreativeTabs tabFLE;

	/**
	 * The fluid control value, use this value in fluid resource location domain
	 * to this and path named fluid name.<br>
	 * The render will load in fluid path.
	 */
	public static String FLUID_RENDER_CONTROL = "*d>&";
	
	/**
	 * Move to constant current class.
	 * @see farcore.util.Vs
	 */
//	public static final int WATER_FREEZE_POINT = 273;
//	public static final int ingot_mol = 36;

	public static final FleDecimalFormat format_level = new FleDecimalFormat(EnumChatFormatting.RED, "############0.0");
	public static final FleDecimalFormat format_MJ = new FleDecimalFormat(EnumChatFormatting.LIGHT_PURPLE, "############0MJ");
	public static final FleDecimalFormat format_K = new FleDecimalFormat(EnumChatFormatting.YELLOW, "############0K");
	public static final FleDecimalFormat format_L = new FleDecimalFormat(EnumChatFormatting.AQUA, "############0L");
	public static final FleDecimalFormat format_progress = new FleDecimalFormat(EnumChatFormatting.GREEN, "##0.00%");
	public static final FleDecimalFormat format_mol = new FleDecimalFormat(EnumChatFormatting.DARK_GREEN, "########0mol");
	public static final String info_shift = String.format("Press %sshift %sto get more info.", EnumChatFormatting.AQUA.toString(), EnumChatFormatting.GRAY.toString());
	
}