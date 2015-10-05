package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.core.te.TileEntitySifter;

@SideOnly(Side.CLIENT)
public class GuiSifter extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/sifter.png");
	private TileEntitySifter tile;
	
	public GuiSifter(EntityPlayer aPlayer, TileEntitySifter aTile)
	{
		super(new ContainerShifter(aPlayer.inventory, aTile));
		tile = aTile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		int l = tile.getRecipeProgress(9);
		if(l > 0)
			drawTexturedModalRect(aXOffset + 65, aYOffset + 39, 176, 0, 18, l);
		drawSolid(66, 23, tile.getSolidTank(0), 16, 16);
		drawSolid(66, 49, tile.getSolidTank(1), 16, 16);
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}