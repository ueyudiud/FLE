package fla.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.util.FlaValue;
import fla.core.Fla;
import fla.core.gui.base.GuiBase;
import fla.core.gui.base.GuiIconButton;
import fla.core.gui.base.GuiIconButton.ButtonSize;

@SideOnly(Side.CLIENT)
public class GuiWashing extends GuiBase
{
	protected static final ResourceLocation locate = new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/ore_washing.png");
	
	public GuiWashing(EntityPlayer player) 
	{
		super(new ContainerWashing(player.inventory));
	}
	
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		//drawAreaTooltip(par1, par2, "Push this button to washing items.", xoffset + 25, yoffset + 18, 16, 16);
	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		this.drawTexturedModalRect(xoffset + 53, yoffset + 39, 176, 0, 27, ((ContainerWashing)this.container).getWashPrograss(33));
		drawError(26, 55, ((ContainerWashing) container).type);
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 25, yoffset + 18, ButtonSize.Standard, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 0, 0));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		Fla.fla.nwm.get().initiateGuiButtonPress(this, container.player.player, 0, 0, 0, guibutton.id);
		((ContainerWashing)this.container).washItem();
		
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