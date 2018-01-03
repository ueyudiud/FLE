/*
 * copyright© 2016-2018 ueyudiud
 */
package fra;

import java.util.List;
import java.util.Map;

import nebula.common.util.A;

/**
 * @author ueyudiud
 */
public class FSObjects
{
	public static FSBool valueOf(boolean value)
	{
		return value ? FSBool.TRUE : FSBool.FALSE;
	}
	
	public static FSChar valueOf(char value)
	{
		return new FSChar(value);
	}
	
	public static FSInt valueOf(long value)
	{
		return new FSInt(value);
	}
	
	public static FSFloat valueOf(double value)
	{
		return new FSFloat(value);
	}
	
	public static IFSObject valueOfCompare(boolean value, IFSObject rigit)
	{
		return value ? new FSBoolLazyCompute(rigit) : FSFailedCompute.INSTANCE;
	}
	
	public static IFSObject valueOf(String str)
	{
		return new FSString(str);
	}
	
	public static IFSObject valueOfBuf(String str)
	{
		return new FSStringBuf(str);
	}
	
	public static IFSObject pack(Object object)
	{
		if (object == null)
		{
			return FSNull.NULL;
		}
		else if (object instanceof IFSObject)
		{
			return (IFSObject) object;
		}
		else if (object instanceof Boolean)
		{
			return valueOf(((Boolean) object).booleanValue());
		}
		else if (object instanceof Byte || object instanceof Short ||
				object instanceof Integer || object instanceof Long)
		{
			return valueOf(((Number) object).longValue());
		}
		else if (object instanceof Float || object instanceof Double)
		{
			return valueOf(((Number) object).doubleValue());
		}
		else if (object instanceof Character)
		{
			return valueOf(((Character) object).charValue());
		}
		else if (object instanceof List<?>)
		{
			return new FSList((List<?>) object);
		}
		else if (object instanceof Map<?, ?>)
		{
			return new FSMap((Map<?, ?>) object);
		}
		else
		{
			return new FSObject<>(object);
		}
	}
	
	public static Object unpack(IFSObject object)
	{
		if (object instanceof FSBool)
		{
			return ((FSBool) object).value ? Boolean.TRUE : Boolean.FALSE;
		}
		else if (object instanceof FSChar)
		{
			return (char) ((FSChar) object).value;
		}
		else if (object instanceof FSInt)
		{
			return ((FSInt) object).value;
		}
		else if (object instanceof FSFloat)
		{
			return ((FSFloat) object).value;
		}
		else if (object == FSNull.NULL)
		{
			return null;
		}
		else if (object instanceof FSString || object instanceof FSStringBuf)
		{
			return object.asString();
		}
		else if (object instanceof FSTuple)
		{
			return A.transform(((FSTuple) object).elements, FSObjects::unpack);
		}
		else if (object instanceof FSObject<?>)
		{
			return ((FSObject) object).value;
		}
		else
		{
			return object;
		}
	}
}
