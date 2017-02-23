/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.block;

import nebula.common.world.chunk.ExtendedBlockStateRegister;
import net.minecraft.block.Block;

/**
 * @author ueyudiud
 */
public interface IMetaExtHandler extends IMetaHandler
{
	void registerStateToRegister(Block block, ExtendedBlockStateRegister register);
}