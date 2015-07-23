package fla.core.tech;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import fla.api.tech.IPageGui;
import fla.api.tech.Page;

public abstract class FlaPage extends Page
{
	public abstract void drawOther(IPageGui gui);
	
	protected void drawItemStack(IPageGui gui, int x, int y, ItemStack stack)
	{
        String s = null;

        ItemStack itemstack = stack.copy();
        if (itemstack.stackSize > itemstack.getMaxStackSize())
        {
            s = EnumChatFormatting.YELLOW + "" + itemstack.getMaxStackSize();
        }
        else
        {
        	s = String.valueOf(itemstack.stackSize);
        }

        RenderItem itemRender = gui.getItemRender();
        
        itemRender.zLevel = 100.0F;

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        itemRender.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemstack, x, y);
        itemRender.renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemstack, x, y, s);

        itemRender.zLevel = 0.0F;
	}
	
	protected void drawIcon(IPageGui gui, int x, int y, IIcon icon)
	{
		gui.drawTexturedModelRectFromIcon(x, y, icon, icon.getIconWidth(), icon.getIconHeight());
	}
}