/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ueyudiud
 */
class LinkCompact extends Link
{
	Node[] sideNode;
	List<Link> links;
	List<Node> nodes;
	
	LinkCompact(Node nodeThis, Node nodeThat, List<Link> currentLinks)
	{
		this.sideNode = new Node[]{nodeThis, nodeThat};
		this.links = currentLinks;
	}
	
	@Override
	void update(double current)
	{
		for(Link link : this.links)
		{
			link.update(current);
		}
		this.resistance[0] = recaculateResistance();
	}
	
	public boolean isOnLink(Node node)
	{
		if(this.nodes == null)
		{
			this.nodes = new ArrayList(this.links.size());
			for(Link link : this.links)
			{
				for(Node node2 : link.nodes())
				{
					if(!this.nodes.contains(node2))
					{
						this.nodes.add(node2);
					}
				}
			}
		}
		return this.nodes.contains(node);
	}
	
	private double recaculateResistance()
	{
		double R = 0;
		for(Link link : this.links)
		{
			R += link.resistance();
		}
		return R;
	}
	
	@Override
	public Node[] nodes()
	{
		return this.sideNode;
	}
}