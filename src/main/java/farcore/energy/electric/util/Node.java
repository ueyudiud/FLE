/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

import java.util.Map;

/**
 * @author ueyudiud
 */
public abstract class Node
{
	public final double[] voltage = new double[1];
	
	/**
	 * Use in rebuild net.
	 */
	int id = -1;
	
	public final int rebuildID()
	{
		return this.id;
	}
	
	abstract void refreshProperties();
	
	public abstract Map<Node, Link> links();
	
	public final double voltage()
	{
		return this.voltage[0];
	}
}