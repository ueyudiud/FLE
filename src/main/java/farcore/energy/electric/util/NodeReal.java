/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.energy.electric.util;

import java.util.HashMap;
import java.util.Map;

import farcore.energy.electric.IElectricalElement;
import farcore.energy.electric.IElectricalHandler;
import farcore.energy.electric.IElectricalNode;
import farcore.energy.electric.ISidedElectricalElement;
import farcore.lib.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * @author ueyudiud
 */
public class NodeReal extends Node
{
	/**
	 * 
	 */
	private final Routes routes;
	IElectricalHandler handler;
	int idx;
	/** To prevent handler change node configuration cause null pointer. */
	IElectricalNode node;
	Map<Node, Link> map = new HashMap();
	
	public NodeReal(Routes routes, IElectricalHandler handler, int idx, IElectricalNode node)
	{
		this.routes = routes;
		this.handler = handler;
		this.idx = idx;
		this.node = node;
	}
	
	void buildLinks(NodeReal[] nodes)
	{
		int[] links = this.node.link();
		this.map = new HashMap(links.length + 1);
		for(int i : links)
		{
			IElectricalElement element = this.handler.getElement(this.idx, i);
			if(element instanceof ISidedElectricalElement)
			{
				Direction direction = ((ISidedElectricalElement) element).getSide();
				BlockPos pos = direction.offset(this.handler.pos());
				this.routes.cache.array[0] = pos.getX();
				this.routes.cache.array[1] = pos.getY();
				this.routes.cache.array[2] = pos.getZ();
				this.routes.cache.array[3] = Direction.OPPISITE[direction.ordinal()];
				NodeReal node = this.routes.nodes.get(this.routes.cache);
				if(node != null)
				{
					IElectricalElement element2 = node.handler.getEnelementFromFace(direction.getOpposite());
					this.map.put(node, new LinkOuter(this.routes, this, node, element, element2));
					node.addExtraLink(this, element2, element);
				}
			}
			else
			{
				this.map.put(nodes[i], new LinkInner(this, nodes[i], element));
			}
		}
	}
	
	void addExtraLink(NodeReal node, IElectricalElement elementThis, IElectricalElement elementThat)
	{
		this.map.put(node, new LinkOuter(this.routes, this, node, elementThis, elementThat));
		this.routes.changedNodes.add(this);
	}
	
	void removeAllLinks()
	{
		this.map.clear();
		int[] links = this.node.link();
		for(int i : links)
		{
			IElectricalElement element = this.handler.getElement(this.idx, i);
			if(element instanceof ISidedElectricalElement)
			{
				Direction direction = ((ISidedElectricalElement) element).getSide();
				BlockPos pos = direction.offset(this.handler.pos());
				this.routes.cache.array[0] = pos.getX();
				this.routes.cache.array[1] = pos.getY();
				this.routes.cache.array[2] = pos.getZ();
				this.routes.cache.array[3] = Direction.OPPISITE[direction.ordinal()];
				NodeReal node = this.routes.nodes.get(this.routes.cache);
				if(node != null)
				{
					node.removeLink(this);
				}
			}
			else
			{
				//The inner node link needn't remove.
			}
		}
	}
	
	void removeLink(NodeReal node)
	{
		this.map.remove(node);
		this.routes.changedNodes.add(this);
	}
	
	@Override
	public Map<Node, Link> links()
	{
		return this.map;
	}
	
	@Override
	void refreshProperties()
	{
		this.voltage[0] = this.node.voltage();
	}
}