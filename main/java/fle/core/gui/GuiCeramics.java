package fle.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.FLE;
import fle.api.FleValue;
import fle.api.gui.GuiContainerBase;
import fle.api.gui.GuiIconButton;
import fle.api.gui.GuiIconButton.ButtonSize;
import fle.core.net.FleGuiPacket;

@SideOnly(Side.CLIENT)
public class GuiCeramics extends GuiContainerBase
{
	private static final ResourceLocation locate = new ResourceLocation(FleValue.TEXTURE_FILE , "textures/gui/clay_model.png");
	
	public GuiCeramics(World world, int x, int y, int z, EntityPlayer player) 
	{
		super(new ContainerCeramics(world, x, y, z, player.inventory));
	}
	
	@Override
	public void initGui() 
	{
		super.initGui();
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		int i = 0;
		for(; i < 5; ++i)
		{
			buttonList.add(new GuiIconButton(i, xoffset + 21, yoffset + 16 + 11 * i, ButtonSize.Small, new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/button.png"), 72, 0));
		}
		for(; i < 10; ++i)
		{
			buttonList.add(new GuiIconButton(i, xoffset + 80, yoffset + 16 + 11 * (i - 5), ButtonSize.Small, new ResourceLocation(FleValue.TEXTURE_FILE, "textures/gui/button.png"), 64, 0));
		}
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		FLE.fle.getNetworkHandler().sendTo(new FleGuiPacket((byte) 1, guibutton.id));
		((ContainerCeramics) container).onReceive((byte) 1, guibutton.id);
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
		float[] fs = ((ContainerCeramics) container).fs;
		int i = 0;
		for(; i < 5; ++i)
		{
			int i0 = (int) (fs[i] * 22);
			drawTexturedModalRect(aXOffset + 34 + (22 - i0), aYOffset + 15 + 11 * i, 176 + (22 - i0), -1 + i * 11, i0, 11);
		}
		for(i = 0; i < 5; ++i)
		{
			int i0 = (int) (fs[i + 5] * 22);
			drawTexturedModalRect(aXOffset + 55, aYOffset + 15 + 11 * i, 198, -1 + i * 11, i0, 11);
		}
		drawTexturedModalRect(aXOffset + 34, aYOffset + 16, 176, 54, 43, 54);
		
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}
}