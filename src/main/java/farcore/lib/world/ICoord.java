package farcore.lib.world;

import net.minecraft.world.World;

public interface ICoord
{
	World world();
	
	double[] coordD();
	
	int[] coordI();
}