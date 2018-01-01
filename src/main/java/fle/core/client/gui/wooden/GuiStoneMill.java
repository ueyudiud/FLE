/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.gui.wooden;

import farcore.lib.gui.GuiContainerTIF;
import fle.core.FLE;
import fle.core.common.gui.wooden.ContainerStoneMill;
import fle.core.tile.wooden.TEStoneMill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiStoneMill extends GuiContainerTIF<TEStoneMill>
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/stone_mill.png");
	
	public GuiStoneMill(TEStoneMill tile, EntityPlayer player)
	{
		super(new ContainerStoneMill(tile, player), LOCATION);
		this.tile = tile;
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		super.drawOther(mouseX, mouseY);
		int pow = this.tile.getField(2);
		if (pow > 0)
		{
			drawProgressScaleUTD(this.guiLeft + 82, this.guiTop + 17, 176, 0, 19, 21, pow, 20);
		}
		if (this.tile.isWorking())
		{
			drawProgressScaleLTR(this.guiLeft + 48, this.guiTop + 39, 0, 166, 66, 9, this.tile.getRecipeTick(), this.tile.getMaxRecipeTick());
		}
	}
}
