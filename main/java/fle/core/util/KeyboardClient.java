package fle.core.util;

import java.util.EnumSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import fle.FLE;
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
				keys.add(Key.Place);
			if (GameSettings.isKeyDown(techKey))
				keys.add(Key.Tech);
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindForward))
				keys.add(Key.Forward);
			if (GameSettings.isKeyDown(mc.gameSettings.keyBindJump))
				keys.add(Key.Jump);
		}
		int currentKeyState = Key.toInt(keys);
		if (currentKeyState != lastKeyState)
		{
			FLE.fle.getNetworkHandler().sendToServer(new FleKeyTypePacket(currentKeyState));
			super.processKeyUpdate(FLE.fle.getPlatform().getPlayerInstance(), currentKeyState);
			lastKeyState = currentKeyState;
		}
	}
}