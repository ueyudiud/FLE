/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.io.image;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.BitSet;

/**
 * @author ueyudiud
 */
class BufferedBitmapSimple extends BufferedBitmap
{
	final int width;
	BitSet set;
	
	BufferedBitmapSimple(int width, int height, Color color1, Color color2)
	{
		super(height, color1, color2);
		this.width = width;
		this.set = new BitSet(width * height);
	}
	
	@Override
	public int getWidth()
	{
		return this.width;
	}
	
	@Override
	public void fill(boolean flag)
	{
		this.set.set(0, this.width * this.height, flag);
	}
	
	@Override
	public void drawPixel(int x, int y)
	{
		if (x >= 0 && x < this.width && y >= 0 && y < this.height)
			this.set.set(x + y * this.width);
	}
	
	@Override
	public void removePixel(int x, int y)
	{
		if (x >= 0 && x < this.width && y >= 0 && y < this.height)
			this.set.clear(x + y * this.width);
	}
	
	@Override
	public void drawRect(int x, int y, int u, int v)
	{
		if (x >= this.width || x + u < 0 || y >= this.height || y + v < 0) return;
		int x1 = x < 0 ? 0 : x;
		int x2 = x + u >= this.width ? this.width : x + u;
		int y1 = y < 0 ? 0 : y;
		int y2 = y + v >= this.height ? this.height : y + v;
		for (int j = y1; j < y2; ++j)
		{
			this.set.set(x1 + j * this.width, x2 + j * this.width);
		}
	}
	
	@Override
	public void removeRect(int x, int y, int u, int v)
	{
		if (x >= this.width || x + u < 0 || y >= this.height || y + v < 0) return;
		int x1 = x < 0 ? 0 : x;
		int x2 = x + u >= this.width ? this.width : x + u;
		int y1 = y < 0 ? 0 : y;
		int y2 = y + v >= this.height ? this.height : y + v;
		for (int j = y1; j < y2; ++j)
		{
			this.set.clear(x1 + j * this.width, x2 + j * this.width);
		}
	}
	
	@Override
	protected void writeBitmap(DataOutputStream stream) throws IOException
	{
		if ((this.width & 0x3) == 0)
		{
			stream.write(this.set.toByteArray());
		}
		else
		{
			int index = 0;
			for (int i = 0; i < this.height; ++i)
			{
				int x = 0;
				for (int j = 0; j < this.width; ++j)
				{
					if (this.set.get(index))
						x |= 1 << (~j & 0x1F);
					if ((j & 0x1F) == 0x1F)
					{
						stream.writeInt(x);
						x = 0;
					}
					++index;
				}
				stream.writeInt(x);
			}
		}
	}
}