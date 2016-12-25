/*
 * copyright© 2016 ueyudiud
 */

package farcore.lib.model.item.unused;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Maps.EntryTransformer;
import com.google.common.collect.Table;

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
@SideOnly(Side.CLIENT)
public class FarCoreItemLayerUnbakedSimple extends FarCoreItemLayerUnbaked
{
	byte mark;
	
	int baseColor = 0xFFFFFFFF;
	Function<ItemStack, String> function = FarCoreItemModelLoader.NORMAL_FUNCTION;
	Function<ItemStack, Integer> colorMultiplier = FarCoreItemModelLoader.NORMAL_MULTIPLIER;
	
	List<String> base = new ArrayList();
	List<String> covert = new ArrayList();
	Function<ItemStack, String> coverts = null;
	
	@Override
	public FarCoreItemLayer bake(VertexFormat format, com.google.common.base.Function<ResourceLocation, TextureAtlasSprite> bakeTextureGetter,
			Optional<TRSRTransformation> transform)
	{
		switch (this.mark)
		{
		case 1:
			if(this.coverts == null)
			{
				TRSRTransformation transformation = transform.isPresent() ? transform.get() : TRSRTransformation.identity();
				Map<String, List<BakedQuad>> map;
				if(this.covert.size() > 1) throw new IllegalArgumentException();
				else if(this.covert.size() == 1)
				{
					String covertStr = this.covert.get(0);
					TextureAtlasSprite covert = bakeTextureGetter.apply(this.textureSets.getOrDefault(covertStr, TextureMap.LOCATION_MISSING_TEXTURE));
					ImmutableMap.Builder<String, List<BakedQuad>> builder = ImmutableMap.builder();
					for(Entry<String, ResourceLocation> entry : this.textureSets.entrySet())
					{
						if(entry.getKey().equals(covertStr)) continue;
						TextureAtlasSprite icon = bakeTextureGetter.apply(entry.getValue());
						ImmutableList.Builder<BakedQuad> builder1 = ImmutableList.builder();
						builder1.addAll(ItemTextureHelper.convertTexture(format, transformation, covert, icon, (8F - this.zOffset) / Z_LEVEL_SCALE, EnumFacing.NORTH, this.layer, this.baseColor));
						builder1.addAll(ItemTextureHelper.convertTexture(format, transformation, covert, icon, (8F - this.zOffset) / Z_LEVEL_SCALE, EnumFacing.NORTH, this.layer, this.baseColor));
						builder.put(entry.getKey(), builder1.build());
					}
					map = builder.build();
				}
				else
				{
					EntryTransformer<String, ResourceLocation, List<BakedQuad>> transformer =
							(String key, ResourceLocation location) ->
					{
						TextureAtlasSprite icon = bakeTextureGetter.apply(location);
						return ImmutableList.of(ItemTextureHelper.genQuad(format, transformation, 0, 0, 16, 16, (8F - this.zOffset) / Z_LEVEL_SCALE, icon, EnumFacing.NORTH, this.layer, this.baseColor),
								ItemTextureHelper.genQuad(format, transformation, 0, 0, 16, 16, (8F + this.zOffset) / Z_LEVEL_SCALE, icon, EnumFacing.SOUTH, this.layer, this.baseColor));
					};
					map = Maps.transformEntries(this.textureSets, transformer);
				}
				return new FarCoreItemLayerFunctional(this.layer, this.function, map, this.colorMultiplier);
			}
			return new FarCoreItemLayerTable(this.layer, this.function, this.coverts, buildQuadsTable(format, bakeTextureGetter, transform.isPresent() ? transform.get() : TRSRTransformation.identity()));
		case 2 :
			TextureAtlasSprite icon;
			return null;
			//			return new FarCoreItemLayerSimple(this.layer, (icon = bakeTextureGetter.apply(this.textureSets.getOrDefault(this.base.get(0), TextureMap.LOCATION_MISSING_TEXTURE))), ItemTextureHelper.getQuadsForSprite(this.layer, icon, format, transform, this.zOffset, this.baseColor));
		default :
			return new FarCoreItemLayerFunctional(this.layer, this.function, null,
					//					Maps.transformEntries(this.textureSets,
					//							(String key, ResourceLocation location) -> ItemTextureHelper.getQuadsForSprite(this.layer, bakeTextureGetter.apply(location), format, transform, this.zOffset, this.baseColor)),
					this.colorMultiplier);
		}
	}
	
	private Table<String, String, List<BakedQuad>> buildQuadsTable(VertexFormat format, com.google.common.base.Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		ImmutableTable.Builder<String, String, List<BakedQuad>> builder = ImmutableTable.builder();
		for(String column : this.covert)
		{
			TextureAtlasSprite icon1 = bakedTextureGetter.apply(this.textureSets.getOrDefault(column, TextureMap.LOCATION_MISSING_TEXTURE));
			for(String row : this.base)
			{
				TextureAtlasSprite icon = bakedTextureGetter.apply(this.textureSets.getOrDefault(row, TextureMap.LOCATION_MISSING_TEXTURE));
				ImmutableList.Builder<BakedQuad> builder1 = ImmutableList.builder();
				builder1.addAll(ItemTextureHelper.convertTexture(format, transformation, icon1, icon, (8F - this.zOffset) / Z_LEVEL_SCALE, EnumFacing.NORTH, this.layer, this.baseColor));
				builder1.addAll(ItemTextureHelper.convertTexture(format, transformation, icon1, icon, (8F - this.zOffset) / Z_LEVEL_SCALE, EnumFacing.NORTH, this.layer, this.baseColor));
				builder.put(row, column, builder1.build());
			}
		}
		return builder.build();
	}
}