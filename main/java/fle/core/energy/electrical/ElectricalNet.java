package fle.core.energy.electrical;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.IEventExceptionHandler;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import farcore.util.Direction;
import farcore.util.FleLog;
import flapi.energy.ElectricalEvent.ElectricalChangedEvent;
import flapi.energy.ElectricalEvent.ElectricalChangedEvent.ChangeType;
import flapi.energy.ElectricalEvent.ElectricalLoadEvent;
import flapi.energy.ElectricalEvent.ElectricalUnloadEvent;
import flapi.energy.EnergyType;
import flapi.energy.IEleTile;
import flapi.energy.IElectricalNet;
import fle.core.world.WorldData;

public class ElectricalNet implements IElectricalNet, IEventExceptionHandler
{
	private final EventBus bus = new EventBus(this);

	public ElectricalNet()
	{
		
	}
	
	@Override
	public EnergyType getType()
	{
		return EnergyType.Electrical;
	}
	
	@Override
	public void addTileInNet(IEleTile tile)
	{
		bus.post(new ElectricalLoadEvent(tile));
	}
	
	@Override
	public void markTileInNet(IEleTile tile, ChangeType type)
	{
		bus.post(new ElectricalChangedEvent(tile, type));
	}
	
	@Override
	public void removeTileInNet(IEleTile tile)
	{
		bus.post(new ElectricalUnloadEvent(tile));	
	}
	
	@Override
	public double getCurrent(IEleTile tile, Direction dir)
	{
		return WorldData.get(tile.getWorld()).eleNet.getVoltage(tile.getBlockPos()) / tile.getNodes().getResistance(dir);
	}
	
	@Override
	public double getVoltage(IEleTile tile)
	{
		return WorldData.get(tile.getWorld()).eleNet.getVoltage(tile.getBlockPos());
	}

	@Override
	public EventBus getElectricalEvtBus()
	{
		return bus;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEleChange(ElectricalChangedEvent evt)
	{
		switch(evt.type)
		{
		case RESIDENCE :
			WorldData.get(evt.tile.getWorld()).eleNet.markTileEntity(evt.tile);
		break;
		case STATE:
			WorldData.get(evt.tile.getWorld()).eleNet.rejoinTileEntity(evt.tile);
		break;
		default:
			break;
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEleLoad(ElectricalLoadEvent evt)
	{
		WorldData.get(evt.tile.getWorld()).eleNet.addTileEntity(evt.tile);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEleUnload(ElectricalUnloadEvent evt)
	{
		WorldData.get(evt.tile.getWorld()).eleNet.removeTileEntity(evt.tile);
	}
	
	@Override
	public void handleException(EventBus bus, Event event,
			IEventListener[] listeners, int index, Throwable throwable)
	{
		FleLog.error("Catch an exception during handle energy net. "
				+ "Listener name " + listeners[index].toString() + " index " + index, throwable);
	}
}