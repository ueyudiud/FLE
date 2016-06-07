package farcore;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreKeyHandler;
import farcore.interfaces.ICalendar;
import farcore.interfaces.energy.IEnergyNet;
import farcore.lib.render.RenderBase;
import farcore.util.CalendarHandler;
import farcore.util.FleTextureMap;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class FarCoreRegistry
{
	public static void registerOre(String name, Item ore)
	{
		U.OreDict.registerValid(name, ore);
	}
	public static void registerOre(String name, Block ore)
	{
		U.OreDict.registerValid(name, ore);
	}
	public static void registerOre(String name, ItemStack ore)
	{
		U.OreDict.registerValid(name, ore);
	}
	
	public static void registerKey(String name, int keycode)
	{
		FarCoreKeyHandler.register(name, keycode);
	}
	
	public static void addEnergyNet(IEnergyNet net)
	{
		FarCoreEnergyHandler.addNet(net);
	}

	@SideOnly(Side.CLIENT)
	public static void registerCustomRenderBlock(Block block, int meta, Class<RenderBase> clazz)
	{
		FarCore.handlerB.register(block, meta, clazz);
	}

	@SideOnly(Side.CLIENT)
	public static void registerCustomRenderBlockWithoutInventory(Block block, int meta, Class<RenderBase> clazz)
	{
		FarCore.handlerA.register(block, meta, clazz);
	}
	
	/**
	 * Register event listener to MF event bus.
	 * @param object
	 */
	public static void registerMFEventHandler(Object object)
	{
		MinecraftForge.EVENT_BUS.register(object);
	}
	
	/**
	 * Register event listener to FML event bus.
	 * @param object
	 */
	public static void registerFMLEventHandler(Object object)
	{
		FMLCommonHandler.instance().bus().register(object);
	}
		
	public static void addCalendar(int dim, String name, ICalendar calendar)
	{
		CalendarHandler.addCalendar(dim, name, calendar);
	}
	
	@SideOnly(Side.CLIENT)
	public static FleTextureMap newTextureMap(String name)
	{
		return FleTextureMap.TextureMapRegistry.newTextureMap(name);
	}
	
	/**
	 * Create a new texture map.
	 * @param name The name and locate of texture map.
	 * @param skipFirst
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public static FleTextureMap newTextureMap(String name, boolean skipFirst)
	{
		return FleTextureMap.TextureMapRegistry.newTextureMap(name, skipFirst);
	}
}