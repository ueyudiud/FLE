/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.Iterator;
import java.util.Map;

import nebula.common.data.Misc;
import nebula.common.util.L;

/**
 * @author ueyudiud
 */
class FSMap extends FSObject<Map>
{
	FSMap(Map value)
	{
		super(value);
	}
	
	@Override
	public IFSObject operator_addset(IFSObject object)
	{
		if (object instanceof FSTuple)
		{
			FSTuple tuple = (FSTuple) object;
			return FSObjects.pack(this.value.put(tuple.elements[0], tuple.elements[1]));
		}
		return super.operator_addset(object);
	}
	
	@Override
	public IFSObject operator_subset(IFSObject object)
	{
		return FSObjects.pack(this.value.remove(FSObjects.unpack(object)));
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		if (objects.length != 1)
			throw new UnsupportedOperationException();
		Object key = FSObjects.unpack(objects[0]);
		return new WrapperFSObject()
		{
			@Override
			public IFSObject operator_set(IFSObject object)
			{
				FSMap.this.value.put(key, FSObjects.unpack(object));
				return object;
			}
			
			@Override
			protected IFSObject object()
			{
				return new FSMapLazyEntry(FSMap.this.value, key);
			}
			
			@Override
			public IFSObject eval()
			{
				return object();
			}
		};
	}
	
	@Override
	public Iterator<?> asIterator()
	{
		return this.value.entrySet().iterator();
	}
}

class FSMapLazyEntry extends WrapperFSObject
{
	private static final Object NULL = new Object();
	
	private Map<Object, ?> source;
	private Object key;
	
	FSMapLazyEntry(Map map, Object key)
	{
		this.source = map;
		this.key = key;
	}
	
	@Override
	protected IFSObject object()
	{
		return FSObjects.pack(this.source.get(this.key));
	}
	
	@Override
	public IFSObject operator_ior(IFSObject object)
	{
		Object value = this.source.getOrDefault(this.key, L.castAny(NULL));
		return value == NULL ? object : FSObjects.pack(value);
	}
	
	@Override
	public IFSObject operator_orset(IFSObject object)
	{
		return FSObjects.pack(this.source.computeIfAbsent(object, L.castAny(Misc.anyTo(this.key))));
	}
}
