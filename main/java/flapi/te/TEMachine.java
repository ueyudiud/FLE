package flapi.te;

import farcore.block.TEBase;
import flapi.FleAPI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class TEMachine extends TEBase
{
	public boolean openGUI(EntityPlayer player, int side, float x, float y, float z)
	{
		int id = getGUIID(side, x, y, z);
		if(id != Integer.MIN_VALUE)
		{
			player.openGui(FleAPI.MODID, id, worldObj, xCoord, yCoord, zCoord);
			return true;
		}
		return false;
	}
	
	protected int getGUIID(int side, float x, float y, float z)
	{
		return Integer.MIN_VALUE;
	}
	
	public Container getContainer(EntityPlayer player, int id)
	{
		return null;
	}
	
	public GuiContainer getGUIContainer(EntityPlayer player, int id)
	{
		return null;
	}
}