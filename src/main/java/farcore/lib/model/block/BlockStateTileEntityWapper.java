package farcore.lib.model.block;

import farcore.lib.util.BlockStateWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;

public class BlockStateTileEntityWapper<T extends TileEntity> extends BlockStateWrapper
{
	public static <T extends TileEntity> BlockStateTileEntityWapper<T> wrap(T tile, IBlockState state)
	{
		return new BlockStateTileEntityWapper<T>(tile, state);
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
		return new BlockStateTileEntityWapper<T>(tile, state);
	}
}