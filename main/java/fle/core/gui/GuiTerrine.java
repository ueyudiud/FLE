package fle.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flapi.gui.GuiContainerBase;
import flapi.gui.GuiIconButton;
import flapi.gui.GuiIconButton.ButtonSize;
import flapi.util.FleValue;
import fle.core.te.argil.TileEntityTerrine;

@SideOnly(Side.CLIENT)
public class GuiTerrine extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/terrine.png");
	private TileEntityTerrine tile;
	
	public GuiTerrine(EntityPlayer player, TileEntityTerrine tile) 
	{
		super(new ContainerTerrine(player.inventory, tile));
		this.tile = tile;
	}

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition)
	{		
		drawCondition(107, 46, tile.getError());
		
		drawTexturedModalRect(aXOffset + 75, aYOffset + 32, 176, 0, 8, 30);
		
		switch(tile.mode)
		{
		case 0 :
		{
			if(tile.getTank(0).getFluidAmount() > 0)
			{
				drawTexturedModalRect(aXOffset + 88, aYOffset + 27, 176, 34, 18, 36);
			}
		}
		break;
		case 1 :
		{
			drawTexturedModalRect(aXOffset + 88, aYOffset + 27, 176, 72, 18, 36);
			if(tile.getProgress() > 0)
			{
				int i = (int) (tile.getProgress() * 34);
				drawFleRect(aXOffset + 107, aYOffset + 28 + 34 - i, 2, i, 0xC10900);
			}
		}
		default :;
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;

		if(tile.getFluidStackInTank(0) != null)
			drawAreaTooltip(par1, par2, tile.getFluidStackInTank(0).getLocalizedName() + " " + FleValue.format_L.format(tile.getTank(0).getFluidAmount()), xoffset + 75, yoffset + 32, 8, 30);
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 74, yoffset + 21, ButtonSize.Small, GuiIconButton.buttonLocate, 80, 8));
		buttonList.add(new GuiIconButton(1, xoffset + 64, yoffset + 53, ButtonSize.Small, GuiIconButton.buttonLocate, 88, 8));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		sendToContainer(1, guibutton.id);
		if(guibutton.id == 0) tile.getTank(0).drain(tile.getTank(0).getCapacity(), true);
		else if(guibutton.id == 1) tile.setClose();
		
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

	@Override
	public boolean hasCustomName()
	{
		return false;
	}
}