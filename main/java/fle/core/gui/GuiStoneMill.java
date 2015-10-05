package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.core.te.TileEntityStoneMill;

@SideOnly(Side.CLIENT)
public class GuiStoneMill extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/stone_mill.png");
	
	TileEntityStoneMill tile;
	
	public GuiStoneMill(EntityPlayer aPlayer, TileEntityStoneMill aTile)
	{
		super(new ContainerStoneMill(aPlayer.inventory, aTile));
		tile = aTile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		int p1 = (int) (20 * tile.getEnergyContain());
		int p2 = (int) tile.getProgress(68);
		if(p1 > 0)
		{
			drawTexturedModalRect(aXOffset + 82, aYOffset + 17, 176, 0, p1, 21);
		}
		if(p2 > 0)
		{
			drawTexturedModalRect(aXOffset + 49, aYOffset + 39, 0, 166, p2, 10);
		}
		drawSolid(73, 52, tile.getSolidTank(), 16, 16);
		if(tile.type != null)
		{
			drawCondition(111, 21, tile.type);
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;

		if(tile.getFluidStackInTank(0) != null)
			drawAreaTooltip(par1, par2, tile.getSolidTank().get().getLocalizedName() + " " + FleValue.format_L.format(tile.getSolidTank(0).getStack().getSize()), xoffset + 73, yoffset + 52, 16, 16);
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}	
}