/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.block.property;

import nebula.base.IRegister;
import net.minecraft.block.state.IBlockState;

/**
 * @author ueyudiud
 */
public class PropertyRegister<V> extends PropertyString
{
	public static <V> PropertyRegister<V> create(String name, IRegister<V> register)
	{
		return new PropertyRegister<>(name, register);
	}
	
	final IRegister<V> register;
	
	PropertyRegister(String name, IRegister<V> register)
	{
		super(name, register.names());
		this.register = register;
	}
	
	public V getValue(IBlockState state)
	{
		return this.register.get(state.getValue(this));
	}
	
	public IBlockState withProperty(IBlockState state, V value)
	{
		return state.withProperty(this, this.register.name(value));
	}
	
	public int getIndex(IBlockState state)
	{
		return this.register.id(state.getValue(this));
	}
	
	public IBlockState fromIndex(IBlockState state, int index)
	{
		return state.withProperty(this, this.register.name(index));
	}
}