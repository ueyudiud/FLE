package farcore.energy.electric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.FarCore;
import farcore.energy.IEnergyNet;
import farcore.energy.electric.util.EquationResolver;
import farcore.energy.electric.util.RoutesAC;
import farcore.energy.electric.util.RoutesAC.Link;
import farcore.energy.electric.util.RoutesAC.Node;
import farcore.energy.electric.util.RoutesAC.NodeReal;
import farcore.energy.electric.util.RoutesAC.NodeRebuild;
import farcore.lib.collection.IntArray;
import farcore.lib.util.EnumModifyFlag;
import farcore.lib.util.Log;
import net.minecraft.world.World;

public class ElectricACNet implements IEnergyNet
{
	public static final ElectricACNet instance = new ElectricACNet();
	private static Map<Integer, Local> netMap = new HashMap();
	
	@Override
	public void update(World world)
	{
		getNet(world, false).updateNet();
	}

	@Override
	public void add(Object tile)
	{
		if(tile instanceof IACHandler)
		{
			getNet(((IACHandler) tile).world(), false).add((IACHandler) tile);
		}
	}

	@Override
	public void remove(Object tile)
	{
		if(tile instanceof IACHandler)
		{
			getNet(((IACHandler) tile).world(), false).remove((IACHandler) tile);
		}
	}
	
	@Override
	public void mark(Object tile)
	{
		if(tile instanceof IACHandler)
		{
			getNet(((IACHandler) tile).world(), false).mark((IACHandler) tile);
		}
	}

	@Override
	public void reload(Object tile)
	{
		if(tile instanceof IACHandler)
		{
			getNet(((IACHandler) tile).world(), false).reload((IACHandler) tile);
		}
	}

	@Override
	public void unload(World world)
	{
		netMap.remove(world.provider.getDimension());
	}

	@Override
	public void load(World world)
	{
		netMap.put(world.provider.getDimension(), new Local(world));
	}
	
	private static Local getNet(World world, boolean create)
	{
		if(!create)
			return netMap.getOrDefault(world.provider.getDimension(), Local.instance);
		if(!netMap.containsKey(world.provider.getDimension()))
		{
			netMap.put(world.provider.getDimension(), new Local(world));
		}
		return netMap.get(world.provider.getDimension());
	}
	
	private static class Local implements IElectricalNet
	{
		private static final Local instance = new Local(null);
		
		private volatile boolean isUpdating = false;

		private boolean coefficientChanged = false;
		private boolean structureChanged = false;
		
		private final World world;
		private final IntArray cache = new IntArray(5);
		private final EquationResolver resolver = new EquationResolver();
		private final RoutesAC routes = new RoutesAC();
		private final List<IACHandler> list = new ArrayList();
		private final List<IACHandler> routeChangedList = new ArrayList();
		private final Map<IACHandler, EnumModifyFlag> cacheChangedList = new HashMap();
		private final List<Double> voltageCache = new ArrayList();
		
		public Local(World world)
		{
			this.world = world;
		}

		public void add(IACHandler tile)
		{
			if(isUpdating)
			{
				cacheChangedList.put(tile, EnumModifyFlag.add);
			}
			else
			{
				addUnsafe(tile);
			}
		}
		
		private void addUnsafe(IACHandler tile)
		{
			list.add(tile);
			routes.addHandler(tile);
			structureChanged = true;
		}

		public void remove(IACHandler tile)
		{
			if(isUpdating)
			{
				cacheChangedList.put(tile, EnumModifyFlag.remove);
			}
			else
			{
				removeUnsafe(tile);
			}
		}
		
		private void removeUnsafe(IACHandler tile)
		{
			list.remove(tile);
			routes.removeHandler(tile);
			structureChanged = true;
		}

		@Override
		public void reload(IElectricalHandler tile)
		{
			if(tile instanceof IACHandler)
			{
				reload((IACHandler) tile);
			}
		}

		public void reload(IACHandler tile)
		{
			if(isUpdating)
			{
				cacheChangedList.put(tile, EnumModifyFlag.reload);
			}
			else
			{
				removeUnsafe(tile);
				addUnsafe(tile);
			}
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
				routes.onUpdate(voltageCache);
			}
			catch(Exception exception)
			{
				if(FarCore.debug)
				{
					Log.error("Fail to update energy net.", exception);
				}
			}
			for(Entry<IACHandler, EnumModifyFlag> entry : cacheChangedList.entrySet())
			{
				switch(entry.getValue())
				{
				case add : addUnsafe(entry.getKey());
				break;
				case remove : removeUnsafe(entry.getKey());
				break;
				case reload :
					removeUnsafe(entry.getKey());
					addUnsafe(entry.getKey());
					break;
				case mark :
					coefficientChanged = true;
					break;
				}
			}
			cacheChangedList.clear();
			isUpdating = false;
		}
		
		private void refind()
		{
			//Initialize routes.
			routes.rebuildNodes();
			//Initialize resolver and size.
			List<NodeRebuild> list = routes.rebuildNodes;
			int size = list.size();
			if(size <= 1) return;//If only has one electrical element, will not calculate.
			resolver.rewind(size);
			double[] I = new double[size];
			
			//Get all coefficients of matrix.
			for(int y = 0; y < size; ++y)
			{
				Node node = list.get(y);
				Map<Node, Link> links = node.links();
				I[y] = getBasicCurrent(links, node);
				for(int x = 0; x < size; ++x)
				{
					resolver.push(getCoefficient(list, links, x, y));
				}
				resolver.enter();
			}
			resolver.finalized();
			//Solve equations.
			if(!resolver.solve(I)) throw new RuntimeException("Invalid solving!");
			//Set voltage cache to each node.
			voltageCache.clear();
			for(int i = 0; i < size; ++i)
			{
				voltageCache.add(i, I[i]);
			}
			routes.resetChanging();
		}
		
		private void recalculate()
		{
			List<NodeReal> changedNodes = routes.getChangedNodes();
			//As I am thinking how to recalculate with changed coefficient.
			//Now just use refind instead.
			refind();
		}

		public void mark(IACHandler tile)
		{
			if(isUpdating)
			{
				cacheChangedList.put(tile, EnumModifyFlag.mark);
			}
			else
			{
				markUnsafe(tile);
			}
		}

		private void markUnsafe(IACHandler tile)
		{
			coefficientChanged = true;
		}
		
		/**
		 * Get basic current of each node, add in matrix.
		 * @param links
		 * @param node
		 * @return
		 */
		private double getBasicCurrent(Map<Node, Link> links, Node node)
		{
			double ret = 0;
			double V = node.voltage();
			double V1;
			if(links.size() != 1)
			{
				if(V == 0) return ret;
			}
			else if(V == 0)
			{
				V = -1D;
			}
			else
			{
				for(Entry<Node, Link> entry : links.entrySet())
				{
					V1 = entry.getKey().voltage();
					ret -= V - V1 / entry.getValue().resistance();
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
		private double getCoefficient(List<NodeRebuild> list, Map<Node, Link> links, int x, int y)
		{
			double cellData = 0;
			if(x == y)
			{
				for(Entry<Node, Link> entry : links.entrySet())
				{
					cellData -= 1.0 / entry.getValue().resistance();
				}
			}
			else
			{
				for(Entry<Node, Link> entry : links.entrySet())
				{
					if(list.indexOf(entry.getKey()) == x)
					{
						cellData += 1.0 / entry.getValue().resistance();
						break;
					}
				}
			}
			return cellData;
		}
	}
}