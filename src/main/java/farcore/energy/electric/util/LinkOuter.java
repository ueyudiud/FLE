/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

import farcore.energy.electric.IElectricalElement;

/**
 * @author ueyudiud
 */
class LinkOuter extends Link
{
	/**
	 * 
	 */
	private final Routes linkOuter;
	NodeReal[] nodes;
	IElectricalElement element1;
	IElectricalElement element2;
	private double[] resistance1 = new double[2];
	
	LinkOuter(Routes routes, NodeReal nodeThis, NodeReal nodeThat, IElectricalElement element1, IElectricalElement element2)
	{
		this.linkOuter = routes;
		this.nodes = new NodeReal[]{nodeThis, nodeThat};
		this.element1 = element1;
		this.element2 = element2;
		//Initialized resistance.
		this.resistance1[0] = this.element1.resistance();
		this.resistance1[1] = this.element2.resistance();
	}
	
	@Override
	public NodeReal[] nodes()
	{
		return this.nodes;
	}
	
	@Override
	void update(double current)
	{
		this.element1.onUpdate(this.resistance1[0] * current, current);
		this.element2.onUpdate(this.resistance1[1] * current, current);
		this.resistance1[0] = this.element1.resistance();
		this.resistance1[1] = this.element2.resistance();
		this.resistance[0] = this.resistance1[0] + this.resistance1[1];
	}
}