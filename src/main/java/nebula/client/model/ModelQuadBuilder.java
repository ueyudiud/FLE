/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableList;

import nebula.client.util.CoordTransformer;
import nebula.common.util.Direction;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Use for ordered model...<br>
 * Some model need use some data from tile entity or other place, and data is
 * too large for state variant load.<br>
 * Here provide some vanilla quad builder.
 * 
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public final class ModelQuadBuilder
{
	private VertexFormat		format;
	private CoordTransformer	transformer				= new CoordTransformer();
	private float				minX					= 0.0F;
	private float				minY					= 0.0F;
	private float				minZ					= 0.0F;
	private float				maxX					= 1.0F;
	private float				maxY					= 1.0F;
	private float				maxZ					= 1.0F;
	private float				red						= 1.0F;
	private float				green					= 1.0F;
	private float				blue					= 1.0F;
	private float				alpha					= 1.0F;
	public int					tidx					= -1;
	private TextureAtlasSprite	icon;
	@SuppressWarnings("unused")
	private EnumFacing			facing;
	private boolean				applyDiffuseLighting	= true;
	public boolean				renderXNeg				= true;
	public boolean				renderXPos				= true;
	public boolean				renderYNeg				= true;
	public boolean				renderYPos				= true;
	public boolean				renderZNeg				= true;
	public boolean				renderZPos				= true;
	public boolean				renderOppisite			= false;
	
	public static ModelQuadBuilder newInstance()
	{
		return new ModelQuadBuilder();
	}
	
	private ModelQuadBuilder()
	{
	}
	
	public void setFormat(VertexFormat format)
	{
		this.format = format;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}
	
	public void setColor(int rgba)
	{
		this.red = ((rgba >> 24)) / 255.0F;
		this.green = ((rgba >> 16) & 0xFF) / 255.0F;
		this.blue = ((rgba >> 8) & 0xFF) / 255.0F;
		this.alpha = ((rgba) & 0xFF) / 255.0F;
	}
	
	public void setBound(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	public void setIcon(TextureAtlasSprite icon)
	{
		this.icon = icon;
	}
	
	public void setFacing(EnumFacing facing)
	{
		this.facing = facing;
	}
	
	public void setApplyDiffuseLighting(boolean applyDiffuseLighting)
	{
		this.applyDiffuseLighting = applyDiffuseLighting;
	}
	
	public void putCubeQuads(Consumer<BakedQuad> executable, float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		setBound(minX, minY, minZ, maxX, maxY, maxZ);
		if (this.renderYNeg) executable.accept(bakeYNegFace());
		if (this.renderYPos) executable.accept(bakeYPosFace());
		if (this.renderZNeg) executable.accept(bakeZNegFace());
		if (this.renderZPos) executable.accept(bakeZPosFace());
		if (this.renderXNeg) executable.accept(bakeXNegFace());
		if (this.renderXPos) executable.accept(bakeXPosFace());
	}
	
	public void putCubeQuads(ImmutableList.Builder<BakedQuad> executable, float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		putCubeQuads(executable::add, minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public void putCubeQuads(Collection<BakedQuad> executable, float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		putCubeQuads(executable::add, minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public List<BakedQuad> bakeCube()
	{
		List<BakedQuad> list = new ArrayList<>();
		putCubeQuads(list::add, this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
		return list;
	}
	
	public BakedQuad bakeFace(@Nullable EnumFacing facing, float normalX, float normalY, float normalZ, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2)
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.format);
		builder.setQuadTint(this.tidx);
		builder.setTexture(this.icon);
		builder.setQuadOrientation(this.transformer.transform(facing));
		builder.setApplyDiffuseLighting(this.applyDiffuseLighting);
		float minU = this.icon.getInterpolatedU(u1 * 16.0F);
		float maxU = this.icon.getInterpolatedU(u2 * 16.0F);
		float minV = this.icon.getInterpolatedV(v1 * 16.0F);
		float maxV = this.icon.getInterpolatedV(v2 * 16.0F);
		if (this.renderOppisite)
		{
			putVertex(builder, this.format, x1, y1, z1, minU, minV, normalX, normalY, normalZ, this.renderOppisite);
			putVertex(builder, this.format, x4, y4, z4, maxU, minV, normalX, normalY, normalZ, this.renderOppisite);
			putVertex(builder, this.format, x3, y3, z3, maxU, maxV, normalX, normalY, normalZ, this.renderOppisite);
			putVertex(builder, this.format, x2, y2, z2, minU, maxV, normalX, normalY, normalZ, this.renderOppisite);
		}
		else
		{
			putVertex(builder, this.format, x1, y1, z1, minU, minV, normalX, normalY, normalZ, this.renderOppisite);
			putVertex(builder, this.format, x2, y2, z2, minU, maxV, normalX, normalY, normalZ, this.renderOppisite);
			putVertex(builder, this.format, x3, y3, z3, maxU, maxV, normalX, normalY, normalZ, this.renderOppisite);
			putVertex(builder, this.format, x4, y4, z4, maxU, minV, normalX, normalY, normalZ, this.renderOppisite);
		}
		return builder.build();
	}
	
	public BakedQuad bakeYPosFace()
	{
		return bakeFace(EnumFacing.UP, Direction.U.x, Direction.U.y, Direction.U.z, this.minX, this.maxY, this.minZ, this.minX, this.maxY, this.maxZ, this.maxX, this.maxY, this.maxZ, this.maxX, this.maxY, this.minZ, this.minX, this.minZ, this.maxX, this.maxZ);
	}
	
	public BakedQuad bakeYNegFace()
	{
		return bakeFace(EnumFacing.DOWN, Direction.D.x, Direction.D.y, Direction.D.z, this.minX, this.minY, this.maxZ, this.minX, this.minY, this.minZ, this.maxX, this.minY, this.minZ, this.maxX, this.minY, this.maxZ, this.minX, 1F - this.maxZ, this.maxX, 1F - this.minZ);
	}
	
	public BakedQuad bakeXPosFace()
	{
		return bakeFace(EnumFacing.EAST, Direction.E.x, Direction.E.y, Direction.E.z, this.maxX, this.maxY, this.maxZ, this.maxX, this.minY, this.maxZ, this.maxX, this.minY, this.minZ, this.maxX, this.maxY, this.minZ, 1F - this.maxZ, 1F - this.maxY, 1F - this.minZ, 1F - this.minY);
	}
	
	public BakedQuad bakeXNegFace()
	{
		return bakeFace(EnumFacing.WEST, Direction.W.x, Direction.W.y, Direction.W.z, this.minX, this.maxY, this.minZ, this.minX, this.minY, this.minZ, this.minX, this.minY, this.maxZ, this.minX, this.maxY, this.maxZ, this.minZ, 1F - this.maxY, this.maxZ, 1F - this.minY);
	}
	
	public BakedQuad bakeZPosFace()
	{
		return bakeFace(EnumFacing.SOUTH, Direction.S.x, Direction.S.y, Direction.S.z, this.minX, this.maxY, this.maxZ, this.minX, this.minY, this.maxZ, this.maxX, this.minY, this.maxZ, this.maxX, this.maxY, this.maxZ, this.minX, 1F - this.maxY, this.maxX, 1F - this.minY);
	}
	
	public BakedQuad bakeZNegFace()
	{
		return bakeFace(EnumFacing.NORTH, Direction.N.x, Direction.N.y, Direction.N.z, this.maxX, this.maxY, this.minZ, this.maxX, this.minY, this.minZ, this.minX, this.minY, this.minZ, this.minX, this.maxY, this.minZ, 1F - this.maxX, 1F - this.maxY, 1F - this.minX, 1F - this.minY);
	}
	
	@SuppressWarnings("unused")
	private void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, float x, float y, float z, float u, float v, Direction face, boolean oppisite)
	{
		putVertex(builder, format, x, y, z, u, v, face.x, face.y, face.z, oppisite);
	}
	
	private void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, boolean oppisite)
	{
		for (int e = 0; e < format.getElementCount(); e++)
		{
			switch (format.getElement(e).getUsage())
			{
			case POSITION:
			{
				float[] xyz = { x, y, z };
				this.transformer.transform(xyz);
				builder.put(e, xyz[0], xyz[1], xyz[2], 1);
				break;
			}
			case COLOR:
			{
				builder.put(e, this.red, this.green, this.blue, this.alpha);
				break;
			}
			case UV:
				if (format.getElement(e).getIndex() == 0)
				{
					builder.put(e, u, v, 0F, 1F);
					break;
				}
			case NORMAL:
			{
				Vector3f vector3f = new Vector3f(normalX, normalY, normalZ);
				this.transformer.normal(vector3f);
				if (!oppisite)
				{
					builder.put(e, vector3f.x, vector3f.y, vector3f.z, 0f);
				}
				else
				{
					builder.put(e, -vector3f.x, -vector3f.y, -vector3f.z, 0f);
				}
				break;
			}
			default:
			{
				builder.put(e);
				break;
			}
			}
		}
	}
	
	public CoordTransformer getTransformer()
	{
		return this.transformer;
	}
	
	public VertexFormat getFormat()
	{
		return this.format;
	}
	
	public void resetOption()
	{
		this.renderXNeg = this.renderXPos = this.renderYNeg = this.renderYPos = this.renderZNeg = this.renderZPos = true;
		this.red = this.green = this.blue = this.alpha = 1.0F;
		this.renderOppisite = false;
		this.transformer.normalize();
	}
}
