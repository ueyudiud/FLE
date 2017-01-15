/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

/**
 * @author ueyudiud
 */
public abstract class Link
{
	public final double[] resistance = new double[1];
	
	public abstract Node[] nodes();
	
	abstract void update(double current);
	
	public final double resistance()
	{
		return this.resistance[0];
	}
}