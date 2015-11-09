package fle.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AttributeIntArray extends Attribute<int[]>
{
	protected AttributeIntArray(String aName, int hashValue)
	{
		super(int[].class, aName, new int[0], hashValue);
	}

	@Override
	public void write(DataOutputStream stream, int[] art) throws IOException
	{
		stream.write(art.length);
		for(int i : art)
		{
			stream.write(i);
		}
	}

	@Override
	public int[] read(DataInputStream stream) throws IOException
	{
		int[] ret = new int[stream.read()];
		for(int i = 0; i < ret.length; ++i)
		{
			ret[i] = stream.read();
		}
		return ret;
	}
}