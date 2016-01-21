package flapi.debug;

import com.google.common.base.Function;

import net.minecraft.block.state.IBlockState;

public interface IModelLocateProvider extends Function<IBlockState, String>
{
	/**
	 * Apply a model locate with block state.
	 */
	@Override
	String apply(IBlockState state);
}