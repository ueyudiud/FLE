/*
 * copyright© 2016 ueyudiud
 */

package fle.core.client.model;

import java.util.Collection;
import java.util.List;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import farcore.data.M;
import farcore.data.MC;
import farcore.instances.MaterialTextureLoader;
import farcore.lib.material.Mat;
import farcore.lib.model.ModelHelper;
import farcore.lib.model.block.IFarCustomModelLoader;
import farcore.lib.model.block.ModelBase;
import farcore.lib.model.block.statemap.BlockStateTileEntityWapper;
import farcore.lib.model.item.unused.ICustomItemRenderModel;
import farcore.lib.util.SubTag;
import fle.core.FLE;
import fle.core.tile.ditchs.TEDitch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;

/**
 * @author ueyudiud
 */
public class ModelDitch extends ModelBase implements IFarCustomModelLoader
{
	public static final ModelDitch MODEL = new ModelDitch();
	
	public static final ResourceLocation PARENT = new ResourceLocation(FLE.MODID, "ditch");
	
	private ModelDitch() { }
	
	@Override
	public String getLoaderPrefix()
	{
		return "ditch";
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return this;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of(PARENT);
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableList.of();
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		IModel model = ModelLoaderRegistry.getModelOrMissing(PARENT);
		return new BakedModelDitch(model.bake(state, format, bakedTextureGetter));
	}
	
	private static class BakedModelDitch implements IPerspectiveAwareModel, ICustomItemRenderModel
	{
		private final IBakedModel parent;
		
		BakedModelDitch(IBakedModel model)
		{
			this.parent = model;
		}
		
		@Override
		public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
		{
			List<BakedQuad> list = this.parent.getQuads(null, facing, rand);
			TextureAtlasSprite icon = getIcon(Mat.material(stack.getItemDamage(), M.stone));
			return Lists.transform(list, quad -> new BakedQuadRetextured(quad, icon));
		}
		
		@Override
		public List<BakedQuad> getQuads(@Nullable IBlockState state, EnumFacing side, long rand)
		{
			List<BakedQuad> list = this.parent.getQuads(state, side, rand);
			if(state instanceof BlockStateTileEntityWapper)
			{
				BlockStateTileEntityWapper<TEDitch> state1 = (BlockStateTileEntityWapper<TEDitch>) state;
				TextureAtlasSprite icon = getIcon(state1.tile.getMaterial());
				return Lists.transform(list, quad -> new BakedQuadRetextured(quad, icon));
			}
			return list;
		}
		
		private TextureAtlasSprite getIcon(Mat material)
		{
			if(material.contain(SubTag.ROCK))
			{
				return MaterialTextureLoader.getIcon(material, MC.stone);
			}
			else if(material.contain(SubTag.WOOD))
			{
				return MaterialTextureLoader.getIcon(material, MC.plankBlock, "");
			}
			return ModelHelper.getIcon(null);
		}
		
		@Override
		public boolean isAmbientOcclusion()
		{
			return this.parent.isAmbientOcclusion();
		}
		
		@Override
		public boolean isGui3d()
		{
			return this.parent.isGui3d();
		}
		
		@Override
		public boolean isBuiltInRenderer() { return false; }
		
		@Override
		public TextureAtlasSprite getParticleTexture()
		{
			return this.parent.getParticleTexture();
		}
		
		@Override
		public ItemCameraTransforms getItemCameraTransforms()
		{
			return this.parent.getItemCameraTransforms();
		}
		
		@Override
		public ItemOverrideList getOverrides()
		{
			return this.parent.getOverrides();
		}
		
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
		{
			if (this.parent instanceof IPerspectiveAwareModel)
			{
				return ((IPerspectiveAwareModel) this.parent).handlePerspective(cameraTransformType);
			}
			return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, ModelHelper.BLOCK_STANDARD_TRANSFORMS, cameraTransformType);
		}
	}
}