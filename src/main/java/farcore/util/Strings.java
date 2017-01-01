/*
 * copyright© 2016 ueyudiud
 */

package farcore.util;

import static farcore.util.U.handlerGatway;

import java.text.DecimalFormat;

import javax.annotation.Nullable;

/**
 * @author ueyudiud
 */
public class Strings
{
	static final DecimalFormat format1 = new DecimalFormat("##0.0%");
	
	public static String locale()
	{
		return handlerGatway.getLocale();
	}
	
	public static String translateByI18n(String unlocal, Object...parameters)
	{
		return handlerGatway.translateToLocalByI18n(unlocal, parameters);
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
		if(s.length() == 0) return s;
		char chr = name.charAt(0);
		String sub = name.substring(1);
		return Character.toString(Character.toUpperCase(chr)) + sub;
	}
	
	public static String validateOre(boolean upperFirst, String name)
	{
		String string = validate(name);
		String ret = "";
		boolean shouldUpperCase = upperFirst;
		for(char chr : string.toCharArray())
			if(chr == '_' || chr == ' ' ||
			chr == '-' || chr == '$' ||
			chr == '@' || chr == ' ')
			{
				shouldUpperCase = true;
				continue;
			}
			else if(shouldUpperCase)
			{
				ret += Character.toString(Character.toUpperCase(chr));
				shouldUpperCase = false;
			}
			else
			{
				ret += Character.toString(chr);
			}
		return ret;
	}
	
	public static String[] split(@Nullable String str, char split)
	{
		if(str == null) return new String[0];
		else if(str.indexOf(split) != -1)
			return str.split(Character.toString(split));
		else
			return new String[]{str};
	}
	
	public static String progress(double value)
	{
		return format1.format(value);
	}
	
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
}