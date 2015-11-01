package fle.core.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import fle.api.FleValue;
import fle.api.gui.ContainerBase;
import fle.api.gui.GuiContainerBase;
import fle.core.te.tank.TileEntityMultiTankInterface;

@SideOnly(Side.CLIENT)
public class GuiMTInterface extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/tank_ii.png");
	
	private TileEntityMultiTankInterface tile;
	
	public GuiMTInterface(EntityPlayer aPlayer, TileEntityMultiTankInterface aTile)
	{
		super(new ContainerMTInterface(aPlayer.inventory, aTile));
		tile = aTile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		drawFluid(41, 18, tile.getMainTank(), 50, 50);
		drawTexturedModalRect(aXOffset + 41, aYOffset + 18, 176, 0, 50, 50);
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}