/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.plant;

import java.util.Random;

import farcore.blocks.BlockPlant;
import farcore.blocks.BlockPlantVine;
import farcore.lib.material.Mat;
import nebula.common.LanguageManager;
import nebula.common.block.IBlockStateRegister;
import nebula.common.data.Misc;
import nebula.common.util.Direction;
import nebula.common.util.L;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public class PlantVineLike extends PlantNormal
{
	public final BlockPlantVine vine;
	
	public PlantVineLike(Mat material)
	{
		super(material);
		this.vine = createVineBlock(material);
		LanguageManager.registerLocal(this.vine.getTranslateNameForItemStack(0), material.localName);
	}
	
	protected BlockPlantVine createVineBlock(Mat material)
	{
		return new BlockPlantVine(material).setBaseBlock(this.block);
	}
	
	@Override
	public void registerStateToRegister(Block block, IBlockStateRegister register)
	{
		register.registerState(block.getDefaultState());
	}
	
	@Override
	public BlockStateContainer createBlockState(Block block)
	{
		return new BlockStateContainer(block);
	}
	
	@Override
	public int getMeta(Block block, IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getState(Block block, int meta)
	{
		return block.getDefaultState();
	}
	
	@Override
	public IBlockState initDefaultState(IBlockState state)
	{
		return state;
	}
	
	@Override
	protected IBlockState randomGrowState(IBlockState parent, Random random)
	{
		return parent;
	}
	
	@Override
	public void randomTick(BlockPlant block, IBlockState state, World world, BlockPos pos, Random random)
	{
		if (!world.isRemote)
		{
			if (random.nextInt(8) == 0)
			{
				Direction direction = L.random(Direction.DIRECTIONS_2D, random);
				BlockPos pos2;
				if (this.vine.getGrowDirection() > 0)
				{
					IBlockState up = world.getBlockState(pos2 = pos.up());
					if (up.getBlock().isAir(up, world, pos2) && up.getBlock().canPlaceBlockOnSide(world, pos2, direction.of()))
					{
						world.setBlockState(pos2, this.vine.getDefaultState().withProperty(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal], true));
					}
					else if (up.getBlock() == this.vine && !up.getValue(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal]))
					{
						world.setBlockState(pos2, up.withProperty(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal], true), 2);
					}
				}
				else
				{
					IBlockState down = world.getBlockState(pos2 = direction.opposite().offset(pos.down()));
					if (down.getBlock().isAir(down, world, pos2) && down.getBlock().canPlaceBlockOnSide(world, pos2, direction.of()))
					{
						world.setBlockState(pos2, this.vine.getDefaultState().withProperty(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal], true));
					}
					else if (down.getBlock() == this.vine && !down.getValue(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal]))
					{
						world.setBlockState(pos2, down.withProperty(Misc.PROPS_SIDE_HORIZONTALS[direction.horizontalOrdinal], true), 2);
					}
				}
			}
			
		}
		super.randomTick(block, state, world, pos, random);
	}
}
