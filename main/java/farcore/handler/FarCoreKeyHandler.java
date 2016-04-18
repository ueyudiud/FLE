package farcore.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import farcore.FarCoreSetup;
import farcore.lib.collection.IRegister;
import farcore.lib.collection.Register;
import farcore.lib.net.PacketKey;
import farcore.util.U;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class FarCoreKeyHandler
{	
	@SidedProxy(serverSide = "farcore.handler.FarCoreKeyHandler$KB", clientSide= "farcore.handler.FarCoreKeyHandler$KC")
	private static KB keyRegister;

	private static IRegister<KeyBinding> keys = new Register();
	private static Map<EntityPlayer, List<String>> keyMap = new HashMap();

	public static void remove(EntityPlayer player)
	{
		keyMap.remove(player);
	}
	
	public static void reset(EntityPlayer player)
	{
		if(keyMap.containsKey(player))
		{
			keyMap.get(player).clear();
		}
	}
	
	public static void add(EntityPlayer player, String key)
	{
		if(!keyMap.containsKey(player))
		{
			keyMap.put(player, new ArrayList());
		}
		keyMap.get(player).add(key);
	}
	
	public static boolean get(EntityPlayer player, String key)
	{
		return "sneak".equals(key) ? player.isSneaking() :
			keyMap.containsKey(player) && keyMap.get(player).contains(key);
	}
	
	public static void register(String name, int keycode)
	{
		register(name, keycode, U.Mod.getActiveModID());
	}
	
	public static void register(String name, int keycode, String modid)
	{
		keyRegister.register(name, keycode, modid);
	}
	
	public static class KB
	{
		public void register(String name, int keycode, String modid)
		{
			keys.register(name, null);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class KC extends KB
	{
		@Override
		public void register(String name, int keycode, String modid)
		{
			KeyBinding binding = new KeyBinding(U.Lang.validate(name), keycode, modid);
			ClientRegistry.registerKeyBinding(binding);
			keys.register(name, binding);
		}
	}
	
	@SideOnly(Side.SERVER)
	public static void onServerRecieve(EntityPlayer player, long code)
	{
		reset(player);
		for(int i = 0; i < keys.size(); ++i)
		{
			if((code & (1L << i)) != 0)
			{
				add(player, keys.name(i));
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerLogOut(PlayerLoggedOutEvent event)
	{
		if(!U.Sides.isClient())
		{
			remove(event.player);
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event)
	{
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
		if (currentScreen == null || currentScreen.allowUserInput)
		{
			if(event.phase == Phase.START)
			{
				if(U.Sides.isSimulating())
				{
					long v = 0;
					for(int i = 0; i < keys.size(); ++i)
					{
						if(GameSettings.isKeyDown(keys.get(i)))
						{
							v |= (1L << i);
						}
					}
					FarCoreSetup.network.sendToServer(new PacketKey(v));
				}
				reset(U.Worlds.player());
				if(event.phase == Phase.START)
				{
					for(int i = 0; i < keys.size(); ++i)
					{
						if(GameSettings.isKeyDown(keys.get(i)))
						{
							add(U.Worlds.player(), keys.name(i));
						}
					}				
				}
			}
		}
	}
}