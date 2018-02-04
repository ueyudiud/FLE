/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.client.gui.wooden;

import java.io.IOException;

import farcore.data.Keys;
import fle.core.FLE;
import fle.core.common.gui.wooden.ContainerLeverOilMill;
import fle.core.tile.wooden.TELeverOilMill;
import nebula.client.gui.GuiContainerTileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiLeverOilMill extends GuiContainerTileInventory<TELeverOilMill>
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/lever_oil_mill.png");
	
	public GuiLeverOilMill(TELeverOilMill tile, EntityPlayer player)
	{
		super(new ContainerLeverOilMill(tile, player), LOCATION);
		this.tile = tile;
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		boolean mark = startTranslate();
		super.drawOther(mouseX, mouseY);
		int pow = this.tile.getField(2);
		if (pow > 0)
		{
			drawTexturedModalRect(35, 60, 176, 24, 11, 11);
			drawProgressScaleDTU(26, 16, 176, 35, 8, 54, pow, 100);
		}
		if (this.tile.getMaxProgress() > 0)
		{
			drawProgressScaleUTD(63, 38, 176, 0, 10, 16, this.tile.getProgress(), this.tile.getMaxProgress());
		}
		drawTexturedModalRect(60, 56, 176, 16, 20, 8);
		endTranslate(mark);
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
