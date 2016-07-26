package farcore.lib.model.block;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import farcore.FarCore;
import farcore.data.M;
import farcore.lib.block.instance.BlockSapling;
import farcore.lib.material.Mat;
import farcore.lib.model.ModelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelSapling extends ModelBase implements IRetexturableModel
{
	private static final ResourceLocation PARENT_LOCATION = new ResourceLocation(FarCore.ID, "block/sapling");
	
	private static final ModelSapling MODEL = new ModelSapling(null);
	
	private final ResourceLocation saplingLocation;

	public ModelSapling(ResourceLocation location)
	{
		saplingLocation = location;
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableList.of(saplingLocation);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		IModel model = ModelLoaderRegistry.getModelOrMissing(PARENT_LOCATION);
		if(model instanceof IRetexturableModel)
		{
			model = ((IRetexturableModel) model).retexture(ImmutableMap.of("sapling", saplingLocation.toString()));
		}
		return model.bake(state, format, bakedTextureGetter);
	}
	
	@Override
	public IModel retexture(ImmutableMap<String, String> textures)
	{
		ResourceLocation sapling = saplingLocation;
		if(textures.containsKey("sapling"))
		{
			sapling = new ResourceLocation(textures.get("sapling"));
		}
		return new ModelSapling(sapling);
	}

	@SideOnly(Side.CLIENT)
	public static enum Loader implements ICustomModelLoader
	{
		instance;

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager)
		{

		}

		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			return modelLocation.getResourceDomain().equals(FarCore.INNER_RENDER) &&
					modelLocation.getResourcePath().startsWith("sapling");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			String path = modelLocation.getResourcePath();
			String[] strings = path.split("/");
			if(strings.length != 3)
				throw new RuntimeException("Invalid model location : " + path);
			ResourceLocation location = new ResourceLocation(strings[1], "blocks/sapling/" + strings[2]);
			return "saplingitem".equals(strings[0]) ?
					ModelHelper.makeItemModel(location) : new ModelSapling(location);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static enum BlockModelSelector implements IStateMapper
	{
		instance;

		@Override
		public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn)
		{
			ImmutableMap.Builder<IBlockState, ModelResourceLocation> map = ImmutableMap.builder();
			for(IBlockState state : blockIn.getBlockState().getValidStates())
			{
				Mat material = state.getValue(BlockSapling.PROP_SAPLING);
				map.put(state, new ModelResourceLocation(FarCore.INNER_RENDER + ":sapling/" + material.modid + "/" + material.name));
			}
			return map.build();
		}
	}

	@SideOnly(Side.CLIENT)
	public static enum ItemModelSelector implements ICustomItemModelSelector
	{
		instance;
		
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			Mat material = Mat.register.get(stack.getItemDamage(), M.VOID);
			return new ModelResourceLocation(new ResourceLocation(FarCore.INNER_RENDER, "saplingitem/" + material.modid + "/" + material.name), "inventory");
		}

		@Override
		public List<ResourceLocation> getAllowedResourceLocations(Item item)
		{
			ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
			for(Mat material : BlockSapling.PROP_SAPLING.getAllowedValues())
			{
				builder.add(new ModelResourceLocation(new ResourceLocation(FarCore.INNER_RENDER, "saplingitem/" + material.modid + "/" + material.name), "inventory"));
			}
			return builder.build();
		}
	}
}