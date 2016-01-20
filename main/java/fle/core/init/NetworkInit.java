package fle.core.init;

import net.minecraftforge.fml.relauncher.Side;
import farcore.net.FleBaseTEPacket;
import farcore.net.FleEntityPacket;
import farcore.net.FleNBTPacket;
import farcore.net.FleTEPacket;
import fle.core.net.FleKeyTypePacket;
import fle.core.net.FleLargePacket;
import fle.core.net.FleNetworkHandler;

public class NetworkInit
{
	public static void init(FleNetworkHandler handler)
	{
		handler.registerMessage(FleLargePacket.class, Side.CLIENT);
		handler.registerMessage(FleBaseTEPacket.class, Side.CLIENT);
		handler.registerMessage(FleNBTPacket.class, Side.CLIENT);
		handler.registerMessage(FleTEPacket.class, Side.CLIENT);
		handler.registerMessage(FleKeyTypePacket.class, Side.SERVER);
		handler.registerMessage(FleEntityPacket.class, Side.CLIENT);
	}
}