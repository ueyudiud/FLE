/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.client.util;

import nebula.Nebula;
import nebula.client.ClientProxy;
import nebula.client.model.ICustomItemModelSelector;
import nebula.client.model.StateMapperExt;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class Renders
{
	public static void registerClientRegister(Object object)
	{
		Nebula.proxy.registerRender(object);
	}
	
	public static void registerCompactModel(Block block, boolean splitFile, String modid, String path, IProperty<?> property, IProperty<?>...properties)
	{
		Nebula.proxy.registerCompactModel(splitFile, block, modid, path, property, properties);
	}
	
	public static void registerColorMultiplier(IBlockColor color, Block...block)
	{
		((ClientProxy) Nebula.proxy).registerColorMultiplier(color, block);
	}
	
	public static void registerColorMultiplier(IItemColor color, Item...item)
	{
		((ClientProxy) Nebula.proxy).registerColorMultiplier(color, item);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerCustomItemModelSelector(Block item, ICustomItemModelSelector selector)
	{
		registerCustomItemModelSelector(Item.getItemFromBlock(item), selector);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerCustomItemModelSelector(Item item, ICustomItemModelSelector selector)
	{
		ModelLoader.setCustomMeshDefinition(item, selector);
		ModelBakery.registerItemVariants(item, selector.getAllowedResourceLocations(item).toArray(new ResourceLocation[0]));
	}
	
	public static void registerBuildInModel(Block block)
	{
		nebula.client.ClientProxy.registerBuildInModel(block);
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
	
	public static void registerCompactModel(StateMapperExt mapper, Block block, int metaCount)
	{
		Item item = Item.getItemFromBlock(block);
		for (int i = 0; i < metaCount; ++i)
		{
			ModelLoader.setCustomModelResourceLocation(item, i, mapper.getModelResourceLocation(block.getStateFromMeta(i)));
		}
		ModelLoader.setCustomStateMapper(block, mapper);
	}
}