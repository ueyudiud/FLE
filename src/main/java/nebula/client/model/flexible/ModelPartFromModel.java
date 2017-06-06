/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.client.model.flexible;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import nebula.client.util.IIconHandler;
import nebula.common.util.L;
import nebula.common.util.Strings;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * @author ueyudiud
 */
public class ModelPartFromModel implements IRetexturableNebulaModelPart, INebulaDirectResourcesModelPart
{
	final IModel model;
	@Nullable
	final BlockStateContainer container;
	
	public ModelPartFromModel(IModel model, @Nullable BlockStateContainer container)
	{
		this.model = model;
		this.container = container;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return this.model.getDependencies();
	}
	
	@Override
	public Collection<String> getResources()
	{
		return ImmutableList.of();
	}
	
	@Override
	public Collection<ResourceLocation> getDirectResources()
	{
		return this.model.getTextures();
	}
	
	@Override
	public INebulaBakedModelPart bake(VertexFormat format, Function<String, IIconHandler> iconHandlerGetter,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, TRSRTransformation transformation)
	{
		Optional<TRSRTransformation> optional = Optional.fromNullable(transformation);
		return new BakedModelPartFromModel(this.model.bake(any->optional, format, bakedTextureGetter::apply), this.container);
	}
	
	@Override
	public INebulaModelPart retexture(Map<String, String> retexture)
	{
		if (this.model instanceof IRetexturableModel)
		{
			return new ModelPartFromModel(((IRetexturableModel) this.model).retexture(ImmutableMap.copyOf(retexture)), this.container);
		}
		return this;
	}
	
	private static class BakedModelPartFromModel implements INebulaBakedModelPart
	{
		private final IBakedModel model;
		private final BlockStateContainer container;
		
		BakedModelPartFromModel(IBakedModel model, BlockStateContainer container)
		{
			this.model = model;
			this.container = container;
		}
		
		@Override
		public List<BakedQuad> getQuads(EnumFacing facing, String key)
		{
			try
			{
				return this.model.getQuads(withProperty(key), facing, 1L);
			}
			catch (InternalError e)
			{
				return ImmutableList.of();
			}
		}
		
		private IBlockState withProperty(String key)
		{
			if (this.container == null) return null;
			IBlockState state = this.container.getBaseState();
			String[] split = Strings.split(key, ',');
			for (String v : split)
			{
				String v1 = v.trim();
				int i = v1.indexOf('=');
				IProperty<?> property = this.container.getProperty(v1.substring(0, i));
				if (property == null) throw new InternalError();
				Optional<?> optional = property.parseValue(v1.substring(i + 1));
				if (optional.isPresent())
				{
					state = state.withProperty(property, L.castAny(optional.get()));
				}
				else throw new InternalError();
			}
			return state;
		}
	}
}