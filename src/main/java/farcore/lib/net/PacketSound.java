package farcore.lib.net;

import java.io.IOException;

import farcore.network.IPacket;
import farcore.network.NetworkBasic;
import farcore.network.PacketAbstract;
import farcore.util.U;
import farcore.util.io.DataStream;
import net.minecraft.world.World;

public class PacketSound extends PacketAbstract
{
	private double x, y, z;
	private String name;
	private float s, m;
	
	public PacketSound(double x, double y, double z, String name, float s, float m)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = name;
		this.s = s;
		this.m = m;
	}

	@Override
	protected void encode(DataStream output) throws IOException
	{
		output.writeString(name);
		output.writeDouble(x);
		output.writeDouble(y);
		output.writeDouble(z);
		output.writeFloat(s);
		output.writeFloat(m);
	}

	@Override
	protected void decode(DataStream input) throws IOException
	{
		name = input.readString();
		x = input.readDouble();
		y = input.readDouble();
		z = input.readDouble();
		s = input.readFloat();
		m = input.readFloat();
	}

	@Override
	public IPacket process(NetworkBasic network)
	{
		U.Sound.displaySound(name, 1, s, x, y, z);
		return null;
	}
}