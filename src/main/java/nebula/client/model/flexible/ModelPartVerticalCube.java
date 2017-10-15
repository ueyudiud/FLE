/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import nebula.base.ArrayListAddWithCheck;
import nebula.client.util.BakedQuadBuilder;
import nebula.client.util.BakedQuadRetex;
import nebula.client.util.CoordTransformer;
import nebula.client.util.IIconCollection;
import nebula.client.util.IModelModifier;
import nebula.common.util.A;
import nebula.common.util.Direction;
import nebula.common.util.Jsons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
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
public class ModelPartVerticalCube implements INebulaModelPart, Cloneable
{
	static final JsonDeserializer<ModelPartVerticalCube> LOADER = (json, typeOfT, context) ->
	{
		JsonObject object = json.getAsJsonObject();
		ModelPartVerticalCube part = new ModelPartVerticalCube();
		part.tint = Jsons.getOrDefault(object, "tint", -1);
		boolean flag = object.has("face") || object.has("faces");
		if (object.has("pos"))
		{
			JsonElement element1 = object.get("pos");
			if(element1.isJsonArray())
			{
				float[] array = Jsons.getFloatArray(object, "pos", 6);
				if(array.length != 6)
					throw new JsonParseException("Position array length is 6, got " + array.length);
				part.xyzPos = array;
				part.fullCube = false;
				part.normalizeUVPos();
				if (flag)
					part.renderFlag = 0;
			}
		}
		else if (object.has("from")/* && object.has("to") */)//Minecraft loader.
		{
			float[] array1 = Jsons.getFloatArray(object, "from", 3);
			float[] array2 = Jsons.getFloatArray(object, "to", 3);
			part.xyzPos = new float[]{array1[0], array1[1], array1[2], array2[0], array2[1], array2[2]};
			part.fullCube = false;
			part.normalizeUVPos();
			if (flag)
				part.renderFlag = 0;
		}
		if (object.has("layer"))//For normal layer offset, expand cube size to make sure the new layer cover will on.
		{
			int layer = object.get("layer").getAsInt();
			part.xyzPos[0] -= 1.6E-3F * layer;
			part.xyzPos[1] -= 1.6E-3F * layer;
			part.xyzPos[2] -= 1.6E-3F * layer;
			part.xyzPos[3] += 1.6E-3F * layer;
			part.xyzPos[4] += 1.6E-3F * layer;
			part.xyzPos[5] += 1.6E-3F * layer;
		}
		if (flag)
		{
			if (object.has("face"))
			{
				JsonArray array = object.getAsJsonArray("face");
				for (JsonElement element : array)
				{
					loadFaceData(part, element);
				}
			}
			else
			{
				JsonObject object1 = object.getAsJsonObject("faces");
				for (Entry<String, JsonElement> entry : object1.entrySet())
				{
					loadFaceData(part, EnumFacing.byName(entry.getKey()), entry.getValue());
				}
			}
		}
		return part;
	};
	
	static void loadFaceData(ModelPartVerticalCube cube, EnumFacing facing, JsonElement json) throws JsonParseException
	{
		if (!json.isJsonObject()) throw new JsonParseException("The face data should be json object.");
		JsonObject object = json.getAsJsonObject();
		
		int side = facing.getIndex();
		cube.renderFlag |= 1 << side;
		String location = object.get("texture").getAsString();
		if (object.has("uv"))
		{
			float[] uv = Jsons.getFloatArray(object, "uv", 4);
			cube.uvPos[side] = uv;
		}
		cube.icons[side] = location;
	}
	
	static void loadFaceData(ModelPartVerticalCube cube, JsonElement json) throws JsonParseException
	{
		if (!json.isJsonObject()) throw new JsonParseException("The face data should be json object.");
		JsonObject object = json.getAsJsonObject();
		
		Direction direction = Direction.valueOf(object.get("side").getAsString());
		int side = direction.ordinal();
		cube.renderFlag |= direction.flag;
		String location = object.get("texture").getAsString();
		if (object.has("uv"))
		{
			float[] uv = Jsons.getFloatArray(object, "uv", 4);
			cube.uvPos[side] = uv;
		}
		cube.icons[side] = location;
	}
	
	float[] xyzPos = {0, 0, 0, 16, 16, 16};
	float[][] uvPos = {
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16},
			{0, 0, 16, 16}};
	
	byte renderFlag = 0x3F;
	CoordTransformer transformer = new CoordTransformer();
	boolean uvLock;
	
	int tint = -1;
	
	//	int rotateX = 0;
	//	int rotateY = 0;
	
	float red = 1.0F, green = 1.0F, blue = 1.0F, alpha = 1.0F;
	
	boolean fullCube = true;
	String[] icons = new String[6];
	
	private void normalizeUVPos()
	{
		this.uvPos[0][0] = this.xyzPos[0]; this.uvPos[0][1] = 16.0F - this.xyzPos[5]; this.uvPos[0][2] = this.xyzPos[3]; this.uvPos[0][3] = 16.0F - this.xyzPos[2];
		this.uvPos[1][0] = this.xyzPos[0]; this.uvPos[1][1] = this.xyzPos[2]; this.uvPos[1][2] = this.xyzPos[3]; this.uvPos[1][3] = this.xyzPos[5];
		this.uvPos[2][0] = 16.0F - this.xyzPos[3]; this.uvPos[2][1] = 16.0F - this.xyzPos[4]; this.uvPos[2][2] = 16.0F - this.xyzPos[0]; this.uvPos[2][3] = 16.0F - this.xyzPos[1];
		this.uvPos[3][0] = this.xyzPos[0]; this.uvPos[3][1] = 16.0F - this.xyzPos[4]; this.uvPos[3][2] = this.xyzPos[3]; this.uvPos[3][3] = 16.0F - this.xyzPos[1];
		this.uvPos[4][0] = this.xyzPos[5]; this.uvPos[4][1] = 16.0F - this.xyzPos[4]; this.uvPos[4][2] = this.xyzPos[2]; this.uvPos[4][3] = 16.0F - this.xyzPos[1];
		this.uvPos[5][0] = 16.0F - this.xyzPos[2]; this.uvPos[5][1] = 16.0F - this.xyzPos[4]; this.uvPos[5][2] = 16.0F - this.xyzPos[5]; this.uvPos[5][3] = 16.0F - this.xyzPos[1];
	}
	
	@Override
	public Collection<String> getResources()
	{
		List<String> list = ArrayListAddWithCheck.requireNonnull();
		list.addAll(ArrayListAddWithCheck.argument(this.icons));
		return list;
	}
	
	private float[][][] datas()
	{
		return new float[][][]{
			{
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[5], this.uvPos[0][0], this.uvPos[0][1]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[2], this.uvPos[0][0], this.uvPos[0][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[2], this.uvPos[0][2], this.uvPos[0][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[5], this.uvPos[0][2], this.uvPos[0][1]}
			},
			{
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[2], this.uvPos[1][0], this.uvPos[1][1]},
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[5], this.uvPos[1][0], this.uvPos[1][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[5], this.uvPos[1][2], this.uvPos[1][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[2], this.uvPos[1][2], this.uvPos[1][1]}
			},
			{
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[2], this.uvPos[2][0], this.uvPos[2][1]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[2], this.uvPos[2][0], this.uvPos[2][3]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[2], this.uvPos[2][2], this.uvPos[2][3]},
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[2], this.uvPos[2][2], this.uvPos[2][1]}
			},
			{
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[5], this.uvPos[3][0], this.uvPos[3][1]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[5], this.uvPos[3][0], this.uvPos[3][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[5], this.uvPos[3][2], this.uvPos[3][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[5], this.uvPos[3][2], this.uvPos[3][1]}
			},
			{
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[2], this.uvPos[4][0], this.uvPos[4][1]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[2], this.uvPos[4][0], this.uvPos[4][3]},
				{this.xyzPos[0], this.xyzPos[1], this.xyzPos[5], this.uvPos[4][2], this.uvPos[4][3]},
				{this.xyzPos[0], this.xyzPos[4], this.xyzPos[5], this.uvPos[4][2], this.uvPos[4][1]}
			},
			{
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[5], this.uvPos[5][0], this.uvPos[5][1]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[5], this.uvPos[5][0], this.uvPos[5][3]},
				{this.xyzPos[3], this.xyzPos[1], this.xyzPos[2], this.uvPos[5][2], this.uvPos[5][3]},
				{this.xyzPos[3], this.xyzPos[4], this.xyzPos[2], this.uvPos[5][2], this.uvPos[5][1]}
			}
		};
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		IModelModifier modifier = new ModelModifierByCoordTransformer(transformation, this.transformer);
		float[][][] datas = datas();
		Map<String, BakedQuad>[] quadMaps = new Map[6];
		for (int i = 0; i < 6; ++i)
		{
			final int i0 = i;
			BakedQuadBuilder builder = new BakedQuadBuilder(format, modifier,
					quad-> {
						quadMaps[i0] = ImmutableMap.copyOf(
								Maps.transformValues(iconHandlerGetter.apply(this.icons[i0]).build(),
										loc->new BakedQuadRetex(quad, bakedTextureGetter.apply(loc))));
					});
			if ((this.renderFlag & (1 << i)) != 0 && this.icons[i] != null)
			{
				EnumFacing facing;
				builder.startQuad(facing = EnumFacing.VALUES[i], this.tint, Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite());
				builder.normal(facing.getFrontOffsetX(), facing.getFrontOffsetY(), facing.getFrontOffsetZ());
				builder.color(this.red, this.green, this.blue, this.alpha);
				for (float[] array : datas[i])
					builder.pos(array[0] / 16.0F, array[1] / 16.0F, array[2] / 16.0F, array[3], array[4]);
				builder.endQuad();
			}
		}
		return new BakedModelPartFullCube(this.fullCube, quadMaps);
	}
	
	@Override
	protected ModelPartVerticalCube clone()
	{
		try
		{
			return (ModelPartVerticalCube) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError(e);
		}
	}
	
	@Override
	public INebulaModelPart retexture(Map<String, String> retexture)
	{
		String[] icons = this.icons.clone();
		ModelPartVerticalCube part = clone();
		part.icons = icons;
		if (retexture.containsKey("all"))
		{
			Arrays.fill(icons, retexture.get("all"));
		}
		for (Entry<String, String> entry : retexture.entrySet())
		{
			switch (entry.getKey())
			{
			case "down" : icons[0] = entry.getValue(); break;
			case "up"   : icons[1] = entry.getValue(); break;
			case "north": icons[2] = entry.getValue(); break;
			case "south": icons[3] = entry.getValue(); break;
			case "west" : icons[4] = entry.getValue(); break;
			case "east" : icons[5] = entry.getValue(); break;
			default: break;
			}
		}
		return part;
	}
	
	static class BakedModelPartFullCube implements INebulaBakedModelPart
	{
		final boolean fullCube;
		final Map<String, BakedQuad>[] quads;
		
		BakedModelPartFullCube(boolean fullCube, Map<String, BakedQuad>[] quadMaps)
		{
			this.fullCube = fullCube;
			this.quads = quadMaps;
		}
		
		@Override
		public List<BakedQuad> getQuads(EnumFacing facing, String key)
		{
			if (this.fullCube)
			{
				return facing == null ||
						this.quads[facing.getIndex()] == null ||
						!this.quads[facing.getIndex()].containsKey(key) ? ImmutableList.of() :
							ImmutableList.of(this.quads[facing.getIndex()].get(key));
			}
			else
			{
				if (facing == null)
				{
					List<BakedQuad> list = ArrayListAddWithCheck.requireNonnull();
					A.executeAll(this.quads, m-> { if (m != null) list.add(m.get(key)); });
					return list;
				}
				else return ImmutableList.of();
			}
		}
	}
}