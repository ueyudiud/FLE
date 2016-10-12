package farcore.lib.model.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.M;
import farcore.data.MC;
import farcore.lib.item.ItemMulti;
import farcore.lib.item.instance.ItemOreChip;
import farcore.lib.material.Mat;
import farcore.lib.model.ModelHelper;
import farcore.lib.model.block.ICustomItemModelSelector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ModelOreChip implements ICustomItemModelSelector, ICustomModelLoader, IModel
{
	instance;

	public static final ModelResourceLocation PARENT_MODEL_LOCATION = new ModelResourceLocation(FarCore.ID + ":models/item/chip", "inventory");
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation(FarCore.INNER_RENDER + ":ore_chip", "inventory");

	private static final ResourceLocation OVERRIDE_LOCATION = new ResourceLocation(FarCore.ID, "items/group/ore_chip/override");
	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return MODEL_LOCATION;
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
	{
	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return MODEL_LOCATION.equals(modelLocation);
	}
	
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception
	{
		return this;
	}
	
	@Override
	public List<ResourceLocation> getAllowedResourceLocations(Item item)
	{
		return ImmutableList.of(MODEL_LOCATION);
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies()
	{
		return ImmutableList.of(PARENT_MODEL_LOCATION);
	}
	
	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableList.of(OVERRIDE_LOCATION);
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		ImmutableMap.Builder<String, List<BakedQuad>> builder = ImmutableMap.builder();
		Optional<TRSRTransformation> optional = state.apply(Optional.absent());
		TextureAtlasSprite particle = bakedTextureGetter.apply(OVERRIDE_LOCATION);
		for(Mat material : Mat.filt(MC.chip_rock))
		{
			TextureAtlasSprite icon = bakedTextureGetter.apply(new ResourceLocation(material.modid, "items/group/chip/" + material.name));
			builder.put(material.name, ItemLayerModel.getQuadsForSprite(0, icon, format, optional));
		}
		return new BakedOreChipModel(particle, builder.build(), ItemLayerModel.getQuadsForSprite(1, particle, format, optional));
	}
	
	@Override
	public IModelState getDefaultState()
	{
		return TRSRTransformation.identity();
	}

	@SideOnly(Side.CLIENT)
	public static class BakedOreChipModel implements ICustomItemRenderModel, IPerspectiveAwareModel
	{
		private TextureAtlasSprite particleIcon;
		private ImmutableMap<String, List<BakedQuad>> map1;
		private List<BakedQuad> map2;
		private boolean isCulled;
		private final IBakedModel submodel;

		public BakedOreChipModel(TextureAtlasSprite icon, ImmutableMap<String, List<BakedQuad>> map, List<BakedQuad> map2)
		{
			this(null, icon, map, map2);
		}
		public BakedOreChipModel(IBakedModel model, TextureAtlasSprite icon, ImmutableMap<String, List<BakedQuad>> map, List<BakedQuad> map2)
		{
			if(model == null)
			{
				isCulled = false;
				ImmutableMap.Builder<String, List<BakedQuad>> builder = ImmutableMap.builder();
				for(Entry<String, List<BakedQuad>> entry : map.entrySet())
				{
					ImmutableList.Builder<BakedQuad> builder2 = ImmutableList.builder();
					for(BakedQuad quad : entry.getValue())
					{
						if(quad.getFace() == EnumFacing.SOUTH)
						{
							builder2.add(quad);
						}
					}
					builder.put(entry.getKey(), builder2.build());
				}
				ImmutableList.Builder<BakedQuad> builder1 = ImmutableList.builder();
				for(BakedQuad quad : map2)
				{
					if(quad.getFace() == EnumFacing.SOUTH)
					{
						builder1.add(quad);
					}
				}
				submodel = new BakedOreChipModel(this, icon, builder.build(), builder1.build());
			}
			else
			{
				isCulled = true;
				submodel = model;
			}
			particleIcon = icon;
			map1 = map;
			this.map2 = map2;
		}
		
		@Override
		public List<BakedQuad> getQuads(ItemStack stack, EnumFacing facing, long rand)
		{
			if(facing != null) return ImmutableList.of();
			List<BakedQuad> list = new ArrayList(2);
			String rock = ItemOreChip.getRockString(stack);
			if(rock.length() > 0)
			{
				list.addAll(map1.get(rock));
			}
			Mat ore = ItemMulti.getMaterial(stack);
			if(ore != M.VOID)
			{
				list.addAll(map2);
			}
			return list;
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			return ImmutableList.of();
		}
		@Override
		public boolean isAmbientOcclusion() {return true;}
		@Override
		public boolean isGui3d() {return false;}
		@Override
		public boolean isBuiltInRenderer() {return false;}
		@Override
		public TextureAtlasSprite getParticleTexture() {return particleIcon;}
		@Override
		public ItemCameraTransforms getItemCameraTransforms() {return ItemCameraTransforms.DEFAULT;}
		@Override
		public ItemOverrideList getOverrides() {return ItemOverrideList.NONE;}
		
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType type)
		{
			Pair<? extends IBakedModel, Matrix4f> pair = IPerspectiveAwareModel.MapWrapper.handlePerspective(this, ModelHelper.ITEM_STANDARD_TRANSFORMS, type);
			if(type == TransformType.GUI && !isCulled && pair.getRight() == null)
				return Pair.of(submodel, null);
			else if(type != TransformType.GUI && isCulled)
				return Pair.of(submodel, pair.getRight());
			return pair;
		}
	}
}