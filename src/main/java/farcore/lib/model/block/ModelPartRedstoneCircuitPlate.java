/*
 * copyright© 2016-2017 ueyudiud
 */
package farcore.lib.model.block;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.vecmath.Matrix4f;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializer;

import farcore.data.MC;
import farcore.data.SubTags;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.material.Mat;
import nebula.Log;
import nebula.client.model.flexible.INebulaBakedModelPart;
import nebula.client.model.flexible.INebulaDirectResourcesModelPart;
import nebula.client.model.flexible.IRetexturableNebulaModelPart;
import nebula.client.model.flexible.ModelModifierByCoordTransformer;
import nebula.client.util.BakedQuadBuilder;
import nebula.client.util.IIconCollection;
import nebula.common.util.L;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public class ModelPartRedstoneCircuitPlate implements IRetexturableNebulaModelPart, INebulaDirectResourcesModelPart
{
	public static final JsonDeserializer<ModelPartRedstoneCircuitPlate> DESERIALIZER = (json, typeOfT, context)->
	new ModelPartRedstoneCircuitPlate();
	
	private ResourceLocation layer;
	private String texture;//Default for all rocks.
	
	private float plateHeight = .125F;
	
	public ModelPartRedstoneCircuitPlate()
	{
		textures = null;//Clean cache.
	}
	ModelPartRedstoneCircuitPlate(ResourceLocation layer)
	{
		this.layer = layer;
	}
	
	/**
	 * For game was always crashing when I debugging.<br>
	 * It might takes too much memory to build quad, so I tried cached those quads.
	 * @author ueyudiud
	 */
	private static final Map<ResourceLocation, List<BakedQuad>[]> bakedQuads = new HashMap<>();
	
	@Override
	public Collection<String> getResources()
	{
		return ImmutableList.of(this.texture);
	}
	
	@Override
	public Collection<ResourceLocation> getDirectResources()
	{
		return ImmutableList.of(this.layer);
	}
	
	private static Map<Mat, TextureAtlasSprite> textures;
	
	static Map<Mat, TextureAtlasSprite> getTextures()
	{
		if (textures == null)
		{
			textures = Maps.toMap(Mat.filt(SubTags.ROCK), material->MaterialTextureLoader.getIcon(material, MC.stone));
		}
		return textures;
	}
	
	@Override
	public BakedModelPartRedstoneCircuitPlate bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		TextureAtlasSprite iconDef = Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
		List<BakedQuad>[] quads = bakedQuads.computeIfAbsent(this.layer, l-> {
			TextureAtlasSprite layer = bakedTextureGetter.apply(l);
			@SuppressWarnings("unchecked")
			List<BakedQuad>[] result = new List[4];
			for (EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				TRSRTransformation transformation2 = transformation == TRSRTransformation.identity() ? new TRSRTransformation(facing) : transform(facing, transformation);
				result[facing.getHorizontalIndex()] = buildQuads(iconDef, layer, format, transformation2);
			}
			return result;
		});
		getTextures();
		return new BakedModelPartRedstoneCircuitPlate(quads);
	}
	
	private static TRSRTransformation transform(EnumFacing facing, TRSRTransformation transformation)
	{
		Matrix4f matrix = new Matrix4f();
		matrix.mul(transformation.getMatrix(), TRSRTransformation.getMatrix(facing));
		return new TRSRTransformation(matrix);
	}
	
	private List<BakedQuad> buildQuads(TextureAtlasSprite texture, TextureAtlasSprite layer, VertexFormat format, TRSRTransformation transformation)
	{
		if(texture.getIconWidth() != layer.getIconWidth() || texture.getIconHeight() != layer.getIconHeight())
		{
			Log.warn("The icon {%s, %s} pixel size is not same, plase use same size icon for uses, because it need take height render.", texture.getIconName(), layer.getIconName());
			return ImmutableList.of();
		}
		int u = texture.getIconWidth();
		int v = texture.getIconHeight();
		byte[] height = new byte[u * v];
		ImmutableList.Builder<BakedQuad> builder2 = ImmutableList.builder();
		BakedQuadBuilder builder = new BakedQuadBuilder(format,
				new ModelModifierByCoordTransformer(transformation, null), builder2::add);
		
		builder.switchTextureScale();//Switch to 1 pixel scale.
		
		int[] pixels = layer.getFrameTextureData(0)[0];//Only provide first frame for height.
		for(int v1 = 0; v1 < v; v1++)
		{
			for(int u1 = 0; u1 < u; u1++)
			{
				int idx = (u1 + v1 * u);
				height[idx] = (byte) (pixels[idx] >> 24);
			}
		}
		for(int f = 0; f < layer.getFrameCount(); ++f)
		{
			for(int v1 = 0; v1 < v; v1++)
			{
				for(int u1 = 0; u1 < u; u1++)
				{
					int idx = (u1 + v1 * u);
					if(height[idx] == 0)
					{
						continue;
					}
					float minX = (float) u1 / (float) u;
					float maxX = (float) (u1 + 1) / (float) u;
					float minZ = (float) v1 / (float) v;
					float maxZ = (float) (v1 + 1) / (float) v;
					float minY;
					float maxY = (L.uint(height[idx]) + 1) * this.plateHeight / 256F;
					if(u1 == 0 || L.minusUbyte(height[idx - 1], height[idx]) > 0)
					{
						minY = u1 == 0 ? 0 : value(height[idx - 1]) * this.plateHeight / 256F;
						builder.startQuad(u1 == 0 ? EnumFacing.WEST : null, -1, texture);
						builder.normal(-1, 0, 0);//WEST
						builder.pos(minX, minY, minZ, minZ, minY);
						builder.pos(minX, minY, maxZ, maxZ, minY);
						builder.pos(minX, maxY, maxZ, maxZ, maxY);
						builder.pos(minX, maxY, minZ, minZ, maxY);
						builder.endQuad();
					}
					if(u1 == u - 1 || L.minusUbyte(height[idx + 1], height[idx]) > 0)
					{
						minY = u1 == u - 1 ? 0 : value(height[idx + 1]) * this.plateHeight / 256F;
						builder.startQuad(u1 == u-1 ? EnumFacing.EAST : null, -1, texture);
						builder.normal(1, 0, 0);//EAST
						builder.pos(maxX, minY, maxZ, minZ, minY);
						builder.pos(maxX, minY, minZ, maxZ, minY);
						builder.pos(maxX, maxY, minZ, maxZ, maxY);
						builder.pos(maxX, maxY, maxZ, minZ, maxY);
						builder.endQuad();
					}
					if(v1 == 0 || L.minusUbyte(height[idx - u], height[idx]) > 0)
					{
						minY = v1 == 0 ? 0 : value(height[idx - u]) * this.plateHeight / 256F;
						builder.startQuad(v1 == 0 ? EnumFacing.NORTH : null, -1, texture);
						builder.normal(0, 0, -1);//NORTH
						builder.pos(minX, minY, minZ, minX, minY);
						builder.pos(minX, maxY, minZ, minX, maxY);
						builder.pos(maxX, maxY, minZ, maxX, maxY);
						builder.pos(maxX, minY, minZ, maxX, minY);
						builder.endQuad();
					}
					if(v1 == v - 1 || L.minusUbyte(height[idx + u], height[idx]) > 0)
					{
						minY = v1 == v - 1 ? 0 : value(height[idx + u]) * this.plateHeight / 256F;
						builder.startQuad(v1 == v - 1 ? EnumFacing.SOUTH : null, -1, texture);
						builder.normal(0, 0, 1);//SOUTH
						builder.pos(maxX, minY, maxZ, minX, minY);
						builder.pos(maxX, maxY, maxZ, minX, maxY);
						builder.pos(minX, maxY, maxZ, maxX, maxY);
						builder.pos(minX, minY, maxZ, maxX, minY);
						builder.endQuad();
					}
					builder.startQuad(null, -1, texture);
					builder.normal(0, 1, 0);//UP
					builder.pos(minX, maxY, maxZ, minX, maxZ);
					builder.pos(maxX, maxY, maxZ, maxX, maxZ);
					builder.pos(maxX, maxY, minZ, maxX, minZ);
					builder.pos(minX, maxY, minZ, minX, minZ);
					builder.endQuad();
				}
			}
			builder.startQuad(null, -1, texture);
			builder.normal(0, -1, 0);//DOWN
			builder.pos(0, 0, 0, 0, 1);
			builder.pos(1, 0, 0, 1, 1);
			builder.pos(1, 0, 1, 1, 0);
			builder.pos(0, 0, 1, 0, 0);
			builder.endQuad();
		}
		return builder2.build();
	}
	
	private static int value(byte value)
	{
		return value == 0 ? 0 : L.uint(value) + 1;
	}
	
	static class BakedModelPartRedstoneCircuitPlate implements INebulaBakedModelPart
	{
		private List<BakedQuad>[] quads;
		
		BakedModelPartRedstoneCircuitPlate(List<BakedQuad>[] quads)
		{
			this.quads = quads;
		}
		
		@Override
		public List<BakedQuad> getQuads(EnumFacing facing, String key)
		{
			if (facing != null) return ImmutableList.of();
			String[] split = key.split(",");
			TextureAtlasSprite icon = textures.get(split[1]);
			List<BakedQuad> list = this.quads[EnumFacing.valueOf(split[0]).getHorizontalIndex()];
			return icon == null ? list : Lists.transform(list, quad->new BakedQuadRetextured(quad, icon));
		}
	}
	
	@Override
	public ModelPartRedstoneCircuitPlate retexture(Map<String, String> retexture)
	{
		ResourceLocation layer = this.layer;
		if (retexture.containsKey("layer"))
		{
			layer = new ResourceLocation(retexture.get("layer"));
		}
		return new ModelPartRedstoneCircuitPlate(layer);
	}
}