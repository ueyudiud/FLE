/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.render;

import nebula.common.util.Maths;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The {@link net.minecraft.client.renderer.Tessellator} wrapper type.
 * 
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class Drawer
{
	public static final Drawer INSTANCE = new Drawer();
	
	private final Tessellator	tessellator	= Tessellator.getInstance();
	private final VertexBuffer	buffer		= this.tessellator.getBuffer();
	private TextureAtlasSprite	icon;
	private float				iconCoordScale;
	
	private Drawer()
	{
	}
	
	public void setIconCoordScale(float iconCoordScale)
	{
		this.iconCoordScale = iconCoordScale;
	}
	
	public Drawer bindIcon(TextureAtlasSprite icon)
	{
		this.icon = icon;
		return this;
	}
	
	public Drawer begin(int mode, VertexFormat format)
	{
		this.buffer.begin(mode, format);
		return this;
	}
	
	public Drawer draw()
	{
		this.tessellator.draw();
		return this;
	}
	
	private Drawer tex(double u, double v)
	{
		if (this.icon != null)
			this.buffer.tex(Maths.lerp(this.icon.getMinU(), this.icon.getMaxU(), u * this.iconCoordScale), Maths.lerp(this.icon.getMinV(), this.icon.getMaxV(), v * this.iconCoordScale));
		else
			this.buffer.tex(u, v);
		return this;
	}
	
	public Drawer vertex_p(double x, double y, double z)
	{
		this.buffer.pos(x, y, z).endVertex();
		return this;
	}
	
	public Drawer vertex_pt(double x, double y, double z, double u, double v)
	{
		this.buffer.pos(x, y, z);
		tex(u, v);
		this.buffer.endVertex();
		return this;
	}
	
	public Drawer vertex_ptn(double x, double y, double z, double u, double v, float n1, float n2, float n3)
	{
		this.buffer.pos(x, y, z);
		tex(u, v);
		this.buffer.normal(n1, n2, n3).endVertex();
		return this;
	}
	
	public Drawer vertex_pc(double x, double y, double z, float r, float g, float b, float a)
	{
		this.buffer.pos(x, y, z).color(r, g, b, a).endVertex();
		return this;
	}
	
	public Drawer vertex_pc(double x, double y, double z, int rgba)
	{
		this.buffer.pos(x, y, z).color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF).endVertex();
		return this;
	}
	
	public Drawer vertex_ptc(double x, double y, double z, double u, double v, float r, float g, float b, float a)
	{
		this.buffer.pos(x, y, z);
		tex(u, v);
		this.buffer.color(r, g, b, a).endVertex();
		return this;
	}
	
	public Drawer vertex_ptc(double x, double y, double z, double u, double v, int rgba)
	{
		this.buffer.pos(x, y, z);
		tex(u, v);
		this.buffer.color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF).endVertex();
		return this;
	}
	
	public Drawer vertex_ptlc(double x, double y, double z, double u, double v, int skylight, int blocklight, int rgba)
	{
		this.buffer.pos(x, y, z);
		tex(u, v);
		this.buffer.lightmap(skylight, blocklight).color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF).endVertex();
		return this;
	}
	
	public Drawer vertex_ptlc(double x, double y, double z, double u, double v, int light, float r, float g, float b, float a)
	{
		this.buffer.pos(x, y, z);
		tex(u, v);
		this.buffer.lightmap(light >> 16 & 0xFF, light & 0xFF).color(r, g, b, a).endVertex();
		return this;
	}
	
	public Drawer vertex_ptlc(double x, double y, double z, double u, double v, int light, int rgba)
	{
		this.buffer.pos(x, y, z);
		tex(u, v);
		this.buffer.lightmap(light >> 16 & 0xFF, light & 0xFF).color((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, rgba & 0xFF).endVertex();
		return this;
	}
	
	public Drawer face_p(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3, double x4, double y4, double z4)
	{
		vertex_p(x1, y1, z1);
		vertex_p(x2, y2, z2);
		vertex_p(x3, y3, z3);
		vertex_p(x4, y4, z4);
		return this;
	}
	
	public Drawer face_pt(TextureAtlasSprite icon, double x1, double y1, double z1, float u1, float v1, double x2, double y2, double z2, float u2, float v2, double x3, double y3, double z3, float u3, float v3, double x4, double y4, double z4, float u4, float v4)
	{
		bindIcon(icon);
		vertex_pt(x1, y1, z1, u1, v1);
		vertex_pt(x2, y2, z2, u2, v2);
		vertex_pt(x3, y3, z3, u3, v3);
		vertex_pt(x4, y4, z4, u4, v4);
		bindIcon(null);
		return this;
	}
	
	public Drawer face_ptn(TextureAtlasSprite icon, double x1, double y1, double z1, float u1, float v1, double x2, double y2, double z2, float u2, float v2, double x3, double y3, double z3, float u3, float v3, double x4, double y4, double z4, float u4, float v4, float nx, float ny, float nz)
	{
		bindIcon(icon);
		vertex_ptn(x1, y1, z1, u1, v1, nx, ny, nz);
		vertex_ptn(x2, y2, z2, u2, v2, nx, ny, nz);
		vertex_ptn(x3, y3, z3, u3, v3, nx, ny, nz);
		vertex_ptn(x4, y4, z4, u4, v4, nx, ny, nz);
		bindIcon(null);
		return this;
	}
	
	public Drawer face_ptn(double x1, double y1, double z1, float u1, float v1, double x2, double y2, double z2, float u2, float v2, double x3, double y3, double z3, float u3, float v3, double x4, double y4, double z4, float u4, float v4, float nx, float ny, float nz)
	{
		vertex_ptn(x1, y1, z1, u1, v1, nx, ny, nz);
		vertex_ptn(x2, y2, z2, u2, v2, nx, ny, nz);
		vertex_ptn(x3, y3, z3, u3, v3, nx, ny, nz);
		vertex_ptn(x4, y4, z4, u4, v4, nx, ny, nz);
		return this;
	}
	
	public Drawer face_pt(double x1, double y1, double z1, float u1, float v1, double x2, double y2, double z2, float u2, float v2, double x3, double y3, double z3, float u3, float v3, double x4, double y4, double z4, float u4, float v4)
	{
		vertex_pt(x1, y1, z1, u1, v1);
		vertex_pt(x2, y2, z2, u2, v2);
		vertex_pt(x3, y3, z3, u3, v3);
		vertex_pt(x4, y4, z4, u4, v4);
		return this;
	}
	
	public Drawer face_ptc(short[] rgba, double x1, double y1, double z1, float u1, float v1, double x2, double y2, double z2, float u2, float v2, double x3, double y3, double z3, float u3, float v3, double x4, double y4, double z4, float u4, float v4)
	{
		vertex_ptc(x1, y1, z1, u1, v1, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		vertex_ptc(x2, y2, z2, u2, v2, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		vertex_ptc(x3, y3, z3, u3, v3, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		vertex_ptc(x4, y4, z4, u4, v4, rgba[0] / 255F, rgba[1] / 255F, rgba[2] / 255F, rgba[3] / 255F);
		return this;
	}
	
	public Drawer face_ptc(float r, float g, float b, float a, double x1, double y1, double z1, float u1, float v1, double x2, double y2, double z2, float u2, float v2, double x3, double y3, double z3, float u3, float v3, double x4, double y4, double z4, float u4, float v4)
	{
		vertex_ptc(x1, y1, z1, u1, v1, r, g, b, a);
		vertex_ptc(x2, y2, z2, u2, v2, r, g, b, a);
		vertex_ptc(x3, y3, z3, u3, v3, r, g, b, a);
		vertex_ptc(x4, y4, z4, u4, v4, r, g, b, a);
		return this;
	}
	
	public Drawer face_ptc(TextureAtlasSprite icon, double x1, double y1, double z1, float u1, float v1, float rgb1, double x2, double y2, double z2, float u2, float v2, float rgb2, double x3, double y3, double z3, float u3, float v3, float rgb3, double x4, double y4, double z4, float u4,
			float v4, float rgb4)
	{
		vertex_ptc(x1, y1, z1, u1, v1, rgb1, rgb1, rgb1, 1.0F);
		vertex_ptc(x2, y2, z2, u2, v2, rgb2, rgb2, rgb2, 1.0F);
		vertex_ptc(x3, y3, z3, u3, v3, rgb3, rgb3, rgb3, 1.0F);
		vertex_ptc(x4, y4, z4, u4, v4, rgb4, rgb4, rgb4, 1.0F);
		return this;
	}
	
	public Drawer face_ptc(TextureAtlasSprite icon, double x1, double y1, double z1, float u1, float v1, float r1, float g1, float b1, float a1, double x2, double y2, double z2, float u2, float v2, float r2, float g2, float b2, float a2, double x3, double y3, double z3, float u3, float v3,
			float r3, float g3, float b3, float a3, double x4, double y4, double z4, float u4, float v4, float r4, float g4, float b4, float a4)
	{
		vertex_ptc(x1, y1, z1, u1, v1, r1, g1, b1, a1);
		vertex_ptc(x2, y2, z2, u2, v2, r2, g2, b2, a2);
		vertex_ptc(x3, y3, z3, u3, v3, r3, g3, b3, a3);
		vertex_ptc(x4, y4, z4, u4, v4, r4, g4, b4, a4);
		return this;
	}
	
	public Drawer face_ptlc(TextureAtlasSprite icon, double x1, double y1, double z1, float u1, float v1, int light1, float r1, float g1, float b1, float a1, double x2, double y2, double z2, float u2, float v2, int light2, float r2, float g2, float b2, float a2, double x3, double y3, double z3,
			float u3, float v3, int light3, float r3, float g3, float b3, float a3, double x4, double y4, double z4, float u4, float v4, int light4, float r4, float g4, float b4, float a4)
	{
		vertex_ptlc(x1, y1, z1, u1, v1, light1, r1, g1, b1, a1);
		vertex_ptlc(x2, y2, z2, u2, v2, light2, r2, g2, b2, a2);
		vertex_ptlc(x3, y3, z3, u3, v3, light3, r3, g3, b3, a3);
		vertex_ptlc(x4, y4, z4, u4, v4, light4, r4, g4, b4, a4);
		return this;
	}
}
