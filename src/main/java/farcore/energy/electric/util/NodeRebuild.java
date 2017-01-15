/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

import java.util.Map;

/**
 * @author ueyudiud
 */
public class NodeRebuild extends Node
{
	Node node;
	Map<Node, Link> links;
	
	public NodeRebuild(Node node, Map<Node, Link> links)
	{
		this.node = node;
		this.links = links;
	}
	
	public void setID(int i)
	{
		this.node.id = this.id = i;
	}
	
	@Override
	public Map<Node, Link> links()
	{
		return this.links;
	}
	
	@Override
	void refreshProperties()
	{
		this.voltage[0] = this.node.voltage();
	}
}