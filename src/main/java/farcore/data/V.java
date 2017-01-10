/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.data;

import net.minecraftforge.fluids.Fluid;

/**
 * Some constant value in mod.
 * @author ueyudiud
 *
 */
public class V
{
	//Non-constant values.
	public static boolean generateState = false;
	public static boolean removeErroredTile = false;
	public static int treeScanRange = 6;
	
	//Constant values.
	
	public static final int WATER_FREEZE_POINT_I = 273;
	public static final float WATER_FREEZE_POINT_F = 273.15F;
	
	public static final int BUCKET_CAPACITY = Fluid.BUCKET_VOLUME;
	
	/** Vacuum Heat Conduct Constant */
	public static final float k0 = 1.37498248E-1F;
	
	/** sqrt(2) float value */
	public static final float sq2f = 1.4142135F;
	/** sqrt(2) double value */
	public static final double sq2d = 1.4142135623730951D;
	/** sqrt(2 * π) float value */
	public static final double sqrt2π = 2.5066282746310007D;
}