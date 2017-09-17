/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nebula.Nebula;
import nebula.base.IRegister;
import nebula.base.Register;
import nebula.common.network.packet.PacketKey;
import nebula.common.util.Game;
import nebula.common.util.Players;
import nebula.common.util.Sides;
import nebula.common.util.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NebulaKeyHandler
{
	@SidedProxy(serverSide = "nebula.common.NebulaKeyHandler$KeyB",
			clientSide= "nebula.common.NebulaKeyHandler$KeyC")
	static KeyB keyRegister;
	
	static IRegister<Object> keys = new Register<>();
	private static Map<EntityPlayer, List<String>> keyMap = new HashMap<>();
	
	public static void remove(EntityPlayer player)
	{
		keyMap.remove(player);
	}
	
	public static void reset(EntityPlayer player)
	{
		if(keyMap.containsKey(player))
			keyMap.get(player).clear();
	}
	
	public static void add(EntityPlayer player, String key)
	{
		if(!keyMap.containsKey(player))
			keyMap.put(player, new ArrayList<>());
		keyMap.get(player).add(key);
	}
	
	public static boolean get(EntityPlayer player, String key)
	{
		return "sneak".equals(key) ? player.isSneaking() :
			keyMap.containsKey(player) && keyMap.get(player).contains(key);
	}
	
	public static void register(String name, int keycode)
	{
		register(name, keycode, Game.getActiveModID());
	}
	
	public static void register(String name, int keycode, String modid)
	{
		keyRegister.register(name, keycode, modid);
	}
	
	public static class KeyB
	{
		Map<String, Object> keyCodeMap = new HashMap<>();
		
		public void register(String name, int keycode, String modid)
		{
			keys.register(name, null);
			this.keyCodeMap.put(name, keycode);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static class KeyC extends KeyB
	{
		@Override
		public void register(String name, int keycode, String modid)
		{
			KeyBinding binding = new KeyBinding(Strings.validate(name), keycode, modid);
			ClientRegistry.registerKeyBinding(binding);
			NebulaKeyHandler.keys.register(name, binding);
			this.keyCodeMap.put(name, binding);
		}
	}
	
	@SideOnly(Side.SERVER)
	public static void onServerRecieve(EntityPlayer player, long code)
	{
		reset(player);
		for(int i = 0; i < keys.size(); ++i)
			if((code & (1L << i)) != 0)
				add(player, keys.name(i));
	}
	
	@SubscribeEvent
	public void onPlayerLogOut(PlayerLoggedOutEvent event)
	{
		if (!Sides.isClient())
			remove(event.player);
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
				if(Sides.isSimulating())
				{
					long v = 0;
					for(int i = 0; i < keys.size(); ++i)
						if(GameSettings.isKeyDown((KeyBinding) keys.get(i)))
							v |= (1L << i);
					Nebula.network.sendToServer(new PacketKey(v));
				}
				reset(Players.player());
				for(int i = 0; i < keys.size(); ++i)
					if(GameSettings.isKeyDown((KeyBinding) keys.get(i)))
						add(Players.player(), keys.name(i));
			}
		}
		else
		{
			if(Sides.isSimulating())
				Nebula.network.sendToServer(new PacketKey(0L));
			reset(Players.player());
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static KeyBinding getBinding(String name)
	{
		return (KeyBinding) keyRegister.keyCodeMap.get(name);
	}
}