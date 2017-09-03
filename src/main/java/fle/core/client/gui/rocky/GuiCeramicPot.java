/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.gui.rocky;

import fle.core.FLE;
import fle.core.common.gui.rocky.ContainerCeramicPot;
import fle.core.tile.rocky.TECeramicPot;
import nebula.client.gui.GuiContainerTileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiCeramicPot extends GuiContainerTileInventory<TECeramicPot>
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/boiling_heater.png");
	
	public GuiCeramicPot(TECeramicPot tile, EntityPlayer player)
	{
		super(new ContainerCeramicPot(tile, player), LOCATION);
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		super.drawOther(mouseX, mouseY);
		drawTexturedModalRect(this.guiLeft + 66, this.guiTop + 15, 176, 14, 8, 20);
	}
}