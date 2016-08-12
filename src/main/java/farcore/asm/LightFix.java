package farcore.asm;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

public class LightFix
{
	public static boolean checkLightFor(World world, EnumSkyBlock type, BlockPos pos)
	{
		if (!world.isAreaLoaded(pos, 17, false))
			return false;
		else
		{
			world.checkLightFor(type, pos);
			return true;
		}
	}
}