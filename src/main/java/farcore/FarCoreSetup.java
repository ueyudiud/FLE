/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore;

import java.util.Map;

import farcore.data.Config;
import farcore.lib.command.CommandDate;
import farcore.lib.command.CommandSkill;
import farcore.lib.oredict.OreDictExt;
import farcore.lib.solid.Solid;
import farcore.load.ClientLoader;
import farcore.load.CommonLoader;
import nebula.Log;
import nebula.common.config.NebulaConfiguration;
import nebula.common.util.ModCompator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The Far Core modification.
 * <p>
 * Some configuration should called when forge initialization step, so there
 * should has a mod to call these configuration to initialize.
 * 
 * @author ueyudiud
 * @see farcore.FarCore
 */
@Mod(modid = FarCore.ID, version = FarCoreSetup.VERSION, name = "Far Core", dependencies = "required-after:nebula; required-after:ed")
public class FarCoreSetup
{
	/**
	 * The version of Far Core not always refresh. Now Far Core is main update
	 * continent. Please use FLE version to compact your Far Core modification
	 * is needed update.
	 */
	public static final String VERSION = "1.2a";
	
	/** The sided proxy of far core. */
	@SidedProxy(serverSide = "farcore.FarCoreSetup$Proxy", clientSide = "farcore.FarCoreSetup$ClientProxy")
	public static Proxy proxy;
	
	/** The mod instance. */
	@Instance(FarCore.ID)
	public static FarCoreSetup setup;
	
	private ModCompator compator;
	
	public FarCoreSetup()
	{
		setup = this;
		OreDictExt.init();
	}
	
	@EventHandler
	public void load(FMLPreInitializationEvent event)
	{
		ModMetadata modMetadata = event.getModMetadata();
		modMetadata.authorList.add("ueyudiud");
		modMetadata.name = "Far Core";
		modMetadata.credits = "ueyudiud";
		/** Loading non-resource-file from Minecraft running path. */
		Log.info("Loading configuration.");
		NebulaConfiguration.loadStaticConfig(Config.class);
		FarCoreSetup.proxy.load(event);
	}
	
	@EventHandler
	public void Load(FMLInitializationEvent event)
	{
		FarCoreSetup.proxy.load(event);
		this.compator = ModCompator.newCompactor();
		this.compator.addCompatible("fle", "farcore.load.FLECompact");
		this.compator.addCompatible("JEI", "farcore.load.JEICompact");
	}
	
	@EventHandler
	public void load(FMLPostInitializationEvent event)
	{
		FarCoreSetup.proxy.load(event);
		this.compator.call("post");
	}
	
	@EventHandler
	public void complete(FMLLoadCompleteEvent event)
	{
		FarCoreSetup.proxy.load(event);
		this.compator.call("complete");
	}
	
	@EventHandler
	public void load(FMLServerStartingEvent event)
	{
		Solid.REGISTRY.allocIdAuto();
		event.registerServerCommand(new CommandDate());
		event.registerServerCommand(new CommandSkill());
	}
	
	@NetworkCheckHandler
	public static boolean check(Map<String, String> versions, Side side)
	{
		return true;
	}
	
	public static class Proxy implements IGuiHandler
	{
		CommonLoader loader;
		
		public Proxy()
		{
			this.loader = createLoader();
		}
		
		protected CommonLoader createLoader()
		{
			return new CommonLoader();
		}
		
		public void load(FMLPreInitializationEvent event)
		{
			this.loader.preload();
		}
		
		public void load(FMLInitializationEvent event)
		{
			NetworkRegistry.INSTANCE.registerGuiHandler(FarCore.ID, proxy);
			this.loader.load();
		}
		
		public void load(FMLPostInitializationEvent event)
		{
			this.loader.postload();
		}
		
		public void load(FMLLoadCompleteEvent event)
		{
			this.loader.complete();
		}
		
		@Override
		public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			return null;
		}
		
		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			return null;
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class ClientProxy extends Proxy
	{
		@Override
		protected CommonLoader createLoader()
		{
			return new ClientLoader();
		}
		
		@Override
		public void load(FMLPreInitializationEvent event)
		{
			super.load(event);
			// Register all render object.
			nebula.client.ClientProxy.registerRenderObject();
		}
		
		@Override
		public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
		{
			return null;
		}
	}
}
