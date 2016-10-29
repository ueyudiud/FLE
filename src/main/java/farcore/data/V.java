package farcore.data;

public class V
{
	//Non-constant values.
	public static boolean generateState = false;
	public static boolean removeErroredTile = false;
	public static int treeScanRange = 6;
	
	//Constant values.

	public static final float waterFreezePoint = 273.15F;

	/** Vacuum Heat Conduct Constant */
	public static final float k0 = 1.37498248E-1F;
	
	/** sqrt(2) float value */
	public static final float sq2f = 1.4142135F;
	/** sqrt(2) double value */
	public static final double sq2d = 1.4142135623730951D;
	/** sqrt(2 * π) float value */
	public static final double sqrt2π = 2.5066282746310007D;
}