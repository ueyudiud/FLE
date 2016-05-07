package fle.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.container.GuiFactory;
import farcore.util.FleTextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class FleAPI
{
	public static final String ID = "fle";
	
	public static GuiFactory guiFactory;
	
	@SideOnly(Side.CLIENT)
	public static FleTextureMap conditionTextureMap;
	
	public static void openGui(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		guiFactory.openGui(id, player, world, x, y, z);
	}
}