/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.light;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

@Deprecated
public class LightFix
{
	public static boolean checkLightFor(World world, EnumSkyBlock type, BlockPos pos)
	{
		return world.checkLightFor(type, pos);
	}
}