package fla.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.recipe.ErrorType;
import fla.api.util.FlaValue;
import fla.core.gui.base.GuiBase;
import fla.core.tileentity.TileEntityDryingTable;
import fla.core.util.ColorUtil;
import fla.core.util.HeatUtil;
import fla.core.world.HeatManager;

@SideOnly(Side.CLIENT)
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
		drawTexturedModalRect(xoffset + 88, yoffset + 33, 176, 52, tile.getRecipeProgressBar(26), 19);
		if(tile.type != ErrorType.DEFAULT) drawError(92, 53, tile.type);
		int a = (int) (HeatUtil.getFTempretureToInteger(tile.getTempretureLevel()) / 400D * 52);
		drawR(xoffset + 25, yoffset + 17 + 52 - a, 3, a, ColorUtil.getColorWithTdWd(tile.getTempretureLevel(), tile.getWaterLevel() / 100D));
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