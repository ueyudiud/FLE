package farcore.world;

import net.minecraft.world.World;

public interface IObjectInWorld
{
	World world();
	
	BlockPos pos();
}