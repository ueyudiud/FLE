package farcore.lib.world;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Just use in far util.
 * @author ueyudiud
 * @see farcore.util.U.Worlds
 */
public class WorldCfg
{
	public World a(int id) 
	{
		return DimensionManager.getWorld(id);
	}
	
	public EntityPlayer b()
	{
		return null;
	}
	
	public File c()
	{
		return new File(".");
	}
}