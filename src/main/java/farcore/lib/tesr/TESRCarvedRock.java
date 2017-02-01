package farcore.lib.tesr;

import org.lwjgl.opengl.GL11;

import farcore.lib.tile.instance.TECustomCarvedStone;
import nebula.client.render.BrightnessUtil;
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
	private static final float off = 0.25F;
	private BrightnessUtil util = BrightnessUtil.instance();

	@Override
	public void renderTileEntityAt(TECustomCarvedStone tile, double x, double y, double z, float partialTicks,
			int destroyStage)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		TextureAtlasSprite icon = tile.getIcon();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.disableLighting();
		int count = 0;
		for(int i = 0; i < 4; ++i)
		{
			for(int j = 0; j < 4; ++j)
			{
				for(int k = 0; k < 4; ++k)
				{
					float x1 = 0.25F * i;
					float y1 = 0.25F * j;
					float z1 = 0.25F * k;
					float x2 = 0.25F * (i + 1);
					float y2 = 0.25F * (j + 1);
					float z2 = 0.25F * (k + 1);
					renderNorth = tile.shouldSideRender(i, j, k, Direction.N);
					renderSouth = tile.shouldSideRender(i, j, k, Direction.S);
					renderWest  = tile.shouldSideRender(i, j, k, Direction.W);
					renderEast  = tile.shouldSideRender(i, j, k, Direction.E);
					renderDown  = tile.shouldSideRender(i, j, k, Direction.D);
					renderUp    = tile.shouldSideRender(i, j, k, Direction.U);
					renderCubeWithLight(x1, y1, z1, x2, y2, z2, util, tile, i, j, k, icon);
				}
			}
		}
		GL11.glPopMatrix();
		GlStateManager.enableLighting();
	}
}