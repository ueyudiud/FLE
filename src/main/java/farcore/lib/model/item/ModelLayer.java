package farcore.lib.model.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelLayer
{
	public static final List<BakedQuad> MISSING_TEXTURE = ImmutableList.of();

	private final int layer;
	private final Map<String, List<BakedQuad>> quads;
	private final Function<ItemStack, String> textureKeyFunction;
	private final Function<ItemStack, Integer> colorMultiplier;
	
	public ModelLayer(int layer, Map<String, List<BakedQuad>> quads, Function<ItemStack, String> textureKeyFunction, Function<ItemStack, Integer> colorMultiplier)
	{
		this.layer = layer;
		this.quads = quads;
		this.textureKeyFunction = textureKeyFunction;
		this.colorMultiplier = colorMultiplier;
	}
	
	public int getLayer()
	{
		return layer;
	}

	public List<BakedQuad> getQuads(ItemStack stack)
	{
		return quads.getOrDefault(textureKeyFunction == null ? FarCoreItemModelLoader.NORMAL : textureKeyFunction.apply(stack), MISSING_TEXTURE);
	}
	
	public int getColor(ItemStack stack)
	{
		return colorMultiplier == null ? 0xFFFFFFFF : colorMultiplier.apply(stack);
	}
	
	@SideOnly(Side.CLIENT)
	public static class UnbakedModelLayer
	{
		private static final float Z_LEVEL_SCALE = 16F;
		
		public int layer = -1;
		/**
		 * Should provide quad with side.
		 */
		public boolean renderFull3D = true;
		public float zOffset = 0.5F;
		public int baseColor = 0xFFFFFFFF;
		public List<String> allowedTextures = new ArrayList();
		public Function<ItemStack, String> function = FarCoreItemModelLoader.NORMAL_FUNCTION;
		public Function<ItemStack, Integer> colorMultiplier = FarCoreItemModelLoader.NORMAL_MULTIPLIER;
		public String converts = null;

		Map<String, ResourceLocation> locations = new HashMap();
		ResourceLocation convertLocation;

		private byte bakedState = 0x0;

		public void loadTexturesAndSubmetaGetter(Map<String, ResourceLocation> textures, Map<String, Map<String, ResourceLocation>> multipleTextures)
		{
			if (bakedState != 0x0) throw new RuntimeException("Already load textures!");
			if (allowedTextures == null)
			{
				locations.putAll(textures);
				for (Map<String, ResourceLocation> textureMap : multipleTextures.values())
				{
					locations.putAll(textureMap);
				}
			}
			else
			{
				for(String key : allowedTextures)
				{
					if(key.charAt(0) == '[')
					{
						locations.putAll(multipleTextures.get(key.substring(1)));
					}
					else
					{
						locations.put(key, textures.getOrDefault(key, TextureMap.LOCATION_MISSING_TEXTURE));
					}
				}
			}
			if (converts != null)
			{
				convertLocation = locations.get(converts);
			}
			bakedState = 0x1;
		}

		public ModelLayer bake(VertexFormat format, com.google.common.base.Function<ResourceLocation, TextureAtlasSprite> bakeTextureGetter, Optional<TRSRTransformation> transform)
		{
			if (bakedState != 0x1) throw new RuntimeException("Not load texture yet!");
			ImmutableMap.Builder<String, List<BakedQuad>> map = ImmutableMap.builder();
			TextureAtlasSprite covertsIcon = converts != null ? bakeTextureGetter.apply(convertLocation) : null;
			for (Entry<String, ResourceLocation> entry : locations.entrySet())
			{
				TextureAtlasSprite icon = bakeTextureGetter.apply(entry.getValue());
				if (renderFull3D)
				{
					map.put(entry.getKey(), ItemTextureHelper.getQuadsForSprite(layer, icon, format, transform, zOffset, baseColor));
				}
				else
				{
					TRSRTransformation transformation = transform.isPresent() ? transform.get() : TRSRTransformation.identity();
					if (covertsIcon != null)
					{
						ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
						builder.addAll(ItemTextureHelper.convertTexture(format, transformation, covertsIcon, icon, (8F - zOffset) / Z_LEVEL_SCALE, EnumFacing.NORTH, layer, baseColor));
						builder.addAll(ItemTextureHelper.convertTexture(format, transformation, covertsIcon, icon, (8F + zOffset) / Z_LEVEL_SCALE, EnumFacing.SOUTH, layer, baseColor));
						map.put(entry.getKey(), builder.build());
					}
					else
					{
						map.put(entry.getKey(), ImmutableList.of(
								ItemTextureHelper.genQuad(format, transformation, 0, 0, 16, 16, (8F - zOffset) / Z_LEVEL_SCALE, icon, EnumFacing.NORTH, layer, baseColor),
								ItemTextureHelper.genQuad(format, transformation, 0, 0, 16, 16, (8F + zOffset) / Z_LEVEL_SCALE, icon, EnumFacing.SOUTH, layer, baseColor)));
					}
				}
			}
			return new ModelLayer(layer, map.build(), function, colorMultiplier);
		}
	}
}