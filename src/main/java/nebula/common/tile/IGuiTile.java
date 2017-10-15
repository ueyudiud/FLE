/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.tile;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public interface IGuiTile
{
	Container openContainer(int id, EntityPlayer player);
	
	@SideOnly(Side.CLIENT)
	GuiContainer openGui(int id, EntityPlayer player);
}