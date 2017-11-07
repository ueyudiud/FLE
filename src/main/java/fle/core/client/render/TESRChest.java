/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import fle.core.tile.chest.TEChest;
import nebula.client.render.TESRBase;
import nebula.common.util.Maths;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public abstract class TESRChest<T extends TEChest> extends TESRBase<T>
{
	float minX, minY, minZ, midY, maxX, maxY, maxZ;
	
	protected TESRChest(float minX, float minY, float minZ, float midY, float maxX, float maxY, float maxZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.midY = midY;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	@Override
	public void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GlStateManager.enableDepth();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.depthMask(true);
		
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		// Render chest body.
		GL11.glTranslated(x + .5F, y, z + .5F);
		GL11.glRotatef(te.facing.getHorizontalAngle(), 0, -1, 0);
		GL11.glTranslatef(-.5F, 0, -.5F);
		;
		renderChestBody(te);
		
		// Render chest top.
		GL11.glPushMatrix();
		GL11.glTranslatef(0, this.midY, this.maxZ);
		GL11.glRotatef(Maths.lerp(te.prevLidAngle, te.lidAngle, partialTicks) * 60.0F, 1, 0, 0);
		GL11.glTranslatef(0, -this.midY, -this.maxZ);
		renderChestTop(te);
		
		GL11.glPopMatrix();
		
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
	
	protected abstract void renderChestBody(T te);
	
	protected abstract void renderChestTop(T te);
}
