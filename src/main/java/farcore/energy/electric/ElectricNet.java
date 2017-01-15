package farcore.energy.electric;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import farcore.FarCore;
import farcore.energy.IEnergyNet;
import farcore.energy.electric.util.ElectricNetCaculator;
import farcore.energy.electric.util.NodeReal;
import farcore.energy.electric.util.NodeRebuild;
import farcore.energy.electric.util.Routes;
import farcore.lib.collection.IntegerArray;
import farcore.lib.util.EnumModifyFlag;
import farcore.lib.util.Log;
import net.minecraft.world.World;

/**
 * The electrical net.
 * @author ueyudiud
 *
 */
public class ElectricNet implements IEnergyNet
{
	public static final ElectricNet instance = new ElectricNet();
	private static Map<Integer, Local> netMap = new HashMap();
	
	@Override
	public void update(World world)
	{
		getNet(world, false).updateNet();
	}
	
	@Override
	public void add(Object tile)
	{
		if(tile instanceof IElectricalHandler)
		{
			getNet(((IElectricalHandler) tile).world(), false).add((IElectricalHandler) tile);
		}
	}
	
	@Override
	public void remove(Object tile)
	{
		if(tile instanceof IElectricalHandler)
		{
			getNet(((IElectricalHandler) tile).world(), false).remove((IElectricalHandler) tile);
		}
	}
	
	@Override
	public void mark(Object tile)
	{
		if(tile instanceof IElectricalHandler)
		{
			getNet(((IElectricalHandler) tile).world(), false).mark((IElectricalHandler) tile);
		}
	}
	
	@Override
	public void reload(Object tile)
	{
		if(tile instanceof IElectricalHandler)
		{
			getNet(((IElectricalHandler) tile).world(), false).reload((IElectricalHandler) tile);
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
		
		//		private boolean coefficientChanged = false;
		private boolean structureChanged = false;
		
		private final World world;
		private final IntegerArray cache = new IntegerArray(5);
		private final ElectricNetCaculator resolver = new ElectricNetCaculator();
		private final Routes routes = new Routes();
		private final List<IElectricalHandler> list = new ArrayList();
		private final List<IElectricalHandler> routeChangedList = new ArrayList();
		private final Map<IElectricalHandler, EnumModifyFlag> cacheChangedList = new HashMap();
		
		public Local(World world)
		{
			this.world = world;
		}
		
		public void add(IElectricalHandler tile)
		{
			if(this.isUpdating)
			{
				this.cacheChangedList.put(tile, EnumModifyFlag.add);
			}
			else
			{
				addUnsafe(tile);
			}
		}
		
		private void addUnsafe(IElectricalHandler tile)
		{
			this.list.add(tile);
			this.routes.addHandler(tile);
			this.structureChanged = true;
		}
		
		public void remove(IElectricalHandler tile)
		{
			if(this.isUpdating)
			{
				this.cacheChangedList.put(tile, EnumModifyFlag.remove);
			}
			else
			{
				removeUnsafe(tile);
			}
		}
		
		private void removeUnsafe(IElectricalHandler tile)
		{
			this.list.remove(tile);
			this.routes.removeHandler(tile);
			this.structureChanged = true;
		}
		
		@Override
		public void reload(IElectricalHandler tile)
		{
			if(this.isUpdating)
			{
				this.cacheChangedList.put(tile, EnumModifyFlag.reload);
			}
			else
			{
				removeUnsafe(tile);
				addUnsafe(tile);
			}
		}
		
		public void updateNet()
		{
			if(this.world == null) return;
			if(this.structureChanged)
			{
				refind();
			}
			this.structureChanged = false;
			this.isUpdating = true;
			try
			{
				for(IElectricalHandler handler : this.list)
				{
					handler.onElectricNetTicking(this);
				}
			}
			catch(Exception exception)
			{
				if(FarCore.debug)
				{
					Log.error("Fail to update energy net.", exception);
				}
			}
			for(Entry<IElectricalHandler, EnumModifyFlag> entry : this.cacheChangedList.entrySet())
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
					//					this.coefficientChanged = true;
					break;
				}
			}
			this.cacheChangedList.clear();
			this.isUpdating = false;
		}
		
		private void refind()
		{
			//Initialize routes.
			this.routes.rebuildNodes();
			//Initialize resolver and size.
			List<NodeRebuild> list = this.routes.rebuildNodes;
			int size = list.size();
			if(size <= 1) return;//If only has one electrical element, will not calculate.
			this.resolver.refreshNodes(this.routes);
			this.routes.resetChanging();
		}
		
		@Deprecated
		private void recalculate()
		{
			List<NodeReal> changedNodes = this.routes.getChangedNodes();
			//As I am thinking how to recalculate with changed coefficient.
			//Now just use refind instead.
			refind();
		}
		
		public void mark(IElectricalHandler tile)
		{
			if(this.isUpdating)
			{
				this.cacheChangedList.put(tile, EnumModifyFlag.mark);
			}
			else
			{
				markUnsafe(tile);
			}
		}
		
		private void markUnsafe(IElectricalHandler tile)
		{
			//			this.coefficientChanged = true;
		}
		
		public void updateNetBody(float tickToNext)
		{
			this.resolver.caculate();
			this.routes.onUpdate(this.resolver.getResult());
		}
	}
}