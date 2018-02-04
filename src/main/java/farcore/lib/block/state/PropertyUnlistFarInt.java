/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.block.state;

import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * @author ueyudiud
 */
public class PropertyUnlistFarInt implements IUnlistedProperty<Integer>
{
	private final String name;
	
	public PropertyUnlistFarInt(String name)
	{
		this.name = name;
	}
	
	@Override
	public String getName()
	{
		return this.name;
	}
	
	@Override
	public boolean isValid(Integer value)
	{
		return true;
	}
	
	@Override
	public Class<Integer> getType()
	{
		return Integer.class;
	}
	
	@Override
	public String valueToString(Integer value)
	{
		return value.toString();
	}
}
