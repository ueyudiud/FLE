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
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		((ContainerWashing) container).washItem();
		FLE.fle.getNetworkHandler().sendToServer(new CoderGuiUpdate((byte) 1, 0));
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