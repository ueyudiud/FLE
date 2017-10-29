/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common.util;

import java.io.File;

import nebula.Nebula;
import nebula.asm.NebulaSetup;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
public final class Game
{
	private Game() {}
	
	/**
	 * Get active modid, or get <tt>minecraft</tt> as modid if no
	 * active mod container found.
	 * @return the id of active mod.
	 */
	public static String getActiveModID()
	{
		try
		{
			if (Loader.instance().activeModContainer() == null)
				return "minecraft";
			return Loader.instance().activeModContainer().getModId();
		}
		catch (Exception exception)
		{
			return "unknown";
		}
	}
	
	/**
	 * Query if we know of a mod named modname.
	 * @param name the modid.
	 * @return return <code>true</code> if mod are loaded.
	 */
	public static boolean isModLoaded(String name)
	{
		return Loader.isModLoaded(name);
	}
	
	/**
	 * Get Minecraft runtime file, the default file position is
	 * ./.minecraft/ in client side, and ./ in server side.
	 * @return the file.
	 */
	public static File getMCFile()
	{
		return NebulaSetup.getMcPath();
	}
	
	/**
	 * Register block with name.
	 * @param block the block.
	 * @param name the block name.
	 */
	public static void registerBlock(Block block, String name)
	{
		registerBlock(block, getActiveModID(), name);
	}
	
	/**
	 * Register block with modid.
	 * (The Forge given big warning if the modid and active mod id can not matched,
	 * I don't think this warning should be given.)
	 * @param block
	 * @param modid
	 * @param name
	 */
	public static void registerBlock(Block block, String modid, String name)
	{
		registerBlock(block, modid, name, new ItemBlock(block));
	}
	
	/**
	 * Register block with modid and ItemBlock type.
	 * @param block the block.
	 * @param modid the registry modid.
	 * @param name the registry name.
	 * @param itemBlock the item block, the item type of this block.
	 */
	public static void registerBlock(Block block, String modid, String name, Item itemBlock)
	{
		GameRegistry.register(block.setRegistryName(modid, name));
		GameRegistry.register(itemBlock.setRegistryName(modid, name));
		registerClientRegister(block);
	}
	
	public static void registerItem(Item item, String name)
	{
		registerItem(item, getActiveModID(), name);
	}
	
	public static void registerItem(Item item, String modid, String name)
	{
		GameRegistry.register(item.setRegistryName(modid, name));
		registerClientRegister(item);
	}
	
	/**
	 * Register normal block model.
	 * @param block
	 * @param meta
	 * @param modid
	 * @param locate
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemBlockModel(Block block, int meta, String modid, String locate)
	{
		registerItemBlockModel(Item.getItemFromBlock(block), meta, modid, locate);
	}
	
	/**
	 * Register normal block model.
	 * @param item The item block.
	 * @param meta
	 * @param modid
	 * @param locate
	 */
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
	
	/**
	 * The client and server both can use this method to
	 * register object implements {@link nebula.client.util.IRenderRegister}.
	 * @param arg the object may implements IRenderRegister.
	 */
	public static void registerClientRegister(Object arg)
	{
		Nebula.proxy.registerRender(arg);
	}
}