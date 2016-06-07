package farcore.network;

import com.jcraft.jogg.Packet;

import cpw.mods.fml.relauncher.Side;
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
		registerPacket(PacketLarge.class, Side.CLIENT);//This is a important packet, used in the method in basic network.
	}
}