/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.function.Function;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

import nebula.client.model.flexible.INebulaBakedModelPart.BakedModelPart;
import nebula.client.util.IIconCollection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ModelPartItemLayer implements INebulaModelPart
{
	int		index;
	float	zOffset;
	String	icon;
	
	public ModelPartItemLayer(int index, String icon)
	{
		this.index = index;
		this.icon = icon;
	}
	
	public ModelPartItemLayer()
	{
	}
	
	@Override
	public Collection<String> getResources()
	{
		return ImmutableList.of(this.icon);
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconCollection> iconHandlerGetter, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		return new BakedModelPart(Maps.transformValues(iconHandlerGetter.apply(this.icon).build(), location -> ItemLayerModel.getQuadsForSprite(this.index, bakedTextureGetter.apply(location), format, Optional.of(transformation))));
	}
}
