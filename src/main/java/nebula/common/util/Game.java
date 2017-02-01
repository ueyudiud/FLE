/*
 * copyright© 2016-2017 ueyudiud
 */

package nebula.common.util;

import java.io.File;

import nebula.Nebula;
import nebula.client.model.ModelFluidBlock;
import nebula.client.util.Renders;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public class Game
{
	public static String getActiveModID()
	{
		try
		{
			if(Loader.instance().activeModContainer() == null)
				return "minecraft";
			return Loader.instance().activeModContainer().getModId();
		}
		catch (Exception exception)
		{
			return "unknown";
		}
	}
	
	public static boolean isModLoaded(String name)
	{
		return Loader.isModLoaded(name);
	}
	
	public static File getMCFile()
	{
		return Nebula.proxy.fileDir();
	}
	
	public static void registerBlock(Block block, String name)
	{
		registerBlock(block, getActiveModID(), name);
	}
	
	public static void registerBlock(Block block, String modid, String name)
	{
		registerBlock(block, modid, name, new ItemBlock(block));
	}
	
	public static void registerBlock(Block block, String modid, String name, Item itemBlock)
	{
		GameRegistry.register(block.setRegistryName(modid, name));
		GameRegistry.register(itemBlock.setRegistryName(modid, name));
		Nebula.proxy.registerRender(block);
	}
	
	public static void registerItem(Item item, String name)
	{
		registerItem(item, getActiveModID(), name);
	}
	
	public static void registerItem(Item item, String modid, String name)
	{
		GameRegistry.register(item.setRegistryName(modid, name));
		Nebula.proxy.registerRender(item);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModel(Block block, int meta, String modid, String locate)
	{
		registerItemBlockModel(Item.getItemFromBlock(block), meta, modid, locate);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModel(Item item, int meta, String modid, String locate)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modid + ":" + locate, "inventory"));
	}
	
	/**
	 * Because this method is often use in item initialization, to
	 * check the side is client or server is too inconvenient, so
	 * this method used handler gateway.
	 * @param item
	 * @param meta
	 * @param modid
	 * @param locate
	 */
	public static void registerItemModel(Item item, int meta, String modid, String locate)
	{
		Nebula.proxy.setModelLocate(item, meta, modid, locate);
	}
	
	public static void registerItemModel(Item item, int meta, String modid, String locate, String type)
	{
		Nebula.proxy.setModelLocate(item, meta, modid, locate, type);
	}
	
	public static void registerBiomeColorMultiplier(Block...block)
	{
		Nebula.proxy.registerBiomeColorMultiplier(block);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerFluid(BlockFluidBase block)
	{
		Renders.registerCustomItemModelSelector(Item.getItemFromBlock(block), ModelFluidBlock.Selector.instance);
		ModelLoader.setCustomStateMapper(block, ModelFluidBlock.Selector.instance);
	}
}