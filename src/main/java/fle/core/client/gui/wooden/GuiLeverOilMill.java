/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.client.gui.wooden;

import java.io.IOException;

import farcore.data.Keys;
import farcore.lib.container.Container04Solid;
import farcore.lib.gui.GuiContainer03Solid;
import fle.core.tile.wooden.TELeverOilMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiLeverOilMill extends GuiContainer03Solid<TELeverOilMill>
{
	public GuiLeverOilMill(TELeverOilMill tile, EntityPlayer player)
	{
		super(new Container04Solid<>(tile, player));
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		if (isShiftKeyDown() && matchKey(Keys.ROTATE, keyCode))
		{
			sendGuiData(0, 0, true);
		}
	}
}
