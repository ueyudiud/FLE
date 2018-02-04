/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.base.register;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import nebula.base.AbstractRegister;
import net.minecraft.network.PacketBuffer;

/**
 * @author ueyudiud
 */
public class IdAllocatableRegister<T extends IRegisterElement<T>> extends AbstractRegister<T>
{
	class IdAllocatableDelegate implements IRegisterDelegate<T>, Entry<String, T>
	{
		final String name;
		boolean fixedID;
		int id;
		T value;
		
		IdAllocatableDelegate(T value, int id)
		{
			this(value);
			this.id = id;
			this.fixedID = true;
		}
		
		IdAllocatableDelegate(T value)
		{
			this.name = value.getRegisteredName();
			this.value = value;
		}
		
		IdAllocatableDelegate(String name)
		{
			this.id = -1;
			this.name = name;
			this.fixedID = false;
		}
		
		@Override
		public T get()
		{
			return this.value;
		}
		
		@Override
		public String name()
		{
			return this.name;
		}
		
		@Override
		public int id()
		{
			return this.id;
		}
		
		@Override
		public void changeRef(T value)
		{
			this.value = value;
		}
		
		@Override
		public void changeID(int id)
		{
			this.id = id;
		}
		
		@Override
		public boolean isDelegateEqual(IRegisterDelegate<T> delegate)
		{
			return this.value == null || delegate.get() == null ?
					delegate.name() == null ?
							this.id != -1 && this.id == delegate.id() :
								this.name.equals(delegate.name()) :
									this.value == delegate.get();
		}
		
		@Override
		public String getKey()
		{
			return this.name;
		}
		
		@Override
		public T getValue()
		{
			return this.value;
		}
		
		@Override
		public T setValue(T value)
		{
			//T old = this.value;
			//changeRef(value);
			//return old;
			throw new UnsupportedOperationException();
		}
	}
	
	class FromIdDelegate implements IRegisterDelegate<T>
	{
		private final int id;
		T value;
		
		FromIdDelegate(int id)
		{
			this.id = id;
		}
		
		@Override
		public T get()
		{
			if (this.value == null)
			{
				this.value = IdAllocatableRegister.this.get(this.id);
			}
			return this.value;
		}
		
		@Override
		public String name()
		{
			T value = get();
			return value != null ? value.getRegisteredName() : null;
		}
		
		@Override
		public int id()
		{
			return this.id;
		}
		
		@Override
		public void changeRef(T value)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void changeID(int id)
		{
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean isDelegateEqual(IRegisterDelegate<T> delegate)
		{
			return delegate instanceof IdAllocatableRegister<?>.FromIdDelegate ?
					((FromIdDelegate) delegate).id == this.id :
						delegate.isDelegateEqual(this);
		}
	}
	
	private final IdAllocatableDelegate empty_delegate = new IdAllocatableDelegate((String) null);
	
	{
		this.empty_delegate.fixedID = true;
	}
	
	private final boolean createEmptyKey;
	private final Map<String, IdAllocatableDelegate> delegates = new HashMap<>();
	private final BiMap<Integer, T> fixedIdMap = HashBiMap.create();
	private final Set<T> values = new HashSet<>();
	private BiMap<Integer, T> idMap = HashBiMap.create();
	
	public IdAllocatableRegister(boolean createEmptyKey)
	{
		this.createEmptyKey = createEmptyKey;
	}
	
	private IdAllocatableDelegate getOrCreateDelegate(String name)
	{
		return this.createEmptyKey ?
				this.delegates.computeIfAbsent(name, IdAllocatableDelegate::new) :
					this.delegates.getOrDefault(name, this.empty_delegate);
	}
	
	private T checkArgName(String name, T arg)
	{
		String name1 = arg.getRegisteredName();
		if (name1 == null)
			arg.setRegistryName(name);
		else if (!name1.equals(name))
			throw new IllegalStateException("The registry name '" + name + "' already exist!");
		if (arg.getRegisteredName().length() > 128)
			throw new IllegalArgumentException("The name '" + arg.getRegisteredName() + "' is too long! Please shorter than 128.");
		return arg;
	}
	
	public IRegisterDelegate<T> register(T arg)
	{
		if (arg.getRegisteredName().length() > 128)
			throw new IllegalArgumentException("The name '" + arg.getRegisteredName() + "' is too long! Please shorter than 128.");
		if (this.createEmptyKey)
		{
			IdAllocatableDelegate delegate = getOrCreateDelegate(arg.getRegisteredName());
			if (delegate.value != null)
				throw new IllegalStateException("The " + arg.getTargetClass() + " with name '" + arg.getRegisteredName() + "' already exist!");
			delegate.changeRef(arg);
			this.values.add(arg);
			return delegate;
		}
		else
		{
			IdAllocatableDelegate delegate;
			if (this.delegates.containsKey(arg.getRegisteredName()))
				throw new IllegalStateException("The " + arg.getTargetClass() + " with name '" + arg.getRegisteredName() + "' already exist!");
			this.delegates.put(arg.getRegisteredName(), delegate = new IdAllocatableDelegate(arg));
			return delegate;
		}
	}
	
	@Override
	public int register(String name, T arg)
	{
		register(checkArgName(name, arg));
		return 0;
	}
	
	@Override
	public void register(int id, String name, T arg)
	{
		checkArgName(name, arg);
		if (this.fixedIdMap.containsKey(id))
			throw new IllegalArgumentException("The id " + id + " already exist!");
		this.fixedIdMap.put(id, arg);
		IdAllocatableDelegate delegate = this.delegates.get(name);
		if (delegate != null)
		{
			if (delegate.value != null)
				throw new IllegalStateException("The registry name '" + name + "' already exist!");
			delegate.changeRef(arg);
			delegate.changeID(id);
			delegate.fixedID = true;
		}
		else
		{
			this.delegates.put(name, new IdAllocatableDelegate(arg, id));
		}
		this.values.add(arg);
	}
	
	@Override
	public int id(T arg)
	{
		return Objects.firstNonNull(this.idMap.inverse().get(arg), -1);
	}
	
	@Override
	public int id(String name)
	{
		return getOrCreateDelegate(name).id;
	}
	
	@Override
	public String name(int id)
	{
		T value = this.idMap.get(id);
		return value != null ? value.getRegisteredName() : null;
	}
	
	@Override
	public final String name(T arg)
	{
		return arg.getRegisteredName();
	}
	
	public IRegisterDelegate<T> getDelegate(String name)
	{
		return getOrCreateDelegate(name);
	}
	
	@Override
	public T get(String name)
	{
		return getOrCreateDelegate(name).value;
	}
	
	public IRegisterDelegate<T> getDelegete(int id)
	{
		T value = this.idMap.get(id);
		return value != null ? this.delegates.get(value.getRegisteredName()) : new FromIdDelegate(id);
	}
	
	@Override
	public T get(int id)
	{
		return this.idMap.get(id);
	}
	
	@Override
	public boolean contain(String name)
	{
		return this.delegates.containsKey(name);
	}
	
	@Override
	public boolean contain(int id)
	{
		return this.idMap.containsKey(id);
	}
	
	@Override
	public boolean contain(T arg)
	{
		return this.values.contains(arg);
	}
	
	@Override
	public Set<T> targets()
	{
		return this.values;
	}
	
	@Override
	public Set<String> names()
	{
		return this.delegates.keySet();
	}
	
	@Override
	public T remove(String name)
	{
		IdAllocatableDelegate delegate = this.delegates.remove(name);
		if (delegate != null)
		{
			T value = delegate.value;
			delegate.changeRef(null);
			this.values.remove(value);
			this.idMap.inverse().remove(value);
			return value;
		}
		return null;
	}
	
	@Override
	public String remove(T arg)
	{
		remove(arg.getRegisteredName());
		return arg.getRegisteredName();
	}
	
	@Override
	public int size()
	{
		return this.values.size();
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return this.values.iterator();
	}
	
	@Override
	public Stream<T> stream()
	{
		return this.values.stream();
	}
	
	@Override
	public Stream<Entry<String, T>> entryStream()
	{
		return (Stream) this.delegates.values().stream();
	}
	
	public void allocIdAuto()
	{
		this.idMap.clear();
		this.idMap.putAll(this.fixedIdMap);
		int id = -1;
		for (IdAllocatableDelegate delegate : this.delegates.values())
		{
			if (!delegate.fixedID)
			{
				if (delegate.value != null)
				{
					while (this.idMap.containsKey(++id));
					delegate.changeID(id);
					this.idMap.put(id, delegate.value);
				}
				else
				{
					delegate.id = -1;
				}
			}
		}
	}
	
	public void clearIdAlloc()
	{
		this.idMap.clear();
	}
	
	public void serialize(PacketBuffer buf)
	{
		buf.writeInt(this.delegates.size());
		List<String> list = new ArrayList<>(this.delegates.keySet());
		list.sort(Comparator.naturalOrder());
		for (String key : list)
		{
			IdAllocatableDelegate delegate = this.delegates.get(key);
			if (!delegate.fixedID && delegate.id != -1)
			{
				buf.writeBoolean(true);
				buf.writeString(delegate.name);
				buf.writeVarInt(delegate.id);
			}
			else buf.writeBoolean(false);
		}
	}
	
	public Map<String, IdAllocatableDelegate> deserialize(PacketBuffer buf,
			@Nullable Consumer<String> missingIDCallback) throws IOException
	{
		this.idMap.clear();
		this.idMap.putAll(this.fixedIdMap);
		Set<Integer> usedSlots = new HashSet<>();
		Map<String, IdAllocatableDelegate> values = new HashMap<>(this.delegates);
		final int length = buf.readInt();
		for (int i = 0; i < length; ++i)
		{
			if (buf.readBoolean())
			{
				String key = buf.readString(128);
				int id = buf.readVarInt();
				IdAllocatableDelegate delegate = values.remove(key);
				if (delegate != null && delegate.value != null)
				{
					if (delegate.fixedID)
						throw new IOException("The delegate " + delegate.name + " has fixed id.");
					delegate.changeID(id);
					this.idMap.put(id, delegate.value);
					usedSlots.add(id);
				}
				else
				{
					if (missingIDCallback != null)
					{
						missingIDCallback.accept(key);
					}
					if (this.createEmptyKey)
					{
						(delegate != null ? delegate : getOrCreateDelegate(key)).changeID(id);
					}
					usedSlots.add(id);
				}
			}
		}
		if (!values.isEmpty())
		{
			int freeID = -1;
			for (IdAllocatableDelegate delegate : values.values())
			{
				while (usedSlots.contains(++freeID));
				delegate.changeID(freeID);
				usedSlots.add(freeID);
			}
			return values;
		}
		else return null;
	}
}