package farcore.lib.tesr;

import javax.vecmath.Vector2f;

import org.lwjgl.opengl.GL11;

import farcore.lib.render.RenderHelper;
import farcore.lib.util.Direction;
import farcore.util.Maths;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TESRBase<T extends TileEntity> extends TileEntitySpecialRenderer<T>
{
	private static final float UP_LIGHT_MULTIPLER = 1.0F;
	private static final float DOWN_LIGHT_MULTIPLER = 0.5F;
	private static final float X_LIGHT_MULTIPLER = 0.6F;
	private static final float Z_LIGHT_MULTIPLER = 0.8F;
	
	private static final int[] X = { 0, 0, 1, 1 };
	private static final int[] Z = { 0, 1, 1, 0 };
	
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
	
	protected void setColor(FluidStack stack)
	{
		if(stack == null)
		{
			resetColor();
		}
		else
		{
			int color = stack.getFluid().getColor(stack);
			colorV(
					((color >> 16) & 0xFF) / 255F,
					((color >>  8) & 0xFF) / 255F,
					( color        & 0xFF) / 255F, 1.0F);
		}
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
	
	protected float getU(TextureAtlasSprite icon, float x)
	{
		return Maths.lerp(icon.getMinU(), icon.getMaxU(), x);
	}
	
	protected float getV(TextureAtlasSprite icon, float x)
	{
		return Maths.lerp(icon.getMinV(), icon.getMaxV(), x);
	}
	
	protected void renderFluidFace(
			double xMin, double zMin, double xMax, double zMax,
			double y1, double y2, double y3, double y4,
			FluidStack stack, Vector2f flow)
	{
		if(stack != null)
		{
			setColor(stack);
			TextureAtlasSprite icon;
			float u1, u2, v1, v2;
			//			if(L.similar(flow.x, 0F) && L.similar(flow.y, 0F))
			{
				icon = getTexture(stack.getFluid().getStill(stack));
				u1 = getU(icon, (float) xMin);
				u2 = getU(icon, (float) xMax);
				v1 = getV(icon, (float) zMin);
				v2 = getV(icon, (float) zMax);
				this.helper.face(this.red, this.green, this.blue, this.alpha,
						xMin, y1, zMin, u1, v1,
						xMin, y2, zMax, u1, v2,
						xMax, y3, zMax, u2, v2,
						xMax, y4, zMin, u2, v1).draw();
			}
			//FIXME
			//			else
			//			{
			//				float c1 = (float) Math.atan2(flow.y, flow.x);
			//				float a = MathHelper.sin(c1);
			//				float b = MathHelper.cos(c1);
			//				int dir = (int) (a / 90);
			//				float xCen = (float) ((xMin + xMax) / 2F);
			//				float zCen = (float) ((zMin + zMax) / 2F);
			//				float xScale = (float) (xMax - xMin) * b;
			//				float zScale = (float) (zMax - zMin) * b;
			//				float u3, v3, u4, v4;
			//				icon = getTexture(stack.getFluid().getFlowing(stack));
			//				u1 = icon.getInterpolatedU(16 * xCen + (b * (X[(dir    ) & 0x3] * 2 - 1) + a * (Z[(dir    ) & 0x3] * 2 - 1)) * xScale * 8);
			//				v1 = icon.getInterpolatedV(16 * zCen + (b * (X[(dir + 1) & 0x3] * 2 - 1) + a * (Z[(dir + 1) & 0x3] * 2 - 1)) * zScale * 8);
			//				u2 = icon.getInterpolatedU(16 * xCen + (b * (X[(dir + 1) & 0x3] * 2 - 1) + a * (Z[(dir + 1) & 0x3] * 2 - 1)) * xScale * 8);
			//				v2 = icon.getInterpolatedV(16 * zCen + (b * (X[(dir + 2) & 0x3] * 2 - 1) + a * (Z[(dir + 2) & 0x3] * 2 - 1)) * zScale * 8);
			//				u3 = icon.getInterpolatedU(16 * xCen + (b * (X[(dir + 2) & 0x3] * 2 - 1) + a * (Z[(dir + 2) & 0x3] * 2 - 1)) * xScale * 8);
			//				v3 = icon.getInterpolatedV(16 * zCen + (b * (X[(dir + 3) & 0x3] * 2 - 1) + a * (Z[(dir + 3) & 0x3] * 2 - 1)) * zScale * 8);
			//				u4 = icon.getInterpolatedU(16 * xCen + (b * (X[(dir + 3) & 0x3] * 2 - 1) + a * (Z[(dir + 3) & 0x3] * 2 - 1)) * xScale * 8);
			//				v4 = icon.getInterpolatedV(16 * zCen + (b * (X[(dir    ) & 0x3] * 2 - 1) + a * (Z[(dir    ) & 0x3] * 2 - 1)) * zScale * 8);
			//				this.helper.face(this.red, this.green, this.blue, this.alpha,
			//						xMin, y1, zMin, u1, v1,
			//						xMin, y2, zMax, u2, v2,
			//						xMax, y3, zMax, u3, v3,
			//						xMax, y4, zMin, u4, v4).draw();
			//			}
		}
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
		if(this.renderUp)
		{
			u1 = getU(icon, (float) x1);
			u2 = getU(icon, (float) x2);
			v1 = getV(icon, (float) z1);
			v2 = getV(icon, (float) z2);
			util.caculateBrightness(provider, i, j, k, Direction.U);
			this.ao = this.diffuseLight ? UP_LIGHT_MULTIPLER : 1.0F;
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
		if(this.renderDown)
		{
			u1 = getU(icon, (float) x1);
			u2 = getU(icon, (float) x2);
			v1 = getV(icon, 1F - (float) z1);
			v2 = getV(icon, 1F - (float) z2);
			util.caculateBrightness(provider, i, j, k, Direction.D);
			this.ao = this.diffuseLight ? DOWN_LIGHT_MULTIPLER : 1.0F;
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
		if(this.renderSouth)
		{
			u1 = getU(icon, (float) x1);
			u2 = getU(icon, (float) x2);
			v1 = getV(icon, 1F - (float) y1);
			v2 = getV(icon, 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.S);
			this.ao = this.diffuseLight ? Z_LIGHT_MULTIPLER : 1.0F;
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
		if(this.renderNorth)
		{
			u1 = getU(icon, 1F - (float) x1);
			u2 = getU(icon, 1F - (float) x2);
			v1 = getV(icon, 1F - (float) y1);
			v2 = getV(icon, 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.N);
			this.ao = this.diffuseLight ? Z_LIGHT_MULTIPLER : 1.0F;
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
		if(this.renderEast)
		{
			u1 = getU(icon, 1F - (float) z1);
			u2 = getU(icon, 1F - (float) z2);
			v1 = getV(icon, 1F - (float) y1);
			v2 = getV(icon, 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.E);
			this.ao = this.diffuseLight ? X_LIGHT_MULTIPLER : 1.0F;
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
		if(this.renderWest)
		{
			u1 = getU(icon, (float) z1);
			u2 = getU(icon, (float) z2);
			v1 = getV(icon, 1F - (float) y1);
			v2 = getV(icon, 1F - (float) y2);
			util.caculateBrightness(provider, i, j, k, Direction.W);
			this.ao = this.diffuseLight ? X_LIGHT_MULTIPLER : 1.0F;
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
			TextureAtlasSprite icon)
	{
		if(this.renderUp)
		{
			this.helper.face(icon,
					x2, y2, z2, (float) x2, (float) z2,
					x2, y2, z1, (float) x2, (float) z1,
					x1, y2, z1, (float) x1, (float) z1,
					x1, y2, z2, (float) x1, (float) z2).draw();
		}
		if(this.renderDown)
		{
			this.helper.face(icon,
					x2, y1, z1, (float) x2, 1F - (float) z1,
					x2, y1, z2, (float) x2, 1F - (float) z2,
					x1, y1, z2, (float) x1, 1F - (float) z2,
					x1, y1, z1, (float) x1, 1F - (float) z1).draw();
		}
		if(this.renderSouth)
		{
			this.helper.face(icon,
					x1, y2, z2, (float) x1, 1F - (float) y2,
					x1, y1, z2, (float) x1, 1F - (float) y1,
					x2, y1, z2, (float) x2, 1F - (float) y1,
					x2, y2, z2, (float) x2, 1F - (float) y2).draw();
		}
		if(this.renderNorth)
		{
			this.helper.face(icon,
					x2, y2, z1, 1F - (float) x2, 1F - (float) y2,
					x2, y1, z1, 1F - (float) x2, 1F - (float) y1,
					x1, y1, z1, 1F - (float) x1, 1F - (float) y1,
					x1, y2, z1, 1F - (float) x1, 1F - (float) y2).draw();
		}
		if(this.renderEast)
		{
			this.helper.face(icon,
					x2, y2, z2, 1F - (float) z2, 1F - (float) y2,
					x2, y1, z2, 1F - (float) z2, 1F - (float) y1,
					x2, y1, z1, 1F - (float) z1, 1F - (float) y1,
					x2, y2, z1, 1F - (float) z1, 1F - (float) y2).draw();
		}
		if(this.renderWest)
		{
			this.helper.face(icon,
					x1, y2, z1, (float) z1, 1F - (float) y2,
					x1, y1, z1, (float) z1, 1F - (float) y1,
					x1, y1, z2, (float) z2, 1F - (float) y1,
					x1, y2, z2, (float) z2, 1F - (float) y2).draw();
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
		this.helper.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		this.helper.vertex(x1, y1, z1, u1, v1, util.brightness[0], this.red * util.color[0] * this.ao, this.green * util.color[0] * this.ao, this.blue * util.color[0] * this.ao, this.alpha);
		this.helper.vertex(x1, y2, z2, u2, v2, util.brightness[1], this.red * util.color[1] * this.ao, this.green * util.color[1] * this.ao, this.blue * util.color[1] * this.ao, this.alpha);
		this.helper.vertex(x3, y3, z3, u3, v3, util.brightness[2], this.red * util.color[2] * this.ao, this.green * util.color[2] * this.ao, this.blue * util.color[2] * this.ao, this.alpha);
		this.helper.vertex(x4, y4, z4, u4, v4, util.brightness[3], this.red * util.color[3] * this.ao, this.green * util.color[3] * this.ao, this.blue * util.color[3] * this.ao, this.alpha);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		this.helper.draw();
		GlStateManager.shadeModel(GL11.GL_FLAT);
	}
	
	protected void face6Dir(
			double x, double y, double z,
			double uOfX, double uOfY, double uOfZ,
			double vOfX, double vOfY, double vOfZ,
			double u1, double v1, double u2, double v2)
	{
		if(this.enableColor)
		{
			this.helper.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		}
		else
		{
			this.helper.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
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
		this.helper.vertex(d5, d13, d9, d2, d1);
		this.helper.vertex(d6, d14, d10, d2, d3);
		this.helper.vertex(d7, d15, d11, d0, d3);
		this.helper.vertex(d8, d16, d12, d0, d1);
		this.helper.vertex(d8, d16, d12, d0, d1);
		this.helper.vertex(d7, d15, d11, d0, d3);
		this.helper.vertex(d6, d14, d10, d2, d3);
		this.helper.vertex(d5, d13, d9, d2, d1);
		this.helper.draw();
	}
}