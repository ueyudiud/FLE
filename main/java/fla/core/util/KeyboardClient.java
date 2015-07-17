package fla.core.util;

import java.util.EnumSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import cpw.mods.fml.client.registry.ClientRegistry;
import fla.core.Fla;
import fla.core.network.NetWorkManager;

public class KeyboardClient extends Keyboard
{
	private static final String keyCategory = Fla.MODID;
	private final Minecraft mc = Minecraft.getMinecraft();
	private final KeyBinding placeKey = new KeyBinding("Place Key", org.lwjgl.input.Keyboard.KEY_P, "Far Land Era");
	private final KeyBinding techKey = new KeyBinding("Tech Key", org.lwjgl.input.Keyboard.KEY_P, "Far Land Era");
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
				keys.add(Key.Place);
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindForward))
				keys.add(Key.Forward);
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindJump))
				keys.add(Key.Jump);
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindSneak))
				keys.add(Key.Sneak);
			if (GameSettings.isKeyDown(techKey))
				keys.add(Key.Tech);
		}
		int currentKeyState = Keyboard.Key.toInt(keys);
		if (currentKeyState != lastKeyState)
		{
			((NetWorkManager) Fla.fla.nwm.get()).initiateKeyPress(currentKeyState);
			super.processKeyUpdate(Fla.fla.p.get().getPlayerInstance(), currentKeyState);
			lastKeyState = currentKeyState;
		}
	}
}
