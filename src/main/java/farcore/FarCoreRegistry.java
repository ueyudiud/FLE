package farcore;

import farcore.energy.IEnergyNet;
import farcore.energy.thermal.IWorldThermalHandler;
import farcore.energy.thermal.ThermalNet;
import farcore.handler.FarCoreEnergyHandler;
import farcore.handler.FarCoreKeyHandler;
import farcore.handler.FarCoreWorldHandler;
import farcore.lib.model.block.ICustomItemModelSelector;
import farcore.lib.render.Colormap;
import farcore.lib.util.LanguageManager;
import farcore.lib.world.IObjectInWorld;
import farcore.network.IPacket;
import farcore.network.Network;
import farcore.util.U;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Here provide most of registration for FarCore and Minecraft.
 * @author ueyudiud
 *
 */
public class FarCoreRegistry
{
	/**
	 * The tile entity need a register name for saving to NBT.
	 * Register tile to map.
	 * @param id The save name of tile entity.
	 * @param tileEntityClass The tile entity class.
	 */
	public static void registerTileEntity(String id, Class<? extends TileEntity> tileEntityClass)
	{
		TileEntity.addMapping(tileEntityClass, id);
	}

	/**
	 * Register event listener to minecraft forge event bus.
	 * @param object
	 */
	public static void registerEventListenerToMF(Object listener)
	{
		MinecraftForge.EVENT_BUS.register(listener);
	}
	
	/**
	 * Added new energy net(Which handle in whole world).
	 * @param net
	 */
	public static void registerEnergyNet(IEnergyNet net)
	{
		FarCoreEnergyHandler.addNet(net);
	}

	/**
	 * Register a object current in world, which is contain
	 * in a world but is not a block or entity.
	 * (Such as node in Thaumcraft)<br>
	 * Give it a save name when saving.
	 * which is not a entity,
	 * @param id
	 * @param objInWorldClass
	 */
	public static void registerWorldObject(String id, Class<? extends IObjectInWorld> objInWorldClass)
	{
		FarCoreWorldHandler.registerObject(id, objInWorldClass);
	}
	
	/**
	 * Register a thermal handler of world.
	 * @param handler
	 */
	public static void registerWorldThermalHandler(IWorldThermalHandler handler)
	{
		ThermalNet.registerWorldThermalHandler(handler);
	}

	/**
	 * Register entity type to FML.
	 * @param name
	 * @param entityClass
	 * @param id
	 * @param mod
	 * @param trackingRange
	 * @param updateFrequency
	 * @param sendsVelocityUpdates
	 */
	public static void registerEntity(String name, Class<? extends Entity> entityClass, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(entityClass, name, id, mod, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	/**
	 * Add localized name to mapping.
	 * @param unlocalized The key name need translate.
	 * @param localized The translated name in English.
	 */
	public static void registerLocalizedName(String unlocalized, String localized)
	{
		LanguageManager.registerLocal(unlocalized, localized);
	}
	
	/**
	 * Get network if it already exist, or create a new network.
	 * @param name
	 * @return
	 */
	public static Network getNetwork(String name)
	{
		return Network.network(name);
	}

	/**
	 * Register a packet type to mapping.<br>
	 * @param id The name of packet, the number id will generates automatically.
	 * @param packetClass The type of packet.
	 * @param sendTo Which side should packet send.
	 */
	public static void registerNetworkPacket(String id, Class<? extends IPacket> packetClass, Side sendTo)
	{
		Network.network(id).registerPacket(packetClass, sendTo);
	}

	/**
	 * Register a key for client side.<br>
	 * @param modid The mod register this key.
	 * @param id The name of key.
	 * @param keycode The key code, see Keyboard to get key.
	 * @see org.lwjgl.input.Keyboard
	 */
	public static void registerKey(String modid, String id, int keycode)
	{
		FarCoreKeyHandler.register(id, keycode, modid);
	}

	public static void registerKey(String id, int keycode)
	{
		FarCoreKeyHandler.register(id, keycode);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerModelSelector(Block block, ICustomItemModelSelector selector)
	{
		U.Mod.registerCustomItemModelSelector(block, selector);
	}
	
	/**
	 * Register item model selector, which can switch model in code.
	 * @param item The item need model selector.
	 * @param selector The model selector.
	 */
	@SideOnly(Side.CLIENT)
	public static void registerModelSelector(Item item, ICustomItemModelSelector selector)
	{
		U.Mod.registerCustomItemModelSelector(item, selector);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerColorMultiplier(Block block, IBlockColor colors)
	{
		U.Mod.registerColorMultiplier(colors, block);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerColorMultiplier(Item item, IItemColor colors)
	{
		U.Mod.registerColorMultiplier(colors, item);
	}

	/**
	 * Register an model need't use model by resource pack.
	 * @param block
	 */
	@SideOnly(Side.CLIENT)
	public static void setBuildinModel(Block block)
	{
		FarCoreSetup.ClientProxy.registerBuildInModel(block);
	}
	
	/**
	 * Get a color map (2D coordinated RGB value), loaded from selected path.
	 * @param location The location of color map.
	 * @return The color map, it will be reload when resources reloading,
	 * do not use the data in map straightly during initializing game.
	 */
	@SideOnly(Side.CLIENT)
	public static Colormap getColormap(String location)
	{
		return Colormap.getColormap(location);
	}
}