package farcore.handler;

import java.util.ArrayList;
import java.util.List;

import farcore.energy.IEnergyNet;
import farcore.event.EnergyEvent;
import nebula.Log;
import nebula.Nebula;
import nebula.common.world.ICoord;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventExceptionHandler;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

/**
 * Handle energy net and object update.
 * @author ueyudiud
 *
 */
public class FarCoreEnergyHandler implements IEventExceptionHandler
{
	private static final FarCoreEnergyHandler HANDLER = new FarCoreEnergyHandler();
	/**
	 * The bus handle energy net updating and handler in net refreshing.
	 */
	public static final EventBus BUS = new EventBus(HANDLER);
	private static final List<IEnergyNet> energyNets = new ArrayList<>();
	
	static
	{
		BUS.register(HANDLER);
	}
	
	public static FarCoreEnergyHandler getHandler()
	{
		return HANDLER;
	}
	
	public static void addNet(IEnergyNet net)
	{
		energyNets.add(net);
		Log.info("The energy net which type is " + net.getClass().toString() + " is registered to energy handler.");
	}
	
	public static void onAddFromWorld(ICoord coord)
	{
		BUS.post(new EnergyEvent.Add(coord.getTE()));
	}
	
	public static void onRemoveFromWorld(ICoord coord)
	{
		BUS.post(new EnergyEvent.Remove(coord.getTE()));
	}
	
	public static void onMarkFromWorld(ICoord coord)
	{
		BUS.post(new EnergyEvent.Mark(coord.getTE()));
	}
	
	public static void onReloadFromWorld(ICoord coord)
	{
		BUS.post(new EnergyEvent.Reload(coord.getTE()));
	}
	
	private FarCoreEnergyHandler() {}
	
	@SubscribeEvent
	public void add(EnergyEvent.Add event)
	{
		if(!event.world().isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.add(event.tile);
				}
				catch(Throwable throwable)
				{
					if(Nebula.debug)
					{
						Log.error("Fail to add tile.", throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void mark(EnergyEvent.Mark event)
	{
		if(!event.world().isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.mark(event.tile);
				}
				catch(Throwable throwable)
				{
					if(Nebula.debug)
					{
						Log.error("Fail to mark for update.", throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void reload(EnergyEvent.Reload event)
	{
		if(!event.world().isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.reload(event.tile);
				}
				catch(Throwable throwable)
				{
					if(Nebula.debug)
					{
						Log.error("Fail to reload tile.", throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void remove(EnergyEvent.Remove event)
	{
		if(!event.world().isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.remove(event.tile);
				}
				catch(Throwable throwable)
				{
					if(Nebula.debug)
					{
						Log.error("Fail to remove tile.", throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void load(WorldEvent.Load event)
	{
		if(!event.getWorld().isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.load(event.getWorld());
				}
				catch(Throwable throwable)
				{
					if(Nebula.debug)
					{
						Log.error("Fail to load energy net.", throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void update(TickEvent.WorldTickEvent event)
	{
		if(!event.world.isRemote && event.phase == Phase.START)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.update(event.world);
				}
				catch(Throwable throwable)
				{
					if(Nebula.debug)
					{
						Log.error("Fail to update net.", throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void unload(WorldEvent.Unload event)
	{
		if(!event.getWorld().isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.unload(event.getWorld());
				}
				catch(Throwable throwable)
				{
					if(Nebula.debug)
					{
						Log.error("Fail to unload net.", throwable);
					}
				}
			}
		}
	}
	
	@Override
	public void handleException(EventBus bus, Event event, IEventListener[] listeners, int index, Throwable throwable)
	{
		Log.logger().catching(throwable);
	}
}