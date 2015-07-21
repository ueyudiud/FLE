package fla.core.tech;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import fla.api.util.FlaValue;
import fla.core.gui.base.GuiIconButton;
import fla.core.gui.base.GuiIconButton.ButtonSize;

public class GuiTech extends GuiScreen
{
	private static final ResourceLocation locate = new ResourceLocation("fla", "textures/gui/technology");
	/** The X size of the inventory window in pixels. */
    protected int xSize = 176;
    /** The Y size of the inventory window in pixels. */
    protected int ySize = 166;
    
	public GuiTech()
	{
		super();
	}

    public void drawScreen(int xMo, int yMo, float a)
    {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(locate);
		int xoffset = (width - xSize) / 2;
		int yoffset = (height - ySize) / 2;
		drawTexturedModalRect(xoffset, yoffset, 0, 0, xSize, ySize);
	}
    
    @Override
    public void initGui() 
    {
    	super.initGui();
		int xoffset = (width - xSize) / 2;
		int yoffset = (height - ySize) / 2;
		buttonList.add(new GuiIconButton(0, xoffset + 25, yoffset + 17, ButtonSize.Standard, new ResourceLocation(FlaValue.TEXT_FILE_NAME, "textures/gui/button.png"), 16, 0));
    }
}