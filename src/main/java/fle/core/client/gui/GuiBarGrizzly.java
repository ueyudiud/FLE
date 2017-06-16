/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.client.gui;

import java.io.IOException;

import farcore.data.Keys;
import fle.core.FLE;
import fle.core.common.gui.ContainerBarGrizzly;
import nebula.client.gui.GuiContainerBase;
import nebula.common.LanguageManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class GuiBarGrizzly extends GuiContainerBase
{
	private static final ResourceLocation LOCATION = new ResourceLocation(FLE.MODID, "textures/gui/washing_bar_grizzly.png");
	
	public GuiBarGrizzly(EntityPlayer player, World world, BlockPos pos)
	{
		super(new ContainerBarGrizzly(player, world, pos), LOCATION);
	}
	
	@Override
	protected void drawOther(int mouseX, int mouseY)
	{
		super.drawOther(mouseX, mouseY);
		int max = ((ContainerBarGrizzly) this.container).getMaxProgress();
		int pro = ((ContainerBarGrizzly) this.container).getProgress();
		int pow = ((ContainerBarGrizzly) this.container).getPower();
		if (max > 1)
		{
			drawTexturedModalRect(this.guiLeft + 57, this.guiTop + 34, 176, 22, 29, 9);
			drawProgressScaleUTD(this.guiLeft + 59, this.guiTop + 50, 176, 0, 28, 21, pro, max);
		}
		if (pow > 0)
		{
			drawTexturedModalRect(this.guiLeft + 35, this.guiTop + 60, 176, 50, 11, 11);
			drawProgressScaleDTU(this.guiLeft + 26, this.guiTop + 16, 176, 61, 8, 54, pow, 400);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRendererObj.drawString(this.container.getOpener().inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
	}
	
	@Override
	protected String getTitleName()
	{
		return LanguageManager.translateToLocal("inventory.bar.grizzly");
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