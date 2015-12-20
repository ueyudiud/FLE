package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiContainerBase;
import flapi.util.FleValue;
import fle.core.te.tank.TileEntityMultiTankSoak;

@SideOnly(Side.CLIENT)
public class GuiMTSoak extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/tank_i.png");
	
	TileEntityMultiTankSoak tile;
	
	public GuiMTSoak(EntityPlayer player, TileEntityMultiTankSoak tile)
	{
		super(new ContainerMTSoak(player.inventory, tile));
		this.tile = tile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		drawTexturedModalRect(aXOffset + 41, aYOffset + 18, 176, 0, 50, 50);
		for(int i = 0; i < 2; ++i)
			for(int j = 0; j < 2; ++j)
			{
				int length = tile.getProgress(i + j * 2, 16);
				if(length > 0)
				{
					drawFleRect(aXOffset + 101 + 18 * i, aYOffset + 41 + 21 * j, length, 1, 0x5B7F00);
				}
			}
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return locate;
	}
}