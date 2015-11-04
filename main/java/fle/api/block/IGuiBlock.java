package fle.api.block;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

/**
 * 
 * @author ueyudiud
 *
 */
public interface IGuiBlock
{
	public Container openContainer(World aWorld, int x, int y, int z, EntityPlayer aPlayer);
	
	public GuiContainer openGui(World aWorld, int x, int y, int z, EntityPlayer aPlayer);
}
