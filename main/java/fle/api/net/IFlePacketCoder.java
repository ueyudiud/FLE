package fle.api.net;

import java.io.IOException;

import fle.api.util.FleDataOutputStream;

public interface IFlePacketCoder
{
	public abstract int getCoderID();
	
	public abstract void createPacket(FleDataOutputStream aStream) throws IOException;
}
