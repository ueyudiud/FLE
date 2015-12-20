package flapi.util;

import java.util.List;

import net.minecraft.world.World;

/**
 * The debug information added for block.
 * Active only on server side.
 * @author ueyudiud
 *
 */
public interface IDebugable
{
	void addInfomationToList(World world, int x, int y, int z, List<String> list);
}