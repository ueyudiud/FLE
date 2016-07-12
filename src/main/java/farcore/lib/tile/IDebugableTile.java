package farcore.lib.tile;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

public interface IDebugableTile
{
	void addDebugInformation(EntityPlayer player, int side, List<String> list);
}
