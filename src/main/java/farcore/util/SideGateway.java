package farcore.util;

import cpw.mods.fml.common.FMLCommonHandler;

public final class SideGateway<T>
{
	private final T clientInstance;
	private final T serverInstance;

	public SideGateway(String serverClass, String clientClass)
	{
		try
		{
			serverInstance = ((Class<? extends T>) Class.forName(serverClass)).newInstance();
			if (U.Sides.isSimulating())
			{
				clientInstance = ((Class<? extends T>) Class.forName(clientClass)).newInstance();
			}
			else
			{
				clientInstance = serverInstance;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public T get()
	{
		if (U.Sides.isClient())
		{
			return clientInstance;
		}
		else
		{
			return serverInstance;
		}
	}
}