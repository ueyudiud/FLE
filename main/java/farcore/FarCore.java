package farcore;

import farcore.nbt.NBT;
import farcore.nbt.NBT.NBTControlor;
import farcore.tileentity.TEBase;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * The base API of Far series mod.
 * 
 * @author ueyudiud
 *		
 */
public class FarCore
{
	public static void registerTileEntity(Class<? extends TEBase> clazz,
			String name)
	{
		registerTileEntity(clazz, name, new NBTControlor[0]);
	}
	
	/**
	 * Register tile entity and save&load handler.
	 * 
	 * @param clazz
	 * @param name
	 * @param controlors
	 */
	public static void registerTileEntity(Class<? extends TEBase> clazz,
			String name, NBTControlor... controlors)
	{
		GameRegistry.registerTileEntity(clazz, name);
		NBT.register(clazz, controlors);
	}
}