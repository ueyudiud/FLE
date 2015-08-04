package fle.api;

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

	public static int FLE_RENDER_ID;

	public static final int WATER_FREEZE_POINT = 273;
}