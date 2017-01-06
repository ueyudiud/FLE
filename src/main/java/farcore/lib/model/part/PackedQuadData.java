/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.part;

import java.util.Collection;
import java.util.function.Function;

import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import farcore.lib.util.CoordTransformer;
import farcore.util.Jsons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public class PackedQuadData implements IFarCoreModelPart
{
	static final IModelPartLoader<PackedQuadData> LOADER = (parent, object, context) ->
	{
		PackedQuadData result = new PackedQuadData();
		if(parent == null)
		{
			parent = new PackedQuadData();
		}
		if(object.has("pos"))
		{
			JsonElement element1 = object.get("pos");
			if(element1.isJsonArray())
			{
				float[] array = Jsons.getFloatArray(object, "pos", 3);
				if(array.length != 5 && array.length != 3)
					throw new JsonParseException("Position array length is 5, got " + array.length);
				result.x = array[0];
				result.y = array[1];
				result.z = array[2];
				if(array.length == 5)
				{
					result.w = array[3];
					result.h = array[4];
				}
				else
				{
					result.w = parent.w;
					result.h = parent.h;
				}
			}
			else if(element1.isJsonObject())
			{
				JsonObject object1 = element1.getAsJsonObject();
				if(object1.has("x")) result.x = object.get("x").getAsFloat();
				else result.x = parent.x;
				if(object1.has("y")) result.y = object.get("y").getAsFloat();
				else result.y = parent.y;
				if(object1.has("z")) result.z = object.get("z").getAsFloat();
				else result.z = parent.z;
				if(object1.has("w")) result.w = object.get("w").getAsFloat();
				else result.w = parent.w;
				if(object1.has("h")) result.h = object.get("h").getAsFloat();
				else result.h = parent.h;
			}
			else throw new JsonParseException("Invalid position type.");
		}
		else
		{
			result.x = parent.x;
			result.y = parent.y;
			result.z = parent.z;
			result.w = parent.w;
			result.h = parent.h;
		}
		if(object.has("normal"))
		{
			result.customNormal = true;
			float[] normal = Jsons.getFloatArray(object, "normal", 3);
			result.normalX = normal[0];
			result.normalY = normal[1];
			result.normalZ = normal[2];
		}
		else if(parent.customNormal)
		{
			result.customNormal = true;
			result.normalX = parent.normalX;
			result.normalY = parent.normalY;
			result.normalZ = parent.normalZ;
		}
		if(object.has("color"))
		{
			JsonElement element = object.get("color");
			if(element.isJsonArray())
			{
				float[] color = Jsons.getFloatArray(object, "color", 3);
				result.red = color[0];
				result.green = color[1];
				result.blue = color[2];
				if(color.length == 4)
				{
					result.alpha = color[3];
				}
				else
				{
					result.alpha = 1.0F;
				}
			}
			else
			{
				int index = element.getAsInt();
				result.red   = ((index >> 24) & 0xFF) / 255.0F;
				result.green = ((index >> 16) & 0xFF) / 255.0F;
				result.blue  = ((index >>  8) & 0xFF) / 255.0F;
				result.alpha = ((index      ) & 0xFF) / 255.0F;
			}
		}
		else
		{
			result.red = parent.red;
			result.green = parent.green;
			result.blue = parent.blue;
			result.alpha = parent.alpha;
		}
		if(object.has("uv"))
		{
			float[] uv = Jsons.getFloatArray(object, "uv", 4);
			result.textureMinU = uv[0];
			result.textureMinV = uv[1];
			result.textureMaxU = uv[2];
			result.textureMaxV = uv[3];
		}
		else
		{
			result.textureMinU = parent.textureMinU;
			result.textureMinV = parent.textureMinV;
			result.textureMaxU = parent.textureMaxU;
			result.textureMaxV = parent.textureMaxV;
		}
		if(object.has("renderTwoFace"))
		{
			result.renderTwoFace = object.get("renderTwoFace").getAsBoolean();
		}
		else
		{
			result.renderTwoFace = parent.renderTwoFace;
		}
		if(object.has("transform"))
		{
			JsonElement element1 = object.get("transform");
			if(element1.isJsonPrimitive())
			{
				String tag = element1.getAsString();
				switch (tag)
				{
				case "identity" ://Default setting
					break;
				default: throw new JsonParseException("No '" + tag + "' transform exist.");
				}
			}
			else if(element1.isJsonObject())
			{
				JsonObject object1 = element1.getAsJsonObject();
				decodeTransformer(result, result.getTransformer(), parent.getTransformer(), object1);
			}
			else throw new JsonParseException("Invalid transfrom type.");
		}
		else
		{
			result.transformer = parent.getTransformer().copy();
		}
		return result;
	};
	
	static CoordTransformer decodeTransformer(PackedQuadData data, CoordTransformer target, CoordTransformer source, JsonObject from) throws JsonParseException
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
					target.setOppisite(data.w / 2, 0, data.h / 2);
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
		if(from.has("rotation"))
		{
			JsonElement element = from.get("rotation");
			if(element.isJsonPrimitive())
			{
				
			}
		}
		return target;
	}
	
	boolean renderTwoFace;
	
	float x, y, z, w, h;
	CoordTransformer transformer = new CoordTransformer();
	
	float textureMinU, textureMaxU, textureMinV, textureMaxV;
	
	boolean customNormal = false;
	float normalX;
	float normalY;
	float normalZ;
	
	float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
	
	String location;
	
	public void setPos(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setWH(float w, float h)
	{
		this.w = w;
		this.h = h;
	}
	
	public void setUV(float u1, float v1, float u2, float v2)
	{
		this.textureMinU = u1;
		this.textureMinV = v1;
		this.textureMaxU = u2;
		this.textureMaxV = v2;
	}
	
	public CoordTransformer getTransformer()
	{
		return this.transformer;
	}
	
	public void setResourceLocation(String location)
	{
		this.location = location;
	}
	
	public void setColor(float r, float g, float b, float a)
	{
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}
	
	public void normalize()
	{
		if(!this.customNormal)
		{
			Vector3f vector3f = new Vector3f(0F, 1F, 0F);
			this.transformer.normal(vector3f);
			this.normalX = vector3f.getX();
			this.normalY = vector3f.getY();
			this.normalZ = vector3f.getZ();
		}
	}
	
	@Override
	public Collection<String> getTextures()
	{
		return ImmutableList.of(this.location);
	}
	
	@Override
	public IFarCoreBakedModelPart bake(TRSRTransformation transformation, VertexFormat format,
			Function<String, TextureAtlasSprite> textureGetter)
	{
		normalize();
		TextureAtlasSprite icon = textureGetter.apply(this.location);
		float u1 = icon.getInterpolatedU(this.textureMinU);
		float u2 = icon.getInterpolatedU(this.textureMaxU);
		float v1 = icon.getInterpolatedV(this.textureMinV);
		float v2 = icon.getInterpolatedV(this.textureMaxV);
		float[][] coords = {
				{this.x         , this.y, this.z         , u1, v1},
				{this.x         , this.y, this.z + this.h, u1, v2},
				{this.x + this.w, this.y, this.z + this.h, u2, v2},
				{this.x + this.w, this.y, this.z         , u2, v1}};
		if(!this.renderTwoFace)
		{
			UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
			builder.setTexture(icon);
			for(float[] coord : coords)
			{
				this.transformer.transform(coord);//Transform coordinate.
				putVertex(builder, format, transformation, coord[0], coord[1], coord[2], coord[3], coord[4], false);
			}
			return IFarCoreBakedModelPart.bake(builder.build());
		}
		else
		{
			UnpackedBakedQuad.Builder builder1 = new UnpackedBakedQuad.Builder(format);
			UnpackedBakedQuad.Builder builder2 = new UnpackedBakedQuad.Builder(format);
			builder1.setTexture(icon);
			builder2.setTexture(icon);
			for(float[] coord : coords)
			{
				this.transformer.transform(coord);//Transform coordinate.
				putVertex(builder1, format, transformation, coord[0], coord[1], coord[2], coord[3], coord[4], false);
				putVertex(builder2, format, transformation, coord[0], coord[1], coord[2], coord[3], coord[4], true);
			}
			return IFarCoreBakedModelPart.bake(builder1.build(), builder2.build());
		}
	}
	
	private void putVertex(UnpackedBakedQuad.Builder builder, VertexFormat format, TRSRTransformation transform, float x, float y, float z, float u, float v, boolean oppisite)
	{
		Vector4f vec = new Vector4f();
		for(int e = 0; e < format.getElementCount(); e++)
		{
			switch(format.getElement(e).getUsage())
			{
			case POSITION:
				if(transform != TRSRTransformation.identity())
				{
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
				if(!oppisite)
				{
					builder.put(e, this.normalX, this.normalY, this.normalZ, 0f);
				}
				else
				{
					builder.put(e, -this.normalX, -this.normalY, -this.normalZ, 0f);
				}
				break;
			default:
				builder.put(e);
				break;
			}
		}
	}
}