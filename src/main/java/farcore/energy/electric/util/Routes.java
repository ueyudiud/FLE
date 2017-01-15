package farcore.energy.electric.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.energy.electric.IElectricalHandler;
import farcore.energy.electric.IElectricalNode;
import farcore.lib.collection.IntegerArray;
import net.minecraft.util.math.BlockPos;

public class Routes
{
	private static final int[] DEAD_NODE = new int[0];
	
	final List<NodeReal> changedNodes = new ArrayList();
	final Map<BlockPos, IElectricalHandler> handlers = new HashMap();
	final IntegerArray cache = new IntegerArray(4);
	final Map<IntegerArray, NodeReal> nodes = new HashMap();
	/**
	 * To rebuild nodes map, remove dead link and compact
	 * link only has two side link to one link.
	 */
	public final List<NodeRebuild> rebuildNodes = new ArrayList();
	public final Map<IntegerArray, Link> rebuildLinks = new HashMap();
	
	public void addHandler(IElectricalHandler handler)
	{
		this.handlers.put(handler.pos(), handler);
		int l = handler.getNodeSize();
		NodeReal[] nodes = new NodeReal[l];
		BlockPos pos0 = handler.pos();
		this.cache.array[0] = pos0.getX();
		this.cache.array[1] = pos0.getY();
		this.cache.array[2] = pos0.getZ();
		for(int i = 0; i < l; ++i)
		{
			IElectricalNode node = handler.getNode(i);
			nodes[i] = new NodeReal(this, handler, i, node);
			this.nodes.put(this.cache.set(3, i).copy(), nodes[i]);
		}
		for(NodeReal node : nodes)
		{
			node.buildLinks(nodes);
			this.changedNodes.add(node);
		}
	}
	
	public void removeHandler(IElectricalHandler handler)
	{
		if(this.handlers.remove(handler.pos()) == null) return;
		int l = handler.getNodeSize();
		NodeReal[] nodes = new NodeReal[l];
		BlockPos pos0 = handler.pos();
		this.cache.array[0] = pos0.getX();
		this.cache.array[1] = pos0.getY();
		this.cache.array[2] = pos0.getZ();
		for(int i = 0; i < l; ++i)
		{
			NodeReal node = this.nodes.remove(this.cache.set(3, i));
			if(node != null)
			{
				node.removeAllLinks();
			}
		}
	}
	
	public List<NodeReal> getChangedNodes()
	{
		return this.changedNodes;
	}
	
	public void resetChanging()
	{
		this.changedNodes.clear();
	}
	
	public void rebuildNodes()
	{
		//Clear cache.
		this.rebuildNodes.clear();
		this.rebuildLinks.clear();
		for(NodeReal node : this.nodes.values())
		{
			node.id = -1;
		}
		//Find route for each.
		for(NodeReal node : this.nodes.values())
		{
			int size = node.links().size();
			//If only connect two node and also not a voltage source, prevent to check.
			if(size == 0 || (size <= 2 && node.voltage() == 0))
			{
				continue;
			}
			ImmutableMap.Builder<Node, Link> builder = ImmutableMap.builder();
			for(Entry<Node, Link> entry : node.links().entrySet())
			{
				Node node2 = entry.getKey();
				ImmutableList.Builder<Link> linkBuilder = ImmutableList.builder();
				if(node2.links().size() <= 2)
				{
					do
					{
						if(node2.links().size() != 2)
						{
							break;
						}
						for(Entry<Node, Link> entry2 : node2.links().entrySet())
						{
							if(entry2.getKey() != node2)
							{
								linkBuilder.add(entry2.getValue());
								node2 = entry.getKey();
								break;
							}
						}
					}
					while(true);
					if(size < 2 && node2.voltage() == 0)
					{
						//Ignore dead node (which only linked with one element).
						continue;
					}
					else
					{
						Link link = new LinkCompact(node, node2, linkBuilder.build());
						builder.put(node2, link);
					}
				}
				else //If links is more than 2, link them directly.
				{
					builder.put(node2, entry.getValue());
				}
			}
			this.rebuildNodes.add(new NodeRebuild(node, builder.build()));
		}
		for(int i = 0; i < this.rebuildNodes.size(); ++i)
		{
			NodeRebuild node = this.rebuildNodes.get(i);
			IntegerArray cache = new IntegerArray(2);
			for(Link link : node.links.values())
			{
				int id1 = link.nodes()[0].id;
				int id2 = link.nodes()[1].id;
				if(id1 < id2)
				{
					cache.set(0, id2).set(1, id2);
				}
				else
				{
					cache.set(0, id1).set(1, id2);
				}
				if(!this.rebuildLinks.containsKey(cache))
				{
					this.rebuildLinks.put(cache.copy(), link);
				}
			}
			node.setID(i);
		}
	}
	
	public void onUpdate(double[] voltages)
	{
		for(Link link : this.rebuildLinks.values())
		{
			Node node1 = link.nodes()[0];
			Node node2 = link.nodes()[1];
			double R = link.resistance();
			double V1 = voltages[node1.id];
			double V2 = voltages[node2.id];
			link.update((V1 - V2) / R);
		}
		for(NodeRebuild node : this.rebuildNodes)
		{
			if(node.voltage() != 0)
			{
				double V = node.voltage();
				double I = 0;
				for(Entry<Node, Link> entry : node.links.entrySet())
				{
					double V1 = voltages[entry.getKey().id];
					double R = entry.getValue().resistance();
					I += (V - V1) / R;
				}
				((NodeReal) node.node).node.onUpdate(V, I);
			}
		}
	}
}