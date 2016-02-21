package fle.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class AttributeFloat extends Attribute<Float>
{
	public AttributeFloat(String aName, float defaultValue, int hashValue)
	{
		super(Float.class, aName, defaultValue, hashValue);
	}
	public AttributeFloat(String aName, int hashValue)
	{
		super(Float.class, aName, 0.0F, hashValue);
	}

	@Override
	public void write(DataOutputStream stream, Float art) throws IOException
	{
		stream.writeFloat(art);
	}

	@Override
	public Float read(DataInputStream stream) throws IOException 
	{
		return stream.readFloat();
	}
}