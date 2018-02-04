/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.model.block;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.SubTags;
import farcore.lib.material.Mat;
import farcore.lib.tile.instance.TESapling;
import nebula.client.blockstate.BlockStateTileEntityWapper;
import nebula.client.model.BakedModelRetexture;
import nebula.client.model.ICustomItemModelSelector;
import nebula.client.model.INebulaCustomModelLoader;
import nebula.client.model.ModelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Will be removed soon, use FlexibleModel instead.
 * 
 * @author ueyudiud
 */
@Deprecated
@SideOnly(Side.CLIENT)
public enum ModelSapling implements INebulaCustomModelLoader, ICustomItemModelSelector, IStateMapper, IModel
{
	instance;
	
	private static final ResourceLocation		PARENT_LOCATION	= new ResourceLocation(FarCore.ID, "block/sapling");
	private static final ModelResourceLocation	MODEL_LOCATION	= new ModelResourceLocation(FarCore.INNER_RENDER + ":sapling", "normal");
	
	public static final Map<String, TextureAtlasSprite> ICON_MAP = new HashMap();
	
	private List<ResourceLocation> textures;
	
	ModelSapling()
	{
	}
	
	@Override
	public String getModID()
	{
		return FarCore.INNER_RENDER;
	}
	
	@Override
	public String getLoaderPrefix()
	{
		return "sapling";
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		if (modelLocation == MODEL_LOCATION) return this;
		String path = modelLocation.getResourcePath();
		String[] strings = path.split("/");
		if (strings.length != 3) throw new RuntimeException("Invalid model location : " + path);
		ResourceLocation location = new ResourceLocation(strings[1], "blocks/sapling/" + strings[2]);
		return ModelHelper.makeItemModel(location);
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		if (this.textures == null)
		{
			ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
			for (Mat material : Mat.filt(SubTags.TREE))
			{
				builder.add(new ResourceLocation(material.modid, "blocks/sapling/" + material.name));
			}
			this.textures = builder.build();
		}
		return this.textures;
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		ICON_MAP.clear();
		IModel model = ModelLoaderRegistry.getModelOrMissing(PARENT_LOCATION);
		ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
		for (Mat material : Mat.filt(SubTags.TREE))
		{
			TextureAtlasSprite icon = bakedTextureGetter.apply(new ResourceLocation(material.modid, "blocks/sapling/" + material.name));
			builder.put(material.name, icon);
			ICON_MAP.put(material.getRegisteredName(), icon);
		}
		return new BakedSaplingModel(builder.build(), model.bake(state, format, bakedTextureGetter));
	}
	
	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of();
	}
	
	@Override
	public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
	{
		ImmutableMap.Builder<IBlockState, ModelResourceLocation> map = ImmutableMap.builder();
		for (IBlockState state : blockIn.getBlockState().getValidStates())
		{
			map.put(state, MODEL_LOCATION);
		}
		return map.build();
	}
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		Mat material = Mat.material(stack.getItemDamage());
		return new ModelResourceLocation(FarCore.INNER_RENDER + ":saplingitem/" + material.modid + "/" + material.name, "inventory");
	}
	
	@Override
	public List<ResourceLocation> getAllowedResourceLocations(Item item)
	{
		ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
		for (Mat material : Mat.filt(SubTags.TREE))
		{
			builder.add(new ModelResourceLocation(FarCore.INNER_RENDER + ":saplingitem/" + material.modid + "/" + material.name, "inventory"));
		}
		return builder.build();
	}
	
	@SideOnly(Side.CLIENT)
	static class BakedSaplingModel extends BakedModelRetexture implements IPerspectiveAwareModel
	{
		private Map<String, TextureAtlasSprite> icons;
		
		public BakedSaplingModel(Map<String, TextureAtlasSprite> icons, IBakedModel basemodel)
		{
			super(basemodel);
			this.icons = icons;
		}
		
		@Override
		protected void replaceQuads(IBlockState state, Builder<BakedQuad> newList, List<BakedQuad> oldList)
		{
			TESapling tile = BlockStateTileEntityWapper.unwrap(state);
			if (tile != null)
			{
				TextureAtlasSprite icon = this.icons.get(tile.tree.getRegisteredName());
				if (icon != null)
				{
					apply(oldList, newList, quad -> new BakedQuadRetextured(quad, icon));
				}
			}
			else
			{
				newList.addAll(oldList);
			}
		}
		
		@Override
		public boolean isAmbientOcclusion()
		{
			return false;
		}
		
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
		{
			return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, ModelHelper.ITEM_STANDARD_TRANSFORMS, cameraTransformType);
		}
	}
}
