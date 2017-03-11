/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.model.part;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.client.model.INebulaBakedModelPart;
import nebula.client.model.part.INebulaModelPart.INebulaRetexturableModelPart;
import nebula.client.util.CoordTransformer;
import nebula.common.base.ArrayListAddWithCheck;
import nebula.common.util.Direction;
import nebula.common.util.Jsons;
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
public class PackedVerticalCube implements INebulaRetexturableModelPart
{
	static final IModelPartLoader<PackedVerticalCube> LOADER = (parent, object, context) ->
	{
		PackedVerticalCube result = new PackedVerticalCube();
		if (parent == null)
		{
			parent = new PackedVerticalCube();
		}
		if (object.has("uvLock"))
		{
			result.setUVLock(object.get("uvLock").getAsBoolean());
		}
		else
		{
			result.setUVLock(parent.uvLock);
		}
		boolean flag = object.has("face");
		if (object.has("pos"))
		{
			JsonElement element1 = object.get("pos");
			if(element1.isJsonArray())
			{
				float[] array = Jsons.getFloatArray(object, "pos", 6);
				if(array.length != 6)
					throw new JsonParseException("Position array length is 6, got " + array.length);
				if(flag)
				{
					result.xyzPos = array;
				}
				else
				{
					result.setBound(array[0] / 16.0F, array[1] / 16.0F, array[2] / 16.0F, array[3] / 16.0F, array[4] / 16.0F, array[5] / 16.0F);
				}
			}
			else throw new JsonParseException("Invalid position type.");
		}
		else
		{
			result.xyzPos = parent.xyzPos.clone();
		}
		if (flag)
		{
			JsonArray array = object.getAsJsonArray("face");
			for (JsonElement element : array)
			{
				loadFaceData(result, element);
			}
		}
		if (object.has("rotation"))
		{
			int[] rotation = Jsons.getIntArray(object, "rotation", 2);
			result.setRotate(rotation[0], rotation[1]);
		}
		if (object.has("rgba"))
		{
			float[] rgba = Jsons.getFloatArray(object, "rgba", 3);
			result.setColor(rgba[0], rgba[1], rgba[2], rgba.length > 3 ? rgba[3] : 1.0F);
		}
		return result;
	};
	
	static void loadFaceData(PackedVerticalCube cube, JsonElement json) throws JsonParseException
	{
		if (!json.isJsonObject()) throw new JsonParseException("The face data should be json object.");
		JsonObject object = json.getAsJsonObject();
		
		String side = object.get("side").getAsString();
		String location = object.get("texture").getAsString();
		if (object.has("uv"))
		{
			float[] uv = Jsons.getFloatArray(object, "uv", 4);
			cube.setFaceData(Direction.valueOf(side), uv[0], uv[1], uv[2], uv[3], location);
		}
		else
		{
			cube.setFaceData(Direction.valueOf(side), location);
		}
	}
	
	static CoordTransformer decodeTransformer(PackedVerticalCube cube, CoordTransformer target, CoordTransformer source, JsonObject from) throws JsonParseException
	{
		boolean type = !from.has("modify") || from.get("modify").getAsBoolean();
		if(from.has("transform"))
		{
			double[] array = Jsons.getDoubleArray(from, "transform", 3);
			if(type)
			{
				target.addTransform(array[0], array[1], array[2]);
			}
			else
			{
				target.setTransform(array[0], array[1], array[2]);
			}
		}
		else
		{
			target.setTransform(source);
		}
		if(from.has("oppisite"))
		{
			JsonElement element = from.get("oppisite");
			if(element.isJsonPrimitive())
			{
				switch (element.getAsString())
				{
				case "center" :
					target.setOppisite((cube.xyzPos[0] + cube.xyzPos[3]) / 2, (cube.xyzPos[1] + cube.xyzPos[4]) / 2, (cube.xyzPos[2] + cube.xyzPos[5]) / 2);
					break;
				case "start" :
					target.setOppisite(0, 0, 0);
					break;
				default: throw new JsonParseException("The '" + element.getAsString() + "' is unknown position in PackedQuadData.");
				}
			}
			else if(element.isJsonArray())
			{
				double[] array = Jsons.getDoubleArray(from, "oppisite", 3);
				target.setOppisite(array[0], array[1], array[2]);
			}
			else throw new JsonParseException("Invalid oppisite type.");
		}
		else
		{
			target.setOppisite(source);
		}
		if(from.has("rotation"))
		{
			if (type)
			{
				target.setRotation(source);
			}
			JsonElement element = from.get("rotation");
			if(element.isJsonArray())
			{
				double[] array = Jsons.getDoubleArray(from, "rotation", 4);
				target.applyRotation(array[0], array[1], array[2], array[3]);
			}
			else if (element.isJsonObject())
			{
				JsonObject object = from.getAsJsonObject();
				String t = object.get("type").getAsString();
				switch (t)
				{
				case "quad" :
					double[] array = Jsons.getDoubleArray(from, "array", 4);
					target.applyRotation(array[0], array[1], array[2], array[3]);
					break;
				case "xy" :
					int[] xy = Jsons.getIntArray(object, "array", 2);
					target.applyRotation(ModelRotation.getModelRotation(xy[0], xy[1]));
					break;
				default: throw new JsonParseException("Unknown rotation type '" + type + "'.");
				}
			}
			else throw new JsonParseException("Unknown rotation data.");
		}
		else
		{
			target.setRotation(source);
		}
		return target;
	}
	
	float[] xyzPos = {0, 0, 0, 1, 1, 1};
	float[][] uvPos = {
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16}};
	
	byte renderFlag = 0x3F;
	CoordTransformer transformer = new CoordTransformer();
	//	boolean renderTwoFace;
	boolean uvLock;
	
	int rotateX = 0;
	int rotateY = 0;
	
	float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
	
	boolean fullCube = true;
	
	String[] location = new String[6];
	
	@Override
	protected PackedVerticalCube clone()
	{
		PackedVerticalCube cube;
		try
		{
			cube = (PackedVerticalCube) super.clone();
			cube.transformer = this.transformer.copy();
			cube.xyzPos = this.xyzPos.clone();
			cube.uvPos = this.uvPos.clone();
			cube.location = this.location.clone();
			return cube;
		}
		catch (CloneNotSupportedException exception)
		{
			return new PackedVerticalCube();
		}
	}
	
	public CoordTransformer getTransformer()
	{
		return this.transformer;
	}
	
	public void setBound(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
	{
		this.fullCube = false;
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
		if (location == null) return;
		this.renderFlag |= facing.flag;
		this.location[facing.ordinal()] = location;
	}
	
	public void setFaceData(Direction facing, float u1, float v1, float u2, float v2, String location)
	{
		if (location == null) return;
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
		List<String> list = ArrayListAddWithCheck.requireNonnull();
		for (String loc : this.location) list.add(loc);
		return ImmutableList.copyOf(list);
	}
	
	@Override
	public PackedVerticalCube retexture(Map<String, String> textures)
	{
		PackedVerticalCube cube = clone();
		if (textures.containsKey("all"))
		{
			cube.setResourceLocation(textures.get("all"));
		}
		else
		{
			cube.setFaceData(Direction.D, textures.getOrDefault("down", cube.location[0]));
			cube.setFaceData(Direction.U, textures.getOrDefault("up", cube.location[1]));
			cube.setFaceData(Direction.S, textures.getOrDefault("south", cube.location[2]));
			cube.setFaceData(Direction.N, textures.getOrDefault("north", cube.location[3]));
			cube.setFaceData(Direction.E, textures.getOrDefault("east", cube.location[4]));
			cube.setFaceData(Direction.W, textures.getOrDefault("west", cube.location[5]));
		}
		if (cube.location[0] != null && cube.location[0].charAt(0) == '#') cube.location[0] = textures.getOrDefault(cube.location[0].substring(1), cube.location[0]);
		if (cube.location[1] != null && cube.location[1].charAt(0) == '#') cube.location[1] = textures.getOrDefault(cube.location[1].substring(1), cube.location[1]);
		if (cube.location[2] != null && cube.location[2].charAt(0) == '#') cube.location[2] = textures.getOrDefault(cube.location[2].substring(1), cube.location[2]);
		if (cube.location[3] != null && cube.location[3].charAt(0) == '#') cube.location[3] = textures.getOrDefault(cube.location[3].substring(1), cube.location[3]);
		if (cube.location[4] != null && cube.location[4].charAt(0) == '#') cube.location[4] = textures.getOrDefault(cube.location[4].substring(1), cube.location[4]);
		if (cube.location[5] != null && cube.location[5].charAt(0) == '#') cube.location[5] = textures.getOrDefault(cube.location[5].substring(1), cube.location[5]);
		return cube;
	}
	
	@Override
	public INebulaBakedModelPart bake(TRSRTransformation transformation, VertexFormat format,
			Function<String, TextureAtlasSprite> textureGetter)
	{
		Direction direction;
		TextureAtlasSprite icon;
		float[][] coords;
		ModelRotation rotation = ModelRotation.getModelRotation((this.rotateX & 0x3) * 90, (this.rotateY & 0x3) * 90);
		ArrayList<BakedQuad> list = new ArrayList();
		if((this.renderFlag & 0x1) != 0 && this.location[0] != null)
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
		if((this.renderFlag & 0x2) != 0 && this.location[1] != null)
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
		if((this.renderFlag & 0x4) != 0 && this.location[2] != null)
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
		if((this.renderFlag & 0x8) != 0 && this.location[3] != null)
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
		if((this.renderFlag & 0x10) != 0 && this.location[4] != null)
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
		if((this.renderFlag & 0x20) != 0 && this.location[5] != null)
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
		return INebulaBakedModelPart.bake(list);
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