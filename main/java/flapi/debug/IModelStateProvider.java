package flapi.debug;

import net.minecraft.block.state.IBlockState;

import flapi.debug.BlockStateJsonWriter.BlockModel;

public interface IModelStateProvider
{
	void set(IBlockState state, BlockModel model);
}
