package farcore.lib.nbt;

import java.util.Arrays;
import java.util.Collection;

import farcore.util.IDataChecker;
import net.minecraft.nbt.NBTTagCompound;
import scala.language;

public abstract class NBTProperty implements IDataChecker<NBTTagCompound>
{
	public static NBTProperty rangeMax(String tag, int max)
	{
		return rangeMax(tag, (long) max);
	}
	public static NBTProperty rangeMax(String tag, long max)
	{
		return range(tag, max, Long.MIN_VALUE);
	}
	public static NBTProperty rangeMax(String tag, double max)
	{
		return range(tag, max, Double.MIN_VALUE);
	}
	public static NBTProperty rangeMin(String tag, int min)
	{
		return rangeMin(tag, (long) min);
	}
	public static NBTProperty rangeMin(String tag, long min)
	{
		return range(tag, Long.MAX_VALUE, min);
	}
	public static NBTProperty rangeMin(String tag, double min)
	{
		return range(tag, Double.MAX_VALUE, min);
	}
	public static NBTProperty range(String tag, int max, int min)
	{
		return range(tag, (long) max, (long) min);
	}
	public static NBTProperty range(String tag, long max, long min)
	{
		return new NBTPropertyRangeLong(tag, max, min);
	}
	public static NBTProperty range(String tag, double max, double min)
	{
		return new NBTPropertyRangeDouble(tag, max, min);
	}
	public static NBTProperty contain(String tag, String...strings)
	{
		return contain(tag, Arrays.asList(strings));
	}
	public static NBTProperty contain(String tag, Collection<String> collection)
	{
		return new NBTPropertyContain(tag, collection);
	}
	
	protected NBTProperty()
	{
		
	}

	@Override
	public abstract boolean isTrue(NBTTagCompound target);
	
	public abstract void addInstanceTag(NBTTagCompound nbt);

	private static class NBTPropertyRangeLong extends NBTProperty
	{
		String tag;
		long min;
		long max;
		
		public NBTPropertyRangeLong(String tag, long m1, long m2)
		{
			this.tag = tag;
			this.max = m1;
			this.min = m2;
		}
		
		@Override
		public boolean isTrue(NBTTagCompound target)
		{
			long value = target.getLong(tag);
			return value >= min && value < max;
		}
		
		@Override
		public void addInstanceTag(NBTTagCompound nbt)
		{
			nbt.setLong(tag, (max + min) / 2);
		}
	}
	private static class NBTPropertyRangeDouble extends NBTProperty
	{
		String tag;
		double min;
		double max;
		
		public NBTPropertyRangeDouble(String tag, double m1, double m2)
		{
			this.tag = tag;
			this.max = m1;
			this.min = m2;
		}
		
		@Override
		public boolean isTrue(NBTTagCompound target)
		{
			double value = target.getDouble(tag);
			return value >= min && value <= max;
		}
		
		@Override
		public void addInstanceTag(NBTTagCompound nbt)
		{
			nbt.setDouble(tag, (max + min) * 0.5);
		}
	}
	
	private static class NBTPropertyContain extends NBTProperty
	{
		String tag;
		Collection<String> collection;
		
		public NBTPropertyContain(String tag, Collection<String> collection)
		{
			this.tag = tag;
			this.collection = collection;
		}
		
		@Override
		public boolean isTrue(NBTTagCompound target)
		{
			return !target.hasKey(tag) ? false :
				collection.contains(target.getString(tag));
		}
		
		@Override
		public void addInstanceTag(NBTTagCompound nbt)
		{
			nbt.setString(tag, collection.iterator().next());
		}
	}
}