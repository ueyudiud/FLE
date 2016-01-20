package flapi.util;

import java.text.DecimalFormat;

import net.minecraft.util.EnumChatFormatting;

public class FleDecimalFormat extends DecimalFormat
{
	/**
	 * The fle decimal format UUID.
	 */
	private static final long serialVersionUID = 4952146830229549349L;
	private EnumChatFormatting e;
	
	public FleDecimalFormat(String str)
	{
		this(null, str);
	}
	public FleDecimalFormat(EnumChatFormatting aE, String str)
	{
		super(str);
		e = aE;
	}
	
	public String formatColor(long value)
	{
		if(e == null)
		{
			return super.format(value);
		}
		else
		{
			return e.toString() + super.format(value);
		}
	}
	
	public String formatColor(double value)
	{
		if(e == null)
		{
			return super.format(value);
		}
		else
		{
			return e.toString() + super.format(value);
		}
	}
}