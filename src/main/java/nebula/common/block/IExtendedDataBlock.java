/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import nebula.common.world.chunk.IBlockStateRegister;
import net.minecraft.block.state.IBlockState;

/**
 * The block contain more than 16 data.<p>
 * Use for save, load and sync data.
 * The data provide a 20bit length integer value to save.
 * 
 * @author ueyudiud
 * @see nebula.common.world.chunk.ExtendedBlockStateRegister
 */
public interface IExtendedDataBlock
{
	/**
	 * Get 20 bit data of state.
	 * @param state the state to get meta.
	 * @return the meta.
	 */
	int getDataFromState(IBlockState state);
	
	/**
	 * Get state from 20 bit data(meta).
	 * @param meta the meta of block state.
	 * @return the block state.
	 */
	IBlockState getStateFromData(int meta);
	
	/**
	 * Register all state can be exist to register.
	 * @param register the state register.
	 * @see nebula.common.world.chunk.IBlockStateRegister#registerState(IBlockState)
	 * @see nebula.common.world.chunk.IBlockStateRegister#registerStateMap(IBlockState, java.util.Collection)
	 * @see nebula.common.world.chunk.IBlockStateRegister#registerStates(net.minecraft.block.Block, net.minecraft.block.properties.IProperty...)
	 */
	void registerStateToRegister(IBlockStateRegister register);
}