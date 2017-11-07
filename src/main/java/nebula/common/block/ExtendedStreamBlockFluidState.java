/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import static nebula.common.block.BlockStreamFluid.LEVEL;
import static net.minecraftforge.fluids.BlockFluidBase.FLUID_RENDER_PROPS;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.ObjectArrays;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.BlockFluidBase;

/**
 * @author ueyudiud
 */
public class ExtendedStreamBlockFluidState extends ExtendedBlockState
{
	static final class HidingFluidState extends ExtendedBlockState.ExtendedStateImplementation
	{
		protected HidingFluidState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties, ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> table)
		{
			super(block, properties, unlistedProperties, table);
		}
		
		@Override
		public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value)
		{
			return super.withProperty(checkCast(property), value);
		}
		
		@Override
		public <T extends Comparable<T>> T getValue(IProperty<T> property)
		{
			return super.getValue(checkCast(property));
		}
		
		private <T extends Comparable<T>> IProperty<T> checkCast(IProperty<T> property)
		{
			return property == BlockFluidBase.LEVEL || property == BlockLiquid.LEVEL ? (IProperty<T>) LEVEL : property;
		}
	}
	
	private static final IUnlistedProperty<?>[] FLUID_RENDER = FLUID_RENDER_PROPS.toArray(new IUnlistedProperty<?>[0]);
	
	public ExtendedStreamBlockFluidState(Block blockIn, IProperty<?>[] properties, IUnlistedProperty<?>[] unlistedProperties)
	{
		super(blockIn, properties, ObjectArrays.concat(FLUID_RENDER, unlistedProperties, IUnlistedProperty.class));
	}
	
	public ExtendedStreamBlockFluidState(Block blockIn, IProperty<?>[] properties)
	{
		super(blockIn, properties, FLUID_RENDER);
	}
	
	@Override
	protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties, ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
	{
		return new HidingFluidState(block, properties, unlistedProperties, null);
	}
}
