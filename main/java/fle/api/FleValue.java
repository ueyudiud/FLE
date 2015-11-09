package fle.api;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.util.FleDecimalFormat;

public class FleValue 
{
	public static final String TEXTURE_FILE = "fle";
	public static final String VOID_ICON_FILE = "void";

	public static final int MAX_STACK_SIZE = 64;

	public static final int[][] MACHINE_FACING = new int[][]
			{
		new int[]{0, 1, 2, 3, 4, 5},
		new int[]{0, 1, 3, 2, 5, 4},
		new int[]{0, 1, 2, 3, 4, 5},
		new int[]{0, 1, 3, 2, 5, 4},
		new int[]{0, 1, 5, 4, 2, 3},
		new int[]{0, 1, 4, 5, 3, 2},
			};
	
	public static final int is[] = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, -2147483648};
	public static final int CAPACITY[] = {250, 500, 1000, 2000, 3000, 4000, 5000, 7500, 10000, 12000, 16000, 24000, 30000, 36000, 40000, 48000, 64000, 80000, 128000, 160000};

	public static int FLE_NOINV_RENDER_ID;
	public static int FLE_RENDER_ID;

	@SideOnly(Side.CLIENT)
	public static CreativeTabs tabFLE;
	
	public static final int WATER_FREEZE_POINT = 273;
	public static final int ingot_mol = 36;

	public static final FleDecimalFormat format_MJ = new FleDecimalFormat(EnumChatFormatting.LIGHT_PURPLE, "############0MJ");
	public static final FleDecimalFormat format_K = new FleDecimalFormat(EnumChatFormatting.YELLOW, "############0K");
	public static final FleDecimalFormat format_L = new FleDecimalFormat(EnumChatFormatting.AQUA, "############0L");
	public static final FleDecimalFormat format_progress = new FleDecimalFormat(EnumChatFormatting.GREEN, "##0.00%");
	public static final String info_shift = String.format("Press %sshift %sto get more info.", EnumChatFormatting.AQUA.toString(), EnumChatFormatting.GRAY.toString());
	
}