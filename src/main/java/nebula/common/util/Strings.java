/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.text.DecimalFormat;

import javax.annotation.Nullable;

import nebula.Nebula;

/**
 * @author ueyudiud
 */
public class Strings
{
	static final DecimalFormat FORMAT1;
	static final long[] OFFSET;
	
	static
	{
		FORMAT1 = new DecimalFormat("##0.0%");
		OFFSET = new long[16];
		long v = 1;
		for (int i = 0; i < OFFSET.length; ++i)
		{
			OFFSET[i] = v;
			v *= 10;
		}
	}
	
	public static String locale()
	{
		return Nebula.proxy.getLocale();
	}
	
	public static String translateByI18n(String unlocal, Object...parameters)
	{
		return Nebula.proxy.translateToLocalByI18n(unlocal, parameters);
	}
	
	public static String validate(@Nullable String string)
	{
		if(string == null) return "";
		return string.trim();
	}
	
	public static String validateProperty(@Nullable String string)
	{
		if(string == null) return "";
		String newString = "";
		for(char chr : string.toCharArray())
		{
			if(chr == '-' || chr == '\\' || chr == '/' || chr == '.' || chr == ' ')
			{
				newString += '_';
			}
			else
			{
				newString += chr;
			}
		}
		return newString.trim();
	}
	
	public static String upcaseFirst(@Nullable String name)
	{
		String s = validate(name);
		if(s.length() == 0) return "";
		char[] array = s.toCharArray();
		array[0] = Character.toUpperCase(array[0]);
		return new String(array);
	}
	
	/**
	 * Format a name use '_', '@', ' ', etc to split word to use Upper case character
	 * to split word.<p>
	 * The name will be trim before format.<p>
	 * Return "" if input name is null.
	 * @param upperFirst Should first character be upper case.
	 * @param name The formated name.
	 * @return The validate name.
	 */
	public static String validateOre(boolean upperFirst, @Nullable String name)
	{
		String string = validate(name);
		if (string.length() == 0) return string;
		boolean shouldUpperCase = upperFirst;
		char[] array = string.toCharArray();
		int j = -1;
		for (int i = 0; i < array.length; ++i)
		{
			char place = ' ';
			switch (array[i])
			{
			case '_' :
			case ' ' :
			case '-' :
			case '$' :
			case '@' :
				if (array.length == i + 1) break;
				place = ' ';
				break;
			default:
				place = array[i];
				++j;
				break;
			}
			if (place == ' ')
			{
				shouldUpperCase = true;
				continue;
			}
			else
			{
				if (shouldUpperCase)
				{
					place = Character.toUpperCase(place);
					shouldUpperCase = false;
				}
				array[j] = place;
			}
		}
		return new String(array, 0, j + 1);
	}
	
	/**
	 * For split string may throw an exception if split key is not exist in split string,
	 * return the full string if it is no words exist.
	 * @param str The split string.
	 * @param split The split character.
	 * @return
	 */
	public static String[] split(@Nullable String str, char split)
	{
		if(str == null) return new String[0];
		else if(str.indexOf(split) != -1)
			return str.split(Character.toString(split));
		else
			return new String[]{str};
	}
	
	/**
	 * Format a double value as a progress.
	 * @param value
	 * @return
	 */
	public static String progress(double value)
	{
		return FORMAT1.format(value);
	}
	
	/**
	 * Format a value to ordinal number.
	 * @param value
	 * @return
	 */
	public static String toOrdinalNumber(int value)
	{
		if(value < 0)
			return Integer.toString(value);
		int i1 = Maths.mod(value, 100);
		if(i1 <= 20 && i1 > 3)
			return value + "th";
		int i2 = Maths.mod(i1, 10);
		switch(i2)
		{
		case 1 : return value + "st";
		case 2 : return value + "nd";
		case 3 : return value + "rd";
		default: return value + "th";
		}
	}
	
	public static String toOrdinalNumber(long value)
	{
		if(value < 0)
			return Long.toString(value);
		int i1 = (int) Maths.mod(value, 100L);
		if(i1 <= 20 && i1 > 3)
			return value + "th";
		int i2 = Maths.mod(i1, 10);
		switch(i2)
		{
		case 1 : return value + "st";
		case 2 : return value + "nd";
		case 3 : return value + "rd";
		default: return value + "th";
		}
	}
	
	public static String getScaledNumber(long value)
	{
		if (value >= 1000000000000000L)
			return value / 1000000000000000L + "." + value % 1000000000000000L / 10000000000000L + "P";
		if (value >= 1000000000000L)
			return value / 1000000000000L + "." + value % 1000000000000L / 10000000000L + "T";
		if (value >= 1000000000L)
			return value / 1000000000L + "." + value % 1000000000L / 10000000L + "G";
		if (value >= 1000000L)
			return value / 1000000L + "." + value % 1000000L / 10000L + "M";
		if (value >= 1000L)
			return value / 1000L + "." + value % 1000L / 10L + "k";
		return String.valueOf(value);
	}
	
	public static String getDecimalNumber(double value, int digit)
	{
		return Double.toString((double) ((long) (value * OFFSET[digit])) / (double) OFFSET[digit]);
	}
}