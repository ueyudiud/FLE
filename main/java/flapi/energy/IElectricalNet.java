package flapi.energy;

import net.minecraftforge.fml.common.eventhandler.EventBus;
import farcore.util.Direction;
import flapi.energy.ElectricalEvent.ElectricalChangedEvent.ChangeType;

public interface IElectricalNet extends IEnergyNet
{
	void addTileInNet(IEleTile tile);
	void markTileInNet(IEleTile tile, ChangeType type);
	void removeTileInNet(IEleTile tile);
	
	double getVoltage(IEleTile tile);
	
	double getCurrent(IEleTile tile, Direction dir);
	
	EventBus getElectricalEvtBus();
}