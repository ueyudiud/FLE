/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block;

import farcore.lib.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.state.IBlockState;

/**
 * @author ueyudiud
 */
public interface IExtendedDataBlock
{
	int getDataFromState(IBlockState state);
	
	IBlockState getStateFromData(int meta);
	
	void registerStateToRegister(ExtendedBlockStateRegister register);
}