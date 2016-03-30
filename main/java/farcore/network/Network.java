package farcore.network;

import io.netty.channel.ChannelHandler;

@ChannelHandler.Sharable
public class Network extends NetworkBasic
{
	public static Network newHandler(String name)
	{
		return new Network(name);
	}

	private Network(String name)
	{
		super(name);
		register.register(name, this);
	}
}