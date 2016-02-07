package farcore.nbt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;

import farcore.collection.CollectionUtil;
import farcore.util.FleLog;

/**
 * 
 * @author ueyudiud
 * 		
 */
public class NBT implements NBTSaver, NBTLoader
{
	public static interface NBTControlor
	{
		;
	}
	
	private static Map<Class<?>, NBT> nbtManagers = new HashMap();
	
	public static void register(Class<?> clazz, NBTControlor... controlors)
	{
		if (nbtManagers.containsKey(clazz))
		{
			throw new RuntimeException(
					"Class " + clazz.getName() + " has already registered!");
		}
		nbtManagers.put(clazz, new NBT(clazz, controlors));
	}
	
	public static NBT getNBT(Class clazz)
	{
		return nbtManagers.get(clazz);
	}
	
	public static <T> T readFromNBT(T target, NBTTagCompound nbt)
	{
		if (nbtManagers.containsKey(target.getClass()))
		{
			nbtManagers.get(target.getClass()).loadFromNBT(target, nbt);
		}
		return target;
	}
	
	public static <T> NBTTagCompound writeToNBT(T target, NBTTagCompound nbt)
	{
		if (nbtManagers.containsKey(target.getClass()))
		{
			return nbtManagers.get(target.getClass()).saveToNBT(target, nbt);
		}
		return nbt;
	}
	
	private Map<Field, Entry<NBTSaver, NBTSave>> saveMap = new HashMap();
	private Map<Field, Entry<NBTLoader, NBTLoad>> loadMap = new HashMap();
	
	private List<String> loadedList = new ArrayList<String>();
	// private WeakHashMap<String, Object> weakMap = new WeakHashMap();
	
	public NBT(Class<?> clazz, NBTControlor... controlors)
	{
		List<NBTLoader> loaders = new ArrayList();
		List<NBTSaver> savers = new ArrayList();
		for (NBTControlor controlor : controlors)
		{
			if (controlor == null)
				throw new RuntimeException("Can not input null controlor!");
			if (controlor instanceof NBTLoader)
				loaders.add((NBTLoader) controlor);
			if (controlor instanceof NBTSaver)
				savers.add((NBTSaver) controlor);
		}
		for (Field field : clazz.getFields())
		{
			NBTSave save = field.getAnnotation(NBTSave.class);
			NBTLoad load = field.getAnnotation(NBTLoad.class);
			Class<?> saveType = field.getType();
			if (save != null)
			{
				NBTSaver saver = null;
				for (NBTSaver s : savers)
				{
					if (s.canSave(saveType))
					{
						if (saver == null)
						{
							saver = s;
						}
						else
							throw new IllegalArgumentException(
									"Two saver wants to save a same field with type "
											+ saveType.toString() + "!");
					}
				}
				if (saver == null && canSave(saveType))
					saver = this;
				else if (saver == null)
					throw new IllegalArgumentException(
							"Can not save type of " + saveType.getName());
				saveMap.put(field, CollectionUtil.e(saver, save));
			}
			if (load != null)
			{
				NBTLoader loader = null;
				for (NBTLoader l : loaders)
				{
					if (l.canLoad(saveType))
					{
						if (loader == null)
						{
							loader = l;
						}
						else
							throw new IllegalArgumentException(
									"Two loader wants to load a same field with type "
											+ saveType.toString() + "!");
					}
				}
				if (loader == null && canLoad(saveType))
					loader = this;
				else if (loader == null)
					throw new IllegalArgumentException(
							"Can not save type of " + saveType.getName());
				loadMap.put(field, CollectionUtil.e(loader, load));
			}
		}
	}
	
	public NBTTagCompound saveToNBT(Object saver, NBTTagCompound nbt)
	{
		for (Entry<Field, Entry<NBTSaver, NBTSave>> entry : saveMap.entrySet())
		{
			try
			{
				if (entry.getValue().getKey() == this)
					;
				entry.getValue().getKey().save(
						entry.getValue().getValue().name(),
						entry.getKey().get(saver), nbt);
			}
			catch (Exception exception)
			{
				FleLog.warn("Fail to save "
						+ entry.getKey().getName().toString() + " to nbt.");
			}
		}
		return nbt;
	}
	
	private ThreadLocal<Class> clazzTypeThread = new ThreadLocal();
	
	public void loadFromNBT(Object loader, NBTTagCompound nbt)
	{
		for (Entry<Field, Entry<NBTLoader, NBTLoad>> entry : loadMap.entrySet())
		{
			boolean flag;
			try
			{
				if (entry.getValue().getKey() == this)
					clazzTypeThread.set(entry.getKey().getType());
				entry.getKey().set(loader, entry.getValue().getKey()
						.load(entry.getValue().getValue().name(), nbt));
						
			}
			catch (Exception exception)
			{
				FleLog.warn("Fail to load " + entry.getValue().toString()
						+ " to nbt.");
			}
		}
	}
	
	@Override
	public boolean canLoad(Class clazz)
	{
		return boolean.class.equals(clazz) || byte.class.equals(clazz)
				|| short.class.equals(clazz) || int.class.equals(clazz)
				|| long.class.equals(clazz) || float.class.equals(clazz)
				|| double.class.equals(clazz) || int[].class.equals(clazz)
				|| String.class.equals(clazz) || clazz.isEnum();
	}
	
	@Override
	public Object load(String name, NBTTagCompound tag)
	{
		Class clazz = clazzTypeThread.get();
		if (boolean.class.equals(clazz))
			return tag.getBoolean(name);
		else if (byte.class.equals(clazz))
			return tag.getByte(name);
		else if (short.class.equals(clazz))
			return tag.getShort(name);
		else if (int.class.equals(clazz))
			return tag.getInteger(name);
		else if (long.class.equals(clazz))
			return tag.getLong(name);
		else if (float.class.equals(clazz))
			return tag.getFloat(name);
		else if (double.class.equals(clazz))
			return tag.getDouble(name);
		else if (int[].class.equals(clazz))
			return tag.getIntArray(name);
		else if (String.class.equals(clazz))
			return tag.getString(name);
		else if (clazz.isEnum())
			return Enum.valueOf(clazz, tag.getString(name));
		else
			throw new IllegalArgumentException("Can not load tag named " + name
					+ " with tag " + tag.toString());
	}
	
	@Override
	public boolean canSave(Class clazz)
	{
		return boolean.class.equals(clazz) || byte.class.equals(clazz)
				|| short.class.equals(clazz) || int.class.equals(clazz)
				|| long.class.equals(clazz) || float.class.equals(clazz)
				|| double.class.equals(clazz) || int[].class.equals(clazz)
				|| String.class.equals(clazz) || clazz.isEnum();
	}
	
	@Override
	public void save(String name, Object obj, NBTTagCompound nbt)
	{
		if (obj instanceof Boolean)
			nbt.setBoolean(name, (Boolean) obj);
		else if (obj instanceof Byte)
			nbt.setByte(name, (Byte) obj);
		else if (obj instanceof Short)
			nbt.setShort(name, (Short) obj);
		else if (obj instanceof Integer)
			nbt.setInteger(name, (Integer) obj);
		else if (obj instanceof Long)
			nbt.setLong(name, (Long) obj);
		else if (obj instanceof Float)
			nbt.setFloat(name, (Float) obj);
		else if (obj instanceof Double)
			nbt.setDouble(name, (Double) obj);
		else if (obj instanceof int[])
			nbt.setIntArray(name, (int[]) obj);
		else if (obj instanceof String)
			nbt.setString(name, (String) obj);
		else if (obj instanceof Enum)
			nbt.setString(name, ((Enum) obj).name());
		else
			throw new IllegalArgumentException("Can not save tag named " + name
					+ " with object " + obj.toString());
	}
}