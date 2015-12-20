package fle.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiContainerBase;
import flapi.gui.GuiIconButton;
import flapi.gui.GuiIconButton.ButtonSize;
import flapi.util.FleValue;
import fle.core.te.TileEntityOilMill;

@SideOnly(Side.CLIENT)
public class GuiLeverOilMill extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/lever_oil_mill.png");
	
	TileEntityOilMill tile;
	
	public GuiLeverOilMill(EntityPlayer aPlayer, TileEntityOilMill aTile)
	{
		super(new ContainerLeverOilMill(aPlayer.inventory, aTile));
		tile = aTile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		int p1 = tile.getCache(8) + (int)((float) (tick * 8) / 100F);
		int p2 = tile.getRecipeProgress(16);
		if(p1 > 0)
		{
			drawTexturedModalRect(aXOffset + 98, aYOffset + 63, 176, 24, p1, 1);
		}
		if(p2 > 0)
		{
			drawTexturedModalRect(aXOffset + 63, aYOffset + 38, 176, 0, 10, p2);
		}
		drawTexturedModalRect(aXOffset + 60, aYOffset + 56, 176, 16, 20, 8);
		if(tile.type != null)
		{
			drawCondition(35, 21, tile.type);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;

		if(tile.getFluidStackInTank(0) != null)
			drawAreaTooltip(par1, par2, tile.getTank(0).getFluid().getLocalizedName() + " " + FleValue.format_L.format(tile.getFluidStackInTank(0).amount), xoffset + 60, yoffset + 56, 20, 8);
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		buttonList.add(new GuiIconButton(0, guiLeft + 97, guiTop + 42, ButtonSize.Standard, GuiIconButton.buttonLocate, 112, 0));
	}
	
	int tick = 0;
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if(tick > 100)
		{
			tile.onWork();
			sendToContainer(0, 0);
			tick = 0;
		}
		if(tick > 0)
		{
			--tick;
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		
		if(button.id == 0)
		{
			tick += 20;
		}
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}	
}