package fle.api.cg;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IGuiRecipeHanler
{
	void openGUI(Object mod, int id);

	void closeGUI();
	
	void transferPage(IDisplayable displayable);
	
	void transferByObject(String transferKey, Object transferItem, boolean detectedInAll);
}