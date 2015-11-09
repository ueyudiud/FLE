package fle.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.api.gui.GuiError;
import fle.core.energy.FleThermalNet;
import fle.core.te.TileEntityDryingTable;
import fle.core.util.ColorUtil;

@SideOnly(Side.CLIENT)
public class GuiDryingTable extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/drying_table.png");
	
	private TileEntityDryingTable tile;
	
	public GuiDryingTable(EntityPlayer player, TileEntityDryingTable tile) 
	{
		super(new ContainerDryingTable(player.inventory, tile));
		this.tile = tile;
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

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{
		drawTexturedModalRect(aXOffset + 88, aYOffset + 33, 176, 52, tile.getRecipeProgressBar(26), 19);
		if(tile.type != GuiError.DEFAULT) drawCondition(92, 53, tile.type);
		int temp = FLE.fle.getThermalNet().getEnvironmentTemperature(tile.getBlockPos());
		int a = (int) (temp / 400D * 52);
		drawRect(xoffset + 25, yoffset + 17 + 52 - a, xoffset + 28, yoffset + 17 + 52, ColorUtil.getColorWithTdWd(temp, tile.getWaterLevel() / 415D));
	}

	@Override
	public boolean hasCustomName()
	{
		return tile.hasCustomInventoryName();
	}
}