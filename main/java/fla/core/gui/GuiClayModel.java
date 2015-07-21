package fla.core.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fla.api.util.FlaValue;
import fla.core.Fla;
import fla.core.gui.base.GuiBase;
import fla.core.gui.base.GuiIconButton;
import fla.core.gui.base.GuiIconButton.ButtonSize;

@SideOnly(Side.CLIENT)
public class GuiClayModel extends GuiBase
{
	private static final ResourceLocation locate = new ResourceLocation(FlaValue.TEXT_FILE_NAME , "textures/gui/clay_model.png");
	
	public GuiClayModel(World world, int x, int y, int z, EntityPlayer player) 
	{
		super(new ContainerClayModel(world, x, y, z, player.inventory));
	}

	protected void drawGuiContainerBackgroundLayer(float f, int x, int y)
	{
		super.drawGuiContainerBackgroundLayer(f, x, y);
		xoffset = (width - xSize) / 2;
		yoffset = (height - ySize) / 2;
		float[] fs = ((ContainerClayModel) container).fs;
		int i = 0;
		for(; i < 5; ++i)
		{
			int i0 = (int) (fs[i] * 21);
			drawTexturedModalRect(xoffset + 34 + (21 - i0), yoffset + 15 + 11 * i, 176 + (21 - i0), -1 + i * 11, i0, 11);
		}
		for(i = 0; i < 5; ++i)
		{
			int i0 = (int) (fs[i + 5] * 21);
			drawTexturedModalRect(xoffset + 56, yoffset + 15 + 11 * i, 198, -1 + i * 11, i0, 11);
		}
		drawTexturedModalRect(xoffset + 55, yoffset + 16, 197, 0, 1, 54);
		drawTexturedModalRect(xoffset + 34, yoffset + 16, 176, 54, 43, 54);
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
			buttonList.add(new GuiIconButton(i, xoffset + 21, yoffset + 16 + 11 * i, ButtonSize.Small, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 72, 0));
		}
		for(; i < 10; ++i)
		{
			buttonList.add(new GuiIconButton(i, xoffset + 80, yoffset + 16 + 11 * (i - 5), ButtonSize.Small, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 64, 0));
		}
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		Fla.fla.nwm.get().initiateGuiButtonPress(this, container.player.player, 0, 0, 0, guibutton.id);
		((ContainerClayModel) this.container).onPacketData(0, 0, 0, (byte) 1, (short) guibutton.id);;
		
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