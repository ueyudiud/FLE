package farcore.alpha.interfaces.block;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IDebugableBlock
{
	void addInformation(EntityPlayer player, World world, int x, int y, int z, int side, List<String> list);
}
