/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import static nebula.base.ObjArrayParseHelper.create;

import java.util.function.Function;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;

import farcore.lib.block.state.PropertyFarInt;
import nebula.common.fluid.FluidBase;
import nebula.common.util.L;
import nebula.common.world.chunk.IBlockStateRegister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * @author ueyudiud
 */
public class BlockStreamFluid extends BlockStandardFluid implements IExtendedDataBlock
{
	private static final int MAX_QUANTA_VALUE = 100;
	
	public static final PropertyFarInt LEVEL = new PropertyFarInt("level", 0, MAX_QUANTA_VALUE - 1);
	
	protected static Function<Object[], IBlockState> createFunctionApplier(BlockStreamFluid block)
	{
		return objects->block.getDefaultState().withProperty(LEVEL, create(objects).readOrSkip(block.quantaPerBlock) - 1);
	}
	
	public BlockStreamFluid(String registerName, FluidBase fluid, Material material)
	{
		super(registerName, fluid, material);
		setQuantaPerBlock(MAX_QUANTA_VALUE);
	}
	
	public BlockStreamFluid(FluidBase fluid, Material material)
	{
		super(fluid, material);
		setQuantaPerBlock(MAX_QUANTA_VALUE);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(this, new IProperty[] { LEVEL }, FLUID_RENDER_PROPS.toArray(new IUnlistedProperty<?>[0]))
		{
			final class HidingFluidState extends ExtendedBlockState.ExtendedStateImplementation
			{
				protected HidingFluidState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
						ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties,
						ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> table)
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
			
			@Override
			protected StateImplementation createState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
					ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties)
			{
				return new HidingFluidState(block, properties, unlistedProperties, null);
			}
		};
	}
	
	@Override
	protected IProperty<Integer> getLevelProperty()
	{
		return LEVEL;
	}
	
	@Override
	public BlockStreamFluid setQuantaPerBlock(int quantaPerBlock)
	{
		this.quantaPerBlockFloat = this.quantaPerBlock = L.range(1, MAX_QUANTA_VALUE, quantaPerBlock);
		setDefaultState(getDefaultState().withProperty(LEVEL, this.quantaPerBlock - 1));
		return this;
	}
	
	@Override
	public int getDataFromState(IBlockState state)
	{
		return state.getValue(LEVEL);
	}
	
	@Override
	public IBlockState getStateFromData(int meta)
	{
		return getDefaultState().withProperty(LEVEL, (meta % this.quantaPerBlock));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}
	
	@Override
	public void registerStateToRegister(IBlockStateRegister register)
	{
		register.registerStates(LEVEL);
	}
	
	@Override
	public float getFluidHeightForRender(IBlockAccess world, BlockPos pos)
	{
		IBlockState here = world.getBlockState(pos);
		IBlockState up = world.getBlockState(pos.down(this.densityDir));
		if (here.getBlock() == this)
		{
			if (up.getMaterial().isLiquid() || up.getBlock() instanceof IFluidBlock)
			{
				return 1;
			}
			
			if (getDataFromState(here) == getMaxRenderHeightMeta())
			{
				return 0.875F;
			}
		}
		return !here.getMaterial().isSolid() && up.getBlock() == this ? 1 : getQuantaPercentage(world, pos) * 0.875F;
	}
}