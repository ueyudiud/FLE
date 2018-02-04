/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.block;

import net.minecraft.block.state.IBlockState;

/**
 * The block contain more than 16 data.
 * <p>
 * Use for save, load and sync data. The data provide a 20bit length integer
 * value to save.
 * 
 * @author ueyudiud
 * @see ed.chunk.ExtendedBlockStateRegister
 */
public interface IExtendedDataBlock
{
	/**
	 * Get 20 bits data of state.
	 * 
	 * @param state the state to get meta.
	 * @return the meta.
	 */
	int getDataFromState(IBlockState state);
	
	/**
	 * Get 4 bits data from state, called when 20 bits data are missing.
	 * 
	 * @param state the state to get meta.
	 * @return the meta.
	 */
	default int getUnfixableDataFromState(IBlockState state)
	{
		return getDataFromState(state) & 0xF;
	}
	
	/**
	 * Get state from 20 bit data(meta).
	 * 
	 * @param meta the meta of block state.
	 * @return the block state.
	 */
	IBlockState getStateFromData(int meta);
	
	/**
	 * Register all state can be exist to register.
	 * 
	 * @param register the state register.
	 * @see nebula.common.block.IBlockStateRegister#registerState(IBlockState)
	 * @see nebula.common.block.IBlockStateRegister#registerStateMap(IBlockState,
	 *      java.util.Collection)
	 * @see nebula.common.block.IBlockStateRegister#registerStates(net.minecraft.block.Block,
	 *      net.minecraft.block.properties.IProperty...)
	 */
	void registerStateToRegister(IBlockStateRegister register);
}
