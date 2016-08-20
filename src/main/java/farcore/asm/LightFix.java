package farcore.asm;

import farcore.data.Config;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LightFix
{
	private static final LightFixLocal FIX_SERVER = new LightFixLocal();

	public static boolean checkLightFor(World world, EnumSkyBlock type, BlockPos pos)
	{
		//Should this method just share the calculation of light with server?
		if(Config.multiThreadLight && !world.isRemote)
		{
			if (!world.isAreaLoaded(pos, 17, false))
				return false;
			else
			{
				markLightForCheck(world, type, pos);
				return true;
			}
		}
		else return world.checkLightFor(type, pos);
	}

	public static void tickLightUpdate(World world)
	{
		FIX_SERVER.tickLightUpdate(world);
	}
	
	public static void onWorldUnload(World world)
	{
		FIX_SERVER.onWorldUnload(world);
	}
	
	private static void markLightForCheck(World world, EnumSkyBlock type, BlockPos pos)
	{
		FIX_SERVER.markLightForCheck(world, type, pos);
	}

	@SideOnly(Side.CLIENT)
	public static String getOverlayInfo()
	{
		return String.format("Lu %d Lc %d", FIX_SERVER.list.size(), FIX_SERVER.lightMap.size());
	}

	public static void startThread()
	{
		FIX_SERVER.startThread();
	}
}