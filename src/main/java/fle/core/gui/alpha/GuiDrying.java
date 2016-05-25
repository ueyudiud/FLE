package fle.core.gui.alpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.gui.GuiBase;
import farcore.lib.gui.GuiIconButton;
import fle.core.container.alpha.ContainerDrying;
import fle.core.container.alpha.ContainerWashing;
import fle.core.tile.TileEntityDryingTable;
import fle.load.Icons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class GuiDrying extends GuiBase<TileEntityDryingTable>
{
	public GuiDrying(TileEntityDryingTable inventory, EntityPlayer player)
	{
		super(new ContainerDrying(inventory, player));
	}

	@Override
	protected void drawOther(int xOffset, int uOffset, int mouseXPosition, int mouseYPosition)
	{
		int s;
		for(int i = 0; i < 3; ++i)
		{
			s = inventory.getProgressScale(i, 25);
		    if (s > 0)
		    {
		    	drawTexturedModalRect(xoffset + 75, yoffset + 16 + 18 * i, 176, 0, i, 18);
		    }
		}
	}

	@Override
	public ResourceLocation getResourceLocation()
	{
		return new ResourceLocation("fle", "textures/gui/drying.png");
	}
}