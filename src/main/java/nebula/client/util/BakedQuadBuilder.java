/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.util;

import java.util.function.Consumer;

import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import nebula.common.util.Maths;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

/**
 * @author ueyudiud
 */
public class BakedQuadBuilder
{
	private final VertexFormat format;
	private final IModelModifier modifier;
	private final Consumer<BakedQuad> consumer;
	private UnpackedBakedQuad.Builder builder;
	private TextureAtlasSprite icon;
	private float px, py, pz, nx, ny, nz, tu, tv, cr, cg, cb, ca;
	
	private boolean textureScaleFlag = true;
	
	public BakedQuadBuilder(VertexFormat format, IModelModifier modifier, Consumer<BakedQuad> consumer)
	{
		this.format = format;
		this.modifier = modifier;
		this.consumer = consumer;
	}
	
	public void switchTextureScale()
	{
		this.textureScaleFlag = !this.textureScaleFlag;
	}
	
	public void nextQuad()
	{
		this.builder = new UnpackedBakedQuad.Builder(this.format);
	}
	
	public void startQuad(EnumFacing facing)
	{
		startQuad(facing, -1, null);
	}
	
	public void startQuad(EnumFacing facing, int tindex, TextureAtlasSprite icon)
	{
		this.builder = new UnpackedBakedQuad.Builder(this.format);
		this.builder.setQuadOrientation(facing);
		this.builder.setQuadTint(tindex);
		this.builder.setTexture(this.icon = icon);
	}
	
	public void endQuad()
	{
		this.px = this.py = this.pz = this.tu = this.tv = 0F;
		this.cr = this.cg = this.cb = this.ca = 1.0F;
		this.nx = this.ny = this.nz = 0F;
		this.consumer.accept(this.builder.build());
	}
	
	public void color(float r, float g, float b, float a)
	{
		if (this.modifier != null)
		{
			Vector4f v;
			this.modifier.recolor(v = new Vector4f(r, g, b, a));
			this.cr = v.x;
			this.cg = v.y;
			this.cb = v.z;
			this.ca = v.w;
		}
		else
		{
			this.cr = r;
			this.cg = g;
			this.cb = b;
			this.ca = a;
		}
	}
	
	public void normal(float x, float y, float z)
	{
		if (this.modifier != null)
		{
			Vector3f v;
			this.modifier.transform(v = new Vector3f(x, y, z));
			this.nx = v.x;
			this.ny = v.y;
			this.nz = v.z;
		}
		else
		{
			this.nx = x;
			this.ny = y;
			this.nz = z;
		}
	}
	
	public void pos(float x, float y, float z)
	{
		if (this.modifier != null)
		{
			Point3f p;
			this.modifier.transform(p = new Point3f(x, y, z));
			this.px = p.x;
			this.py = p.y;
			this.pz = p.z;
		}
		else
		{
			this.px = x;
			this.py = y;
			this.pz = z;
		}
		put();
	}
	
	public void pos(float x, float y, float z, float u, float v)
	{
		if (this.modifier != null)
		{
			Point3f p;
			this.modifier.transform(p = new Point3f(x, y, z));
			this.px = p.x;
			this.py = p.y;
			this.pz = p.z;
		}
		else
		{
			this.px = x;
			this.py = y;
			this.pz = z;
		}
		uv(u, v);
		put();
	}
	
	public void uv(float u, float v)
	{
		if (this.textureScaleFlag)
		{
			this.tu = this.icon.getInterpolatedU(u);
			this.tv = this.icon.getInterpolatedV(v);
		}
		else
		{
			this.tu = Maths.lerp(this.icon.getMinU(), this.icon.getMaxU(), u);
			this.tv = Maths.lerp(this.icon.getMinV(), this.icon.getMaxV(), v);
		}
	}
	
	private void put()
	{
		for(int e = 0; e < this.format.getElementCount(); e++)
		{
			switch(this.format.getElement(e).getUsage())
			{
			case POSITION : this.builder.put(e, this.px, this.py, this.pz); break;
			case UV :
				if(this.format.getElement(e).getIndex() == 0)
					this.builder.put(e, this.tu, this.tv, 0, 1);
				else
					this.builder.put(e);
				break;
			case NORMAL : this.builder.put(e, this.nx, this.ny, this.nz); break;
			case COLOR : this.builder.put(e, this.cr, this.cg, this.cb, this.ca); break;
			default: this.builder.put(e); break;
			}
		}
	}
}