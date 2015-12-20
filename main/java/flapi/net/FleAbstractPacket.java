package flapi.net;

import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import flapi.util.io.FleDataInputStream;
import flapi.util.io.FleDataOutputStream;

public abstract class FleAbstractPacket extends IPacket
{
	protected abstract void write(FleDataOutputStream os) throws IOException;
	
	protected abstract void read(FleDataInputStream is) throws IOException;
	
	public final void init(ByteArrayDataOutput output, boolean flag)
	{
		FleDataOutputStream os = new FleDataOutputStream(output);
		try
		{
			write(os);
			if(flag)
				os.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public ByteArrayDataOutput encode() throws IOException
	{
		ByteArrayDataOutput output = ByteStreams.newDataOutput();
		init(output, true);
		return output;
	}
	
	@Override
	public void decode(ByteArrayDataInput input)
			throws IOException
	{
		read(new FleDataInputStream(input));
	}
}