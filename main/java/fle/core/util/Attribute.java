package fle.core.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import fle.api.FleValue;
import fle.api.util.Register;
import fle.core.util.AttributeObjectWithRegister.AttributeBlock;
import fle.core.util.AttributeObjectWithRegister.AttributeItem;

public abstract class Attribute<T>
{	
	final static Register<Attribute> map = new Register<Attribute>();
	public static final Attribute<Block> block_material = new AttributeBlock("BLOCK_MATERIAL", 1);
	public static final Attribute<Item> item_material = new AttributeItem("ITEM_MATERIAL", 2);
	public static final Attribute<Integer> metadata = new AttributeInteger("METADATA", 3);
	public static final Attribute<Integer> tank_capcity = new AttributeInteger("TANK_CAPCITY", 1000, 4);
	public static final Attribute<Integer> max_temp = new AttributeInteger("MAX_TEMPRETURE", FleValue.WATER_FREEZE_POINT + 100, 5);
	public static final Attribute<int[]> access_side = new AttributeIntArray("ACCESS_SIDE", 6);
	public static final Attribute<Float> thermalSpeed = new AttributeFloat("THERMAL_SPEED", 1.0F, 7);
	public static final Attribute<Boolean> flag = new AttributeBoolean("FLAG", 8);
	public static final Attribute<Block> output_block = new AttributeBlock("OUTPUT_BLOCK", 65);
	
	public static Attribute getAttribute(int id)
	{
		return map.get(id);
	}
	public static Attribute getAttribute(String name)
	{
		return map.get(name);
	}
	
	private Class<? extends T> clazz;
	private String name;
	private T defaultValue;
	private int hashCode;
	
	protected Attribute(Class<? extends T> clazz, String aName, T aValue, int hashValue)
	{
		this.clazz = clazz;
		defaultValue = aValue;
		map.register(hashCode = hashValue, this, name = aName);
	}
	
	public final boolean isInstance(Object obj)
	{
		return clazz.isInstance(obj);
	}
	
	public final Class<?> getAccessClass()
	{
		return clazz;
	}
	
	public final T defaultValue()
	{
		return defaultValue;
	}
	
	@Override
	public final int hashCode()
	{
		return hashCode;
	}
	
	@Override
	public final boolean equals(Object obj)
	{
		return obj instanceof Attribute ? ((Attribute) obj).hashCode == hashCode : false;
	}
	
	@Override
	public final String toString()
	{
		return name;
	}
	
	@Override
	protected final Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException("Attribute tag can't clone!");
	}
	
	public abstract void write(DataOutputStream stream, T art) throws IOException;
	
	public abstract T read(DataInputStream stream) throws IOException;
	
}