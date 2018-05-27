/*
 * copyright 2016-2018 ueyudiud
 */
package fle.api.util;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nebula.base.A;
import nebula.client.util.UnlocalizedList;
import nebula.common.LanguageManager;
import nebula.common.util.ItemStacks;
import nebula.common.util.NBTs;
import nebula.common.util.Strings;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class ToolPropertiesModificater
{
	public static enum Property
	{
		ATTACK_SPEED(2), ATTACK_DAMAGE(1), HARVEST_LEVEL(1), MINING_SPEED(2), DURABILITY(0), THROUGHNESS(1), BRITTLENESS(1), ENCHANTABILITY(0), SPECIAL(2), UNKNOWN(0);
		
		int digit;
		
		Property(int digit)
		{
			this.digit = digit;
		}
		
		public String getTranslateName()
		{
			return "info.tool.property." + name().toLowerCase() + ".name";
		}
	}
	
	public static enum ModificationType
	{
		BASE_ADD, BASE_MUL, MULTIPLIER, SPECIAL;
		
		public static final int length = ModificationType.values().length;
	}
	
	public static class ToolPropertyTag
	{
		String				name;
		Property			property;
		ModificationType	type;
		float				amount;
		
		public ToolPropertyTag(String name, Property property, ModificationType type, float amount)
		{
			this.name = name;
			this.property = property;
			this.type = type;
			this.amount = amount;
		}
		
		public String getTranslateName()
		{
			return "info.tool.prop.tag." + this.name + ".name";
		}
	}
	
	private Map<String, ToolPropertyTag>				tags		= new HashMap<>();
	private EnumMap<Property, List<ToolPropertyTag>[]>	properties	= new EnumMap<>(Property.class);
	
	public ToolPropertiesModificater()
	{
		
	}
	
	public ToolPropertiesModificater(NBTTagCompound compound)
	{
		readFromNBT(compound);
	}
	
	public ToolPropertiesModificater(ItemStack stack)
	{
		readFromNBT(ItemStacks.getSubOrSetupNBT(stack, "toolprop", false));
	}
	
	public ToolPropertyTag getTag(String name)
	{
		return this.tags.get(name);
	}
	
	public ToolPropertiesModificater addProperties(ToolPropertyTag...tags)
	{
		for (ToolPropertyTag tag : tags)
			addProperty(tag);
		return this;
	}
	
	public ToolPropertyTag addProperty(ToolPropertyTag tag)
	{
		ToolPropertyTag old = this.tags.put(tag.name, tag);
		List<ToolPropertyTag>[] lists = this.properties.computeIfAbsent(tag.property, p -> A.createArray(ModificationType.length, new ArrayList<>()));
		assert (old == null || old.property == tag.property);
		if (old != null)
		{
			lists[old.type.ordinal()].remove(old);
		}
		lists[tag.type.ordinal()].add(tag);
		return tag;
	}
	
	public boolean removeProperty(String name)
	{
		ToolPropertyTag tag = this.tags.remove(name);
		if (tag != null)
		{
			this.properties.get(tag.property)[tag.type.ordinal()].remove(tag);
			return true;
		}
		return false;
	}
	
	public void cleanProperty()
	{
		this.tags.clear();
		this.properties.clear();
	}
	
	@SideOnly(Side.CLIENT)
	public void getDisplayInformation(UnlocalizedList list)
	{
		for (ToolPropertyTag tag : this.tags.values())
		{
			String formats = LanguageManager.translateLocal(tag.property.getTranslateName());
			switch (tag.type)
			{
			case BASE_ADD:
			case SPECIAL:
			default:
				formats += " +" + Strings.getDecimalNumber(tag.amount, tag.property.digit);
				break;
			case BASE_MUL:
			case MULTIPLIER:
				formats += " x" + Strings.progress((1.0F + tag.amount) * 100);
				break;
			}
			list.add(tag.getTranslateName(), formats);
		}
	}
	
	public float applyModification(float value, Property property)
	{
		assert (property != Property.SPECIAL);// Special property can not use
		// this to get modification.
		List<ToolPropertyTag>[] tags = this.properties.get(property);
		if (tags == null) return value;
		
		float value1;
		value1 = 0;
		for (ToolPropertyTag tag : tags[0])
			value1 += tag.amount;
		value += value1;
		value1 = 1.0F;
		for (ToolPropertyTag tag : tags[1])
			value1 += tag.amount;
		value *= value1;
		value1 = 1.0F;
		for (ToolPropertyTag tag : tags[2])
			value1 += tag.amount;
		value *= value1;
		value1 = 0;
		for (ToolPropertyTag tag : tags[3])
			value1 += tag.amount;
		value += value1;
		
		return value;
	}
	
	public void writeToStack(ItemStack stack)
	{
		NBTTagCompound nbt = ItemStacks.getOrSetupNBT(stack, true);
		NBTTagCompound compound = new NBTTagCompound();
		nbt.setTag("toolprop", compound);
		writeToNBT(compound);
	}
	
	public void writeToNBT(NBTTagCompound tag)
	{
		for (ToolPropertyTag propertyTag : this.tags.values())
		{
			NBTTagCompound compound = new NBTTagCompound();
			if (propertyTag.property.digit == 0)
				compound.setInteger("value", (int) propertyTag.amount);
			else
				compound.setFloat("value", propertyTag.amount);
			NBTs.setEnum(compound, "prop", propertyTag.property);
			NBTs.setEnum(compound, "type", propertyTag.type);
			tag.setTag(propertyTag.name, compound);
		}
	}
	
	protected void readFromNBT(NBTTagCompound tag)
	{
		for (String key : tag.getKeySet())
		{
			NBTTagCompound compound = tag.getCompoundTag(key);
			ToolPropertyTag propertyTag = new ToolPropertyTag(key, NBTs.getEnumOrDefault(compound, "prop", Property.UNKNOWN), NBTs.getEnumOrDefault(compound, "type", ModificationType.BASE_ADD), compound.getFloat("value"));
			if (propertyTag.property == Property.UNKNOWN) continue;
			addProperty(propertyTag);
		}
	}
}
