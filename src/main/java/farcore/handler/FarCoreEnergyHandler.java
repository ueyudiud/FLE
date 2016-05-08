package farcore.handler;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.IEventExceptionHandler;
import cpw.mods.fml.common.eventhandler.IEventListener;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import farcore.event.EnergyEvent;
import farcore.interfaces.energy.IEnergyNet;
import farcore.util.FleLog;
import farcore.util.V;
import net.minecraftforge.event.world.WorldEvent;

public class FarCoreEnergyHandler implements IEventExceptionHandler
{
	public static final EventBus BUS = new EventBus();
	private static final List<IEnergyNet> energyNets = new ArrayList();
		
	public static void addNet(IEnergyNet net)
	{
		energyNets.add(net);
		FleLog.getCoreLogger().info("The energy net which type is " + net.getClass().toString() + " is registered to energy handler.");
	}
	
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
					if(V.debug)
					{
						FleLog.getCoreLogger().catching(throwable);
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
					if(V.debug)
					{
						FleLog.getCoreLogger().catching(throwable);
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
					if(V.debug)
					{
						FleLog.getCoreLogger().catching(throwable);
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
					if(V.debug)
					{
						FleLog.getCoreLogger().catching(throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void load(WorldEvent.Load event)
	{
		if(!event.world.isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.load(event.world);
				}
				catch(Throwable throwable)
				{
					if(V.debug)
					{
						FleLog.getCoreLogger().catching(throwable);
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
					if(V.debug)
					{
						FleLog.getCoreLogger().catching(throwable);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void unload(WorldEvent.Unload event)
	{
		if(!event.world.isRemote)
		{
			for(IEnergyNet net : energyNets)
			{
				try
				{
					net.unload(event.world);
				}
				catch(Throwable throwable)
				{
					if(V.debug)
					{
						FleLog.getCoreLogger().catching(throwable);
					}
				}
			}
		}
	}

	@Override
	public void handleException(EventBus bus, Event event, IEventListener[] listeners, int index, Throwable throwable)
	{
		FleLog.getCoreLogger().catching(throwable);
	}
}