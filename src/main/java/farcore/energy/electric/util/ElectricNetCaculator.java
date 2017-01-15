/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.lib.collection.Ety;

/**
 * @author ueyudiud
 */
public class ElectricNetCaculator
{
	private int length;
	private int[][] nodes;//Node Index -> Link Index list.
	private int[][] links;//Link Index -> Node Index list.
	private double[][] nodeProperties;//Node Index -> Node Properties
	private double[][] linkProperties;//Link Index -> Node Properties
	
	private EquationResolver resolver = new EquationResolver();
	
	private double[] result;
	
	public synchronized void refreshNodes(Routes routes)
	{
		int length1 = routes.rebuildLinks.size();
		List<Link> links = new ArrayList();
		Map<Link, Entry<int[], double[]>> linkMap = new HashMap();
		if(length1 > this.length)
		{
			this.nodes = new int[length1][];
			this.nodeProperties = new double[length1][];
		}
		this.length = length1;
		for(int i = 0; i < this.length; ++i)
		{
			NodeRebuild node = routes.rebuildNodes.get(i);
			int[] linksID = new int[node.links().size()];
			int j = 0;
			for(Link link : node.links().values())
			{
				int id;
				if(!links.contains(link))
				{
					id = links.size();
					links.add(link);
					Node[] nodes = link.nodes();
					linkMap.put(link, new Ety(new int[]{nodes[0].rebuildID(), nodes[1].rebuildID()}, link.resistance));
				}
				else id = links.indexOf(link);
				linksID[j] = id;
				++j;
			}
			this.nodes[i] = linksID;
			this.linkProperties[i] = node.voltage;
		}
		this.links = new int[links.size()][];
		this.linkProperties = new double[links.size()][];
		for(int j = 0; j < links.size(); ++j)
		{
			Entry<int[], double[]> entry = linkMap.get(links.get(j));
			this.links[j] = entry.getKey();
			this.linkProperties[j] = entry.getValue();
		}
	}
	
	public void caculate()
	{
		this.resolver.rewind(this.length);
		double[] I = new double[this.length];
		synchronized (this)
		{
			for (int i = 0; i < this.length; ++i)
			{
				I[i] = getBaseValue(i);
				if(!Double.isNaN(I[i]))
				{
					this.resolver.push(1, i);//Identity
				}
				else
				{
					I[i] = 0;
					for (int j = 0; j < this.length; ++j)
					{
						this.resolver.push(getCoefficient(i, j));
					}
					this.resolver.enter();
				}
			}
		}
		this.resolver.finalized();
		if(!this.resolver.solve(I)) throw new RuntimeException("Invalid solving!");
		synchronized (this)
		{
			if(this.result.length < this.length)
			{
				this.result = new double[this.length];
			}
			System.arraycopy(I, 0, this.result, 0, this.length);
		}
	}
	
	private boolean presentUncaculatableVoltage(int id)
	{
		return !Double.isNaN(this.nodeProperties[id][0]);
	}
	
	/**
	 * Get basic current of each node, add in matrix.
	 * @param x Node index.
	 * @return
	 */
	private double getBaseValue(int x)
	{
		return this.nodeProperties[x][0];
	}
	
	/**
	 *  The equation used Kirchhoff laws, the independent variable is
	 *  each node voltage.<br>
	 *  The coefficient is resistance of each node.<br>
	 *  Example:
	 *    U1 * G(total) - U2 * G12 - U3 * G13 = 0
	 *  - U1 * G21 + U2 * G(total) - U3 * G31 = 0
	 *  - U1 * G31 - U2 * G31 - U3 * G(total) = 0<br>
	 *  This method is to get coefficient of each element.
	 * 	@param x The search node index.
	 *  @param y The target node index.
	 *  @return The coefficient added in matrix.
	 */
	private double getCoefficient(int x, int y)
	{
		if(presentUncaculatableVoltage(y))
		{
			return 0;//The voltage is selected, needn't to caculate.
		}
		else if(x == y)
		{
			double cell = 0.0;
			for(int linkID : this.nodes[x])
			{
				cell += this.linkProperties[linkID][0];
			}
			return cell;
		}
		else for(int linkID : this.nodes[x])
		{
			if(this.links[linkID][0] == y || this.links[linkID][1] == y)
			{
				
				return -this.linkProperties[linkID][0];
			}
		}
		return 0;
	}
	
	public synchronized double[] getResult()
	{
		return this.result.clone();
	}
}