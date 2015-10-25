package fle.core.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class Attribute<T>
{	
	public static final Attribute<Block> block_material = new Attribute(Block.class, "BLOCK_MATERIAL", Blocks.stone);
	public static final Attribute<Item> item_material = new Attribute(Item.class, "ITEM_MATERIAL", Items.stick);
	public static final Attribute<Integer> metadata = new Attribute(Integer.class, "METADATA", 0);
	public static final Attribute<Integer> tank_capcity = new Attribute(Integer.class, "TANK_CAPCITY", 1000);
	public static final Attribute<Integer> max_temp = new Attribute(Integer.class, "MAX_TEMPRETURE", 500);
	
	private Class<? extends T> clazz;
	private String name;
	private T defaultValue;
	
	private Attribute(Class<? extends T> clazz, String aName, T aValue)
	{
		this.clazz = clazz;
		name = aName;
		defaultValue = aValue;
	}
	
	public T getDefaultValue()
	{
		return defaultValue;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof Attribute ? ((Attribute) obj).name == name : false;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("Attribute tag can't clone!");
	}
}