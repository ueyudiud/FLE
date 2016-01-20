package farcore.util;

import java.util.List;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/**
 * The debug information added for block.
 * Active only on server side.
 * @author ueyudiud
 *
 */
public interface IDebugable
{
	void addInfomationToList(World world, BlockPos pos, List<String> list);
}