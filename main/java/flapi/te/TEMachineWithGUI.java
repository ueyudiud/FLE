package flapi.te;

import flapi.util.FleValue;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.common.util.ForgeDirection;

public class TEMachineWithGUI extends TEMachine
{
	@Override
	public boolean onBlockActivated(EntityPlayer player, int side, float hitX,
			float hitY, float hitZ)
	{
		if(!isClient())
		{
			return openGUI(player, side, hitX, hitY, hitZ);
		}
		else
		{
			return true;
		}
	}
	
	@Override
	protected int getGUIID(int side, float x, float y, float z)
	{
		return FleValue.TILE_GUI_CHANNEL | side;
	}
	
	@Override
	public Container getContainer(EntityPlayer player, int id)
	{
		if((id & FleValue.TILE_GUI_CHANNEL) != 0)
		{
			return getContainer(player,
					ForgeDirection.VALID_DIRECTIONS[id ^ FleValue.TILE_GUI_CHANNEL]);
		}
		return null;
	}
	
	public Container getContainer(EntityPlayer player, ForgeDirection dir)
	{
		return null;
	}
	
	@Override
	public GuiContainer getGUIContainer(EntityPlayer player, int id)
	{
		if((id & FleValue.TILE_GUI_CHANNEL) != 0)
		{
			return getGUIContainer(player,
					ForgeDirection.VALID_DIRECTIONS[id ^ FleValue.TILE_GUI_CHANNEL]);
		}
		return null;
	}
	
	public GuiContainer getGUIContainer(EntityPlayer player, ForgeDirection dir)
	{
		return null;
	}
}