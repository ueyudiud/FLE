/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableList;

import farcore.lib.util.CoordTransformer;
import farcore.lib.util.Direction;
import farcore.util.Executable;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Use for ordered model...<br>
 * Some model need use some data from tile entity or
 * other place, and data is too large for state variant
 * load.<br>
 * Here provide some vanilla quad builder.
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public final class ModelQuadBuilder
{
	private VertexFormat format;
	private CoordTransformer transformer = new CoordTransformer();
	private float minX = 0.0F;
	private float minY = 0.0F;
	private float minZ = 0.0F;
	private float maxX = 1.0F;
	private float maxY = 1.0F;
	private float maxZ = 1.0F;
	private float red = 1.0F;
	private float green = 1.0F;
	private float blue = 1.0F;
	private float alpha = 1.0F;
	public int tidx = -1;
	private TextureAtlasSprite icon;
	private EnumFacing facing;
	private boolean applyDiffuseLighting = true;
	public boolean renderXNeg = true;
	public boolean renderXPos = true;
	public boolean renderYNeg = true;
	public boolean renderYPos = true;
	public boolean renderZNeg = true;
	public boolean renderZPos = true;
	
	public static ModelQuadBuilder newInstance()
	{
		return new ModelQuadBuilder();
	}
	
	private ModelQuadBuilder() { }
	
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
		this.red   = ((rgba >> 24)       ) / 255.0F;
		this.green = ((rgba >> 16) & 0xFF) / 255.0F;
		this.blue  = ((rgba >> 8 ) & 0xFF) / 255.0F;
		this.alpha = ((rgba      ) & 0xFF) / 255.0F;
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
	
	public void putCubeQuads(Executable<BakedQuad> executable, float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		setBound(minX, minY, minZ, maxX, maxY, maxZ);
		if(this.renderYNeg) executable.execute(bakeYNegFace());
		if(this.renderYPos) executable.execute(bakeYPosFace());
		if(this.renderZNeg) executable.execute(bakeZNegFace());
		if(this.renderZPos) executable.execute(bakeZPosFace());
		if(this.renderXNeg) executable.execute(bakeXNegFace());
		if(this.renderXPos) executable.execute(bakeXPosFace());
	}
	
	public void putCubeQuads(ImmutableList.Builder<BakedQuad> executable, float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		putCubeQuads(quad -> executable.add(quad), minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public void putCubeQuads(Collection<BakedQuad> executable, float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		putCubeQuads(quad -> executable.add(quad), minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	public List<BakedQuad> bakeCube()
	{
		List<BakedQuad> list = new ArrayList();
		if(this.renderYNeg) list.add(bakeYNegFace());
		if(this.renderYPos) list.add(bakeYPosFace());
		if(this.renderZNeg) list.add(bakeZNegFace());
		if(this.renderZPos) list.add(bakeZPosFace());
		if(this.renderXNeg) list.add(bakeXNegFace());
		if(this.renderXPos) list.add(bakeXPosFace());
		return list;
	}
	
	public BakedQuad bakeYPosFace()
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.format);
		builder.setQuadTint(this.tidx);
		builder.setTexture(this.icon);
		builder.setQuadOrientation(EnumFacing.UP);
		builder.setApplyDiffuseLighting(this.applyDiffuseLighting);
		float minU = this.icon.getInterpolatedU(this.minX * 16.0F);
		float maxU = this.icon.getInterpolatedU(this.maxX * 16.0F);
		float minV = this.icon.getInterpolatedV(this.minZ * 16.0F);
		float maxV = this.icon.getInterpolatedV(this.maxZ * 16.0F);
		putVertex(builder, this.format, this.minX, this.maxY, this.minZ, minU, minV, Direction.U, false);
		putVertex(builder, this.format, this.minX, this.maxY, this.maxZ, minU, maxV, Direction.U, false);
		putVertex(builder, this.format, this.maxX, this.maxY, this.maxZ, maxU, maxV, Direction.U, false);
		putVertex(builder, this.format, this.maxX, this.maxY, this.minZ, maxU, minV, Direction.U, false);
		return builder.build();
	}
	
	public BakedQuad bakeYNegFace()
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.format);
		builder.setQuadTint(this.tidx);
		builder.setTexture(this.icon);
		builder.setQuadOrientation(EnumFacing.DOWN);
		builder.setApplyDiffuseLighting(this.applyDiffuseLighting);
		float minU = this.icon.getInterpolatedU(this.minX * 16.0F);
		float maxU = this.icon.getInterpolatedU(this.maxX * 16.0F);
		float minV = this.icon.getInterpolatedV(16F - this.maxZ * 16.0F);
		float maxV = this.icon.getInterpolatedV(16F - this.minZ * 16.0F);
		putVertex(builder, this.format, this.minX, this.maxY, this.maxZ, minU, minV, Direction.D, false);
		putVertex(builder, this.format, this.minX, this.maxY, this.minZ, minU, maxV, Direction.D, false);
		putVertex(builder, this.format, this.maxX, this.maxY, this.minZ, maxU, maxV, Direction.D, false);
		putVertex(builder, this.format, this.maxX, this.maxY, this.maxZ, maxU, minV, Direction.D, false);
		return builder.build();
	}
	
	public BakedQuad bakeXPosFace()
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.format);
		builder.setQuadTint(this.tidx);
		builder.setTexture(this.icon);
		builder.setQuadOrientation(EnumFacing.EAST);
		builder.setApplyDiffuseLighting(this.applyDiffuseLighting);
		float minU = this.icon.getInterpolatedU(16F - this.maxZ * 16.0F);
		float maxU = this.icon.getInterpolatedU(16F - this.minZ * 16.0F);
		float minV = this.icon.getInterpolatedV(16F - this.maxY * 16.0F);
		float maxV = this.icon.getInterpolatedV(16F - this.minY * 16.0F);
		putVertex(builder, this.format, this.maxX, this.maxY, this.maxZ, minU, minV, Direction.E, false);
		putVertex(builder, this.format, this.maxX, this.minY, this.maxZ, minU, maxV, Direction.E, false);
		putVertex(builder, this.format, this.maxX, this.minY, this.minZ, maxU, maxV, Direction.E, false);
		putVertex(builder, this.format, this.maxX, this.maxY, this.minZ, maxU, minV, Direction.E, false);
		return builder.build();
	}
	
	public BakedQuad bakeXNegFace()
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.format);
		builder.setQuadTint(this.tidx);
		builder.setTexture(this.icon);
		builder.setQuadOrientation(EnumFacing.WEST);
		builder.setApplyDiffuseLighting(this.applyDiffuseLighting);
		float minU = this.icon.getInterpolatedU(this.minZ * 16.0F);
		float maxU = this.icon.getInterpolatedU(this.maxZ * 16.0F);
		float minV = this.icon.getInterpolatedV(16F - this.maxY * 16.0F);
		float maxV = this.icon.getInterpolatedV(16F - this.minY * 16.0F);
		putVertex(builder, this.format, this.minX, this.maxY, this.minZ, minU, minV, Direction.W, false);
		putVertex(builder, this.format, this.minX, this.minY, this.minZ, minU, maxV, Direction.W, false);
		putVertex(builder, this.format, this.minX, this.minY, this.maxZ, maxU, maxV, Direction.W, false);
		putVertex(builder, this.format, this.minX, this.maxY, this.maxZ, maxU, minV, Direction.W, false);
		return builder.build();
	}
	
	public BakedQuad bakeZPosFace()
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.format);
		builder.setQuadTint(this.tidx);
		builder.setTexture(this.icon);
		builder.setQuadOrientation(EnumFacing.SOUTH);
		builder.setApplyDiffuseLighting(this.applyDiffuseLighting);
		float minU = this.icon.getInterpolatedU(this.minX * 16.0F);
		float maxU = this.icon.getInterpolatedU(this.maxX * 16.0F);
		float minV = this.icon.getInterpolatedV(16F - this.maxY * 16.0F);
		float maxV = this.icon.getInterpolatedV(16F - this.minY * 16.0F);
		putVertex(builder, this.format, this.minX, this.maxY, this.maxZ, minU, minV, Direction.S, false);
		putVertex(builder, this.format, this.minX, this.minY, this.maxZ, minU, maxV, Direction.S, false);
		putVertex(builder, this.format, this.maxX, this.minY, this.maxZ, maxU, maxV, Direction.S, false);
		putVertex(builder, this.format, this.maxX, this.maxY, this.maxZ, maxU, minV, Direction.S, false);
		return builder.build();
	}
	
	public BakedQuad bakeZNegFace()
	{
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(this.format);
		builder.setQuadTint(this.tidx);
		builder.setTexture(this.icon);
		builder.setQuadOrientation(EnumFacing.NORTH);
		builder.setApplyDiffuseLighting(this.applyDiffuseLighting);
		float minU = this.icon.getInterpolatedU(16F - this.maxX * 16.0F);
		float maxU = this.icon.getInterpolatedU(16F - this.minX * 16.0F);
		float minV = this.icon.getInterpolatedV(16F - this.maxY * 16.0F);
		float maxV = this.icon.getInterpolatedV(16F - this.minY * 16.0F);
		putVertex(builder, this.format, this.maxX, this.maxY, this.minZ, minU, minV, Direction.N, false);
		putVertex(builder, this.format, this.maxX, this.minY, this.minZ, minU, maxV, Direction.N, false);
		putVertex(builder, this.format, this.minX, this.minY, this.minZ, maxU, maxV, Direction.N, false);
		putVertex(builder, this.format, this.minX, this.maxY, this.minZ, maxU, minV, Direction.N, false);
		return builder.build();
	}
	
	private void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, float x, float y, float z, float u, float v, Direction face, boolean oppisite)
	{
		putVertex(builder, format, x, y, z, u, v, face.x, face.y, face.z, oppisite);
	}
	private void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, float x, float y, float z, float u, float v, float normalX, float normalY, float normalZ, boolean oppisite)
	{
		for(int e = 0; e < format.getElementCount(); e++)
		{
			switch(format.getElement(e).getUsage())
			{
			case POSITION:
			{
				builder.put(e, x, y, z, 1);
				break;
			}
			case COLOR:
			{
				builder.put(e, this.red, this.green, this.blue, this.alpha);
				break;
			}
			case UV: if(format.getElement(e).getIndex() == 0)
			{
				builder.put(e, u, v, 0F, 1F);
				break;
			}
			case NORMAL:
			{
				Vector3f vector3f = new Vector3f(normalX, normalY, normalZ);
				this.transformer.transform(vector3f);
				if(!oppisite)
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
	
	
	public VertexFormat getFormat()
	{
		return this.format;
	}
}