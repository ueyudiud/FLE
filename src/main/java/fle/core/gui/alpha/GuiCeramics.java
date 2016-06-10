package fle.core.gui.alpha;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.inventory.Inventory;
import farcore.lib.gui.GuiBase;
import farcore.lib.gui.GuiIconButton;
import fle.core.FLE;
import fle.core.container.alpha.ContainerCeramics;
import fle.load.Icons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCeramics extends GuiBase<Inventory>
{	
	public GuiCeramics(EntityPlayer player) 
	{
		super(new ContainerCeramics(player));
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
			buttonList.add(new GuiIconButton(i, xoffset + 21, yoffset + 16 + 11 * i, 10, Icons.next));
		}
		for(; i < 10; ++i)
		{
			buttonList.add(new GuiIconButton(i, xoffset + 80, yoffset + 16 + 11 * (i - 5), 10, Icons.last));
		}
	}
	
	protected void actionPerformed(GuiButton guibutton)
	{
		sendToContainer(1, guibutton.id);
		((ContainerCeramics) container).onActive(1, guibutton.id);
		super.actionPerformed(guibutton);
	}

	@Override
	public String getName() 
	{
		return container.inventory.getInventoryName();
	}

	@Override
	public ResourceLocation getResourceLocation() 
	{
		return new ResourceLocation("fle" , "textures/gui/clay_model.png");
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
			drawTexturedModalRect(aXOffset + 56, aYOffset + 15 + 11 * i, 198, -1 + i * 11, i0, 11);
		}
		drawTexturedModalRect(aXOffset + 34, aYOffset + 16, 176, 54, 43, 54);
		
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}
}