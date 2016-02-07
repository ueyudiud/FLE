package farcore;

import farcore.net.INetworkHandler;
import farcore.util.IColorMapHandler;
import farcore.util.IKeyBoard;

public interface FleMod
{
	//IEnergyNet getEnergyNet(EnergyType type);

	INetworkHandler getNetworkHandler();
	
	IKeyBoard getKeyboard();

	IColorMapHandler getColorMapHandler();
}