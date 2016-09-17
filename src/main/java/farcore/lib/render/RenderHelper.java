package farcore.lib.render;

import org.lwjgl.opengl.GL11;

import farcore.util.U.Maths;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;

public class RenderHelper
{
	public static final RenderHelper instance = new RenderHelper();

	private final Tessellator tessellator = Tessellator.getInstance();
	private final VertexBuffer buffer = tessellator.getBuffer();

	private RenderHelper(){}
	
	public RenderHelper draw()
	{
		tessellator.draw();
		return this;
	}

	public RenderHelper begin(int mode, VertexFormat format)
	{
		buffer.begin(mode, format);
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z)
	{
		buffer.pos(x, y, z);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, double u, double v)
	{
		buffer.pos(x, y, z).tex(u, v);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, float r, float g, float b, float a)
	{
		buffer.pos(x, y, z).color(r, g, b, a);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, int rgba)
	{
		buffer.pos(x, y, z).color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, double u, double v, float r, float g, float b, float a)
	{
		buffer.pos(x, y, z).tex(u, v).color(r, g, b, a);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, double u, double v, int rgba)
	{
		buffer.pos(x, y, z).tex(u, v).color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, double u, double v, int skylight, int blocklight, int rgba)
	{
		buffer.pos(x, y, z).tex(u, v).lightmap(skylight, blocklight).color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, double u, double v, int light, float r, float g, float b, float a)
	{
		buffer.pos(x, y, z).tex(u, v).lightmap(light >> 16 & 0xFF, light & 0xFF).color(r, g, b, a);
		buffer.endVertex();
		return this;
	}
	
	public RenderHelper vertex(double x, double y, double z, double u, double v, int light, int rgba)
	{
		buffer.pos(x, y, z).tex(u, v).lightmap(light >> 16 & 0xFF, light & 0xFF).color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF);
		buffer.endVertex();
		return this;
	}

	public RenderHelper face(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4)
	{
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		vertex(x1, y1, z1);
		vertex(x2, y2, z2);
		vertex(x3, y3, z3);
		vertex(x4, y4, z4);
		buffer.endVertex();
		return this;
	}

	public RenderHelper face(TextureAtlasSprite icon,
			double x1, double y1, double z1, float u1, float v1,
			double x2, double y2, double z2, float u2, float v2,
			double x3, double y3, double z3, float u3, float v3,
			double x4, double y4, double z4, float u4, float v4)
	{
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vertex(x1, y1, z1, Maths.lerp(icon.getMinU(), icon.getMaxU(), u1), Maths.lerp(icon.getMinV(), icon.getMaxV(), v1));
		vertex(x2, y2, z2, Maths.lerp(icon.getMinU(), icon.getMaxU(), u2), Maths.lerp(icon.getMinV(), icon.getMaxV(), v2));
		vertex(x3, y3, z3, Maths.lerp(icon.getMinU(), icon.getMaxU(), u3), Maths.lerp(icon.getMinV(), icon.getMaxV(), v3));
		vertex(x4, y4, z4, Maths.lerp(icon.getMinU(), icon.getMaxU(), u4), Maths.lerp(icon.getMinV(), icon.getMaxV(), v4));
		buffer.endVertex();
		return this;
	}

	public RenderHelper face(
			double x1, double y1, double z1, float u1, float v1,
			double x2, double y2, double z2, float u2, float v2,
			double x3, double y3, double z3, float u3, float v3,
			double x4, double y4, double z4, float u4, float v4)
	{
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vertex(x1, y1, z1, u1, v1);
		vertex(x2, y2, z2, u2, v2);
		vertex(x3, y3, z3, u3, v3);
		vertex(x4, y4, z4, u4, v4);
		buffer.endVertex();
		return this;
	}

	public RenderHelper face(short[] rgba,
			double x1, double y1, double z1, float u1, float v1,
			double x2, double y2, double z2, float u2, float v2,
			double x3, double y3, double z3, float u3, float v3,
			double x4, double y4, double z4, float u4, float v4)
	{
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		vertex(x1, y1, z1, u1, v1, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		vertex(x2, y2, z2, u2, v2, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		vertex(x3, y3, z3, u3, v3, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		vertex(x4, y4, z4, u4, v4, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		buffer.endVertex();
		return this;
	}

	public RenderHelper face(TextureAtlasSprite icon,
			double x1, double y1, double z1, float u1, float v1, float rgb1,
			double x2, double y2, double z2, float u2, float v2, float rgb2,
			double x3, double y3, double z3, float u3, float v3, float rgb3,
			double x4, double y4, double z4, float u4, float v4, float rgb4)
	{
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		vertex(x1, y1, z1, Maths.lerp(icon.getMinU(), icon.getMaxU(), u1), Maths.lerp(icon.getMinV(), icon.getMaxV(), v1), rgb1, rgb1, rgb1, 1.0F);
		vertex(x2, y2, z2, Maths.lerp(icon.getMinU(), icon.getMaxU(), u2), Maths.lerp(icon.getMinV(), icon.getMaxV(), v2), rgb2, rgb2, rgb2, 1.0F);
		vertex(x3, y3, z3, Maths.lerp(icon.getMinU(), icon.getMaxU(), u3), Maths.lerp(icon.getMinV(), icon.getMaxV(), v3), rgb3, rgb3, rgb3, 1.0F);
		vertex(x4, y4, z4, Maths.lerp(icon.getMinU(), icon.getMaxU(), u4), Maths.lerp(icon.getMinV(), icon.getMaxV(), v4), rgb4, rgb4, rgb4, 1.0F);
		buffer.endVertex();
		return this;
	}

	public RenderHelper face(TextureAtlasSprite icon,
			double x1, double y1, double z1, float u1, float v1, float r1, float g1, float b1, float a1,
			double x2, double y2, double z2, float u2, float v2, float r2, float g2, float b2, float a2,
			double x3, double y3, double z3, float u3, float v3, float r3, float g3, float b3, float a3,
			double x4, double y4, double z4, float u4, float v4, float r4, float g4, float b4, float a4)
	{
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		vertex(x1, y1, z1, Maths.lerp(icon.getMinU(), icon.getMaxU(), u1), Maths.lerp(icon.getMinV(), icon.getMaxV(), v1), r1, g1, b1, a1);
		vertex(x2, y2, z2, Maths.lerp(icon.getMinU(), icon.getMaxU(), u2), Maths.lerp(icon.getMinV(), icon.getMaxV(), v2), r2, g2, b2, a2);
		vertex(x3, y3, z3, Maths.lerp(icon.getMinU(), icon.getMaxU(), u3), Maths.lerp(icon.getMinV(), icon.getMaxV(), v3), r3, g3, b3, a3);
		vertex(x4, y4, z4, Maths.lerp(icon.getMinU(), icon.getMaxU(), u4), Maths.lerp(icon.getMinV(), icon.getMaxV(), v4), r4, g4, b4, a4);
		buffer.endVertex();
		return this;
	}

	public RenderHelper face(TextureAtlasSprite icon,
			double x1, double y1, double z1, float u1, float v1, int light1, float r1, float g1, float b1, float a1,
			double x2, double y2, double z2, float u2, float v2, int light2, float r2, float g2, float b2, float a2,
			double x3, double y3, double z3, float u3, float v3, int light3, float r3, float g3, float b3, float a3,
			double x4, double y4, double z4, float u4, float v4, int light4, float r4, float g4, float b4, float a4)
	{
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		vertex(x1, y1, z1, Maths.lerp(icon.getMinU(), icon.getMaxU(), u1), Maths.lerp(icon.getMinV(), icon.getMaxV(), v1), light1, r1, g1, b1, a1);
		vertex(x2, y2, z2, Maths.lerp(icon.getMinU(), icon.getMaxU(), u2), Maths.lerp(icon.getMinV(), icon.getMaxV(), v2), light2, r2, g2, b2, a2);
		vertex(x3, y3, z3, Maths.lerp(icon.getMinU(), icon.getMaxU(), u3), Maths.lerp(icon.getMinV(), icon.getMaxV(), v3), light3, r3, g3, b3, a3);
		vertex(x4, y4, z4, Maths.lerp(icon.getMinU(), icon.getMaxU(), u4), Maths.lerp(icon.getMinV(), icon.getMaxV(), v4), light4, r4, g4, b4, a4);
		buffer.endVertex();
		return this;
	}
}