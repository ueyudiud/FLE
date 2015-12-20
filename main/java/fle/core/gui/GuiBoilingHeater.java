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
import fle.core.te.argil.TileEntityBoilingHeater;

@SideOnly(Side.CLIENT)
public class GuiBoilingHeater extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/boiling_heater.png");
	
	protected TileEntityBoilingHeater tile;
	
	public GuiBoilingHeater(EntityPlayer player, TileEntityBoilingHeater tile)
	{
		super(new ContainerBoilingHeater(player.inventory, tile));
		this.tile = tile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		int p = tile.getBurnProgress(14);
		if(p > 0)
		{
			drawTexturedModalRect(aXOffset + 81, aYOffset + 41 + 14 - p, 176, 14 - p, 14, p);
		}
		drawTexturedModalRect(aXOffset + 66, aYOffset + 15, 176, 14, 8, 20);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;

		if(tile.getFluidStackInTank(0) != null)
			drawAreaTooltip(par1, par2, tile.getFluidStackInTank(0).getLocalizedName() + " " + FleValue.format_L.format(tile.getFluidStackInTank(0).amount), xoffset + 66, yoffset + 15, 8, 20);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 65, yoffset + 42, ButtonSize.Small, GuiIconButton.buttonLocate, 96, 8));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		if(button.id == 0)
		{
			sendToContainer(0, button.id);
			tile.drainTank(0, tile.getTank(0).getCapacity(), true);
			tile.resetRecipe();
		}
	}
	
	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}