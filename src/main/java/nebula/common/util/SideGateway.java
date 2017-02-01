package nebula.common.util;

public final class SideGateway<T>
{
	private final T clientInstance;
	private final T serverInstance;
	
	public SideGateway(String serverClass, String clientClass)
	{
		try
		{
			this.serverInstance = (T) Class.forName(serverClass).newInstance();
			this.clientInstance = Sides.isSimulating() ? (T) Class.forName(clientClass).newInstance() : this.serverInstance;
		}
		catch (Exception exception)
		{
			throw new RuntimeException(exception);
		}
	}
	
	public T get()
	{
		return Sides.isSimulating() ? this.clientInstance : this.serverInstance;
	}
}