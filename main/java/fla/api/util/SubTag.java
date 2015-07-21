package fla.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubTag<T>
{
	public static final SubTag<Integer> MAX_STACK_SIZE = new SubTag<Integer>("MaxStackSize", 64);

	public static final SubTag<Boolean> BLOCK_FLE_RENDER = new SubTag<Boolean>("Render", true);
	public static final SubTag<Boolean> BLOCK_SUB = new SubTag<Boolean>("SubBlock", true);
	public static final SubTag<Boolean> BLOCK_TICK_RANDOMLY = new SubTag<Boolean>("TickRandomly", true);
	public static final SubTag<Boolean> BLOCK_CHECK_STAY = new SubTag<Boolean>("CheckCanStay", true);
	public static final SubTag<Boolean> BLOCK_TILE = new SubTag<Boolean>("ContainTileEntity", true);
	public static final SubTag<Float> BLOCK_HARDNESS = new SubTag<Float>("Hardness", 1.0F);
	public static final SubTag<Float> BLOCK_RESISTANCE = new SubTag<Float>("Resistance", 3.0F);
	public static final SubTag<Float> BLOCK_LIGHT_LEVEL = new SubTag<Float>("LightLevel", 0.0F);
	public static final SubTag<Byte> BLOCK_STAY = new SubTag<Byte>("BlockStayType", new Byte((byte) 0));
	public static final SubTag<Integer> BLOCK_NOT_SOILD = new SubTag<Integer>("SolidSide", 0);
	
	
	private final String tagName;
	private T obj;
	
	public SubTag(String name)
	{
		this(name, null);
	}
	public SubTag(String name, T obj)
	{
		this.tagName = name;
		this.obj = obj;
	}
	
	public final String getTagName()
	{
		return tagName;
	}
	
	public boolean isTagEqual(SubTag tag) 
	{
		return tag.tagName == this.tagName && this.obj.equals(tag.obj);
	}
	
	public boolean isTagNameEqual(SubTag tag)
	{
		return this.tagName.equals(tag.tagName);
	}
	
	public T getTagValue()
	{
		return obj;
	}

	public SubTag<T> copy(T value) 
	{
		return new SubTag(tagName, value);
	}
	
	public SubTag<T> copy() 
	{
		return new SubTag(tagName, obj);
	}
	
	public static class TagsInfomation
	{
		Map<String, SubTag> map = new HashMap();
		List<SubTag> list = new ArrayList();
		
		public TagsInfomation() 
		{
			
		}

		public void add(SubTag...target)
		{
			for(int i = 0; i < target.length; ++i)
			{
				list.add(target[i]);
				map.put(target[i].getTagName(), target[i]);
			}
		}
		
		public boolean contain(String tagName)
		{
			return map.containsKey(tagName);
		}
		
		public boolean contain(SubTag target)
		{
			for(SubTag tag : list)
			{
				if(tag.isTagEqual(target)) return true;
			}
			return false;
		}
		
		public SubTag get(String tagName)
		{
			return map.get(tagName);
		}
		
		public <T> T getValue(String tagName)
		{
			return (T) map.get(tagName).getTagValue();
		}
	}
}