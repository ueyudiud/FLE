package farcore.energy.electric.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.energy.electric.EnumCurrentType;
import farcore.energy.electric.IACHandler;
import farcore.energy.electric.IElectricalElement;
import farcore.energy.electric.IElectricalNode;
import farcore.energy.electric.ISidedElectricalElement;
import farcore.lib.collection.IntegerArray;
import farcore.lib.util.Direction;
import net.minecraft.util.math.BlockPos;

public class RoutesAC
{
	private static final int[] DEAD_NODE = new int[0];
	
	final List<NodeReal> changedNodes = new ArrayList();
	final Map<BlockPos, IACHandler> handlers = new HashMap();
	final IntegerArray cache = new IntegerArray(4);
	final Map<IntegerArray, NodeReal> nodes = new HashMap();
	/**
	 * To rebuild nodes map, remove dead link and compact
	 * link only has two side link to one link.
	 */
	public final List<NodeRebuild> rebuildNodes = new ArrayList();
	public final Map<IntegerArray, Link> rebuildLinks = new HashMap();
	
	public void addHandler(IACHandler handler)
	{
		handlers.put(handler.pos(), handler);
		int l = handler.getNodeSize();
		NodeReal[] nodes = new NodeReal[l];
		BlockPos pos0 = handler.pos();
		cache.array[0] = pos0.getX();
		cache.array[1] = pos0.getY();
		cache.array[2] = pos0.getZ();
		for(int i = 0; i < l; ++i)
		{
			IElectricalNode node = handler.getNode(i);
			nodes[i] = new NodeReal(handler, i, node);
			this.nodes.put(cache.set(3, i).copy(), nodes[i]);
		}
		for(NodeReal node : nodes)
		{
			node.buildLinks(nodes);
			changedNodes.add(node);
		}
	}
	
	public void removeHandler(IACHandler handler)
	{
		if(handlers.remove(handler.pos()) == null) return;
		int l = handler.getNodeSize();
		NodeReal[] nodes = new NodeReal[l];
		BlockPos pos0 = handler.pos();
		cache.array[0] = pos0.getX();
		cache.array[1] = pos0.getY();
		cache.array[2] = pos0.getZ();
		for(int i = 0; i < l; ++i)
		{
			NodeReal node = this.nodes.remove(cache.set(3, i));
			if(node != null)
			{
				node.removeAllLinks();
			}
		}
	}

	public List<NodeReal> getChangedNodes()
	{
		return changedNodes;
	}
	
	public void resetChanging()
	{
		changedNodes.clear();
	}
	
	public void rebuildNodes()
	{
		//Clear cache.
		rebuildNodes.clear();
		rebuildLinks.clear();
		for(NodeReal node : nodes.values())
		{
			node.id = -1;
		}
		//Find route for each.
		for(NodeReal node : nodes.values())
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
						Link link = new CompactLink(node, node2, linkBuilder.build());
						builder.put(node2, link);
					}
				}
				else //If links is more than 2, link them directly.
				{
					builder.put(node2, entry.getValue());
				}
			}
			rebuildNodes.add(new NodeRebuild(node, builder.build()));
		}
		for(int i = 0; i < rebuildNodes.size(); ++i)
		{
			NodeRebuild node = rebuildNodes.get(i);
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
				if(!rebuildLinks.containsKey(cache))
				{
					rebuildLinks.put(cache.copy(), link);
				}
			}
			node.setID(i);
		}
	}

	public void onUpdate(List<Double> voltages)
	{
		for(Link link : rebuildLinks.values())
		{
			Node node1 = link.nodes()[0];
			Node node2 = link.nodes()[1];
			double R = link.resistance();
			double V1 = voltages.get(node1.id);
			double V2 = voltages.get(node2.id);
			link.update((V1 - V2) / R);
		}
		for(NodeRebuild node : rebuildNodes)
		{
			if(node.voltage() != 0)
			{
				double V = node.voltage();
				double I = 0;
				for(Entry<Node, Link> entry : node.links.entrySet())
				{
					double V1 = voltages.get(entry.getKey().id);
					double R = entry.getValue().resistance();
					I += (V - V1) / R;
				}
				((NodeReal) node.node).node.onUpdate(V, I);
			}
		}
	}
	
	public abstract class Node
	{
		/**
		 * Use in rebuild net.
		 */
		int id = -1;

		int rebuildID()
		{
			return id;
		}

		public abstract Map<Node, Link> links();
		
		public abstract double voltage();
	}
	
	public class NodeReal extends Node
	{
		IACHandler handler;
		int idx;
		/** To prevent handler change node configuration cause null pointer. */
		IElectricalNode node;
		Map<Node, Link> map = new HashMap();

		public NodeReal(IACHandler handler, int idx, IElectricalNode node)
		{
			this.handler = handler;
			this.idx = idx;
			this.node = node;
		}

		void buildLinks(NodeReal[] nodes)
		{
			int[] links = node.link();
			map = new HashMap(links.length + 1);
			for(int i : links)
			{
				IElectricalElement element = handler.getElement(idx, i);
				if(element instanceof ISidedElectricalElement)
				{
					Direction direction = ((ISidedElectricalElement) element).getSide();
					BlockPos pos = direction.offset(handler.pos());
					cache.array[0] = pos.getX();
					cache.array[1] = pos.getY();
					cache.array[2] = pos.getZ();
					cache.array[3] = Direction.OPPISITE[direction.ordinal()];
					NodeReal node = RoutesAC.this.nodes.get(cache);
					if(node != null)
					{
						IElectricalElement element2 = node.handler.getEnelementFromFace(direction.getOpposite());
						map.put(node, new OuterLink(this, node, element, element2));
						node.addExtraLink(this, element2, element);
					}
				}
				else
				{
					map.put(nodes[i], new InnerLink(this, nodes[i], element));
				}
			}
		}
		
		void addExtraLink(NodeReal node, IElectricalElement elementThis, IElectricalElement elementThat)
		{
			map.put(node, new OuterLink(this, node, elementThis, elementThat));
			changedNodes.add(this);
		}

		void removeAllLinks()
		{
			map.clear();
			int[] links = node.link();
			for(int i : links)
			{
				IElectricalElement element = handler.getElement(idx, i);
				if(element instanceof ISidedElectricalElement)
				{
					Direction direction = ((ISidedElectricalElement) element).getSide();
					BlockPos pos = direction.offset(handler.pos());
					cache.array[0] = pos.getX();
					cache.array[1] = pos.getY();
					cache.array[2] = pos.getZ();
					cache.array[3] = Direction.OPPISITE[direction.ordinal()];
					NodeReal node = nodes.get(cache);
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
			map.remove(node);
			changedNodes.add(this);
		}
		
		@Override
		public Map<Node, Link> links()
		{
			return map;
		}

		@Override
		public double voltage()
		{
			return node.eVoltage(EnumCurrentType.AC);
		}
	}

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
			node.id = id = i;
		}

		@Override
		public Map<Node, Link> links()
		{
			return links;
		}
		
		@Override
		public double voltage()
		{
			return node.voltage();
		}
	}

	class OuterLink extends Link
	{
		NodeReal[] nodes;
		IElectricalElement element1;
		IElectricalElement element2;
		
		OuterLink(NodeReal nodeThis, NodeReal nodeThat, IElectricalElement element1, IElectricalElement element2)
		{
			nodes = new NodeReal[]{nodeThis, nodeThat};
			this.element1 = element1;
			this.element2 = element2;
		}
		
		@Override
		NodeReal[] nodes()
		{
			return nodes;
		}
		
		@Override
		public double resistance()
		{
			return element1.eResistance(EnumCurrentType.AC) + element2.eResistance(EnumCurrentType.AC);
		}
		
		@Override
		void update(double current)
		{
			element1.onUpdate(element1.eResistance(EnumCurrentType.AC) * current, current);
			element2.onUpdate(element2.eResistance(EnumCurrentType.AC) * current, current);
		}
	}

	class InnerLink extends Link
	{
		NodeReal[] nodes;
		IElectricalElement element;

		InnerLink(NodeReal nodeThis, NodeReal nodeThat, IElectricalElement element)
		{
			nodes = new NodeReal[]{nodeThis, nodeThat};
			this.element = element;
		}
		
		@Override
		NodeReal[] nodes()
		{
			return nodes;
		}
		
		@Override
		public double resistance()
		{
			return element.eResistance(EnumCurrentType.AC);
		}
		
		@Override
		void update(double current)
		{
			element.onUpdate(resistance() * current, current);
		}
	}

	class CompactLink extends Link
	{
		Node[] sideNode;
		List<Link> links;
		List<Node> nodes;
		double resistanceCache = -1;

		CompactLink(Node nodeThis, Node nodeThat, List<Link> currentLinks)
		{
			sideNode = new Node[]{nodeThis, nodeThat};
			links = currentLinks;
		}
		
		@Override
		void update(double current)
		{
			for(Link link : links)
			{
				link.update(current);
			}
		}

		public boolean isOnLink(Node node)
		{
			if(nodes == null)
			{
				nodes = new ArrayList(links.size());
				for(Link link : links)
				{
					for(Node node2 : link.nodes())
					{
						if(!nodes.contains(node2))
						{
							nodes.add(node2);
						}
					}
				}
			}
			return nodes.contains(node);
		}

		public void recaculateResistance()
		{
			resistanceCache = 0;
			for(Link link : links)
			{
				resistanceCache += link.resistance();
			}
		}

		@Override
		Node[] nodes()
		{
			return sideNode;
		}
		
		@Override
		public double resistance()
		{
			if(resistanceCache == -1)
			{
				recaculateResistance();
			}
			return resistanceCache;
		}
	}

	public static abstract class Link
	{
		abstract Node[] nodes();
		
		abstract void update(double current);
		
		public abstract double resistance();
	}
}