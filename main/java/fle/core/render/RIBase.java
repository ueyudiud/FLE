package fle.core.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

import org.lwjgl.opengl.GL11;

public abstract class RIBase
{
	protected ResourceLocation locate;
	protected ItemRenderType type;
	protected ItemStack aStack;
	protected IIcon icon;
	protected double zLevel;
	protected double rgb_r = 1.0F;
	protected double rgb_g = 1.0F;
	protected double rgb_b = 1.0F;

	public void render(ItemRenderType aType, ItemStack stack, Object[] data) 
	{
		type = aType;
		aStack = stack;
		icon = aStack.getIconIndex();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		if (type.equals(ItemRenderType.ENTITY))
	    {
			GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
			GL11.glRotated(90.0D, 0.0D, 1.0D, 0.0D);
			GL11.glTranslated(-0.5D, -0.6D, 0.0D);
	    }
	    else if (type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
	    {
	    	GL11.glTranslated(1.0D, 1.0D, 0.0D);
	    	GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
	    }
	    else if (type.equals(ItemRenderType.EQUIPPED))
	    {
	    	GL11.glRotated(180.0D, 0.0D, 0.0D, 1.0D);
	    	GL11.glTranslated(-1.0D, -1.0D, 0.0D);
	    }
		renderItem();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
	}

	public abstract void renderItem();
	
	protected void bindTexture(ResourceLocation aLocate)
	{
		locate = aLocate;
		Minecraft.getMinecraft().renderEngine.bindTexture(locate);
	}
	
	protected void renderIcon(IIcon icon)
	{
		this.icon = icon;
		GL11.glColor4d(rgb_r, rgb_g, rgb_b, 1.0D);
		if(type == ItemRenderType.INVENTORY)
		{
			renderIcon(16.0D, 0.0D, 0.0F, 0.0F, -1.0F);
		}
		else
		{
			renderIcon(1.0D, -0.0078125D, 0.0F, 0.0F, 1.0F);
			renderIcon(1.0D, -0.0625D, 0.0F, 0.0F, -1.0F);
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	protected void renderIcon(double size, double z, float nx, float ny, float nz)
	{
		renderIcon(0.0D, 0.0D, size, size, z, nx, ny, nz);
	}
	protected void renderIcon(double xStart, double yStart, double xEnd, double yEnd, double zLevel, float nx, float ny, float nz)
	{
		renderIcon(xStart, yStart, xEnd, yEnd, zLevel, nx, ny, nz, 0, 0, 16, 16);
	}
	private void renderIcon(double xStart, double yStart, double xEnd, double yEnd, double zLevel, float nx, float ny, float nz, int uStart, int uEnd, int vStart, int vEnd)
	{
		if (icon == null)
		{
			icon = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(locate)).getAtlasSprite("missingno");
	    }
	    Tessellator tessellator = Tessellator.instance;
	    
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(nx, ny, nz);
	    double u1 = (icon.getMaxU() - icon.getMinU()) * (double) uStart / 16.0D + icon.getMinU();
	    double u2 = (icon.getMaxU() - icon.getMinU()) * (double) uEnd / 16.0D + icon.getMinU();
	    double v1 = (icon.getMaxV() - icon.getMinV()) * (double) vStart / 16.0D + icon.getMinV();
	    double v2 = (icon.getMaxV() - icon.getMinV()) * (double) vEnd / 16.0D + icon.getMinV();
	    if (nz > 0.0F)
	    {
	    	tessellator.addVertexWithUV(xStart, yStart, zLevel, u1, v1);
	    	tessellator.addVertexWithUV(xEnd, yStart, zLevel, u2, v1);
	    	tessellator.addVertexWithUV(xEnd, yEnd, zLevel, u2, v2);
	    	tessellator.addVertexWithUV(xStart, yEnd, zLevel, u1, v2);
	    }
	    else
	    {
	    	tessellator.addVertexWithUV(xStart, yEnd, zLevel, u1, v2);
	    	tessellator.addVertexWithUV(xEnd, yEnd, zLevel, u2, v2);
	    	tessellator.addVertexWithUV(xEnd, yStart, zLevel, u2, v1);
	    	tessellator.addVertexWithUV(xStart, yStart, zLevel, u1, v1);
	    }
	    tessellator.draw();
	}
}