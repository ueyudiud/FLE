package fla.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import fla.api.util.FlaValue;
import fla.core.gui.base.GuiBase;
import fla.core.tileentity.TileEntityDryingTable;

public class GuiDryingTable extends GuiBase
{
	private static final ResourceLocation locate = new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/drying_table.png");
	
	private TileEntityDryingTable tile;
	
	public GuiDryingTable(EntityPlayer player, TileEntityDryingTable tile) 
	{
		super(new ContainerDryingTable(player.inventory, tile));
		this.tile = tile;
	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		double a1 = tile.getWaterLevel() / 100D;
		double a2 = tile.getTempretureLevel();
		drawTexturedModalRect(xoffset + 25, yoffset + 17 + (int) (52 * (1 - a2)), 176 + (int) (49 * a1), (int) (52 * (1 - a2)), 3, (int) (52 * a2));
		drawTexturedModalRect(xoffset + 88, yoffset + 33, 176, 52, tile.getRecipeProgressBar(26), 19);
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