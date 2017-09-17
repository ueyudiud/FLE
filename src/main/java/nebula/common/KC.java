/*
 * copyright© 2016-2017 ueyudiud
 */
package nebula.common;

import nebula.common.NebulaKeyHandler.KB;
import nebula.common.util.Strings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author ueyudiud
 */
@SideOnly(Side.CLIENT)
public class KC extends KB
{
	public static KeyBinding getBinding(String name)
	{
		return (KeyBinding) ((KC) NebulaKeyHandler.keyRegister).keyCodeMap.get(name);
	}
	
	@Override
	public void register(String name, int keycode, String modid)
	{
		KeyBinding binding = new KeyBinding(Strings.validate(name), keycode, modid);
		ClientRegistry.registerKeyBinding(binding);
		NebulaKeyHandler.keys.register(name, binding);
		this.keyCodeMap.put(name, binding);
	}
}