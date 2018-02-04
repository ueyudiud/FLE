/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.blockstate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public class BlockStateTileEntityWapper<T extends TileEntity> extends BlockStateWrapper
{
	public static <T extends TileEntity> BlockStateTileEntityWapper<T> wrap(T tile, IBlockState state)
	{
		return new BlockStateTileEntityWapper<>(tile, state);
	}
	
	public static <T extends TileEntity> T unwrap(IBlockState state)
	{
		return state instanceof BlockStateTileEntityWapper ? (T) ((BlockStateTileEntityWapper<?>) state).tile : null;
	}
	
	public final T tile;
	
	protected BlockStateTileEntityWapper(T tile, IBlockState state)
	{
		super(state);
		this.tile = tile;
	}
	
	@Override
	protected BlockStateTileEntityWapper<T> wrapState(IBlockState state)
	{
		return new BlockStateTileEntityWapper<>(this.tile, state);
	}
}
