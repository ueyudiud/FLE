package fla.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.IFluidTank;
import fla.api.recipe.ErrorType;
import fla.api.util.FlaValue;
import fla.core.Fla;
import fla.core.gui.base.GuiBase;
import fla.core.gui.base.GuiIconButton;
import fla.core.gui.base.GuiIconButton.ButtonSize;
import fla.core.tileentity.argil.TileEntityTerrine;

public class GuiTerrine extends GuiBase
{
	private static final ResourceLocation locate = new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/terrine.png");
	private TileEntityTerrine tile;
	
	public GuiTerrine(EntityPlayer player, TileEntityTerrine tile) 
	{
		super(new ContainerTerrine(player.inventory, tile));
		this.tile = tile;
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		
		if(tile.getError() != ErrorType.DEFAULT)
		{
			drawError(107, 46, tile.getError());
		}
		
		drawFluid(75, 32, tile, 8, 30);
		mc.renderEngine.bindTexture(getResourceLocation());
		drawTexturedModalRect(xoffset + 75, yoffset + 32, 176, 0, 8, 30);
		if(tile.getFluidAmount() > 0)
		{
			drawTexturedModalRect(xoffset + 88, yoffset + 27, 176, 34, 18, 36);
		}
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 74, yoffset + 21, ButtonSize.Small, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 88, 8));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		Fla.fla.nwm.get().initiateGuiButtonPress(this, container.player.player, 0, 0, 0, guibutton.id);
		tile.drain(tile.getCapacity(), true);
		
		super.actionPerformed(guibutton);
	}

	@Override
	public String getName() 
	{
		return container.inv.getInventoryName();
	}

	@Override
	public ResourceLocation getResourceLocation() 
	{
		return locate;
	}
}