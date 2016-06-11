package fle.core.gui.alpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.lib.gui.GuiBase;
import farcore.lib.gui.GuiIconButton;
import fle.core.container.alpha.ContainerTerrine;
import fle.core.tile.TileEntityTerrine;
import fle.load.Icons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiTerrine extends GuiBase<TileEntityTerrine>
{
	public GuiTerrine(TileEntityTerrine inventory, EntityPlayer player) 
	{
		super(new ContainerTerrine(inventory, player));
	}

	@Override
	protected void drawOther(int xOffset, int yOffset, int mouseXPosition,
			int mouseYPosition)
	{
		drawTexturedModalRect(xOffset + 75, yoffset + 32, 176, 0, 8, 30);
		
		switch(inventory.getMode())
		{
		case 0 :
		{
			
		}
		break;
		case 1 :
		{
			drawTexturedModalRect(xOffset + 88, yoffset + 27, 176, 34, 18, 36);
//			drawTexturedModalRect(aXOffset + 88, aYOffset + 27, 176, 72, 18, 36);
//			if(inventory.getProgress() > 0)
//			{
//				int i = (int) (inventory.getProgress() * 34);
//				drawFleRect(aXOffset + 107, aYOffset + 28 + 34 - i, 2, i, 0xC10900);
//			}
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

		if(inventory.getFluidStackInTank(0) != null)
		{
			drawAreaTooltip(par1, par2, inventory.getFluidStackInTank(0).getLocalizedName() + " " + inventory.getTank(0).getFluidAmount() + "L", xoffset + 75, yoffset + 32, 8, 30);
		}
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 74, yoffset + 21, 10, null)
				{
			@Override
			public void drawButton(Minecraft minecraft, int i, int j)
			{
				switch (inventory.getMode())
				{
				case 0 :
					icon = Icons.green;
					break;
				case 1 :
					icon = Icons.blue;
					break;
				}
				super.drawButton(minecraft, i, j);
			}
				});
//		buttonList.add(new GuiIconButton(1, xoffset + 64, yoffset + 53, ButtonSize.Small, GuiIconButton.buttonLocate, 88, 8));
	}
//	
	protected void actionPerformed(GuiButton guibutton)
	{
		sendToContainer(1, guibutton.id);
		super.actionPerformed(guibutton);
	}

	@Override
	public ResourceLocation getResourceLocation() 
	{
		return new ResourceLocation("fle", "textures/gui/terrine.png");
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}
}