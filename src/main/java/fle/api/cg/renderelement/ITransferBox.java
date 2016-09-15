package fle.api.cg.renderelement;

import fle.api.cg.IGuiRecipeHanler;
import fle.api.cg.IRecipeDisplayHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITransferBox
{
	boolean isVisible();

	boolean isMouseMoveOn(int x, int y);

	void onBoxClicked(IGuiRecipeHanler hanler);
	
	@SideOnly(Side.CLIENT)
	void renderTransferBoxBackground(IRecipeDisplayHelper helper);
	
	@SideOnly(Side.CLIENT)
	void renderTransferBoxFrontground(IRecipeDisplayHelper helper);
}