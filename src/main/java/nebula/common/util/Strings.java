/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nebula.Nebula;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author ueyudiud
 */
public final class Strings
{
	static final DecimalFormat	FORMAT1;
	static final long[]			OFFSET;
	
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
	
	private Strings()
	{
	}
	
	/**
	 * Get locale string.
	 * 
	 * @return the locale.
	 * @see net.minecraftforge.fml.common.FMLCommonHandler#getCurrentLanguage()
	 */
	public static String locale()
	{
		return FMLCommonHandler.instance().getCurrentLanguage();
	}
	
	public static String translateByI18n(String unlocal, Object...parameters)
	{
		return Nebula.proxy.translateToLocalByI18n(unlocal, parameters);
	}
	
	/**
	 * Return a string <tt>NONNULL</tt>. If argument is <code>null</code>,
	 * <code>""</code> will be return.
	 * 
	 * @param string the validated string.
	 * @return the non-null string.
	 */
	@Nonnull public static String validate(@Nullable String string)
	{
		return string != null ? string.trim() : "";
	}
	
	/**
	 * Replace a specific character (only the first one) to insert string.
	 * <p>
	 * If <tt>replacement</tt> is not exist, will return <tt>source</tt>
	 * directly.
	 * <p>
	 * Example: <code>replace("foo $", '$', "bar")</code> will return
	 * <code>"foo bar"</code>.
	 * 
	 * @param source the source string.
	 * @param replacement the character to mark for replace.
	 * @param insert the insert string.
	 * @return the replaced string, if <tt>replacement</tt> not exist, return
	 *         <tt>source</tt> directly.
	 */
	public static String replace(@Nonnull String source, char replacement, @Nonnull String insert)
	{
		int i = source.indexOf(replacement);
		if (i == -1) return source;
		return new StringBuilder(source.length() + insert.length() - 1).append(source, 0, i).append(insert).append(source, i + 1, source.length()).toString();
	}
	
	public static String validateProperty(@Nullable String string)
	{
		if (string == null) return "";
		char[] builder = new char[string.length()];
		int idx = 0;
		int i = 0;
		while (string.charAt(i) == ' ')
			i++;
		int j;
		for (j = 0; i < string.length(); ++i)
		{
			char chr = string.charAt(i);
			switch (chr)
			{
			case '-' :
			case '\\':
			case '/' :
			case '.' :
				builder[idx ++] = '_';
				j = idx;
				continue;
			default  :
				builder[idx ++ ] = chr;
				continue;
			case ' ' :
				builder[idx ++] = '_';
				continue;
			}
		}
		return idx == j ? "" : new String(builder, 0, j);
	}
	
	/**
	 * Upper case first character. If argument is <code>null</code>,
	 * <code>""</code> will be return.
	 * 
	 * @param name
	 * @return Upper cased string.
	 */
	public static @Nonnull String upcaseFirst(@Nullable String name)
	{
		String s = validate(name);
		return s.length() == 0 ? "" :
			new StringBuilder(s.length()).append(Character.toUpperCase(name.charAt(0))).append(s, 1, s.length()).toString();
	}
	
	/**
	 * Format a name use '_', '@', ' ', etc to split word to use Upper case
	 * character to split word.
	 * <p>
	 * The name will be trim before format.
	 * <p>
	 * Return "" if input name is null.
	 * 
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
			case '_':
			case ' ':
			case '-':
			case '$':
			case '@':
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
	 * For split string may throw an exception if split key is not exist in
	 * split string, return the full string if it is no words exist.
	 * 
	 * @param str the split String.
	 * @param split the split character.
	 * @return the split String array.
	 */
	public static String[] split(@Nullable String str, char split)
	{
		if (str == null)
			return new String[0];
		else if (str.indexOf(split) != -1)
		{
			List<String> list = new ArrayList<>(4);
			int start = 0, idx = 0, length = str.length();
			for (; idx < length; ++idx)
			{
				if (str.charAt(idx) == split)
				{
					list.add(str.substring(start, idx));
					start = idx + 1;
				}
			}
			list.add(str.substring(start));
			return list.toArray(new String[list.size()]);
		}
		else
			return new String[] { str };
	}
	
	/**
	 * Split first character.
	 * 
	 * @param str the split string.
	 * @param split the split character.
	 * @return
	 */
	public static String[] splitFirst(@Nullable String str, char split)
	{
		int idx;
		if (str == null)
			return new String[0];
		else if ((idx = str.indexOf(split)) != -1)
			return new String[] { str.substring(0, idx), str.substring(idx + 1) };
		else
			return new String[] { str };
	}
	
	/**
	 * Format a double value as a progress.
	 * 
	 * @param value
	 * @return
	 */
	public static String progress(double value)
	{
		return FORMAT1.format(value);
	}
	
	/**
	 * Format a value to ordinal number.
	 * 
	 * @param value
	 * @return
	 */
	public static String toOrdinalNumber(int value)
	{
		if (value <= 0) return toOrdinalNumber((long) value & 0xFFFFFFFF);
		int i1 = Maths.mod(value, 100);
		if (i1 <= 20 && i1 > 3) return value + "th";
		int i2 = i1 % 10;
		switch (i2)
		{
		case 1:
			return value + "st";
		case 2:
			return value + "nd";
		case 3:
			return value + "rd";
		default:
			return value + "th";
		}
	}
	
	/**
	 * Format a value to ordinal number.
	 * 
	 * @param value
	 * @return
	 */
	public static String toOrdinalNumber(long value)
	{
		if (value < 0) throw new IllegalArgumentException("Negative ordinal number: " + value);
		int i1 = (int) Maths.mod(value, 100L);
		if (i1 <= 20 && i1 > 3) return value + "th";
		int i2 = i1 % 10;
		switch (i2)
		{
		case 1:
			return value + "st";
		case 2:
			return value + "nd";
		case 3:
			return value + "rd";
		default:
			return value + "th";
		}
	}
	
	public static String getScaledNumber(long value)
	{
		if (value >= 1000000000000000L) return value / 1000000000000000L + "." + value % 1000000000000000L / 10000000000000L + "P";
		if (value >= 1000000000000L) return value / 1000000000000L + "." + value % 1000000000000L / 10000000000L + "T";
		if (value >= 1000000000L) return value / 1000000000L + "." + value % 1000000000L / 10000000L + "G";
		if (value >= 1000000L) return value / 1000000L + "." + value % 1000000L / 10000L + "M";
		if (value >= 1000L) return value / 1000L + "." + value % 1000L / 10L + "k";
		return String.valueOf(value);
	}
	
	public static String getDecimalNumber(double value, int digit)
	{
		long v1 = (long) (value * OFFSET[digit]);
		return (v1 / OFFSET[digit]) + "." + (v1 % OFFSET[digit]);
	}
}
