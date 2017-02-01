package farcore.lib.block;

import java.util.List;

import nebula.common.util.Direction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IDebugableBlock
{
	void addInformation(EntityPlayer player, World world, BlockPos pos, Direction side, List<String> list);
}