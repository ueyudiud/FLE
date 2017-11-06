/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;

import nebula.client.model.ItemTextureQuadConverterExt;
import nebula.client.util.IIconCollection;
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
public class ModelPartItemLayerConvert extends ModelPartItemLayer
{
	String convert;
	
	public ModelPartItemLayerConvert(ModelPartItemLayer layer, String convert)
	{
		super.icon = layer.icon;
		super.index = layer.index;
		super.zOffset = layer.zOffset;
		this.convert = convert;
	}
	
	@Override
	public Collection<String> getResources()
	{
		return ImmutableList.of(this.icon, this.convert);
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		IIconCollection collection1 = iconHandlerGetter.apply(this.icon);
		IIconCollection collection2 = iconHandlerGetter.apply(this.convert);
		TextureAtlasSprite convertIcon;
		switch (collection2.size())
		{
		case 0 :
			return INebulaBakedModelPart.EMPTY;
		case 1 :
			convertIcon = bakedTextureGetter.apply(Iterables.getOnlyElement(collection2.build().values()));
			ImmutableMap.Builder<String, List<BakedQuad>> builder = ImmutableMap.builder();
			for (Entry<String, ResourceLocation> entry : collection1.build().entrySet())
			{
				ImmutableList.Builder<BakedQuad> builder1 = ImmutableList.builder();
				builder1.addAll(ItemTextureQuadConverterExt.convertTexture(format, transformation, convertIcon, bakedTextureGetter.apply(entry.getValue()), (8.0F - this.zOffset - 1E-3F * this.index) / 16, EnumFacing.NORTH, 0xFFFFFFFF, this.index));
				builder1.addAll(ItemTextureQuadConverterExt.convertTexture(format, transformation, convertIcon, bakedTextureGetter.apply(entry.getValue()), (8.0F + this.zOffset + 1E-3F * this.index) / 16, EnumFacing.SOUTH, 0xFFFFFFFF, this.index));
				builder.put(entry.getKey(), builder1.build());
			}
			return new INebulaBakedModelPart.BakedModelPart(builder.build());
		default:
			builder = ImmutableMap.builder();
			Map<String, TextureAtlasSprite> map1 = new HashMap<>();
			collection1.build().forEach((s, r)-> map1.put(s, bakedTextureGetter.apply(r)));
			for (Entry<String, ResourceLocation> entry1 : collection2.build().entrySet())
			{
				convertIcon = bakedTextureGetter.apply(entry1.getValue());
				for (Entry<String, TextureAtlasSprite> entry2 : map1.entrySet())
				{
					ImmutableList.Builder<BakedQuad> builder1 = ImmutableList.builder();
					builder1.addAll(ItemTextureQuadConverterExt.convertTexture(format, transformation, convertIcon, entry2.getValue(), (8.0F - this.zOffset - 1E-3F * this.index) / 16, EnumFacing.NORTH, 0xFFFFFFFF, this.index));
					builder1.addAll(ItemTextureQuadConverterExt.convertTexture(format, transformation, convertIcon, entry2.getValue(), (8.0F + this.zOffset + 1E-3F * this.index) / 16, EnumFacing.SOUTH, 0xFFFFFFFF, this.index));
					builder.put(entry2.getKey() + "," + entry1.getKey(), builder1.build());
				}
			}
			return new INebulaBakedModelPart.BakedModelPart(builder.build());
		}
	}
}