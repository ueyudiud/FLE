/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.tesr;

import org.lwjgl.opengl.GL11;

import farcore.lib.tile.instance.TECustomCarvedStone;
import nebula.client.render.BrightnessProviderQuarterCoord;
import nebula.client.render.TESRBase;
import nebula.common.util.Direction;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRCarvedRock extends TESRBase<TECustomCarvedStone>
{
	private static final float off = 0x1.0p-2F;
	
	private BrightnessProviderQuarterCoord util = new BrightnessProviderQuarterCoord();
	
	@Override
	public void renderTileEntityAt(TECustomCarvedStone tile, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		TextureAtlasSprite icon = tile.getIcon();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.disableLighting();
		for (int i = 0; i < 4; ++i)
		{
			for (int j = 0; j < 4; ++j)
			{
				for (int k = 0; k < 4; ++k)
				{
					float x1 = off * i;
					float y1 = off * j;
					float z1 = off * k;
					float x2 = off * (i + 1);
					float y2 = off * (j + 1);
					float z2 = off * (k + 1);
					this.renderNorth = tile.shouldSideRender(i, j, k, Direction.N);
					this.renderSouth = tile.shouldSideRender(i, j, k, Direction.S);
					this.renderWest = tile.shouldSideRender(i, j, k, Direction.W);
					this.renderEast = tile.shouldSideRender(i, j, k, Direction.E);
					this.renderDown = tile.shouldSideRender(i, j, k, Direction.D);
					this.renderUp = tile.shouldSideRender(i, j, k, Direction.U);
					this.util.initalizeValue(tile);
					renderCubeWithLight(x1, y1, z1, x2, y2, z2, this.util, tile, i, j, k, icon);
				}
			}
		}
		GL11.glPopMatrix();
		GlStateManager.enableLighting();
	}
}
