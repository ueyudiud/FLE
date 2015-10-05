package fle.api.event;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.gui.FontFLERenderer;

@SideOnly(Side.CLIENT)
public class FLEFontRegisterEvent extends Event
{
	private IIconRegister register;
	
	public FLEFontRegisterEvent(IIconRegister aRegister)
	{
		register = aRegister;
	}
	
	public void registerIcon(char chr, String str)
	{
		FontFLERenderer.registerFont(chr, register.registerIcon(str));
	}
}