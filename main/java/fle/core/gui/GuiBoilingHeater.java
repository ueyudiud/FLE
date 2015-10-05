package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
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
		drawFluid(66, 15, tile.getTank(0), 8, 20);
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
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}