package fle.api.util;

import java.text.DecimalFormat;

import net.minecraft.util.EnumChatFormatting;

public class FleDecimalFormat extends DecimalFormat
{
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
	
	public String format_c(long value)
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
	
	public String format_c(double value)
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