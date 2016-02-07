package farcore.util;

import java.util.List;

import farcore.world.BlockPos;

/**
 * The debug information added for block.
 * Active only on server side.
 * @author ueyudiud
 *
 */
public interface IDebugable
{
	void addInfomationToList(BlockPos pos, List<String> list);
}