/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.gui.cg;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiCraftGuidePage extends GuiScreen
{
	public GuiCraftGuidePage()
	{
		
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
}