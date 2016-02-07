package fle.core.util;

import java.util.EnumSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import farcore.FarCore;
import fle.core.FLE;
import fle.core.net.FleKeyTypePacket;

public class KeyboardClient extends Keyboard
{
	private final Minecraft mc = Minecraft.getMinecraft();
	public static final KeyBinding placeKey = new KeyBinding("Place Key", org.lwjgl.input.Keyboard.KEY_P, FLE.MODID);
	public static final KeyBinding techKey = new KeyBinding("Tech Key", org.lwjgl.input.Keyboard.KEY_T, FLE.MODID);
	private int lastKeyState;
	
	public KeyboardClient()
	{
		lastKeyState = 0;
		ClientRegistry.registerKeyBinding(placeKey);
		ClientRegistry.registerKeyBinding(techKey);
	}
	
	public void sendKeyUpdate()
	{
		Set keys = EnumSet.noneOf(Key.class);
		GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
		if (currentScreen == null || currentScreen.allowUserInput)
		{
			if (GameSettings.isKeyDown(placeKey))
				keys.add(Keyboard.Key.Place);
			if (GameSettings.isKeyDown(techKey))
				keys.add(Keyboard.Key.Tech);
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindForward))
				keys.add(Keyboard.Key.Forward);
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindJump))
				keys.add(Keyboard.Key.Jump);
		}
		int currentKeyState = Keyboard.Key.toInt(keys);
		if (currentKeyState != lastKeyState)
		{
			FLE.fle.getNetworkHandler().sendToServer(new FleKeyTypePacket(currentKeyState));
			super.processKeyUpdate(FarCore.getPlayerInstance(), currentKeyState);
			lastKeyState = currentKeyState;
		}
	}
}