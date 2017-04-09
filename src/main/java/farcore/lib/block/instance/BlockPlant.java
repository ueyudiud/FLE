/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.instance;

import farcore.data.Materials;
import farcore.lib.plant.IPlant;
import nebula.client.util.IRenderRegister;
import nebula.common.block.BlockSubBehavior;
import nebula.common.block.IBlockBehavior;
import nebula.common.block.IBlockStayabilityCheck;
import nebula.common.block.IExtendedDataBlock;
import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public abstract class BlockPlant extends BlockSubBehavior implements IExtendedDataBlock, IBlockStayabilityCheck
{
	public static BlockPlant create(String modid, String name, IPlant<?> plant)
	{
		return new BlockPlant(modid, name, plant)
		{
			@Override
			protected BlockStateContainer createBlockState()
			{
				return plant.createBlockState(this);
			}
			
			@Override
			public void registerStateToRegister(ExtendedBlockStateRegister register)
			{
				plant.registerStateToRegister(this, register);
			}
			
			@Override
			public int getDataFromState(IBlockState state)
			{
				return plant.getMeta(this, state);
			}
			
			@Override
			public IBlockState getStateFromData(int meta)
			{
				return plant.getState(this, meta);
			}
			
			@Override
			protected IBlockState initDefaultState(IBlockState state)
			{
				return plant.initDefaultState(state);
			}
		};
	}
	
	private final IPlant plant;
	
	protected BlockPlant(String name, IPlant<?> plant)
	{
		super(name, Materials.PLANT);
		setTickRandomly(true);
		this.plant = plant;
	}
	protected BlockPlant(String modid, String name, IPlant<?> plant)
	{
		super(modid, name, Materials.PLANT);
		setTickRandomly(true);
		this.plant = plant;
	}
	
	@Override
	protected IBlockBehavior<?> getBehavior(IBlockState state)
	{
		return this.plant;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		if (this.plant instanceof IRenderRegister)
		{
			((IRenderRegister) this.plant).registerRender();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean canBlockStayAt(World world, BlockPos pos, IBlockState state)
	{
		return this.plant.canBlockStay(this, state, world, pos);
	}
}