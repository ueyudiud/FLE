package farcore.lib.tesr;

import org.lwjgl.opengl.GL11;

import farcore.lib.render.RenderHelper;
import farcore.lib.util.Direction;
import farcore.util.U.Maths;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRBase<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
	private static final float UP_LIGHT_MULTIPLER = 1.0F;
	private static final float DOWN_LIGHT_MULTIPLER = 0.5F;
	private static final float X_LIGHT_MULTIPLER = 0.6F;
	private static final float Z_LIGHT_MULTIPLER = 0.8F;
	
	protected boolean renderUp = true;
	protected boolean renderDown = true;
	protected boolean renderNorth = true;
	protected boolean renderSouth = true;
	protected boolean renderEast = true;
	protected boolean renderWest = true;
	protected boolean enableColor = true;
	protected boolean diffuseLight = true;
	protected RenderHelper helper = RenderHelper.instance;
	protected float red = 1.0F;
	protected float green = 1.0F;
	protected float blue = 1.0F;
	protected float alpha = 1.0F;
	protected float ao = 1.0F;
	
	protected TextureAtlasSprite getTexture(IBlockState state)
	{
		return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getTexture(state);
	}

	protected TextureAtlasSprite getTexture(ResourceLocation location)
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
	}

	protected void resetColor()
	{
		colorV(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	protected void colorV(float r, float g, float b, float a)
	{
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}
	
	protected void renderCubeWithLight(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			BrightnessUtil util,
			ICoordableBrightnessProvider provider, int i, int j, int k,
			TextureAtlasSprite icon)
	{
		float u1, u2;
		float v1, v2;
		if(renderUp)
		{
			u1 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) x1);
			u2 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) x2);
			v1 = Maths.lerp(icon.getMinV(), icon.getMaxV(), (float) z1);
			v2 = Maths.lerp(icon.getMinV(), icon.getMaxV(), (float) z2);
			util.caculateBrightness(provider, i, j, k, Direction.U);
			ao = diffuseLight ? UP_LIGHT_MULTIPLER : 1.0F;
			faceWithLight(
					x2, y2, z2,
					x2, y2, z1,
					x1, y2, z1,
					x1, y2, z2,
					u2, v2,
					u2, v1,
					u1, v1,
					u1, v2, util);
		}
		if(renderDown)
		{
			u1 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) x1);
			u2 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) x2);
			v1 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) z1);
			v2 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) z2);
			util.caculateBrightness(provider, i, j, k, Direction.D);
			ao = diffuseLight ? DOWN_LIGHT_MULTIPLER : 1.0F;
			faceWithLight(
					x2, y1, z1,
					x2, y1, z2,
					x1, y1, z2,
					x1, y1, z1,
					u2, v1,
					u2, v2,
					u1, v2,
					u1, v1, util);
		}
		if(renderSouth)
		{
			u1 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) x1);
			u2 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) x2);
			v1 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y1);
			v2 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.S);
			ao = diffuseLight ? Z_LIGHT_MULTIPLER : 1.0F;
			faceWithLight(
					x1, y2, z2,
					x1, y1, z2,
					x2, y1, z2,
					x2, y2, z2,
					u1, v2,
					u1, v1,
					u2, v1,
					u2, v2, util);
		}
		if(renderNorth)
		{
			u1 = Maths.lerp(icon.getMinU(), icon.getMaxU(), 1F - (float) x1);
			u2 = Maths.lerp(icon.getMinU(), icon.getMaxU(), 1F - (float) x2);
			v1 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y1);
			v2 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.N);
			ao = diffuseLight ? Z_LIGHT_MULTIPLER : 1.0F;
			faceWithLight(
					x2, y2, z1,
					x2, y1, z1,
					x1, y1, z1,
					x1, y2, z1,
					u2, v2,
					u2, v1,
					u1, v1,
					u1, v2, util);
		}
		if(renderEast)
		{
			u1 = Maths.lerp(icon.getMinU(), icon.getMaxU(), 1F - (float) z1);
			u2 = Maths.lerp(icon.getMinU(), icon.getMaxU(), 1F - (float) z2);
			v1 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y1);
			v2 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.E);
			ao = diffuseLight ? X_LIGHT_MULTIPLER : 1.0F;
			faceWithLight(
					x2, y2, z2,
					x2, y1, z2,
					x2, y1, z1,
					x2, y2, z1,
					u2, v2,
					u2, v1,
					u1, v1,
					u1, v2, util);
		}
		if(renderWest)
		{
			u1 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) z1);
			u2 = Maths.lerp(icon.getMinU(), icon.getMaxU(), (float) z2);
			v1 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y1);
			v2 = Maths.lerp(icon.getMinV(), icon.getMaxV(), 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.W);
			ao = diffuseLight ? X_LIGHT_MULTIPLER : 1.0F;
			faceWithLight(
					x1, y2, z1,
					x1, y1, z1,
					x1, y1, z2,
					x1, y2, z2,
					u1, v2,
					u1, v1,
					u2, v1,
					u2, v2, util);
		}
	}

	protected void renderCube(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			ICoordableBrightnessProvider provider, int i, int j, int k,
			TextureAtlasSprite icon)
	{
		if(renderUp)
		{
			helper.face(icon,
					x2, y2, z2, (float) x2, (float) z2,
					x2, y2, z1, (float) x2, (float) z1,
					x1, y2, z1, (float) x1, (float) z1,
					x1, y2, z2, (float) x1, (float) z2);
		}
		if(renderDown)
		{
			helper.face(icon,
					x2, y1, z1, (float) x2, 1F - (float) z1,
					x2, y1, z2, (float) x2, 1F - (float) z2,
					x1, y1, z2, (float) x1, 1F - (float) z2,
					x1, y1, z1, (float) x1, 1F - (float) z1);
		}
		if(renderSouth)
		{
			helper.face(icon,
					x1, y2, z2, (float) x1, 1F - (float) y2,
					x1, y1, z2, (float) x1, 1F - (float) y1,
					x2, y1, z2, (float) x2, 1F - (float) y1,
					x2, y2, z2, (float) x2, 1F - (float) y2);
		}
		if(renderNorth)
		{
			helper.face(icon,
					x2, y2, z1, 1F - (float) x2, 1F - (float) y2,
					x2, y1, z1, 1F - (float) x2, 1F - (float) y1,
					x1, y1, z1, 1F - (float) x1, 1F - (float) y1,
					x1, y2, z1, 1F - (float) x1, 1F - (float) y2);
		}
		if(renderEast)
		{
			helper.face(icon,
					x2, y2, z2, 1F - (float) z2, 1F - (float) y2,
					x2, y1, z2, 1F - (float) z2, 1F - (float) y1,
					x2, y1, z1, 1F - (float) z1, 1F - (float) y1,
					x2, y2, z1, 1F - (float) z1, 1F - (float) y2);
		}
		if(renderWest)
		{
			helper.face(icon,
					x1, y2, z1, (float) z1, 1F - (float) y2,
					x1, y1, z1, (float) z1, 1F - (float) y1,
					x1, y1, z2, (float) z2, 1F - (float) y1,
					x1, y2, z2, (float) z2, 1F - (float) y2);
		}
	}

	protected void faceWithLight(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4,
			float u1, float v1,
			float u2, float v2,
			float u3, float v3,
			float u4, float v4,
			BrightnessUtil util)
	{
		helper.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		helper.vertex(x1, y1, z1, u1, v1, util.brightness[0], red * util.color[0] * ao, green * util.color[0] * ao, blue * util.color[0] * ao, alpha);
		helper.vertex(x1, y2, z2, u2, v2, util.brightness[1], red * util.color[1] * ao, green * util.color[1] * ao, blue * util.color[1] * ao, alpha);
		helper.vertex(x3, y3, z3, u3, v3, util.brightness[2], red * util.color[2] * ao, green * util.color[2] * ao, blue * util.color[2] * ao, alpha);
		helper.vertex(x4, y4, z4, u4, v4, util.brightness[3], red * util.color[3] * ao, green * util.color[3] * ao, blue * util.color[3] * ao, alpha);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		helper.draw();
		GlStateManager.shadeModel(GL11.GL_FLAT);
	}
	
	protected void face6Dir(
			double x, double y, double z,
			double uOfX, double uOfY, double uOfZ,
			double vOfX, double vOfY, double vOfZ,
			double u1, double v1, double u2, double v2)
	{
		if(enableColor)
		{
			helper.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		}
		else
		{
			helper.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		}
		double d0 = u1;
		double d1 = v1;
		double d2 = u2;
		double d3 = v2;
		double d5 = x;
		double d6 = x + uOfX;
		double d7 = x + uOfX + vOfX;
		double d8 = x + vOfX;
		double d9 = z;
		double d10 = z + uOfZ;
		double d11 = z + uOfZ + vOfZ;
		double d12 = z + vOfZ;
		double d13 = y;
		double d14 = y + uOfY;
		double d15 = y + uOfY + vOfY;
		double d16 = y + vOfY;
		helper.vertex(d5, d13, d9, d2, d1);
		helper.vertex(d6, d14, d10, d2, d3);
		helper.vertex(d7, d15, d11, d0, d3);
		helper.vertex(d8, d16, d12, d0, d1);
		helper.vertex(d8, d16, d12, d0, d1);
		helper.vertex(d7, d15, d11, d0, d3);
		helper.vertex(d6, d14, d10, d2, d3);
		helper.vertex(d5, d13, d9, d2, d1);
		helper.draw();
	}
}