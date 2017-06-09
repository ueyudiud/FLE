/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.io.image;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author ueyudiud
 */
class BufferedBitmap32Pix extends BufferedBitmap
{
	final int[] bits;
	
	BufferedBitmap32Pix(int height, Color color1, Color color2)
	{
		super(height, color1, color2);
		this.bits = new int[height];
	}
	
	@Override
	public int getWidth()
	{
		return 32;
	}
	
	@Override
	public void fill(boolean flag)
	{
		Arrays.fill(this.bits, flag ? -1 : 0);
	}
	
	@Override
	public void drawPixel(int x, int y)
	{
		if ((x & 0xFFFFFFF0) == 0 && //x >= 0 && x < 32
				y < this.height && y >= 0)
			this.bits[y] |= 1 << (x ^ 0xF);
	}
	
	@Override
	public void removePixel(int x, int y)
	{
		if ((x & 0xFFFFFFF0) == 0 && //x >= 0 && x < 32
				y < this.height && y >= 0)
			this.bits[y] &= ~(1 << (x ^ 0xF));
	}
	
	@Override
	public void drawRect(int x, int y, int u, int v)
	{
		if (x >= 32 || x + u < 0 || y >= this.height || y + v < 0) return;
		int y1 = y < 0 ? 0 : y;
		int y2 = y + v >= this.height ? this.height : y + v;
		x ^= 0x1F;//Flip x
		for (int i = y1; i < y2; ++i)
		{
			this.bits[i] |= ((1L << u) - 1) << (x + 1 - u);
		}
	}
	
	@Override
	public void removeRect(int x, int y, int u, int v)
	{
		if (x >= 32 || x + u < 0 || y >= this.height || y + v < 0) return;
		int y1 = y < 0 ? 0 : y;
		int y2 = y + v >= this.height ? this.height : y + v;
		x ^= 0x1F;//Flip x
		for (int i = y1; i < y2; ++i)
		{
			this.bits[i] &= ~(((1L << u) - 1) << (x + 1 - u));
		}
	}
	
	@Override
	protected int getDataSize()
	{
		return this.height << 2;
	}
	
	@Override
	protected void writeBitmap(DataOutputStream stream) throws IOException
	{
		for (int i : this.bits)
			stream.writeInt(i);
	}
}