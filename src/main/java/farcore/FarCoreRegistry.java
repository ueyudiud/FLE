/*
 * copyright 2016-2018 ueyudiud
 */
package farcore;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import farcore.energy.IEnergyNet;
import farcore.energy.thermal.IWorldThermalHandler;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreEnergyHandler;
import farcore.lib.world.IWorldGenerateReplacer;
import nebula.NebulaLog;
import nebula.client.ClientProxy;
import nebula.client.NebulaTextureHandler;
import nebula.client.model.ICustomItemModelSelector;
import nebula.client.model.flexible.NebulaModelLoader;
import nebula.client.render.Colormap;
import nebula.client.render.IIconLoader;
import nebula.client.util.Renders;
import nebula.common.LanguageManager;
import nebula.common.NebulaKeyHandler;
import nebula.common.NebulaWorldHandler;
import nebula.common.network.IPacket;
import nebula.common.network.Network;
import nebula.common.world.IObjectInWorld;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Here provide most of registration for FarCore and Minecraft.
 * 
 * @author ueyudiud
 */
public class FarCoreRegistry
{
	/** The material registers. */
	public static final List<Runnable> MATERIAL_REGISTERS = new ArrayList<>();
	
	/** The world generate replacer during world generating. */
	public static final List<IWorldGenerateReplacer> WORLD_GENERATE_REPLACERS = new ArrayList<>();
	
	/**
	 * Add material register.
	 * <p>
	 * The material should initialized <b>before</b> block, items, etc has. For
	 * loading material, a material register is needed.
	 * <p>
	 * Call this method before
	 * {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}.
	 * 
	 * @param register the material register.
	 */
	public static void addMaterialRegister(Runnable register)
	{
		MATERIAL_REGISTERS.add(register);
	}
	
	/**
	 * Add world generate replacer to generator.
	 * 
	 * @param replacer
	 */
	public static void addWorldGenerateReplacer(IWorldGenerateReplacer replacer)
	{
		WORLD_GENERATE_REPLACERS.add(replacer);
	}
	
	/**
	 * The tile entity need a register name for saving to NBT. Register tile to
	 * map.
	 * 
	 * @param id The save name of tile entity.
	 * @param tileEntityClass The tile entity class.
	 * @see net.minecraft.tileentity.TileEntity#addMapping(Class, String)
	 */
	public static void registerTileEntity(String id, Class<? extends TileEntity> tileEntityClass)
	{
		TileEntity.addMapping(tileEntityClass, id);
	}
	
	/**
	 * Register tile entity special render.
	 * 
	 * @param <T> the tile entity type.
	 * @param tileEntityClass the tile entity class.
	 * @param renderer the tile entity renderer.
	 * @see net.minecraftforge.fml.client.registry.ClientRegistry#bindTileEntitySpecialRenderer(Class,
	 *      TileEntitySpecialRenderer)
	 */
	public static <T extends TileEntity> void registerTESR(Class<T> tileEntityClass, TileEntitySpecialRenderer<? super T> renderer)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, renderer);
	}
	
	/**
	 * Register tile entity special render.
	 * 
	 * @param <T> the tile entity type.
	 * @param tesrClass the tile entity special renderer.
	 * @see net.minecraftforge.fml.client.registry.ClientRegistry#bindTileEntitySpecialRenderer(Class,
	 *      TileEntitySpecialRenderer)
	 */
	public static <T extends TileEntity> void registerTESR(Class<? extends TileEntitySpecialRenderer<T>> tesrClass)
	{
		try
		{
			ParameterizedType type = (ParameterizedType) tesrClass.getGenericSuperclass();
			Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
			registerTESR(clazz, tesrClass.newInstance());
		}
		catch (Exception exception)
		{
			/**
			 * I think no one like to register an invalid class, but it
			 * shouldn't be crash for game still can run without a renderer.
			 */
			NebulaLog.catching(exception);
		}
	}
	
	/**
	 * Register tile entity special render with item special renderer.
	 * 
	 * @param <T> the tile entity type.
	 * @param tileEntityClass the tile entity class.
	 * @param renderer the tile entity renderer.
	 * @param item the rendering override item.
	 * @param meta the rendering meta.
	 * @see #registerTESR(Class, TileEntitySpecialRenderer)
	 */
	public static <T extends TileEntity> void registerTESR(Class<T> tileEntityClass, TileEntitySpecialRenderer<? super T> renderer, Item item, int meta)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, renderer);
		ForgeHooksClient.registerTESRItemStack(item, meta, tileEntityClass);
	}
	
	/**
	 * @see #registerTESR(Class, TileEntitySpecialRenderer, Item, int)
	 */
	public static <T extends TileEntity> void registerTESR(Class<? extends TileEntitySpecialRenderer<T>> tesrClass, Item item, int meta)
	{
		try
		{
			ParameterizedType type = (ParameterizedType) tesrClass.getGenericSuperclass();
			Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
			registerTESR(clazz, tesrClass.newInstance(), item, meta);
		}
		catch (Exception exception)
		{
			/**
			 * I think no one like to register an invalid class, but it
			 * shouldn't be crash for game still can run without a renderer.
			 */
			NebulaLog.catching(exception);
		}
	}
	
	/**
	 * Mark block render by built-in model(TESR), which will not tried to load
	 * model in file.
	 * 
	 * @param block the marked block.
	 * @see net.minecraft.client.renderer.BlockModelShapes#registerBuiltInBlocks(Block...)
	 */
	public static void registerBuiltInModelBlock(Block block)
	{
		ClientProxy.registerBuildInModel(block);
	}
	
	/**
	 * Register event listener to MinecraftForge EventBus.
	 * 
	 * @param listener the listener.
	 * @see net.minecraftforge.common.MinecraftForge#EVENT_BUS
	 */
	public static void registerEventListenerToMF(Object listener)
	{
		MinecraftForge.EVENT_BUS.register(listener);
	}
	
	/**
	 * Added new energy net(Which handle in whole world).
	 * 
	 * @param net the energy net.
	 * @see farcore.handler.FarCoreEnergyHandler#addNet(IEnergyNet)
	 */
	public static void registerEnergyNet(IEnergyNet net)
	{
		FarCoreEnergyHandler.addNet(net);
	}
	
	/**
	 * Register a object current in world, which is contain in a world but is
	 * not a block or entity. (Such as node in Thaumcraft)<br>
	 * Give it a save name when saving. which is not a entity,
	 * 
	 * @param id
	 * @param objInWorldClass
	 */
	public static void registerworldect(String id, Class<? extends IObjectInWorld> objInWorldClass)
	{
		NebulaWorldHandler.registerObject(id, objInWorldClass);
	}
	
	/**
	 * Register a thermal handler of world.
	 * 
	 * @param handler
	 */
	public static void registerWorldThermalHandler(IWorldThermalHandler handler)
	{
		ThermalNet.registerWorldThermalHandler(handler);
	}
	
	/**
	 * Register entity type to FML.
	 * 
	 * @param name The entity registered name.
	 * @param entityClass The entity class.
	 * @param id The id of entity (Different mod needn't to sort same id).
	 * @param mod The modification of this entity type belong.
	 * @param trackingRange The update range.
	 * @param updateFrequency The frequency of update.
	 * @param sendsVelocityUpdates Should send velocity to client side. (I don't
	 *            know what effect this option takes)
	 */
	public static void registerEntity(String name, Class<? extends Entity> entityClass, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, name, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}
	
	/**
	 * Add localized name to mapping.
	 * 
	 * @param unlocalized The key name need translate.
	 * @param localized The translated name in English.
	 */
	public static void registerLocalizedName(String unlocalized, String localized)
	{
		LanguageManager.registerLocal(unlocalized, localized);
	}
	
	/**
	 * Get network if it already exist, or create a new network with specific
	 * name.
	 * 
	 * @param name the name of network.
	 * @return the network instance.
	 */
	public static Network getNetwork(String name)
	{
		return Network.network(name);
	}
	
	/**
	 * Register a packet type to mapping.<br>
	 * 
	 * @param id the name of packet, the number id will generates automatically.
	 * @param packetClass the type of packet.
	 * @param sendTo which side should packet send.
	 */
	public static void registerNetworkPacket(String id, Class<? extends IPacket> packetClass, Side sendTo)
	{
		Network.network(id).registerPacket(packetClass, sendTo);
	}
	
	/**
	 * Register a key binding.
	 * 
	 * @param modid the modid register this key.
	 * @param id the name of key.
	 * @param keycode the key code, see Keyboard to get key.
	 * @see org.lwjgl.input.Keyboard
	 */
	public static void registerKey(String modid, String id, int keycode)
	{
		NebulaKeyHandler.register(id, keycode, modid);
	}
	
	/**
	 * Register a key for client side with current mod container.<br>
	 * 
	 * @param id the name of key.
	 * @param keycode the key code, see Keyboard to get key.
	 * @see org.lwjgl.input.Keyboard
	 */
	public static void registerKey(String id, int keycode)
	{
		NebulaKeyHandler.register(id, keycode);
	}
	
	/**
	 * Register item model location to FarCoreItemModelLoader, which can make
	 * more flexible item models. Provide custom color multiplier,
	 * auto-generated texture replacer, etc.
	 * 
	 * @param item the item to set model.
	 * @param location the model location.
	 * @see farcore.lib.model.item.unused.FarCoreItemModelLoader
	 */
	@SideOnly(Side.CLIENT)
	public static void registerItemModel(Item item, ResourceLocation location)
	{
		NebulaModelLoader.registerModel(item, location);
	}
	
	/**
	 * Register a Model Selector for block.
	 * 
	 * @param block
	 * @param selector
	 */
	@SideOnly(Side.CLIENT)
	public static void registerModelSelector(Block block, ICustomItemModelSelector selector)
	{
		Renders.registerCustomItemModelSelector(block, selector);
	}
	
	/**
	 * Register item model selector, which can switch model in code.
	 * 
	 * @param item The item need model selector.
	 * @param selector The model selector.
	 */
	@SideOnly(Side.CLIENT)
	public static void registerModelSelector(Item item, ICustomItemModelSelector selector)
	{
		Renders.registerCustomItemModelSelector(item, selector);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerColorMultiplier(Block block, IBlockColor colors)
	{
		Renders.registerColorMultiplier(colors, block);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerColorMultiplier(Item item, IItemColor colors)
	{
		Renders.registerColorMultiplier(colors, item);
	}
	
	/**
	 * Get a Color Map (2D coordinated RGB value), loaded from selected path.
	 * 
	 * @param location the resource location of color map.
	 * @return the color map, it will be reload when resources reloading, do not
	 *         use the data in map straightly during initializing game.
	 */
	@SideOnly(Side.CLIENT)
	public static Colormap getColormap(String location)
	{
		return Colormap.getColormap(location);
	}
	
	/**
	 * Added Icon Loader, for loading icon during resource manager reloaded.
	 * 
	 * @param loader the icon loader.
	 */
	@SideOnly(Side.CLIENT)
	public static void addIconLoader(IIconLoader loader)
	{
		NebulaTextureHandler.addIconLoader(loader);
	}
}
