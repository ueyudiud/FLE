package farcore.render;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.vecmath.Vector3f;

import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import farcore.render.BlockModelBakery.ModelBlockFLE;

public class BlockDeserializer
{
	public static JsonDeserializer newBlockModelDeserializer()
	{
		return new JsonDeserializer<ModelBlockFLE>() 
				{
            public ModelBlockFLE parseModelBlock(JsonElement element, Type type, JsonDeserializationContext context)
            {
                JsonObject jsonobject = element.getAsJsonObject();
                List list = this.getModelElements(context, jsonobject);
                String s = this.getParent(jsonobject);
                boolean flag = StringUtils.isEmpty(s);
                boolean flag1 = list.isEmpty();

                if (flag1 && flag)
                {
                    throw new JsonParseException("BlockModel requires either elements or parent, found neither");
                }
                else if (!flag && !flag1)
                {
                    throw new JsonParseException("BlockModel requires either elements or parent, found both");
                }
                else
                {
                    Map map = this.getTextures(jsonobject);
                    boolean flag2 = this.getAmbientOcclusionEnabled(jsonobject);
                    ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;

                    if (jsonobject.has("display"))
                    {
                        JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "display");
                        itemcameratransforms = (ItemCameraTransforms)context.deserialize(jsonobject1, ItemCameraTransforms.class);
                    }

                    return flag1 ? new ModelBlockFLE(new ResourceLocation(s), map, flag2, true, itemcameratransforms) : new ModelBlockFLE(list, map, flag2, true, itemcameratransforms);
                }
            }

            private Map getTextures(JsonObject obj)
            {
                HashMap hashmap = Maps.newHashMap();

                if (obj.has("textures"))
                {
                    JsonObject jsonobject1 = obj.getAsJsonObject("textures");
                    Iterator iterator = jsonobject1.entrySet().iterator();

                    while (iterator.hasNext())
                    {
                        Entry entry = (Entry)iterator.next();
                        hashmap.put(entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
                    }
                }

                return hashmap;
            }

            private String getParent(JsonObject obj)
            {
                return JsonUtils.getJsonObjectStringFieldValueOrDefault(obj, "parent", "");
            }

            protected boolean getAmbientOcclusionEnabled(JsonObject obj)
            {
                return JsonUtils.getJsonObjectBooleanFieldValueOrDefault(obj, "ambientocclusion", true);
            }

            protected List getModelElements(JsonDeserializationContext context, JsonObject obj)
            {
                ArrayList arraylist = Lists.newArrayList();

                if (obj.has("elements"))
                {
                    Iterator iterator = JsonUtils.getJsonObjectJsonArrayField(obj, "elements").iterator();

                    while (iterator.hasNext())
                    {
                        JsonElement jsonelement = (JsonElement)iterator.next();
                        arraylist.add((BlockPart)context.deserialize(jsonelement, BlockPart.class));
                    }
                }

                return arraylist;
            }

            public ModelBlockFLE deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            {
                return this.parseModelBlock(element, type, context);
            }
		};
	}
	
	public static JsonDeserializer newBlockPartDeserializer()
	{
		return new JsonDeserializer<BlockPart>()
				{
			public BlockPart parseBlockPart(JsonElement p_178254_1_, Type p_178254_2_, JsonDeserializationContext p_178254_3_)
            {
                JsonObject jsonobject = p_178254_1_.getAsJsonObject();
                Vector3f vector3f = this.parsePositionFrom(jsonobject);
                Vector3f vector3f1 = this.parsePositionTo(jsonobject);
                BlockPartRotation blockpartrotation = this.parseRotation(jsonobject);
                Map map = this.parseFacesCheck(p_178254_3_, jsonobject);

                if (jsonobject.has("shade") && !JsonUtils.isJsonObjectBooleanField(jsonobject, "shade"))
                {
                    throw new JsonParseException("Expected shade to be a Boolean");
                }
                else
                {
                    boolean flag = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "shade", true);
                    return new BlockPart(vector3f, vector3f1, map, blockpartrotation, flag);
                }
            }

            private BlockPartRotation parseRotation(JsonObject p_178256_1_)
            {
                BlockPartRotation blockpartrotation = null;

                if (p_178256_1_.has("rotation"))
                {
                    JsonObject jsonobject1 = JsonUtils.getJsonObject(p_178256_1_, "rotation");
                    Vector3f vector3f = this.parsePosition(jsonobject1, "origin");
                    vector3f.scale(0.0625F);
                    EnumFacing.Axis axis = this.parseAxis(jsonobject1);
                    float f = this.parseAngle(jsonobject1);
                    boolean flag = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject1, "rescale", false);
                    blockpartrotation = new BlockPartRotation(vector3f, axis, f, flag);
                }

                return blockpartrotation;
            }

            private float parseAngle(JsonObject p_178255_1_)
            {
                float f = JsonUtils.getJsonObjectFloatFieldValue(p_178255_1_, "angle");

                if (f != 0.0F && MathHelper.abs(f) != 22.5F && MathHelper.abs(f) != 45.0F)
                {
                    throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
                }
                else
                {
                    return f;
                }
            }

            private EnumFacing.Axis parseAxis(JsonObject p_178252_1_)
            {
                String s = JsonUtils.getJsonObjectStringFieldValue(p_178252_1_, "axis");
                EnumFacing.Axis axis = EnumFacing.Axis.byName(s.toLowerCase());

                if (axis == null)
                {
                    throw new JsonParseException("Invalid rotation axis: " + s);
                }
                else
                {
                    return axis;
                }
            }

            private Map parseFacesCheck(JsonDeserializationContext p_178250_1_, JsonObject p_178250_2_)
            {
                Map map = this.parseFaces(p_178250_1_, p_178250_2_);

                if (map.isEmpty())
                {
                    throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
                }
                else
                {
                    return map;
                }
            }

            private Map parseFaces(JsonDeserializationContext p_178253_1_, JsonObject p_178253_2_)
            {
                EnumMap enummap = Maps.newEnumMap(EnumFacing.class);
                JsonObject jsonobject1 = JsonUtils.getJsonObject(p_178253_2_, "faces");
                Iterator iterator = jsonobject1.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Entry entry = (Entry)iterator.next();
                    EnumFacing enumfacing = this.parseEnumFacing((String)entry.getKey());
                    enummap.put(enumfacing, (BlockPartFace)p_178253_1_.deserialize((JsonElement)entry.getValue(), BlockPartFace.class));
                }

                return enummap;
            }

            private EnumFacing parseEnumFacing(String p_178248_1_)
            {
                EnumFacing enumfacing = EnumFacing.byName(p_178248_1_);

                if (enumfacing == null)
                {
                    throw new JsonParseException("Unknown facing: " + p_178248_1_);
                }
                else
                {
                    return enumfacing;
                }
            }

            private Vector3f parsePositionTo(JsonObject p_178247_1_)
            {
                Vector3f vector3f = this.parsePosition(p_178247_1_, "to");

                if (vector3f.x >= -16.0F && vector3f.y >= -16.0F && vector3f.z >= -16.0F && vector3f.x <= 32.0F && vector3f.y <= 32.0F && vector3f.z <= 32.0F)
                {
                    return vector3f;
                }
                else
                {
                    throw new JsonParseException("\'to\' specifier exceeds the allowed boundaries: " + vector3f);
                }
            }

            private Vector3f parsePositionFrom(JsonObject p_178249_1_)
            {
                Vector3f vector3f = this.parsePosition(p_178249_1_, "from");

                if (vector3f.x >= -16.0F && vector3f.y >= -16.0F && vector3f.z >= -16.0F && vector3f.x <= 32.0F && vector3f.y <= 32.0F && vector3f.z <= 32.0F)
                {
                    return vector3f;
                }
                else
                {
                    throw new JsonParseException("\'from\' specifier exceeds the allowed boundaries: " + vector3f);
                }
            }

            private Vector3f parsePosition(JsonObject p_178251_1_, String p_178251_2_)
            {
                JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(p_178251_1_, p_178251_2_);

                if (jsonarray.size() != 3)
                {
                    throw new JsonParseException("Expected 3 " + p_178251_2_ + " values, found: " + jsonarray.size());
                }
                else
                {
                    float[] afloat = new float[3];

                    for (int i = 0; i < afloat.length; ++i)
                    {
                        afloat[i] = JsonUtils.getJsonElementFloatValue(jsonarray.get(i), p_178251_2_ + "[" + i + "]");
                    }

                    return new Vector3f(afloat);
                }
            }

            public BlockPart deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseBlockPart(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
		};
	}
	
	public static JsonDeserializer newBlockFaceDeserializer()
	{
		return new JsonDeserializer<BlockPartFace>()
		{
			public BlockPartFace parseBlockPartFace(JsonElement element, Type p_178338_2_, JsonDeserializationContext p_178338_3_)
			{
				JsonObject jsonobject = element.getAsJsonObject();
				EnumFacing enumfacing = this.parseCullFace(jsonobject);
				int i = this.parseTintIndex(jsonobject);
				String s = this.parseTexture(jsonobject);
				BlockFaceUV blockfaceuv = (BlockFaceUV)p_178338_3_.deserialize(jsonobject, BlockFaceUV.class);
				return new BlockPartFace(enumfacing, i, s, blockfaceuv);
			}

			protected int parseTintIndex(JsonObject p_178337_1_)
			{
				return JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178337_1_, "tintindex", -1);
			}
			
			private String parseTexture(JsonObject p_178340_1_)
			{
				return JsonUtils.getJsonObjectStringFieldValue(p_178340_1_, "texture");
			}

			private EnumFacing parseCullFace(JsonObject p_178339_1_)
			{
				String s = JsonUtils.getJsonObjectStringFieldValueOrDefault(p_178339_1_, "cullface", "");
				return EnumFacing.byName(s);
			}

			public BlockPartFace deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
			{
				return this.parseBlockPartFace(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
			}
		};
	}
	
	public static JsonDeserializer newBlockFaceUVDeserializer()
	{
		return new JsonDeserializer<BlockFaceUV>()
		{
			public BlockFaceUV parseBlockFaceUV(JsonElement p_178293_1_, Type p_178293_2_, JsonDeserializationContext p_178293_3_)
            {
                JsonObject jsonobject = p_178293_1_.getAsJsonObject();
                float[] afloat = this.parseUV(jsonobject);
                int i = this.parseRotation(jsonobject);
                return new BlockFaceUV(afloat, i);
            }

            protected int parseRotation(JsonObject p_178291_1_)
            {
                int i = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178291_1_, "rotation", 0);

                if (i >= 0 && i % 90 == 0 && i / 90 <= 3)
                {
                    return i;
                }
                else
                {
                    throw new JsonParseException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");
                }
            }

            private float[] parseUV(JsonObject p_178292_1_)
            {
                if (!p_178292_1_.has("uv"))
                {
                    return null;
                }
                else
                {
                    JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(p_178292_1_, "uv");

                    if (jsonarray.size() != 4)
                    {
                        throw new JsonParseException("Expected 4 uv values, found: " + jsonarray.size());
                    }
                    else
                    {
                        float[] afloat = new float[4];

                        for (int i = 0; i < afloat.length; ++i)
                        {
                            afloat[i] = JsonUtils.getJsonElementFloatValue(jsonarray.get(i), "uv[" + i + "]");
                        }

                        return afloat;
                    }
                }
            }

            public BlockFaceUV deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseBlockFaceUV(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
		};
	}
	
	public static JsonDeserializer newItemTransformVec3fDeserializer()
	{
		return new JsonDeserializer<ItemTransformVec3f>()
				{
			public ItemTransformVec3f deserialize0(JsonElement element, Type type, JsonDeserializationContext context)
		    {
		        JsonObject jsonobject = element.getAsJsonObject();
		        Vector3f vector3f = this.parseVector3f(jsonobject, "rotation", ROTATION_DEFAULT);
		        Vector3f vector3f1 = this.parseVector3f(jsonobject, "translation", TRANSLATION_DEFAULT);
		        vector3f1.scale(0.0625F);
		        MathHelper.clamp_double((double)vector3f1.x, -1.5D, 1.5D);
		        MathHelper.clamp_double((double)vector3f1.y, -1.5D, 1.5D);
		        MathHelper.clamp_double((double)vector3f1.z, -1.5D, 1.5D);
		        Vector3f vector3f2 = this.parseVector3f(jsonobject, "scale", SCALE_DEFAULT);
		        MathHelper.clamp_double((double)vector3f2.x, -1.5D, 1.5D);
		        MathHelper.clamp_double((double)vector3f2.y, -1.5D, 1.5D);
		        MathHelper.clamp_double((double)vector3f2.z, -1.5D, 1.5D);
		        return new ItemTransformVec3f(vector3f, vector3f1, vector3f2);
		    }

		    private Vector3f parseVector3f(JsonObject jsonObject, String key, Vector3f defaultValue)
		    {
		        if (!jsonObject.has(key))
		        {
		            return defaultValue;
		        }
		        else
		        {
		            JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(jsonObject, key);

		            if (jsonarray.size() != 3)
		            {
		                throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
		            }
		            else
		            {
		                float[] afloat = new float[3];

		                for (int i = 0; i < afloat.length; ++i)
		                {
		                    afloat[i] = JsonUtils.getJsonElementFloatValue(jsonarray.get(i), key + "[" + i + "]");
		                }

		                return new Vector3f(afloat);
		            }
		        }
		    }

		    public ItemTransformVec3f deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
		    {
		        return this.deserialize0(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
		    }
		};
	}
	
	public static JsonDeserializer newItemCameraTransformsDeserializer()
	{
		return new JsonDeserializer<ItemCameraTransforms>()
				{
			public ItemCameraTransforms parseItemCameraTransforms(JsonElement p_178352_1_, Type p_178352_2_, JsonDeserializationContext p_178352_3_)
            {
                JsonObject jsonobject = p_178352_1_.getAsJsonObject();
                ItemTransformVec3f itemtransformvec3f = ItemTransformVec3f.DEFAULT;
                ItemTransformVec3f itemtransformvec3f1 = ItemTransformVec3f.DEFAULT;
                ItemTransformVec3f itemtransformvec3f2 = ItemTransformVec3f.DEFAULT;
                ItemTransformVec3f itemtransformvec3f3 = ItemTransformVec3f.DEFAULT;

                if (jsonobject.has("thirdperson"))
                {
                    itemtransformvec3f = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("thirdperson"), ItemTransformVec3f.class);
                }

                if (jsonobject.has("firstperson"))
                {
                    itemtransformvec3f1 = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("firstperson"), ItemTransformVec3f.class);
                }

                if (jsonobject.has("head"))
                {
                    itemtransformvec3f2 = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("head"), ItemTransformVec3f.class);
                }

                if (jsonobject.has("gui"))
                {
                    itemtransformvec3f3 = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("gui"), ItemTransformVec3f.class);
                }

                return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3);
            }

            public ItemCameraTransforms deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseItemCameraTransforms(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
		};
	}
	
	
	private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
    private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);
}