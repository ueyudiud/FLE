package farcore.lib.block.state;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;

public class ImmutableState implements IFarBlockState
{
	private Block block;
	private IBlockState state;
	private Map<IProperty<?>, Comparable<?>> extraValues;
	
	public ImmutableState(Block block, IBlockState state, Map<IProperty<?>, Comparable<?>> map)
	{
		this.block = block;
		this.state = state;
		extraValues = map;
	}
	
	@Override
	public Collection<IProperty<?>> getPropertyNames()
	{
		ImmutableList.Builder<IProperty<?>> builder = ImmutableList.builder();
		builder.addAll(state.getPropertyNames());
		builder.addAll(extraValues.keySet());
		return builder.build();
	}
	
	@Override
	public <T extends Comparable<T>> T getValue(IProperty<T> property)
	{
		T value = (T) extraValues.get(property);
		return value == null ? state.getValue(property) : value;
	}
	
	@Override
	public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value)
	{
		if(extraValues.containsKey(property))
			throw new UnsupportedOperationException();
		return new ImmutableState(block, state.withProperty(property, value), extraValues);
	}
	
	@Override
	public <T extends Comparable<T>> IBlockState cycleProperty(IProperty<T> property)
	{
		if(extraValues.containsKey(property))
			throw new UnsupportedOperationException();
		return new ImmutableState(block, state.cycleProperty(property), extraValues);
	}
	
	@Override
	public ImmutableMap<IProperty<?>, Comparable<?>> getProperties()
	{
		ImmutableMap.Builder<IProperty<?>, Comparable<?>> builder = ImmutableMap.builder();
		builder.putAll(extraValues);
		builder.putAll(state.getProperties());
		return builder.build();
	}
	
	@Override
	public Block getBlock()
	{
		return block;
	}
	
	@Override
	public IBlockState withMirror(Mirror mirrorIn)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IBlockState withRotation(Rotation rot)
	{
		throw new UnsupportedOperationException();
	}
}