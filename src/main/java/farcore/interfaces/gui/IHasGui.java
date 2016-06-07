package farcore.interfaces.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public interface IHasGui
{
	@SideOnly(Side.CLIENT)
	Gui openGUI(int id, EntityPlayer player, World world, int x, int y, int z);
	
	Container openContainer(int id, EntityPlayer player, World world, int x, int y, int z);
}