/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.gui.pottery;

import fle.core.FLE;
import fle.core.gui.pottery.ContainerTerrine;
import fle.core.tile.pottery.TETerrine;
import nebula.client.gui.GuiContainerTileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiTerrine extends GuiContainerTileInventory<TETerrine>
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/pottery/terrine.png");
	
	public GuiTerrine(TETerrine tile, EntityPlayer player)
	{
		super(new ContainerTerrine(tile, player), LOCATION);
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		super.drawOther(mouseX, mouseY);
		this.zLevel = 400F;
		drawTexturedModalRect(this.guiLeft + 75, this.guiTop + 32, 176, 0, 8, 30);
		this.zLevel = 0F;
	}
}