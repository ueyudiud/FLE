package farcore.energy.electric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import farcore.enums.Direction;
import farcore.interfaces.energy.IEnergyNet;
import farcore.interfaces.energy.electric.IElectricTile;
import farcore.interfaces.energy.electric.IElectricTile.Linkable;
import farcore.interfaces.energy.electric.IElectricTile.Nodable;
import farcore.lib.collection.IntArray;
import farcore.util.FleLog;
import farcore.util.V;
import farcore.util.matrix.MatrixResolver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Deprecated
public class ElectricNet implements IEnergyNet
{
	private static Map<Integer, Local> netMap = new HashMap();
	
	public static double getVoltage(TileEntity tile, Direction direction, int channel)
	{
		return getNet(tile.getWorldObj(), false).getVoltage(tile, direction, channel);
	}
	
	@Override
	public void update(World world)
	{
		getNet(world, false).updateNet();
	}

	@Override
	public void add(TileEntity tile)
	{
		getNet(tile.getWorldObj(), false).add(tile);
	}

	@Override
	public void remove(TileEntity tile)
	{
		getNet(tile.getWorldObj(), false).remove(tile);
	}
	
	@Override
	public void mark(TileEntity tile)
	{
		getNet(tile.getWorldObj(), false).mark(tile);
	}

	@Override
	public void reload(TileEntity tile)
	{
		getNet(tile.getWorldObj(), false).reload(tile);
	}

	@Override
	public void unload(World world)
	{
		netMap.remove(world.provider.dimensionId);
	}

	@Override
	public void load(World world)
	{
		netMap.put(world.provider.dimensionId, new Local(world));
	}
		
	private static Local getNet(World world, boolean create)
	{
		if(!create)
		{
			return netMap.getOrDefault(world.provider.dimensionId, Local.instance);
		}
		if(!netMap.containsKey(world.provider.dimensionId))
		{
			netMap.put(world.provider.dimensionId, new Local(world));
		}
		return netMap.get(world.provider.dimensionId);
	}
	
	private static class Local
	{
		private static final Local instance = new Local(null);
		
		private volatile boolean isUpdating = false;

		private boolean coefficientChanged = false;
		private boolean structureChanged = false;
		
		private final World world;
		private final Routes route = new Routes();
		private final IntArray cache = new IntArray(5);
		private final List<Node> markedNodes = new ArrayList();
		private final List<Node> cachedList = new ArrayList();
		private final Map<Node, Double> voltageCache = new HashMap();
		private final List<IElectricTile> list = new ArrayList();
		private final MatrixResolver matrix = MatrixResolver.newResolver();
		private final List<IElectricTile> cacheAddList = new ArrayList();
		private final List<IElectricTile> cacheRemoveList = new ArrayList();
		
		public Local(World world)
		{
			this.world = world;
		}

		public double getVoltage(TileEntity tile, Direction direction, int channel)
		{
			cache.array[0] = tile.xCoord;
			cache.array[1] = tile.yCoord;
			cache.array[2] = tile.zCoord;
			cache.array[3] = direction.ordinal();
			cache.array[4] = channel;
			return route.nodes.containsKey(cache) ? voltageCache.get(route.nodes.get(cache)).doubleValue() : 0;
		}
		
		public double getVoltage(Node node)
		{
			return voltageCache.containsKey(node) ? voltageCache.get(node).doubleValue() : 0;
		}
		
		public double getPower(Node node)
		{
			double V = getVoltage(node);
			if(V <= 0) return 0;
			double V1;
			double power = 0;
			for(ILink link : route.links(node))
			{
				Node node1 = node.equals(link.node1()) ? link.node2() : link.node1();
				V1 = getVoltage(node1);
				power += (V1 - V) * (V1 - V) / link.resistance() * .5;
			}
			return power;
		}

		public void add(TileEntity tile)
		{
			if(!(tile instanceof IElectricTile)) return;
			if(isUpdating)
			{
				cacheAddList.add((IElectricTile) tile);
			}
			else
			{
				addUnsafe((IElectricTile) tile);
			}
		}
		
		private void addUnsafe(IElectricTile tile)
		{
			list.add(tile);
			for(Direction direction : Direction.directions)
			{
				for(Nodable nodable : tile.getNodes(direction))
				{
					route.add(new Node(tile, nodable));
				}
			}
			structureChanged = true;
		}

		public void remove(TileEntity tile)
		{
			if(!(tile instanceof IElectricTile)) return;
			if(isUpdating)
			{
				cacheRemoveList.add((IElectricTile) tile);
			}
			else
			{
				removeUnsafe((IElectricTile) tile);
			}
		}
		
		private void removeUnsafe(IElectricTile tile)
		{
			list.remove(tile);
			for(Direction direction : Direction.directions)
			{
				for(Nodable nodable : tile.getNodes(direction))
				{
					route.remove(new Node(tile, nodable));
				}
			}
			structureChanged = true;
		}

		public void reload(TileEntity tile)
		{
			remove(tile);
			add(tile);
		}

		public void updateNet()
		{
			if(world == null) return;
			if(structureChanged)
			{
				refind();
			}
			else if(coefficientChanged)
			{
				recalculate();
			}
			structureChanged = coefficientChanged = false;
			isUpdating = true;
			try
			{
				for(IElectricTile tile : list)
				{
					tile.electricPreUpdate();
				}
			}
			catch(Exception exception)
			{
				if(V.debug)
				{
					FleLog.getCoreLogger().error("Fail to update energy net.", exception);
				}
			}
			for(IElectricTile tile : cacheRemoveList)
			{
				removeUnsafe(tile);
			}
			for(IElectricTile tile : cacheAddList)
			{
				addUnsafe(tile);
			}
			cacheRemoveList.clear();
			cacheAddList.clear();
			isUpdating = false;
		}
		
		private void refind()
		{
			cachedList.clear();
			List<Node> deadNode = new ArrayList();
			//Serialize node.
			for(Entry<Node, List<ILink>> entry : route.links())
			{
				entry.getKey().id = -1;
				if(entry.getValue().isEmpty())
				{
					deadNode.add(entry.getKey());
					continue;
				}
				cachedList.add(entry.getKey());
			}
			//Initialize matrix and size.
			int size = cachedList.size();
			matrix.newMatrix(size);
			double[] b = new double[size];
			
			//Get all coefficients of matrix.
			for(int y = 0; y < size; ++y)
			{
				Node node = cachedList.get(y);
				node.id = y;
				List<ILink> links = route.links(node);
				b[y] = getBasicCurrent(links, node);
				for(int x = 0; x < size; ++x)
				{
					matrix.push(getCoefficient(links, x, y, node));
				}
				matrix.enter();
			}
			matrix.finalized();
			//Solve matrix.
			if(!matrix.solve(b)) throw new RuntimeException("Invalid solving!");
			//Set voltage cache to each node.
			voltageCache.clear();
			for(int i = 0; i < size; ++i)
			{
				voltageCache.put(cachedList.get(i), b[i]);
			}
			int id = size;
			for(Node node : deadNode)
			{
				node.id = id++;
				cachedList.add(cachedList.size(), node);
				voltageCache.put(node, 0D);
			}
			route.resetChanging();
		}

		public void mark(TileEntity tile)
		{
			if(isUpdating)
			{
				reload(tile);
			}
			else
			{
				if(!(tile instanceof IElectricTile)) return;
				for(Direction direction : Direction.directions)
				{
					for(Nodable node : ((IElectricTile) tile).getNodes(direction))
					{
						markedNodes.add(new Node((IElectricTile) tile, node));
					}
				}
			}
		}
		
		private void recalculate()
		{
			if(cachedList.size() == 0)
			{
				refind();
				return;
			}
			List<Node> updateList = new LinkedList();
			List<Node> uncheckedList = new ArrayList();
			for(Node node : markedNodes)
			{
				List<ILink> links = route.links(node);
				if(links.isEmpty())
				{
					uncheckedList.add(node);
					continue;
				}
				if(!updateList.contains(node))
				{
					updateList.add(node);
				}
				for(ILink link : links)
				{
					Node node2 = link.other(node);
					if(!updateList.contains(node2))
						updateList.add(node2);
				}
			}
			int size = cachedList.size() - uncheckedList.size();
			double[] b = new double[size];
			for(Node node : updateList)
			{
				int y = node.id;
				this.matrix.selectY(y);
				boolean bCalcutated = false;
				List<ILink> neighborList = route.links(node);
				for (int x = 0; x < size; x++)
				{
					Node node2 = cachedList.get(x);
					if (!bCalcutated)
					{
						b[x] = getBasicCurrent(route.links(node2), node2);
					}
					this.matrix.setCell(getCoefficient(neighborList, x, y, node));
				}
				bCalcutated = true;
				y++;
			}
			if(!matrix.solve(b)) throw new RuntimeException("Invalid solving!");
			voltageCache.clear();
			for(int i = 0; i < size; ++i)
			{
				voltageCache.put(cachedList.get(i), b[i]);
			}
			for(Node node : uncheckedList)
			{
				voltageCache.put(node, 0D);
			}
			route.resetChanging();
		}
		
		/**
		 * Get basic current of each node, add in matrix.
		 * @param links
		 * @param node
		 * @return
		 */
		private double getBasicCurrent(List<ILink> links, Node node)
		{
			double ret = 0D;
			double V = node.nodable.voltage();
			double V1;
			if(links.size() != 1)
			{
				if(V == 0) return 0;
			}
			else if(V == 0)
			{
				V = -1D;
			}
			else
			{
				for(ILink link : links)
				{
					V1 = link.other(node).nodable.voltage();
					ret -= (V - V1) / link.resistance();
				}
			}
			return ret;
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
		 *  @param links All links of node.
		 *  @param x
		 *  @param y
		 *  @param node The select node (Of y size).
		 *  @return The coefficient added in matrix.
		 */
		private double getCoefficient(List<ILink> links, int x, int y, Node node)
		{
		    double cellData = 0.0D;
		    if(x == y)
		    {
		    	for(ILink link : links)
		    	{
		    		cellData += 1D / link.resistance() - 1D;
		    	}
		    }
		    else
		    {
		    	for(ILink link : links)
		    	{
		    		if(cachedList.indexOf(link.other(node)) == x)
		    		{
		    			cellData -= 1D / link.resistance();
		    			break;
		    		}
		    	}
		    }
			return cellData;
		}
	}
	
	/**
	 * The electric node.
	 * @author ueyudiud
	 *
	 */
	public static class Node
	{
		IntArray array;
		IElectricTile tile;
		Nodable nodable;
		int id;
		
		public Node(IElectricTile tile, Nodable nodable)
		{
			this.tile = tile;
			this.nodable = nodable;
			array = new IntArray(new int[]{((TileEntity) tile).xCoord, ((TileEntity) tile).yCoord, ((TileEntity) tile).zCoord, nodable.direction().ordinal(), nodable.channel()});
		}

		public boolean isConnect(int x, int y, int z)
		{
			return array.array[0] == x && array.array[1] == y && array.array[2] == z;
		}
		
		@Override
		public int hashCode()
		{
			return array.hashCode();
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if(obj == this) return true;
			else if(!(obj instanceof Node))
				return false;
			return array.equals(((Node) obj).array);
		}
		
		@Override
		public String toString()
		{
			return array.toString();
		}
		
		public void setID(int id)
		{
			this.id = id;
		}
	}
	
	/**
	 * The electric link.
	 * @author ueyudiud
	 *
	 */
	public static abstract class ILink
	{
		abstract float resistance();
		
		abstract Node node1();
		
		abstract Node node2();
		
		Node other(Node node)
		{
			if(node.equals(node1())) return node2();
			else if(node.equals(node2())) return node1();
			return null;
		}
	}

	public static class InnerLink extends ILink
	{
		IElectricTile.Linkable linkable;
		Node node1;
		Node node2;
		
		public InnerLink(IElectricTile tile, IElectricTile.Linkable linkable)
		{
			this.linkable = linkable;
			node1 = new Node(tile, linkable.getThis());
			node2 = new Node(tile, linkable.getThat());
		}
		
		@Override
		public float resistance()
		{
			return linkable.resistance();
		}

		@Override
		public Node node1()
		{
			return node1;
		}

		@Override
		public Node node2()
		{
			return node2;
		}
	}
	
	public static class OutterLink extends ILink
	{
		Node node1;
		Node node2;
		
		public OutterLink(Node node1, Node node2)
		{
			this.node1 = node1;
			this.node2 = node2;
		}
		
		@Override
		public float resistance()
		{
			return (node1.nodable.resistanceToOther(node2.tile, node2.nodable) + 
					node2.nodable.resistanceToOther(node1.tile, node1.nodable)) / 2F;
		}

		@Override
		public Node node1()
		{
			return node1;
		}

		@Override
		public Node node2()
		{
			return node2;
		}
	}

	@Deprecated
	public static class MixedLink extends ILink
	{
		ILink link1;
		ILink link2;
		Node node;
		
		public MixedLink(ILink link1, ILink link2, Node ingore)
		{
			this.link1 = link1;
			this.link2 = link2;
			this.node = ingore;
		}

		@Override
		public float resistance()
		{
			return link1.resistance() + link2.resistance();
		}

		@Override
		public Node node1()
		{
			return link1.other(node);
		}

		@Override
		public Node node2()
		{
			return link2.other(node);
		}		
	}
	
	public static class Routes
	{
		final List<Node> changedLinkNodes = new ArrayList();
		final IntArray cache = new IntArray(5);
		final Map<IntArray, Node> nodes = new HashMap();
		final Map<Node, List<ILink>> map = new HashMap();
		
		void add(Node node)
		{
			if(!nodes.containsKey(node))
			{
				nodes.put(node.array, node);
				map.put(node, new ArrayList());
			}
			map.get(node).clear();
			Node node1;
			for(Linkable linkable : node.nodable.getInnerLinks())
			{
				node1 = new Node(node.tile, linkable.getThat()); 
				if(!nodes.containsKey(node1))
				{
					nodes.put(node1.array, node1);
					map.put(node1, new ArrayList());
				}
				map.get(node).add(new InnerLink(node.tile, linkable));
				map.get(node1).add(new InnerLink(node1.tile, linkable));
			}
			for(int[] info : node.nodable.getAccessOffsetLinkPath())
			{
				cache.array[0] = node.array.array[0] + info[0];//xOffset
				cache.array[1] = node.array.array[1] + info[1];//yOffset
				cache.array[2] = node.array.array[2] + info[2];//zOffset
				cache.array[3] = info[3];
				if(info.length == 4)
				{
					for(int i = 0; i < 16; ++i)
					{
						cache.array[4] = i;
						if(nodes.containsKey(cache))
						{
							node1 = nodes.get(cache);
							if(node1.nodable.canLinkWith(node.tile, node.nodable))
							{
								if(!changedLinkNodes.contains(node1))
								{
									changedLinkNodes.add(node1);
								}
								addUnsafe(new OutterLink(node, node1));
							}
						}
					}
				}
			}
			changedLinkNodes.add(node);
		}
		
		void addUnsafe(ILink link)
		{
			map.get(link.node1()).add(link);
			map.get(link.node2()).add(link);
		}

		List<ILink> remove(Node node)
		{
			nodes.remove(node.array);
			if(map.containsKey(node))
			{
				for(ILink link : map.get(node))
				{
					Node node2 = link.other(node);
					map.get(node2).remove(link);
					if(!changedLinkNodes.contains(node2))
					{
						changedLinkNodes.add(node2);
					}
				}
			}
			return map.remove(node);
		}

		Set<Node> nodes()
		{
			return map.keySet();
		}
		
		Set<Entry<Node, List<ILink>>> links()
		{
			return map.entrySet();
		}
		
		List<ILink> links(Node node)
		{
			return map.get(node);
		}
		
		void resetChanging()
		{
			changedLinkNodes.clear();
		}
	}
}