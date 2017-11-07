/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import farcore.data.M;
import farcore.data.MC;
import farcore.instances.MaterialTextureLoader;
import fle.core.FLE;
import fle.core.tile.wooden.TEStoneMill;
import nebula.client.NebulaTextureHandler;
import nebula.client.render.TESRBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRStoneMill extends TESRBase<TEStoneMill>
{
	private TextureAtlasSprite	top;
	private TextureAtlasSprite	side;
	private TextureAtlasSprite	bottom;
	
	{
		NebulaTextureHandler.addIconLoader(loader -> {
			this.top = loader.registerIcon(FLE.MODID, "blocks/machine/stone_mill_top");
			this.side = loader.registerIcon(FLE.MODID, "blocks/machine/stone_mill_side");
			this.bottom = loader.registerIcon(FLE.MODID, "blocks/machine/stone_mill_bottom");
		});
	}
	
	@Override
	public void renderTileEntityAt(TEStoneMill tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		// Render stone mill.
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
			if (tile != null) GL11.glRotatef(tile.getRotationAngle(), 0, -1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			
			this.helper.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL);
			renderYPos(0.125, 0.5, 0.125, 0.875, 0.75, 0.875, this.top);
			renderYNeg(0.125, 0.5, 0.125, 0.875, 0.75, 0.875, this.bottom);
			renderZPos(0.125, 0.5, 0.125, 0.875, 0.75, 0.875, this.side);
			renderZNeg(0.125, 0.5, 0.125, 0.875, 0.75, 0.875, this.side);
			renderXPos(0.125, 0.5, 0.125, 0.875, 0.75, 0.875, this.side);
			renderXNeg(0.125, 0.5, 0.125, 0.875, 0.75, 0.875, this.side);
			this.helper.draw();
			
			renderCube(0.875, 0.625, 0.5, 1.0, 0.6875, 0.5625, MaterialTextureLoader.getIcon(M.oak, MC.plankBlock));
			
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
}
