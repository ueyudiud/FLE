package fle.core.gui.alpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.gui.GuiBase;
import fle.core.container.alpha.ContainerCampfire;
import fle.core.tile.TileEntityCampfire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCampfire extends GuiBase<TileEntityCampfire>
{
	public GuiCampfire(TileEntityCampfire tile, EntityPlayer player)
	{
		super(new ContainerCampfire(tile, player), 222);
	}
	
	@Override
	protected void drawOther(int xOffset, int yOffset, int mouseXPosition, int mouseYPosition)
	{
		if(inventory.isBurning())
		{
			int scale = inventory.getBurningProgress(42);
			drawTexturedModalRect(xOffset + 94, yOffset + 44 + 42 - scale, 176, 42 - scale, 38, scale);
		}
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return new ResourceLocation("fle", "textures/gui/campfire.png");
	}
}