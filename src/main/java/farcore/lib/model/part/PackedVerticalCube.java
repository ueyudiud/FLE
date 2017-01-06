/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.google.common.collect.ImmutableList;

import farcore.lib.util.CoordTransformer;
import farcore.lib.util.Direction;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class PackedVerticalCube implements IFarCoreModelPart
{
	float[] xyzPos = {0, 0, 0, 1, 1, 1};
	float[][] uvPos = {
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16}};
	
	byte renderFlag;
	CoordTransformer transformer = new CoordTransformer();
	//	boolean renderTwoFace;
	boolean uvLock;
	
	int rotateX = 0;
	int rotateY = 0;
	
	float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
	
	boolean fullCube = true;
	
	String[] location = new String[6];
	
	public CoordTransformer getTransformer()
	{
		return this.transformer;
	}
	
	public void setBound(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		this.xyzPos[0] = minX;
		this.xyzPos[1] = minY;
		this.xyzPos[2] = minZ;
		this.xyzPos[3] = maxX;
		this.xyzPos[4] = maxY;
		this.xyzPos[5] = maxZ;
		minX *= 16; minY *= 16; minZ *= 16;
		maxX *= 16; maxY *= 16; maxZ *= 16;
		this.uvPos[0][0] = minX; this.uvPos[0][1] = maxZ; this.uvPos[0][2] = maxX; this.uvPos[0][3] = minZ;
		this.uvPos[1][0] = minX; this.uvPos[1][1] = minZ; this.uvPos[1][2] = maxX; this.uvPos[1][3] = maxZ;
		this.uvPos[2][0] = maxX; this.uvPos[2][1] = maxY; this.uvPos[2][2] = minX; this.uvPos[2][3] = minY;
		this.uvPos[3][0] = minX; this.uvPos[3][1] = maxY; this.uvPos[3][2] = maxX; this.uvPos[3][3] = minY;
		this.uvPos[4][0] = maxZ; this.uvPos[4][1] = maxY; this.uvPos[4][2] = minZ; this.uvPos[4][3] = minY;
		this.uvPos[5][0] = minZ; this.uvPos[5][1] = maxY; this.uvPos[5][2] = maxZ; this.uvPos[5][3] = minY;
	}
	
	public void setFaceData(Direction facing, String location)
	{
		this.renderFlag |= facing.flag;
		this.location[facing.ordinal()] = location;
	}
	
	public void setFaceData(Direction facing, float u1, float v1, float u2, float v2, String location)
	{
		if(this.uvLock) throw new IllegalStateException("The lockedUV model part can not remark texture position.");
		this.renderFlag |= facing.flag;
		this.location[facing.ordinal()] = location;
		float[] uv = this.uvPos[facing.ordinal()];
		uv[0] = u1;
		uv[1] = v1;
		uv[2] = u2;
		uv[3] = v2;
	}
	
	public void setResourceLocation(String location)
	{
		Arrays.fill(this.location, location);
		this.renderFlag = 0x3F;
	}
	
	public void setUVLock(boolean uvLock)
	{
		this.uvLock = uvLock;
	}
	
	//	public void setRenderTwoFace(boolean renderTwoFace)
	//	{
	//		this.renderTwoFace = renderTwoFace;
	//	}
	
	public void setRotate(int rotateX, int rotateY)
	{
		this.rotateX = rotateX;
		this.rotateY = rotateY;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}
	
	@Override
	public Collection<String> getTextures()
	{
		return ImmutableList.copyOf(this.location);
	}
	
	@Override
	public IFarCoreBakedModelPart bake(TRSRTransformation transformation, VertexFormat format,
			Function<String, TextureAtlasSprite> textureGetter)
	{
		Direction direction;
		TextureAtlasSprite icon;
		float[][] coords;
		ModelRotation rotation = ModelRotation.getModelRotation((this.rotateX & 0x3) * 90, (this.rotateY & 0x3) * 90);
		ArrayList<BakedQuad> list = new ArrayList();
		if((this.renderFlag & 0x1) != 0)
		{
			icon = textureGetter.apply(this.location[0]);
			direction = Direction.of(rotation, EnumFacing.UP);
			coords = new float[][]{
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[2], this.uvPos[0][0], this.uvPos[0][1]},
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[5], this.uvPos[0][0], this.uvPos[0][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[5], this.uvPos[0][2], this.uvPos[0][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[2], this.uvPos[0][2], this.uvPos[0][1]}
			};
			putFace(list, coords, icon, format, transformation, direction, rotation);
		}
		if((this.renderFlag & 0x2) != 0)
		{
			icon = textureGetter.apply(this.location[1]);
			direction = Direction.of(rotation, EnumFacing.DOWN);
			coords = new float[][]{
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[5], this.uvPos[1][0], this.uvPos[1][1]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[2], this.uvPos[1][0], this.uvPos[1][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[2], this.uvPos[1][2], this.uvPos[1][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[5], this.uvPos[1][2], this.uvPos[1][1]}
			};
			putFace(list, coords, icon, format, transformation, direction, rotation);
		}
		if((this.renderFlag & 0x4) != 0)
		{
			icon = textureGetter.apply(this.location[2]);
			direction = Direction.of(rotation, EnumFacing.NORTH);
			coords = new float[][]{
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[2], this.uvPos[2][0], this.uvPos[2][1]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[2], this.uvPos[2][0], this.uvPos[2][3]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[2], this.uvPos[2][2], this.uvPos[2][3]},
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[2], this.uvPos[2][2], this.uvPos[2][1]}
			};
			putFace(list, coords, icon, format, transformation, direction, rotation);
		}
		if((this.renderFlag & 0x8) != 0)
		{
			icon = textureGetter.apply(this.location[3]);
			direction = Direction.of(rotation, EnumFacing.SOUTH);
			coords = new float[][]{
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[5], this.uvPos[3][0], this.uvPos[3][1]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[5], this.uvPos[3][0], this.uvPos[3][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[5], this.uvPos[3][2], this.uvPos[3][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[5], this.uvPos[3][2], this.uvPos[3][1]}
			};
			putFace(list, coords, icon, format, transformation, direction, rotation);
		}
		if((this.renderFlag & 0x10) != 0)
		{
			icon = textureGetter.apply(this.location[4]);
			direction = Direction.of(rotation, EnumFacing.WEST);
			coords = new float[][]{
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[2], this.uvPos[4][0], this.uvPos[4][1]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[2], this.uvPos[4][0], this.uvPos[4][3]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[5], this.uvPos[4][2], this.uvPos[4][3]},
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[5], this.uvPos[4][2], this.uvPos[4][1]}
			};
			putFace(list, coords, icon, format, transformation, direction, rotation);
		}
		if((this.renderFlag & 0x20) != 0)
		{
			icon = textureGetter.apply(this.location[5]);
			direction = Direction.of(rotation, EnumFacing.EAST);
			coords = new float[][]{
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[5], this.uvPos[5][0], this.uvPos[5][1]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[5], this.uvPos[5][0], this.uvPos[5][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[2], this.uvPos[5][2], this.uvPos[5][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[2], this.uvPos[5][2], this.uvPos[5][1]}
			};
			putFace(list, coords, icon, format, transformation, direction, rotation);
		}
		return IFarCoreBakedModelPart.bake(list);
	}
	
	private void putFace(List<BakedQuad> list, float[][] coords, TextureAtlasSprite icon, VertexFormat format, TRSRTransformation transformation, Direction face, ModelRotation rotation)
	{
		Matrix4f matrix = rotation.getMatrix();
		UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
		for(float[] coord : coords)
		{
			Point3f point = new Point3f(coord);
			matrix.transform(point);//Transform source coordinate to rotated coordinate.
			coord[0] = point.x;
			coord[1] = point.y;
			coord[2] = point.z;
			float u, v;
			if(this.uvLock)
			{
				switch (face)
				{
				case U :
					u = coord[0] * 16F;
					v = coord[2] * 16F;
					break;
				case D :
					u = coord[0] * 16F;
					v = (1F - coord[2]) * 16F;
					break;
				case N :
					u = (1F - coord[0]) * 16F;
					v = coord[1] * 16F;
					break;
				case S :
					u = coord[0] * 16F;
					v = coord[1] * 16F;
					break;
				case W :
					u = (1F - coord[2]) * 16F;
					v = coord[1] * 16F;
					break;
				case E :
					u = coord[2] * 16F;
					v = coord[1] * 16F;
					break;
				default: throw new IllegalArgumentException("Unknown facing!");
				}
			}
			else
			{
				u = coord[3];
				v = coord[4];
			}
			this.transformer.transform(coord);
			putVertex(builder, format, transformation,
					coord[0], coord[1], coord[2], icon.getInterpolatedU(u), icon.getInterpolatedV(v), face, false);
		}
		list.add(builder.build());
	}
	
	private void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, TRSRTransformation transform, float x, float y, float z, float u, float v, Direction facing, boolean oppisite)
	{
		if(this.fullCube)
		{
			builder.setQuadOrientation(facing.of());
		}
		for(int e = 0; e < format.getElementCount(); e++)
		{
			switch(format.getElement(e).getUsage())
			{
			case POSITION:
				if(transform != TRSRTransformation.identity())
				{
					Vector4f vec = new Vector4f();
					vec.x = x;
					vec.y = y;
					vec.z = z;
					vec.w = 1;
					transform.getMatrix().transform(vec);
					builder.put(e, vec.x, vec.y, vec.z, vec.w);
				}
				else
				{
					builder.put(e, x, y, z, 1);
				}
				break;
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
				Vector3f vector3f = new Vector3f(facing.x, facing.y, facing.z);
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
			default:
				builder.put(e);
				break;
			}
		}
	}
}