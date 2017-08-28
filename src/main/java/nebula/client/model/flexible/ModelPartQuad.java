/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.function.Function;

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

/**
 * @author ueyudiud
 */
public class ModelPartQuad implements INebulaModelPart
{
	public static final JsonDeserializer<INebulaModelPart> LOADER = (json, typeOfT, context)-> {
		JsonObject object = json.getAsJsonObject();
		ModelPartQuad part = new ModelPartQuad();
		part.tintindex = Jsons.getOrDefault(object, "tint", -1);
		part.renderTwoFace = Jsons.getOrDefault(object, "biface", true);
		JsonElement json1 = object.get("pos");
		if (json1.isJsonArray())
		{
			if (json1.getAsJsonArray().get(0).isJsonArray())//Full position data
			{
				part.pos = Jsons.getArray(json1.getAsJsonArray(), 4, float[].class,
						j->Jsons.getFloatArray(j.getAsJsonArray(), 3));
			}
			else//x, y, z, w, h
			{
				float[] datas1 = Jsons.getFloatArray(json1.getAsJsonArray(), 5);
				part.pos = new float[][] {
					{datas1[0]            , datas1[1], datas1[2]            },
					{datas1[0] + datas1[3], datas1[1], datas1[2]            },
					{datas1[0] + datas1[3], datas1[1], datas1[2] + datas1[4]},
					{datas1[0]            , datas1[1], datas1[2] + datas1[4]}};
			}
		}
		else//TODO
		{
			throw new JsonParseException("Unsupported position tag: " + json1);
		}
		float[] datas = Jsons.getFloatArray(object, "uv", 4);
		part.textureMinU = datas[0];
		part.textureMinV = datas[1];
		part.textureMaxU = datas[2];
		part.textureMaxV = datas[3];
		part.icon = Jsons.getOrDefault(object, "texture", "#all");
		return part;
	};
	
	int tintindex;
	
	boolean renderTwoFace;
	
	float[][] pos;
	CoordTransformer transformer;
	
	float textureMinU, textureMaxU, textureMinV, textureMaxV;
	
	float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
	
	private String icon;
	
	@Override
	public Collection<String> getResources()
	{
		return ImmutableList.of(this.icon);
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		IModelModifier modifier = new ModelModifierByCoordTransformer(transformation, this.transformer);
		MultiQuadBuilder builder = new MultiQuadBuilder(format, modifier);
		builder.setTIndex(this.tintindex);
		BakedQuadBuilder builder1 = builder.getBuilder();
		
		builder1.startQuad(modifier.rotateFacing(EnumFacing.UP));
		builder1.normal(0, 1, 0);
		builder1.color(this.red, this.green, this.blue, this.alpha);
		builder1.pos(this.pos[0][0], this.pos[0][1], this.pos[0][2], this.textureMinU, this.textureMinV);
		builder1.pos(this.pos[1][0], this.pos[1][1], this.pos[1][2], this.textureMinU, this.textureMaxV);
		builder1.pos(this.pos[2][0], this.pos[2][1], this.pos[2][2], this.textureMaxU, this.textureMaxV);
		builder1.pos(this.pos[3][0], this.pos[3][1], this.pos[3][2], this.textureMaxU, this.textureMinV);
		builder1.endQuad();
		if (this.renderTwoFace)
		{
			builder1.startQuad(modifier.rotateFacing(EnumFacing.DOWN));
			builder1.normal(0, -1, 0);
			builder1.color(this.red, this.green, this.blue, this.alpha);
			builder1.pos(this.pos[0][0], this.pos[0][1], this.pos[0][2], this.textureMinU, this.textureMinV);
			builder1.pos(this.pos[3][0], this.pos[3][1], this.pos[3][2], this.textureMaxU, this.textureMinV);
			builder1.pos(this.pos[2][0], this.pos[2][1], this.pos[2][2], this.textureMaxU, this.textureMaxV);
			builder1.pos(this.pos[1][0], this.pos[1][1], this.pos[1][2], this.textureMinU, this.textureMaxV);
			builder1.endQuad();
		}
		return new BakedModelPart(builder.bake(bakedTextureGetter::apply, iconHandlerGetter.apply(this.icon)));
	}
}