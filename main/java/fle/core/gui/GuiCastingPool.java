package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.core.te.TileEntityCastingPool;

@SideOnly(Side.CLIENT)
public class GuiCastingPool extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/casing_pool.png");
	
	TileEntityCastingPool tile;
	
	public GuiCastingPool(EntityPlayer aPlayer, TileEntityCastingPool aTile)
	{
		super(new ContainerCastingPool(aPlayer.inventory, aTile));
		tile = aTile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		int progress = (int) (tile.getProgress() * 29);
		if(progress != 0)
		{
			drawTexturedModalRect(aXOffset + 122, aYOffset + 15, 176, 0, 26, progress);
		}
		drawFluid(44, 13, tile.getTank(0), 8, 60);
		drawTexturedModalRect(aXOffset + 44, aYOffset + 13, 176, 33, 8, 60);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;

		if(tile.getFluidStackInTank(0) != null)
			drawAreaTooltip(par1, par2, tile.getTank(0).getFluid().getLocalizedName() + " " + FleValue.format_L.format(tile.getFluidStackInTank(0).amount), xoffset + 44, yoffset + 13, 8, 60);
	}

	@Override
	public boolean hasCustomName()
	{
		return tile.hasCustomInventoryName();
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