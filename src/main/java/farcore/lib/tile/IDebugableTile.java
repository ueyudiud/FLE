/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tile;

import java.util.List;

import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;

/**
 * 
 * @author ueyudiud
 */
public interface IDebugableTile
{
	void addDebugInformation(EntityPlayer player, Direction side, List<String> list);
}
