package fle.api;

import farcore.lib.container.GuiFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class FleAPI
{
	public static final String ID = "fle";
	
	public static GuiFactory guiFactory;
	
	public static void openGui(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		guiFactory.openGui(id, player, world, x, y, z);
	}
}