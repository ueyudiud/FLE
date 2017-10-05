/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.block;

import nebula.common.world.chunk.IBlockStateRegister;
import net.minecraft.block.Block;

/**
 * @author ueyudiud
 */
public interface IMetaExtHandler extends IMetaHandler
{
	void registerStateToRegister(Block block, IBlockStateRegister register);
}