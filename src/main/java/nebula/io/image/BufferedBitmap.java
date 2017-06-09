/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.io.image;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author ueyudiud
 */
public abstract class BufferedBitmap
{
	public static BufferedBitmap createBitmap(int width, int height)
	{
		return createBitmap(width, height, Color.BLACK, Color.WHITE);
	}
	
	public static BufferedBitmap createBitmap(int width, int height, Color color1, Color color2)
	{
		switch (width)
		{
		case 32 : return new BufferedBitmap32Pix(height, color1, color2);
		default : return new BufferedBitmapSimple(width, height, color1, color2);
		}
	}
	
	final int height;
	final int backgroundColor;
	final int frontColor;
	
	BufferedBitmap(int height, Color color1, Color color2)
	{
		this.height = height;
		this.backgroundColor = color1.getRGB();
		this.frontColor = color2.getRGB();
	}
	
	public abstract int getWidth();
	
	public final int getHeight()
	{
		return this.height;
	}
	
	public abstract void fill(boolean flag);
	
	public abstract void drawPixel(int x, int y);
	
	public abstract void removePixel(int x, int y);
	
	public void setPixel(int x, int y, boolean flag)
	{
		if (flag) drawPixel(x, y); else removePixel(x, y);
	}
	
	public abstract void drawRect(int x, int y, int u, int v);
	
	public abstract void removeRect(int x, int y, int u, int v);
	
	public void setRect(int x, int y, int u, int v, boolean flag)
	{
		if (flag) drawRect(x, y, u, v); else removeRect(x, y, u, v);
	}
	
	public final void writeToStream(OutputStream stream) throws IOException
	{
		writeToStream(stream, getSize());
	}
	
	public final byte[] toByteArray()
	{
		try
		{
			int size = getSize();
			ByteArrayOutputStream stream = new ByteArrayOutputStream(size);
			writeToStream(stream, size);
			return stream.toByteArray();
		}
		catch (IOException e)
		{
			throw new InternalError(e);
		}
	}
	
	protected int getDataSize()
	{
		return (((getWidth() + 0x1F) >> 5) * getHeight() << 2);
	}
	
	protected int getSize()
	{
		return 62 + getDataSize();
	}
	
	private static void writeShort(OutputStream stream, int v) throws IOException
	{
		stream.write(v      & 0xFF);
		stream.write(v >> 8 & 0xFF);
	}
	
	private static void writeInt(OutputStream stream, int v) throws IOException
	{
		stream.write(v       & 0xFF);
		stream.write(v >>  8 & 0xFF);
		stream.write(v >> 16 & 0xFF);
		stream.write(v >> 24 & 0xFF);
	}
	
	protected void writeToStream(OutputStream rawStream, int length) throws IOException
	{
		writeShort(rawStream, 0x4D42);//Type
		writeInt(rawStream, length);//Size
		writeInt(rawStream, 0);
		writeInt(rawStream, 0x3E);//Data Offset
		
		writeInt(rawStream, 0x28);//Header Size
		writeInt(rawStream, getWidth());//Width
		writeInt(rawStream, this.height);//Height
		writeShort(rawStream, 1);//Plane
		writeShort(rawStream, 1);//Bit per model
		writeInt(rawStream, 0);//Compression
		writeInt(rawStream, getDataSize());//Data Size
		writeInt(rawStream, 0);//HResolution
		writeInt(rawStream, 0);//VResolution
		writeInt(rawStream, 0);//Colors
		writeInt(rawStream, 0);//Important
		
		writeInt(rawStream, this.backgroundColor);
		writeInt(rawStream, this.frontColor);
		
		DataOutputStream stream = rawStream instanceof DataOutputStream ? (DataOutputStream) rawStream : new DataOutputStream(rawStream);
		
		writeBitmap(stream);
		
		rawStream.close();
	}
	
	protected abstract void writeBitmap(DataOutputStream stream) throws IOException;
}