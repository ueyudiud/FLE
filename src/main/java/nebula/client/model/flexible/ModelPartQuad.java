/*
 * copyright© 2016-2018 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.function.Function;

import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.client.model.flexible.INebulaBakedModelPart.BakedModelPart;
import nebula.client.util.BakedQuadBuilder;
import nebula.client.util.CoordTransformer;
import nebula.client.util.IIconCollection;
import nebula.client.util.IModelModifier;
import nebula.client.util.MultiQuadBuilder;
import nebula.common.util.Jsons;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelPartQuad implements INebulaModelPart
{
	private static final float[][]
			ATTACH_UP    = { { 0.0F,15.2F, 0.0F}, { 0.0F,15.2F,16.0F}, {16.0F,15.2F,16.0F}, {16.0F,15.2F, 0.0F} },
			ATTACH_DOWN  = { { 0.0F, 0.8F, 0.0F}, {16.0F, 0.8F, 0.0F}, {16.0F, 0.8F,16.0F}, { 0.0F, 0.8F,16.0F} },
			ATTACH_NORTH = { { 0.0F,16.0F, 0.8F}, { 0.0F, 0.0F, 0.8F}, {16.0F, 0.0F, 0.8F}, {16.0F,16.0F, 0.8F} },
			ATTACH_SOUTH = { {16.0F,16.0F,15.2F}, {16.0F, 0.0F,15.2F}, { 0.0F, 0.0F,15.2F}, { 0.0F,16.0F,15.2F} },
			ATTACH_WEST  = { { 0.8F,16.0F,16.0F}, { 0.8F, 0.0F,16.0F}, { 0.8F, 0.0F, 0.0F}, { 0.8F,16.0F, 0.0F} },
			ATTACH_EAST  = { {15.2F,16.0F, 0.0F}, {15.2F, 0.0F, 0.0F}, {15.2F, 0.0F,16.0F}, {15.2F,16.0F,16.0F} },
			CROSSING_1   = { { 0.8F,16.0F, 0.8F}, { 0.8F, 0.0F, 0.8F}, {15.2F, 0.0F,15.2F}, {15.2F,16.0F,15.2F} },
			CROSSING_2   = { {15.2F,16.0F, 0.8F}, {15.2F, 0.0F, 0.8F}, { 0.8F, 0.0F,15.2F}, { 0.8F,16.0F,15.2F} };
	
	public static final JsonDeserializer<INebulaModelPart> LOADER = (json, typeOfT, context) -> {
		JsonObject object = json.getAsJsonObject();
		ModelPartQuad part = new ModelPartQuad();
		part.tintindex = Jsons.getOrDefault(object, "tint", -1);
		part.renderTwoFace = Jsons.getOrDefault(object, "biface", true);
		JsonElement json1 = object.get("pos");
		if (json1.isJsonArray())
		{
			if (json1.getAsJsonArray().get(0).isJsonArray())// Full position  data.
			{
				part.pos = Jsons.getArray(json1.getAsJsonArray(), 4, float[].class, j -> Jsons.getFloatArray(j.getAsJsonArray(), 3));
			}
			else// x, y, z, w, h
			{
				float[] datas1 = Jsons.getFloatArray(json1.getAsJsonArray(), 5);
				part.pos = new float[][] { { datas1[0], datas1[1], datas1[2] }, { datas1[0] + datas1[3], datas1[1], datas1[2] }, { datas1[0] + datas1[3], datas1[1], datas1[2] + datas1[4] }, { datas1[0], datas1[1], datas1[2] + datas1[4] } };
			}
		}
		else if (json1.isJsonPrimitive())
		{
			switch (json1.getAsString())
			{
			case "attach_up":
				part.pos = ATTACH_UP;
				break;
			case "attach_down":
				part.pos = ATTACH_DOWN;
				break;
			case "attach_north":
				part.pos = ATTACH_NORTH;
				break;
			case "attach_south":
				part.pos = ATTACH_SOUTH;
				break;
			case "attach_west":
				part.pos = ATTACH_WEST;
				break;
			case "attach_east":
				part.pos = ATTACH_EAST;
				break;
			case "cross1":
				part.pos = CROSSING_1;
				break;
			case "cross2":
				part.pos = CROSSING_2;
				break;
			default:
				throw new JsonParseException("The '" + json1.getAsString() + "' does not exist in postion preset collection.");
			}
		}
		else// TODO
		{
			throw new JsonParseException("Unsupported position tag: " + json1);
		}
		if (object.has("uv"))
		{
			float[] datas = Jsons.getFloatArray(object, "uv", 4);
			part.textureMinU = datas[0];
			part.textureMinV = datas[1];
			part.textureMaxU = datas[2];
			part.textureMaxV = datas[3];
		}
		else
		{
			part.textureMinU = 0.0F;
			part.textureMinV = 0.0F;
			part.textureMaxU = 16.0F;
			part.textureMaxV = 16.0F;
		}
		part.icon = Jsons.getOrDefault(object, "texture", "#all");
		return part;
	};
	
	int tintindex;
	
	boolean renderTwoFace;
	
	float[][]			pos;
	CoordTransformer	transformer;
	
	float textureMinU, textureMaxU, textureMinV, textureMaxV;
	
	float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
	
	private String icon;
	
	@Override
	public Collection<String> getResources()
	{
		return ImmutableList.of(this.icon);
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		IModelModifier modifier = new ModelModifierByCoordTransformer(transformation, this.transformer);
		MultiQuadBuilder builder = new MultiQuadBuilder(format, modifier);
		builder.setTIndex(this.tintindex);
		BakedQuadBuilder builder1 = builder.getBuilder();
		// n=P3P1xP4P2
		Vector3f vec1 = new Vector3f(this.pos[2][0] - this.pos[0][0], this.pos[2][1] - this.pos[0][1], this.pos[2][2] - this.pos[0][2]);
		Vector3f vec2 = new Vector3f(this.pos[3][0] - this.pos[1][0], this.pos[3][1] - this.pos[1][1], this.pos[3][2] - this.pos[1][2]);
		vec1.cross(vec1, vec2);
		vec1.normalize();
		
		builder1.startQuad(modifier.rotateFacing(EnumFacing.getFacingFromVector(vec1.x, vec1.y, vec1.z)));
		builder1.normal(vec1.x, vec1.y, vec1.z);
		builder1.color(this.red, this.green, this.blue, this.alpha);
		float[] u = { this.textureMinU, this.textureMinU, this.textureMaxU, this.textureMaxU };
		float[] v = { this.textureMinV, this.textureMaxV, this.textureMaxV, this.textureMinV };
		for (int i = 0; i < 4; ++i)
		{
			builder1.pos(this.pos[i][0] / 16.0F, this.pos[i][1] / 16.0F, this.pos[i][2] / 16.0F, u[i], v[i]);
		}
		builder1.endQuad();
		if (this.renderTwoFace)
		{
			builder1.startQuad(modifier.rotateFacing(EnumFacing.DOWN));
			builder1.normal(vec1.x, vec1.y, vec1.z);
			builder1.color(this.red, this.green, this.blue, this.alpha);
			for (int i = 0; i < 4; ++i)
			{
				int i1 = i ^ 0x3;
				builder1.pos(this.pos[i1][0] / 16.0F, this.pos[i1][1] / 16.0F, this.pos[i1][2] / 16.0F, u[i1], v[i1]);
			}
			builder1.endQuad();
		}
		return new BakedModelPart(builder.bake(bakedTextureGetter::apply, iconHandlerGetter.apply(this.icon)));
	}
}
