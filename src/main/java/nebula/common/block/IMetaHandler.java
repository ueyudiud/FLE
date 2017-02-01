package nebula.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public interface IMetaHandler
{
	BlockStateContainer createBlockState(Block block);
	
	int getMeta(Block block, IBlockState state);

	IBlockState getState(Block block, int meta);
	
	IBlockState initDefaultState(IBlockState state);
}