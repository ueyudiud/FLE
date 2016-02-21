package fle.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AttributeBoolean extends Attribute<Boolean>
{
	public AttributeBoolean(String aName, int hashValue)
	{
		super(Boolean.class, aName, false, hashValue);
	}

	@Override
	public void write(DataOutputStream stream, Boolean art) throws IOException 
	{
		stream.writeBoolean(art);
	}

	@Override
	public Boolean read(DataInputStream stream) throws IOException
	{
		return stream.readBoolean();
	}
}