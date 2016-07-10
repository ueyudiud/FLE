package farcore.lib.world;

import net.minecraft.world.World;

public interface ICrood
{
	World world();
	
	double[] coordD();
	
	int[] coordI();
}