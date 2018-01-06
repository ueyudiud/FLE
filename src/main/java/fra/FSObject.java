/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

import nebula.common.util.A;

/**
 * @author ueyudiud
 */
class FSObject<T> implements IFSObject
{
	T value;
	
	protected FSObject(T value)
	{
		this.value = value;
	}
	
	@Override
	public IFSObject child(String name)
	{
		Field field = null;
		Class<?> class1 = this.value.getClass();
		try
		{
			field = class1.getField(name);
		}
		catch (NoSuchFieldException | SecurityException exception)
		{
			
		}
		boolean method = A.or(class1.getMethods(), m->m.getName().equals(name));
		if (field != null)
		{
			IFSObject result = new FSField(this, field);
			if (method)
			{
				return new WrapperFSObject(result)
				{
					@Override
					public IFSObject apply(IFSObject...objects)
					{
						try
						{
							return new FSMethod(name, FSObject.this.value).apply(objects);
						}
						catch (UnsupportedOperationException exception)
						{
							return result.eval().apply(objects);
						}
					}
				};
			}
			else
			{
				return result;
			}
		}
		else if (method)
		{
			return new FSMethod(name, this.value);
		}
		return IFSObject.super.child(name);
	}
	
	@Override
	public IFSObject operator_objeq(IFSObject object)
	{
		return FSObjects.valueOf(object instanceof FSObject<?> && this.value.equals(((FSObject<?>) object).value));
	}
	
	@Override
	public IFSObject operator_objne(IFSObject object)
	{
		return FSObjects.valueOf(!(object instanceof FSObject<?>) || !this.value.equals(((FSObject<?>) object).value));
	}
	
	@Override
	public IFSObject operator_equal(IFSObject object)
	{
		return FSObjects.valueOfCompare(object instanceof FSObject<?> && this.value == ((FSObject<?>) object).value, object);
	}
	
	@Override
	public IFSObject operator_noneq(IFSObject object)
	{
		return FSObjects.valueOfCompare(!(object instanceof FSObject<?>) || this.value != ((FSObject<?>) object).value, object);
	}
	
	@Override
	public IFSObject operator_shl(IFSObject object)
	{
		return new FSStringBuf(this.value.toString()).operator_shl(object);
	}
	
	@Override
	public IFSObject operator_gt(IFSObject object)
	{
		if (!(this.value instanceof Comparable<?>))
			throw new UnsupportedOperationException(this.value + " can not be compared.");
		if (!(object instanceof FSObject<?>))
			return FSFailedCompute.INSTANCE;
		try
		{
			return FSObjects.valueOfCompare(((Comparable) this.value).compareTo(((FSObject<?>) object).value) > 0, object);
		}
		catch (ClassCastException exception)
		{
			return new FSString(asString()).operator_gt(object);
		}
	}
	
	@Override
	public IFSObject operator_ge(IFSObject object)
	{
		if (!(this.value instanceof Comparable<?>))
			throw new UnsupportedOperationException(this.value + " can not be compared.");
		if (!(object instanceof FSObject<?>))
			return FSFailedCompute.INSTANCE;
		try
		{
			return FSObjects.valueOfCompare(((Comparable) this.value).compareTo(((FSObject<?>) object).value) >= 0, object);
		}
		catch (ClassCastException exception)
		{
			return new FSString(asString()).operator_ge(object);
		}
	}
	
	@Override
	public IFSObject operator_lt(IFSObject object)
	{
		if (!(this.value instanceof Comparable<?>))
			throw new UnsupportedOperationException(this.value + " can not be compared.");
		if (!(object instanceof FSObject<?>))
			return FSFailedCompute.INSTANCE;
		try
		{
			return FSObjects.valueOfCompare(((Comparable) this.value).compareTo(((FSObject<?>) object).value) < 0, object);
		}
		catch (ClassCastException exception)
		{
			return new FSString(asString()).operator_lt(object);
		}
	}
	
	@Override
	public IFSObject operator_le(IFSObject object)
	{
		if (!(this.value instanceof Comparable<?>))
			throw new UnsupportedOperationException(this.value + " can not be compared.");
		if (!(object instanceof FSObject<?>))
			return FSFailedCompute.INSTANCE;
		try
		{
			return FSObjects.valueOfCompare(((Comparable) this.value).compareTo(((FSObject<?>) object).value) <= 0, object);
		}
		catch (ClassCastException exception)
		{
			return new FSString(asString()).operator_le(object);
		}
	}
	
	@Override
	public Iterator<?> asIterator()
	{
		if (this.value instanceof Iterable<?>)
		{
			return ((Iterable) this.value).iterator();
		}
		else if (this.value instanceof Iterator<?>)
		{
			return (Iterator<?>) this.value;
		}
		else
		{
			throw new UnsupportedOperationException();
		}
	}
	
	@Override
	public String asString()
	{
		return this.value.toString();
	}
}

class FSMethod extends FSObject<String>
{
	Object parent;
	
	protected FSMethod(String value, Object parent)
	{
		super(value);
		this.parent = parent;
	}
	
	@Override
	public IFSObject apply(IFSObject...objects)
	{
		Object[] transformed = A.transform(objects, FSObjects::unpack);
		try
		{
			Method method = this.parent.getClass().getMethod(this.value, A.transform(transformed, Class.class, Object::getClass));
			if (Modifier.isStatic(method.getModifiers()))
			{
				return FSObjects.pack(method.invoke(null, transformed));
			}
			else
			{
				return FSObjects.pack(method.invoke(this.parent, transformed));
			}
		}
		catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException exception)
		{
			throw new UnsupportedOperationException(exception);
		}
		catch (InvocationTargetException exception)
		{
			throw new RuntimeException(exception);
		}
	}
}

class FSField extends WrapperFSObject
{
	Field field;
	
	FSField(IFSObject parent, Field field)
	{
		super(parent);
		this.field = field;
	}
	
	@Override
	protected IFSObject object()
	{
		Class<?> class1 = this.field.getType();
		try
		{
			if (class1 == byte.class || class1 == short.class || class1 == int.class || class1 == long.class)
			{
				return new FSInt(this.field.getLong(this.object));
			}
			else if (class1 == boolean.class)
			{
				return FSObjects.valueOf(this.field.getBoolean(this.object));
			}
			else if (class1 == char.class)
			{
				return new FSChar(this.field.getChar(this.object));
			}
			else if (class1.isPrimitive())
			{
				return new FSFloat(this.field.getDouble(this.object));
			}
			else return FSObjects.pack(this.field.get(this.object));
		}
		catch (IllegalArgumentException | IllegalAccessException exception)
		{
			throw new UnsupportedOperationException(exception);
		}
	}
	
	@Override
	public IFSObject eval()
	{
		return object();
	}
	
	@Override
	public IFSObject operator_set(IFSObject object)
	{
		object = object.eval();
		Class<?> class1 = this.field.getType();
		try
		{
			if (class1 == byte.class)
			{
				this.field.set(this.object, (byte) object.asLong());
			}
			else if (class1 == short.class)
			{
				this.field.set(this.object, (short) object.asLong());
			}
			else if (class1 == int.class)
			{
				this.field.set(this.object, (int) object.asLong());
			}
			else if (class1 == long.class)
			{
				this.field.set(this.object, object.asLong());
			}
			else if (class1 == char.class)
			{
				this.field.set(this.object, (char) object.asLong());
			}
			else if (class1 == float.class)
			{
				this.field.set(this.object, (float) object.asDouble());
			}
			else if (class1 == double.class)
			{
				this.field.set(this.object, object.asDouble());
			}
			else if (class1 == boolean.class)
			{
				this.field.set(this.object, object.isTrue());
			}
			else
			{
				this.field.set(this.object, FSObjects.unpack(object));
			}
		}
		catch (IllegalArgumentException | IllegalAccessException exception)
		{
			throw new UnsupportedOperationException(exception);
		}
		return object;
	}
}
