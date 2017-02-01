package farcore.lib.tile;

import java.util.List;

import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;

public interface IDebugableTile
{
	void addDebugInformation(EntityPlayer player, Direction side, List<String> list);
}