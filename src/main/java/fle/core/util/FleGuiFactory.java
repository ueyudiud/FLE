package fle.core.util;

import farcore.lib.container.GuiFactory;
import fle.api.gui.ContainerPlayerCraftingFle;
import fle.api.gui.GuiPlayerCraftingFle;
import fle.core.container.alpha.ContainerWashing;
import fle.core.gui.alpha.GuiWashing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class FleGuiFactory extends GuiFactory
{
	public FleGuiFactory()
	{
		super("fle");
	}

	@Override
	protected Object openServerGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
		case 1 : return new ContainerWashing(player, world, x, y, z);
		case 2 : return new ContainerPlayerCraftingFle(world, player);
		default: return null;
		}
	}
	
	@Override
	protected Object openClientGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
		case 1 : return new GuiWashing(player, world, x, y, z);
		case 2 : return new GuiPlayerCraftingFle(world, player);
		default: return null;
		}
	}
}