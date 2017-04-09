/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.block.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import nebula.client.model.BakedModelBase;
import nebula.client.model.INebulaBakedModelPart;
import nebula.client.model.ModelBase;
import nebula.client.model.part.PackedVerticalCube;
import nebula.common.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Only a block to debug model block.
 * @author ueyudiud
 */
public class BlockModelDebug extends BlockBase
{
	public BlockModelDebug()
	{
		super("model.debug", Material.WOOD);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerRender()
	{
		super.registerRender();
		ModelLoaderRegistry.registerLoader(DebugModel.MODEL);
	}
	
	@SideOnly(Side.CLIENT)
	public static class DebugModel implements ModelBase, ICustomModelLoader
	{
		private static final DebugModel MODEL = new DebugModel();
		private static final ResourceLocation location = new ResourceLocation("farcore", "blocks/rock/andesite/resource");
		
		@Override
		public Collection<ResourceLocation> getTextures()
		{
			return ImmutableList.of(location);
		}
		
		@Override
		public IBakedModel bake(IModelState state, VertexFormat format,
				Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
		{
			PackedVerticalCube data = new PackedVerticalCube();
			data.setResourceLocation(location.toString());
			data.setBound(0.25F, 0.0F, 0.25F, 0.75F, 0.5F, 0.75F);
			data.setRotate(1, 0);
			data.setUVLock(true);
			return new DebugModelBaked(data.bake(TRSRTransformation.identity(), format,
					key -> bakedTextureGetter.apply(new ResourceLocation(key))));
		}
		
		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			return this;
		}
		
		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			return "model.debug".startsWith(modelLocation.getResourcePath());
		}
		
		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{
			
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class DebugModelBaked implements BakedModelBase
	{
		private INebulaBakedModelPart part;
		
		public DebugModelBaked(INebulaBakedModelPart part)
		{
			this.part = part;
		}
		
		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			List<BakedQuad> quads = new ArrayList<>();
			((INebulaBakedModelPart.WrapedBakedModelPart) this.part).putQuads(quads);
			return quads;
		}
	}
}