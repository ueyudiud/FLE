/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

import farcore.energy.electric.IElectricalElement;

/**
 * @author ueyudiud
 */
class LinkInner extends Link
{
	NodeReal[] nodes;
	IElectricalElement element;
	
	LinkInner(NodeReal nodeThis, NodeReal nodeThat, IElectricalElement element)
	{
		this.nodes = new NodeReal[]{nodeThis, nodeThat};
		this.element = element;
	}
	
	@Override
	public NodeReal[] nodes()
	{
		return this.nodes;
	}
	
	@Override
	void update(double current)
	{
		this.element.onUpdate(resistance() * current, current);
		this.resistance[0] = this.element.resistance();
	}
}