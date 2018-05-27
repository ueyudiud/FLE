/*
 * copyright 2016-2018 ueyudiud
 */
package fargen.core.util;

import nebula.base.function.BiIntToObjFunction;

/**
 * @author ueyudiud
 */
public class DataCacheCoord<D> extends DataCache<D[]>
{
	private final byte	len;
	private final int	off;
	
	public DataCacheCoord(BiIntToObjFunction<D[]> function, int size)
	{
		super(function);
		this.len = (byte) size;
		this.off = (1 << this.len) - 1;
	}
	
	public DataCacheCoord(BiIntToObjFunction<D[]> function, int size, long cacheDuration)
	{
		super(function, cacheDuration);
		this.len = (byte) size;
		this.off = (1 << this.len) - 1;
	}
	
	public D getBody(int x, int z)
	{
		return get(x >> this.len, z >> this.len)[(z << this.len & this.off) | x & this.off];
	}
	
	public D getBodyOrDefault(int x, int z, D def)
	{
		return isPresent(x >> this.len, z >> this.len) ? getBody(x, z) : def;
	}
}
