package farcore.block;

import net.minecraft.nbt.NBTTagCompound;

public class LargeRecipeProgress
{
	final String progress;
	public long value;
	public double lastValue;
	public long maxValue = Integer.MAX_VALUE;
	private double part;

	public LargeRecipeProgress(String p)
	{
		this(p, 1024D);
	}
	public LargeRecipeProgress(String p, double part)
	{
		this.progress = p;
		this.part = part;
	}
	
	public final String getProgress()
	{
		return progress;
	}
	
	public double add(double a)
	{
		long i = (long) Math.floor(a / part);
		double b = a % part;
		if(value + i > maxValue || (value + i == maxValue && b + value > part))
		{
			long lValue1 = value;
			double lValue2 = lastValue;
			value = maxValue;
			lastValue = part;
			return (maxValue - lValue1) * part + (part - lValue2);
		}
		value += i;
		b += b;
		while(lastValue > part)
		{
			lastValue -= part;
			++value;
		}
		return a;
	}
	
	public double minus(double a)
	{
		long i = (long) Math.floor(a / part);
		double b = a % part;
		if(value < i || (value == i && b > value))
		{
			value = 0;
			lastValue = 0;
			return value * part + lastValue;
		}
		value -= i;
		lastValue -= b;
		while(lastValue < 0)
		{
			lastValue += part;
			--value;
		}
		return a;
	}
	
	public void set(long first, double a)
	{
		value = first;
		lastValue = a;
	}
	
	public void set(String value)
	{
		String[] s = value.split(",");
		this.value = Long.valueOf(s[0]);
		lastValue = Double.valueOf(s[1]);
	}
	
	public String get()
	{
		return value + "," + lastValue;
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("Name", progress);
		nbt.setLong("FirstValue", value);
		nbt.setDouble("LastValue", lastValue);
		nbt.setLong("MaxValue", maxValue);
		nbt.setDouble("Part", part);
		return nbt;
	}
	
	public void setMaxValue(long maxValue)
	{
		this.maxValue = maxValue;
	}
	
	public void resetValue(long maxValue)
	{
		lastValue = 0;
		value = 0;
		setMaxValue(maxValue);
	}
	
	@Override
	public String toString()
	{
		return progress + ":" + value + "x" + part + "+" + lastValue;
	}
}