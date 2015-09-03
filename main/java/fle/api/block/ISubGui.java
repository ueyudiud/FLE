package fle.api.block;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;

public interface ISubGui<E extends Block>
{
	public Container openContainer(E block, World aWorld, int x, int y, int z, EntityPlayer aPlayer);
	
	public GuiContainer openGui(E block, World aWorld, int x, int y, int z, EntityPlayer aPlayer);
}
