/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import fle.core.FLE;
import fle.core.tile.wooden.TELeverOilMill;
import nebula.client.NebulaTextureHandler;
import nebula.client.render.TESRBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRLeverOilMill extends TESRBase<TELeverOilMill>
{
	private TextureAtlasSprite handle;
	private TextureAtlasSprite rope;
	private TextureAtlasSprite rock;
	
	{
		NebulaTextureHandler.addIconLoader(loader-> {
			this.handle = loader.registerIcon("minecraft", "blocks/logs/oak_side");
			this.rope = loader.registerIcon(FLE.MODID, "blocks/iconset/linen");
			this.rock = loader.registerIcon("minecraft", "blocks/rock/stone/resource");
		});
	}
	
	@Override
	public void renderTileEntityAt(TELeverOilMill tile, double x, double y, double z, float partialTicks,
			int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GlStateManager.disableLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float angle = tile.getRotationAngle();
		//Render lever.
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0.4625F, 0.8F, 0.0625F);
			GL11.glRotatef(angle * 10 - 100, 1, 0, 0);
			GL11.glScalef(1.0F, 1.5F, 1.0F);
			GL11.glTranslatef(0.0F, -0.375F, 0.0F);
			renderCube(0, 0, 0, 0.0625, 1, 0.0625, this.handle);
			GlStateManager.disableBlend();
			GL11.glPopMatrix();
		}
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0.25F, 0.375F - MathHelper.sin(angle) * 0.09F, 0.25F);
			renderCube(0.24F, 0.1F, 0.24F, 0.26F, 0.5F, 0.26F, this.rope);
			renderCube(0, 0F, 0, 0.5F, 0.25F, 0.5F, this.rock);
			GL11.glPopMatrix();
		}
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
}