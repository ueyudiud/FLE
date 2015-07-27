package fle.core.util;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public final class SideGateway<T>
{
	private final T clientInstance;
	private final T serverInstance;

	public SideGateway(String serverClass, String clientClass)
	{
		try
		{
			if (FMLCommonHandler.instance().getSide().isClient())
				clientInstance = ((Class<? extends T>)Class.forName(clientClass)).newInstance();
			else
				clientInstance = null;
			serverInstance = ((Class<? extends T>)Class.forName(serverClass)).newInstance();
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public T get()
	{
		if (FMLCommonHandler.instance().getEffectiveSide().isClient())
			return clientInstance;
		else
			return serverInstance;
	}
}