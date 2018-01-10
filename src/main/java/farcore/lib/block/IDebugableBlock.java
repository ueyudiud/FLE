/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.block;

import java.util.List;

import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author ueyudiud
 */
public interface IDebugableBlock
{
	/**
	 * Added debugging information when player using
	 * {@link farcore.items.ItemDebugger} to get debugging
	 * information. Providing information to chat message and send to player.
	 * 
	 * @param player the player using debugger.
	 * @param world the world.
	 * @param pos the position.
	 * @param side the side player clicked.
	 * @param list the debugging chat list.
	 * @throws RuntimeException if some exception are caught when providing
	 *             data, these exception will be caught by ItemDebugger and
	 *             logged.
	 */
	void addInformation(EntityPlayer player, World world, BlockPos pos, Direction side, List<String> list);
}
