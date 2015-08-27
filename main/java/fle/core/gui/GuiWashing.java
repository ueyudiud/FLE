package fle.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.api.gui.GuiIconButton;
import fle.api.gui.GuiIconButton.ButtonSize;
import fle.api.net.FlePackets.CoderGuiUpdate;

@SideOnly(Side.CLIENT)
public class GuiWashing extends GuiContainerBase
{
	protected static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/ore_washing.png");
	
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
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 25, yoffset + 18, ButtonSize.Standard, GuiIconButton.buttonLocate, 0, 0));
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		FLE.fle.getNetworkHandler().sendToServer(new CoderGuiUpdate((byte) 1, guibutton.id));
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

	@Override
	protected void drawOther(int aXOffset, int aYOffset, int aMouseXPosition,
			int aMouseYPosition) 
	{
		drawTexturedModalRect(aXOffset + 53, aYOffset + 39, 176, 0, 27, ((ContainerWashing)this.container).getWashPrograss(33));
		drawCondition(26, 55, ((ContainerWashing) container).type);
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}
}