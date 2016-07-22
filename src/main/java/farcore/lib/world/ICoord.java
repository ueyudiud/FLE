package farcore.lib.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICoord
{
	World world();

	BlockPos pos();
}