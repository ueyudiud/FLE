package fle.api.net;

import cpw.mods.fml.common.network.FMLEventChannel;
import cpw.mods.fml.common.network.NetworkRegistry;

public abstract class FleNetworkHandler 
{
	public abstract FMLEventChannel getChannel();
}