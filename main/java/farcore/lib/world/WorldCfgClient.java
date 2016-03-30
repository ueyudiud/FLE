package farcore.lib.world;

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
}