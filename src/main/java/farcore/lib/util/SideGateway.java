package farcore.lib.util;

import farcore.util.U.Sides;

public final class SideGateway<T>
{
	private final T clientInstance;
	private final T serverInstance;
	
	public SideGateway(String serverClass, String clientClass)
	{
		try
		{
			serverInstance = (T) Class.forName(serverClass).newInstance();
			clientInstance = Sides.isSimulating() ? (T) Class.forName(clientClass).newInstance() : serverInstance;
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}
	
	public T get()
	{
		if (Sides.isSimulating())
			return clientInstance;
		else
			return serverInstance;
	}
}