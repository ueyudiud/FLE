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
import nebula.common.util.L;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
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
		boolean mark = startTranslate();
		super.drawOther(mouseX, mouseY);
		int max = ((ContainerBarGrizzly) this.container).getMaxProgress();
		int pro = ((ContainerBarGrizzly) this.container).getProgress();
		int pow = ((ContainerBarGrizzly) this.container).getPower();
		if (max > 1)
		{
			drawTexturedModalRect(57, 34, 176, 22, 29, 9);
			drawProgressScaleUTD(59, 50, 176, 0, 28, 21, pro, max);
		}
		if (pow > 0)
		{
			drawTexturedModalRect(35, 60, 176, 50, 11, 11);
			drawProgressScaleDTU(26, 16, 176, 61, 8, 54, pow, 400);
		}
		endTranslate(mark);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRendererObj.drawString(this.container.getOpener().inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (((ContainerBarGrizzly) this.container).getProgress() > 0 && L.nextInt(4) == 0)
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMusicRecord(SoundEvents.BLOCK_WATER_AMBIENT));
		}
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
