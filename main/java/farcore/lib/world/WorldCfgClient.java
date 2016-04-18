package farcore.lib.world;

import java.io.File;

import farcore.util.U;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class WorldCfgClient extends WorldCfg
{
	@Override
	public World a(int id)
	{
		if (U.Sides.isSimulating())
		{
			return super.a(id);
		} 
		else
		{
			World world = Minecraft.getMinecraft().theWorld;
			return world == null ? null : world.provider.dimensionId != id ? null : world;
		}
	}
	
	@Override
	public EntityPlayer b()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	
	@Override
	public File c()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}
}