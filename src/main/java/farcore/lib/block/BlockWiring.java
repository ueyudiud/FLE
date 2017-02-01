package farcore.lib.block;

import farcore.lib.tile.abstracts.TEWiring;
import nebula.common.block.BlockSingleTE;
import nebula.common.data.Misc;
import nebula.common.util.Direction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This type provide wiring type block without
 * limited conduct energy.<br>
 * This class provide some functional method for
 * modder.
 * @author ueyudiud
 *
 */
public abstract class BlockWiring extends BlockSingleTE //The wiring block will use only single tile entity.
{
	public BlockWiring(String name, Material materialIn)
	{
		super(name, materialIn);
	}
	
	public BlockWiring(String modid, String name, Material materialIn)
	{
		super(modid, name, materialIn);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, Misc.PROPS_SIDE);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TEWiring tile = (TEWiring) worldIn.getTileEntity(pos);
		if(tile != null)
		{
			for(Direction direction : Direction.DIRECTIONS_3D)
			{
				if(tile.canConnectWith(direction))
				{
					state = state.withProperty(Misc.PROPS_SIDE[direction.ordinal()], true);
				}
			}
			return state;
		}
		return state;
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
	public abstract TEWiring createNewTileEntity(World worldIn, int meta);
}