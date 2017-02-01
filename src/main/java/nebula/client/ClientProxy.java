/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import nebula.Log;
import nebula.client.model.ModelFluidBlock;
import nebula.client.model.NebulaItemModelLoader;
import nebula.client.model.OrderModelLoader;
import nebula.client.model.StateMapperExt;
import nebula.client.render.Colormap.ColormapFactory;
import nebula.client.render.RenderFallingBlockExt;
import nebula.client.render.RenderProjectileItem;
import nebula.client.util.Client;
import nebula.client.util.IRenderRegister;
import nebula.common.CommonProxy;
import nebula.common.entity.EntityFallingBlockExtended;
import nebula.common.entity.EntityProjectileItem;
import nebula.common.util.Game;
import nebula.common.util.Sides;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IResourceManagerReloadListener
{
	public static final IBlockColor BIOME_COLOR = (state, worldIn, pos, tintIndex) ->
	{
		boolean flag = worldIn == null || pos == null;
		Biome biome = flag ? null : worldIn.getBiome(pos);
		switch(tintIndex)
		{
		case 0 : return flag ? ColorizerGrass.getGrassColor(0.7F, 0.7F) :
			BiomeColorHelper.getGrassColorAtPos(worldIn, pos);
		case 1 : return flag ? ColorizerFoliage.getFoliageColorBasic() :
			BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
		case 2 : return flag ? -1 : BiomeColorHelper.getWaterColorAtPos(worldIn, pos);
		default: return -1;
		}
	};
	
	private static Map<String, List<IRenderRegister>> registers = new HashMap();
	public static Map<IBlockColor, List<Block>> blockColorMap = new HashMap();
	public static Map<IItemColor, List<Item>> itemColorMap = new HashMap();
	public static List<Block> buildInRender = new ArrayList();
	
	public ClientProxy()
	{
		//Take this proxy into resource manager reload listener.
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
	}
	
	@Override
	public void loadClient()
	{
		//Register color map loader.
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(ColormapFactory.INSTANCE);
		//The base item model loader.
		ModelLoaderRegistry.registerLoader(NebulaItemModelLoader.INSTANCE);
		//The custom block model loaders.
		ModelLoaderRegistry.registerLoader(ModelFluidBlock.Loader.INSTANCE);
		ModelLoaderRegistry.registerLoader(OrderModelLoader.INSTANCE);
		//Register entity rendering handlers.
		RenderingRegistry.registerEntityRenderingHandler(EntityFallingBlockExtended.class, RenderFallingBlockExt.Factory.instance);
		RenderingRegistry.registerEntityRenderingHandler(EntityProjectileItem.class, RenderProjectileItem.Factory.instance);
		registerRenderObject();
	}
	
	@Override
	public World worldInstance(int id)
	{
		if (Sides.isSimulating())
			return super.worldInstance(id);
		else
		{
			World world = Minecraft.getMinecraft().world;
			return world == null ? null : world.provider.getDimension() != id ? null : world;
		}
	}
	
	@Override
	public String getLocale()
	{
		return Minecraft.getMinecraft().getLanguageManager()
				.getCurrentLanguage().getLanguageCode();
	}
	
	@Override
	public EntityPlayer playerInstance()
	{
		return Minecraft.getMinecraft().player;
	}
	
	@Override
	public File fileDir()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}
	
	public static <T extends Comparable<T>> void registerCompactModel(StateMapperExt mapper, Block block, int metaCount)
	{
		Item item = Item.getItemFromBlock(block);
		for (int i = 0; i < metaCount; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(item, i, mapper.getModelResourceLocation(block.getStateFromMeta(i)));
		}
		ModelLoader.setCustomStateMapper(block, mapper);
	}
	
	public static <T extends Comparable<T>> void registerCompactModel(StateMapperExt mapper, Block block, IProperty<T> property)
	{
		Item item = Item.getItemFromBlock(block);
		IBlockState state = block.getDefaultState();
		if(property != null)
		{
			for (T value : property.getAllowedValues())
			{
				IBlockState state2 = state.withProperty(property, value);
				ModelLoader.setCustomModelResourceLocation(item, block.getMetaFromState(state2), mapper.getModelResourceLocation(state2));
			}
		}
		else
		{
			ModelLoader.setCustomModelResourceLocation(item, 0, mapper.getModelResourceLocation(state));
		}
		ModelLoader.setCustomStateMapper(block, mapper);
	}
	
	public void addRenderRegisterListener(IRenderRegister register)
	{
		if (Loader.instance().hasReachedState(LoaderState.INITIALIZATION))
		{
			Log.warn("Register too late, place register before initalization.");
		}
		else if(register != null)
		{
			nebula.common.util.L.put(registers, Game.getActiveModID(), register);
		}
	}
	
	@Override
	public void registerRender(Object object)
	{
		if(object instanceof IRenderRegister)
		{
			addRenderRegisterListener((IRenderRegister) object);
		}
	}
	
	@Override
	public <T extends Comparable<T>> void registerCompactModel(boolean splitFile, Block block, String modid, String path, IProperty<T> property,
			IProperty...properties)
	{
		StateMapperExt mapper = new StateMapperExt(modid, path, splitFile ? property : null, properties);
		registerCompactModel(mapper, block, property);
	}
	
	@Override
	public void setModelLocate(Item item, int meta, String modid, String name)
	{
		setModelLocate(item, meta, modid, name, null);
	}
	
	@Override
	public void setModelLocate(Item item, int meta, String modid, String name, String type)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modid + ":" + name, type));
	}
	
	@Override
	public void registerBiomeColorMultiplier(Block... block)
	{
		registerColorMultiplier(BIOME_COLOR, block);
		//		registerColorMultiplier(ColorMultiplier.ITEMBLOCK_COLOR, block);
	}
	
	public void registerColorMultiplier(IBlockColor color, Block[] block)
	{
		nebula.common.util.L.put(blockColorMap, color, block);
	}
	
	private void registerColorMultiplier(IItemColor itemblockColor, Block[] blocks)
	{
		nebula.common.util.L.put(itemColorMap, itemblockColor, Lists.transform(Arrays.asList(blocks), (Block block) -> Item.getItemFromBlock(block)));
	}
	
	public void registerColorMultiplier(IItemColor itemColor, Item[] item)
	{
		nebula.common.util.L.put(itemColorMap, itemColor, item);
	}
	
	@Override
	public String translateToLocalByI18n(String unlocal, Object...parameters)
	{
		return I18n.format(unlocal, parameters);
	}
	
	/**
	 * Internal, do not use. (Use ASM from forge)
	 */
	public static void onRegisterAllBlocks(BlockModelShapes shapes)
	{
		shapes.registerBuiltInBlocks(nebula.common.util.L.cast(buildInRender, Block.class));
		buildInRender = null;
	}
	
	public static void registerBuildInModel(Block block)
	{
		if(Loader.instance().hasReachedState(LoaderState.INITIALIZATION))
		{
			Log.warn("The block '" + block.getRegistryName() + "' register buildin model after initialzation, tried register it early.");
		}
		else
		{
			buildInRender.add(block);
		}
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager manager)
	{
		//Checking is reached in preinitalization state.
		if (Loader.instance().hasReachedState(LoaderState.PREINITIALIZATION))
		{
			BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
			ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
			for(Entry<IBlockColor, List<Block>> entry : blockColorMap.entrySet())
			{
				blockColors.registerBlockColorHandler(
						entry.getKey(), nebula.common.util.L.cast(entry.getValue(), Block.class));
			}
			for(Entry<IItemColor, List<Item>> entry : itemColorMap.entrySet())
			{
				itemColors.registerItemColorHandler(
						entry.getKey(), nebula.common.util.L.cast(entry.getValue(), Item.class));
			}
		}
		
		if (Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION))
		{
			Client.getFontRender().onResourceManagerReload(manager);
		}
	}
	
	/**
	 * Let client proxy called this method when FML pre-initialization.
	 */
	public static void registerRenderObject()
	{
		List<IRenderRegister> reg = registers.remove(Game.getActiveModID());
		if(reg != null)
		{
			for(IRenderRegister register : reg)
			{
				register.registerRender();
			}
		}
	}
}