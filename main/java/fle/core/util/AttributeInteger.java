package fle.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AttributeInteger extends Attribute<Integer>
{
	public AttributeInteger(String aName, int hashValue)
	{
		super(Integer.class, aName, 0, hashValue);
	}
	public AttributeInteger(String aName, int d, int hashValue)
	{
		super(Integer.class, aName, d, hashValue);
	}

	@Override
	public void write(DataOutputStream stream, Integer art) throws IOException
	{
		stream.write(art);
	}

	@Override
	public Integer read(DataInputStream stream) throws IOException
	{
		return stream.read();
	}
}