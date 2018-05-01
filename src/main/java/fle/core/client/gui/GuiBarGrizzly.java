/*
 * copyright© 2016-2018 ueyudiud
 */
package fle.core.client.gui;

import java.io.IOException;

import farcore.data.Keys;
import fle.core.common.gui.ContainerBarGrizzly;
import nebula.client.gui.GuiBackground;
import nebula.client.gui.GuiContainer01Slots;
import nebula.common.LanguageManager;
import nebula.common.util.L;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
@GuiBackground("fle:textures/gui/washing_bar_grizzly.png")
public class GuiBarGrizzly extends GuiContainer01Slots
{
	public GuiBarGrizzly(EntityPlayer player, World world, BlockPos pos)
	{
		super(new ContainerBarGrizzly(player, world, pos));
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer1(float partialTicks, int mouseX, int mouseY)
	{
		int max = ((ContainerBarGrizzly) this.inventorySlots).getMaxProgress();
		int pro = ((ContainerBarGrizzly) this.inventorySlots).getProgress();
		int pow = ((ContainerBarGrizzly) this.inventorySlots).getPower();
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
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		this.fontRendererObj.drawString(((ContainerBarGrizzly) this.inventorySlots).player.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if (((ContainerBarGrizzly) this.inventorySlots).getProgress() > 0 && L.nextInt(4) == 0)
		{
			this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMusicRecord(SoundEvents.BLOCK_WATER_AMBIENT));
		}
	}
	
	@Override
	protected String getTitleName()
	{
		return LanguageManager.translateLocal("inventory.bar.grizzly");
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
