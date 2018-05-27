/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import farcore.lib.material.Mat;
import fle.api.mat.StackContainer;
import fle.core.FLE;
import fle.core.tile.TEDirtMixture;
import nebula.client.NebulaTextureHandler;
import nebula.client.render.TESRBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRDirtMixture extends TESRBase<TEDirtMixture>
{
	private TextureAtlasSprite dirt;
	
	{
		NebulaTextureHandler.addIconLoader(loader -> {
			this.dirt = loader.registerIcon(FLE.MODID, "blocks/iconset/dirtmixture_dirt");
		});
	}
	
	@Override
	public void renderTileEntityAt(TEDirtMixture te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GL11.glTranslated(x, y, z);
		this.renderUp = true;
		this.renderDown = this.renderEast = this.renderNorth = this.renderWest = this.renderSouth = false;
		StackContainer<Mat> container = te.getStacks();
		if (!container.isEmpty())
		{
			double d1 = te.getGroundPercentage();
			if (d1 > 1E-2F)
			{
				colori(te.getGroundRGB());
				GL11.glColor4f(this.red, this.green, this.blue, 1.0F);
				renderCube(0, 0, 0, 1, d1, 1, this.dirt);
			}
		}
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
}
