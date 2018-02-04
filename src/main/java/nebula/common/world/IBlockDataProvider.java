/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.common.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

/**
 * The Block Data Provider, use to provide network data and saving data.
 * 
 * @see nebula.Nebula#blockDataProvider
 * @author ueyudiud
 */
public interface IBlockDataProvider
{
	int getStateData(IBlockState state);
	
	IBlockState getStateFromData(int i);
	
	default int getNetworkID(IBlockState state)
	{
		return getStateData(state);
	}
	
	default IBlockState getStateFromNetworkID(int id)
	{
		return getStateFromData(id);
	}
	
	class Template implements IBlockDataProvider
	{
		@Override
		public int getStateData(IBlockState state)
		{
			return Block.getStateId(state);
		}
		
		@Override
		public IBlockState getStateFromData(int i)
		{
			return Block.getStateById(i);
		}
	}
}
