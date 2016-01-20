package farcore.net;

import java.io.IOException;

public abstract class FleAbstractPacket extends IPacket
{
	protected abstract void write(FlePacketBuffer buffer) throws IOException;
	
	protected abstract void read(FlePacketBuffer buffer) throws IOException;
	
	@Override
	public FlePacketBuffer encode(FlePacketBuffer buffer) throws IOException
	{
		write(buffer);
		return buffer;
	}
	
	@Override
	public void decode(FlePacketBuffer buffer) throws IOException
	{
		read(buffer);
	}
}