/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.lib.model.item.ItemTextureHelper;
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

/**
 * @author ueyudiud
 */
@Deprecated
@SideOnly(Side.CLIENT)
public class UnbakedModelLayer
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
	public Function<ItemStack, String> converts = null;
	
	Map<String, ResourceLocation> locations = new HashMap();
	ResourceLocation convertLocation;
	
	private byte bakedState = 0x0;
	
	public void loadTexturesAndSubmetaGetter(Map<String, ResourceLocation> textures, Map<String, Map<String, ResourceLocation>> multipleTextures)
	{
		if (this.bakedState != 0x0) throw new RuntimeException("Already load textures!");
		if (this.allowedTextures == null)
		{
			this.locations.putAll(textures);
			for (Map<String, ResourceLocation> textureMap : multipleTextures.values())
			{
				this.locations.putAll(textureMap);
			}
		}
		else
		{
			for(String key : this.allowedTextures)
			{
				if(key.charAt(0) == '[')
				{
					this.locations.putAll(multipleTextures.get(key.substring(1)));
				}
				else
				{
					this.locations.put(key, textures.getOrDefault(key, TextureMap.LOCATION_MISSING_TEXTURE));
				}
			}
		}
		if (this.converts != null)
		{
			this.convertLocation = this.locations.get(this.converts);
		}
		this.bakedState = 0x1;
	}
	
	public ModelLayer bake(VertexFormat format, com.google.common.base.Function<ResourceLocation, TextureAtlasSprite> bakeTextureGetter, Optional<TRSRTransformation> transform)
	{
		if (this.bakedState != 0x1) throw new RuntimeException("Not load texture yet!");
		ImmutableMap.Builder<String, List<BakedQuad>> map = ImmutableMap.builder();
		TextureAtlasSprite covertsIcon = this.converts != null ? bakeTextureGetter.apply(this.convertLocation) : null;
		for (Entry<String, ResourceLocation> entry : this.locations.entrySet())
		{
			TextureAtlasSprite icon = bakeTextureGetter.apply(entry.getValue());
			if (this.renderFull3D)
			{
				//				map.put(entry.getKey(), ItemTextureHelper.getQuadsForSprite(this.layer, icon, format, transform, this.zOffset, this.baseColor));
			}
			else
			{
				TRSRTransformation transformation = transform.isPresent() ? transform.get() : TRSRTransformation.identity();
				if (covertsIcon != null)
				{
					ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
					builder.addAll(ItemTextureHelper.convertTexture(format, transformation, covertsIcon, icon, (8F - this.zOffset) / Z_LEVEL_SCALE, EnumFacing.NORTH, this.layer, this.baseColor));
					builder.addAll(ItemTextureHelper.convertTexture(format, transformation, covertsIcon, icon, (8F + this.zOffset) / Z_LEVEL_SCALE, EnumFacing.SOUTH, this.layer, this.baseColor));
					map.put(entry.getKey(), builder.build());
				}
				else
				{
					map.put(entry.getKey(), ImmutableList.of(
							ItemTextureHelper.genQuad(format, transformation, 0, 0, 16, 16, (8F - this.zOffset) / Z_LEVEL_SCALE, icon, EnumFacing.NORTH, this.layer, this.baseColor),
							ItemTextureHelper.genQuad(format, transformation, 0, 0, 16, 16, (8F + this.zOffset) / Z_LEVEL_SCALE, icon, EnumFacing.SOUTH, this.layer, this.baseColor)));
				}
			}
		}
		return new ModelLayerV1(this.layer, map.build(), this.function, this.colorMultiplier);
	}
}