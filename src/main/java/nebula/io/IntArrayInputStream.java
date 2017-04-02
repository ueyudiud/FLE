/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.io;

import java.io.InputStream;
import java.util.Objects;

/**
 * See {@link java.io.ByteArrayInputStream}
 * @author ueyudiud
 */
public
class IntArrayInputStream extends InputStream
{
	private static final int[] OFF = {0, 4, 8, 12};
	
	protected int[] buf;
	
	protected int pos;
	
	protected int count;
	
	protected int mark;
	
	public IntArrayInputStream(int[] buf, int pos, int count)
	{
		this.buf = buf;
		this.pos = pos;
		this.count = count;
	}
	
	@Override
	public synchronized int read()
	{
		int i = this.pos < this.count ? (this.buf[this.pos >> 2] >> OFF[this.pos & 0x3]) & 0xFF : -1;
		this.pos++;
		return i;
	}
	
	@Override
	public synchronized int read(byte[] b, int off, int len)
	{
		Objects.requireNonNull(b);
		if (off < 0 || len < 0 || len > b.length - off)
		{
			throw new IndexOutOfBoundsException();
		}
		
		if (this.pos >= this.count) return -1;
		
		int avail = this.count - this.pos;
		if (len > avail)
		{
			len = avail;
		}
		if (len <= 0)
			return 0;
		int i = this.pos >> 2;
		int o = this.pos & 0x3;
		int idx = off;
		int f = len;
		do
		{
			int val = this.buf[i++];
			switch (o)
			{
			case 0 : b[idx    ] = (byte) ((val      ) & 0xFF);
			case 1 : b[idx | 1] = (byte) ((val >>  4) & 0xFF);
			case 2 : b[idx | 2] = (byte) ((val >>  8) & 0xFF);
			case 3 : b[idx | 3] = (byte) ((val >> 12) & 0xFF);
			default: break;
			}
			f -= o;
			o = f >= 4 ? 0 : f;
			idx += 4;
		}
		while (f > 0);
		return len;
	}
	
	@Override
	public synchronized long skip(long n)
	{
		long k = this.count - this.pos;
		if (n < k)
		{
			k = n < 0 ? 0 : n;
		}
		
		this.pos += k;
		return k;
	}
	
	@Override
	public synchronized int available()
	{
		return this.count - this.pos;
	}
	
	@Override
	public boolean markSupported()
	{
		return true;
	}
	
	@Override
	public synchronized void mark(int readlimit)
	{
		this.mark = this.pos;
	}
	
	@Override
	public synchronized void reset()
	{
		this.pos = this.mark;
	}
	
	@Override
	public void close()
	{
		
	}
}