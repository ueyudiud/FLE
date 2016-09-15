package fle.api.cg;

import java.util.List;

import fle.api.cg.renderelement.IPage;
import fle.api.cg.renderelement.ITransferBox;
import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IDisplayable
{
	@SideOnly(Side.CLIENT)
	void displayRecipe(IPage page, IRecipeDisplayHelper helper);
	
	@SideOnly(Side.CLIENT)
	void addButtons(IPage page, List<GuiButton> list);
	
	@SideOnly(Side.CLIENT)
	void addTooltip(IPage page, ITransferBox box, List<String> list);

	@SideOnly(Side.CLIENT)
	List<IPage> getAllowancePages();
	
	@SideOnly(Side.CLIENT)
	List<IPage> getPages(String key, Object information);
	
	@SideOnly(Side.CLIENT)
	List<ITransferBox> getTransferBoxs(IPage page);
}