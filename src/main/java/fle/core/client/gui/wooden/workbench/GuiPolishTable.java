/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.gui.wooden.workbench;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import fle.api.FLEAPI;
import fle.api.client.PolishingStateIconLoader;
import fle.core.FLE;
import fle.core.common.gui.wooden.workbench.ContainerPolishTable;
import fle.core.tile.wooden.workbench.TEWoodenPolishTable;
import nebula.client.gui.GuiContainerTileInventory;
import nebula.client.gui.GuiIconButton;
import nebula.client.gui.GuiIconButton.ButtonSize;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiPolishTable extends GuiContainerTileInventory<TEWoodenPolishTable>
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/polish.png");
	
	public GuiPolishTable(TEWoodenPolishTable tile, EntityPlayer player)
	{
		super(new ContainerPolishTable(tile, player), LOCATION);
		this.tile = tile;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		addButton(new GuiIconButton(0, this.guiLeft + 102, this.guiTop + 18, ButtonSize.Standard, FLEAPI.BUTTON_LOCATION, 48, 0, this.itemRender));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		super.actionPerformed(button);
		sendGuiData(0, button.id, false);//Let server to handle if the item is success deleted and return back to client.
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		float zOld = this.zLevel;
		super.drawOther(mouseX, mouseY);
		if (this.tile.getPolishingInput() != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(this.guiLeft + 44F, this.guiTop + 18F, 0F);
			GL11.glScalef(3.125F, 3.125F, 1.0F);
			drawItemStack(this.tile.getPolishingInput(), 0, 0, false, null, 50.0F);
			GL11.glPopMatrix();
			bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			char[] array = this.tile.getPolishingMatrix();
			this.zLevel = 300.0F;
			for (int i = 0; i < 3; ++i)
				for (int j = 0; j < 3; ++j)
				{
					drawTexturedModalRect(this.guiLeft + 44 + 17 * i, this.guiTop + 18 + 17 * j,
							PolishingStateIconLoader.getIconFromChr(array[j * 3 + i]), 16, 16);
				}
			bindDefaultTexture();
		}
		drawTexturedModalRect(this.guiLeft + 44, this.guiTop + 18, 176, 0, 50, 50);
		this.zLevel = zOld;
	}
}