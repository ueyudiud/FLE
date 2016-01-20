package flapi;

import farcore.net.INetworkHandler;
import farcore.util.IKeyBoard;
import flapi.energy.EnergyType;
import flapi.energy.IEnergyNet;
import flapi.util.IPlatform;

public interface FleMod
{
	IPlatform getPlatform();

	IEnergyNet getEnergyNet(EnergyType type);

	INetworkHandler getNetworkHandler();
	
	IKeyBoard getKeyboard();
}