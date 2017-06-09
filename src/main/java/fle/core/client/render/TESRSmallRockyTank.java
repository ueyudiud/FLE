/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.client.render;

import org.lwjgl.opengl.GL11;

import farcore.data.EnumRockType;
import farcore.instances.MaterialTextureLoader;
import fle.core.tile.tanks.TESmallRockyTank;
import nebula.client.render.BrightnessUtil;
import nebula.client.render.ICoordableBrightnessProvider;
import nebula.client.render.TESRBase;
import nebula.common.util.Maths;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class TESRSmallRockyTank extends TESRBase<TESmallRockyTank>
{
	private static final float a = 0.25F;
	private static final float b = 0.75F;
	
	@Override
	public void renderTileEntityAt(TESmallRockyTank te, double x, double y, double z, float partialTicks,
			int destroyStage)
	{
		this.helper.setIconCoordScale(1.0F);
		GL11.glPushMatrix();
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GlStateManager.disableLighting();
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite icon1 = MaterialTextureLoader.getIcon(te.getMaterial(), EnumRockType.smoothed.condition, EnumRockType.smoothed.variant);
		TextureAtlasSprite icon2 = MaterialTextureLoader.getIcon(te.getMaterial(), EnumRockType.brick_compacted.condition, EnumRockType.brick_compacted.variant);
		//Render side and bottom.
		{
			this.renderDown = true;
			this.renderNorth = true;
			this.renderSouth = true;
			this.renderEast = true;
			this.renderWest = true;
			this.renderUp = false;
			ICoordableBrightnessProvider provider = ICoordableBrightnessProvider.wrap(te.world());
			renderCubeWithLight(0, 0, 0, 1, 1, 1,
					BrightnessUtil.instance(), provider, te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), icon2);
		}
		//		BrightnessUtil util = BrightnessUtil.instance();
		//		util.caculateBrightness(
		//				aoNN, aoON, aoPN, aoNO, aoOO, aoPO, aoNP, aoOP, aoPP,
		//				oNN, oON, oPN, oNO, oOO, oPO, oNP, oOP, oPP,
		//				bNN, bON, bPN, bNO, bOO, bPO, bNP, bOP, bPP);
		//Render top face.
		{
			this.helper.setIcon(icon1);
			this.helper
			.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL)
			.vertex_ptn(0, 1, 0, 0, 0, 0, 1, 0)
			.vertex_ptn(0, 1, a, 0, a, 0, 1, 0)
			.vertex_ptn(1, 1, a, 1, a, 0, 1, 0)
			.vertex_ptn(1, 1, 0, 1, 0, 0, 1, 0)
			.draw()
			.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL)
			.vertex_ptn(0, 1, b, 0, b, 0, 1, 0)
			.vertex_ptn(0, 1, 1, 0, 1, 0, 1, 0)
			.vertex_ptn(1, 1, 1, 1, 1, 0, 1, 0)
			.vertex_ptn(1, 1, b, 1, b, 0, 1, 0)
			.draw()
			.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL)
			.vertex_ptn(0, 1, a, 0, a, 0, 1, 0)
			.vertex_ptn(0, 1, b, 0, b, 0, 1, 0)
			.vertex_ptn(a, 1, b, a, b, 0, 1, 0)
			.vertex_ptn(a, 1, a, a, a, 0, 1, 0)
			.draw()
			.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_NORMAL)
			.vertex_ptn(b, 1, a, b, a, 0, 1, 0)
			.vertex_ptn(b, 1, b, b, b, 0, 1, 0)
			.vertex_ptn(1, 1, b, 1, b, 0, 1, 0)
			.vertex_ptn(1, 1, a, 1, a, 0, 1, 0)
			.draw();
			this.flip = true;
			this.renderDown = true;
			this.renderNorth = true;
			this.renderSouth = true;
			this.renderEast = true;
			this.renderWest = true;
			this.renderUp = false;
			renderCube(a, a, a, b, 1, b, icon1);
			this.flip = false;
		}
		//Render fluid.
		{
			GlStateManager.enableBlend();
			IFluidTank tank = te.getFluidTank();
			if (tank.getFluid() != null)
			{
				float height = Maths.lerp(a, 1 - 6.25E-2F, (float) tank.getFluidAmount() / (float) tank.getCapacity());
				renderFluidFace(a, a, b, b, height, height, height, height, tank.getFluid(), null);
			}
			GlStateManager.disableBlend();
		}
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
}