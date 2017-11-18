/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import static nebula.base.ObjArrayParseHelper.create;

import java.util.function.Function;

import nebula.common.block.property.PropertyInt;
import nebula.common.fluid.FluidBase;
import nebula.common.util.L;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * @author ueyudiud
 */
public class BlockStreamFluid extends BlockStandardFluid implements IExtendedDataBlock
{
	public static final int MAX_QUANTA_VALUE = 100;
	
	public static final PropertyInt LEVEL_STREAM = new PropertyInt("level", 0, MAX_QUANTA_VALUE - 1);
	
	protected static Function<Object[], IBlockState> createFunctionApplier(BlockStreamFluid block)
	{
		return objects -> block.getDefaultState().withProperty(LEVEL_STREAM, create(objects).readOrSkip(block.quantaPerBlock) - 1);
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
		return new ExtendedStreamBlockFluidState(this, new IProperty[] { LEVEL_STREAM });
	}
	
	@Override
	protected IProperty<Integer> getLevelProperty()
	{
		return LEVEL_STREAM;
	}
	
	@Override
	public BlockStreamFluid setQuantaPerBlock(int quantaPerBlock)
	{
		this.quantaPerBlockFloat = this.quantaPerBlock = L.range(1, MAX_QUANTA_VALUE, quantaPerBlock);
		setDefaultState(getDefaultState().withProperty(LEVEL_STREAM, this.quantaPerBlock - 1));
		return this;
	}
	
	@Override
	public int getDataFromState(IBlockState state)
	{
		return state.getValue(LEVEL_STREAM);
	}
	
	@Override
	public IBlockState getStateFromData(int meta)
	{
		return getDefaultState().withProperty(LEVEL_STREAM, (meta % this.quantaPerBlock));
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
		register.registerStates(LEVEL_STREAM);
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
