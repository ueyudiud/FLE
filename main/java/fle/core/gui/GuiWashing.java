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
import fle.FLE;
import fle.core.net.FleGuiPacket;

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
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 72, yoffset + 18, ButtonSize.Standard, GuiIconButton.buttonLocate, 0, 0));
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		super.actionPerformed(button);
		((ContainerWashing) container).washItem();
		FLE.fle.getNetworkHandler().sendToServer(new FleGuiPacket((byte) 1, 0));
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
		if(((ContainerWashing) container).isWashing())
		{
			drawTexturedModalRect(aXOffset + 54, aYOffset + 36, 176, 0, 27, 11);
			drawTexturedModalRect(aXOffset + 55, aYOffset + 52, 176, 11, 27, ((ContainerWashing)this.container).getWashPrograss(22));
		}
		drawCondition(26, 55, ((ContainerWashing) container).type);
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}
}