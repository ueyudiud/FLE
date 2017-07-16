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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRLeverOilMill extends TESRBase<TELeverOilMill>
{
	private TextureAtlasSprite handle;
	
	{
		NebulaTextureHandler.addIconLoader(loader-> {
			this.handle = loader.registerIcon(FLE.MODID, "blocks/logs/oak_side");
		});
	}
	
	@Override
	public void renderTileEntityAt(TELeverOilMill tile, double x, double y, double z, float partialTicks,
			int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GlStateManager.disableLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		//Render lever.
		{
			GL11.glTranslatef(0.5F, 0.8F, 0.0625F);
			GL11.glRotatef((float) (tile.getRotationAngle() * 10 - 100), 1, 0, 0);
			GL11.glScalef(1.0F, 1.5F, 1.0F);
			GL11.glTranslatef(0.0F, -0.375F, 0.0F);
			renderCube(0, 0, 0, 0.0625, 1, 0.0625, this.handle);
			GlStateManager.disableBlend();
		}
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
}