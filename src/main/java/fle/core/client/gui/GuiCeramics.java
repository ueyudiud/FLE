/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.client.gui;

import java.io.IOException;

import fle.api.FLEAPI;
import fle.core.common.gui.ContainerCeramics;
import nebula.client.gui.GuiBackground;
import nebula.client.gui.GuiContainer01Slots;
import nebula.client.gui.GuiIconButton;
import nebula.client.gui.GuiIconButton.ButtonSize;
import nebula.common.LanguageManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
@GuiBackground("fle:textures/gui/clay_model.png")
public class GuiCeramics extends GuiContainer01Slots
{
	public GuiCeramics(EntityPlayer player, World world, BlockPos pos)
	{
		super(new ContainerCeramics(player, world, pos));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		int i = 0;
		for (; i < 5; ++i)
		{
			this.buttonList.add(new GuiIconButton(i, this.guiLeft + 21, this.guiTop + 16 + 11 * i, ButtonSize.Small, FLEAPI.BUTTON_LOCATION, 72, 0, this.itemRender));
		}
		for (; i < 10; ++i)
		{
			this.buttonList.add(new GuiIconButton(i, this.guiLeft + 80, this.guiTop + 16 + 11 * (i - 5), ButtonSize.Small, FLEAPI.BUTTON_LOCATION, 64, 0, this.itemRender));
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) throws IOException
	{
		super.actionPerformed(guibutton);
		sendGuiData(1, guibutton.id, true);
	}
	
	@Override
	protected String getTitleName()
	{
		return LanguageManager.translateLocal("inventory.ceramics");
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer1(float partialTicks, int mouseX, int mouseY)
	{
		byte[] layers = ((ContainerCeramics) this.inventorySlots).layers;
		int i = 0;
		for (; i < 5; ++i)
		{
			drawProgressScaleRTL(this.guiLeft + 34, this.guiTop + 15 + i * 11, 176, -1 + i * 11, 22, 11, layers[i], 64);
		}
		for (i = 0; i < 5; ++i)
		{
			drawProgressScaleLTR(this.guiLeft + 55, this.guiTop + 15 + i * 11, 198, -1 + i * 11, 22, 11, layers[i + 5], 64);
		}
		drawTexturedModalRect(this.guiLeft + 34, this.guiTop + 16, 176, 54, 43, 54);
	}
}