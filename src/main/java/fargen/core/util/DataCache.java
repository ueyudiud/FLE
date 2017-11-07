/*
 * copyright© 2016-2017 ueyudiud
 */

package fargen.core.util;

import java.util.Iterator;
import java.util.LinkedList;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import nebula.base.function.BiIntToObjFunction;
import net.minecraft.server.MinecraftServer;

/**
 * @author ueyudiud
 */
public class DataCache<D>
{
	public final class Data
	{
		final int		x;
		final int		z;
		public final D	store;
		long			lastAccessTime;
		
		public Data(int x, int z)
		{
			this.x = x;
			this.z = z;
			this.store = DataCache.this.function.apply(x, z);
		}
	}
	
	/** The applier function. */
	private final BiIntToObjFunction<D>	function;
	/** The last time this cache was cleaned, in milliseconds. */
	private long						lastCleanupTime;
	/** The cache not be removed duration. */
	private final long					cacheDuration;
	/** The cache store map. */
	private final Long2ObjectMap<Data>	cacheMap	= new Long2ObjectOpenHashMap<>(4096);
	/** The cache store list. */
	private final LinkedList<Data>		caches		= new LinkedList<>();
	
	public DataCache(BiIntToObjFunction<D> function)
	{
		this(function, 30000L);
	}
	
	public DataCache(BiIntToObjFunction<D> function, long cacheDuration)
	{
		this.function = function;
		this.cacheDuration = cacheDuration;
	}
	
	private long hash(int x, int z)
	{
		return x & 4294967295L | (z & 4294967295L) << 32;
	}
	
	public D get(int x, int z)
	{
		long hash = hash(x, z);
		Data data = this.cacheMap.get(hash);
		
		if (data == null)
		{
			this.cacheMap.put(hash, data = new Data(x, z));
			this.caches.addLast(data);
		}
		
		data.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
		return data.store;
	}
	
	public D getOrDefault(int x, int z, D def)
	{
		long hash = hash(x, z);
		Data data = this.cacheMap.get(hash);
		
		if (data == null) return def;
		
		data.lastAccessTime = MinecraftServer.getCurrentTimeMillis();
		return data.store;
	}
	
	public boolean isPresent(int x, int z)
	{
		return this.cacheMap.containsKey(hash(x, z));
	}
	
	public void clean()
	{
		long i = MinecraftServer.getCurrentTimeMillis();
		long j = i - this.lastCleanupTime;
		
		if (j > 7500L || j < 0L)
		{
			this.lastCleanupTime = i;
			Iterator<Data> itr = this.caches.iterator();
			
			while (itr.hasNext())
			{
				Data data = itr.next();
				long l = i - data.lastAccessTime;
				
				if (l > this.cacheDuration || l < 0L)
				{
					itr.remove();
					long i1 = hash(data.x, data.z);
					this.cacheMap.remove(i1);
				}
			}
		}
	}
}
