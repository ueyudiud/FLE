package fle.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import fle.FLE;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.api.gui.GuiIconButton;
import fle.api.gui.GuiIconButton.ButtonSize;
import fle.api.net.FlePackets.CoderGuiUpdate;
import fle.api.net.INetEventListener;
import fle.core.te.TileEntityColdForgingPlatform;

public class GuiColdForging extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/forging_platform.png");
	
	private TileEntityColdForgingPlatform tile;
	
	public GuiColdForging(EntityPlayer aPlayer, TileEntityColdForgingPlatform aTile)
	{
		super(new ContainerColdForging(aPlayer.inventory, aTile));
		tile = aTile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		int h = tile.getHarness() + 2;
		switch(tile.getDir())
		{
		case 0 : drawTexturedModalRect(aXOffset + 91, aYOffset + 15 - h, 182, 6 - h, 2, 2 + h);
		break;
		case 1 : drawTexturedModalRect(aXOffset + 91, aYOffset + 15, 182, 6, 2 + h, 2);
		break;
		case 2 : drawTexturedModalRect(aXOffset + 91, aYOffset + 15, 182, 6, 2, 2 + h);
		break;
		case 3 : drawTexturedModalRect(aXOffset + 91 - h, aYOffset + 15, 182 - h, 6, 2 + h, 2);
		}
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				drawCondition(67 + 17 * i, 25 + 17 * j, tile.getState(i + j * 3));
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				buttonList.add(new GuiIconButton(i + j * 3, xoffset + 67 + i * 17, yoffset + 25 + j * 17, ButtonSize.Slot));
		buttonList.add(new GuiIconButton(9, xoffset + 70, yoffset + 11, ButtonSize.Small, GuiIconButton.buttonLocate, 104, 0));
		buttonList.add(new GuiIconButton(10, xoffset + 104, yoffset + 11, ButtonSize.Small, GuiIconButton.buttonLocate, 72, 8));
		buttonList.add(new GuiIconButton(11, xoffset + 26, yoffset + 25, ButtonSize.Small, GuiIconButton.buttonLocate, 96, 8));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		sendToContainer(1, button.id);
		super.actionPerformed(button);
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}

	@Override
	public String getName()
	{
		return tile.getInventoryName();
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}