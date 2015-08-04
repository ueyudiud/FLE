package fle.api.te;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public interface ITEWithGUIInWorld extends ITEInWorld
{
	public boolean openGUI(ItemStack tool, ForgeDirection side);
	
	public GuiContainer openGui(int ID);
	
	public Container openContainer(int ID);
}
