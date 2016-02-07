package farcore;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import farcore.nbt.NBT;
import farcore.nbt.NBTLoader;
import farcore.nbt.NBTSaver;
import farcore.nbt.NBT.NBTControlor;
import farcore.util.FleLog;
import farcore.util.IColorMapProvider;
import farcore.util.ILanguageManager;
import farcore.util.Vs;
import farcore.world.BlockPos;
import fle.resource.tile.TileEntityDirt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * The base API of Far series mod.
 * 
 * @author ueyudiud
 *		
 */
public class FarCore
{
	/**
	 * This field is main class object of FLE.
	 * @see fle.core.FLE
	 */
	public static FleMod mod;
	/**
	 * FLE Language Manager, use to translate localized name.
	 * You need register lang with unlocalized name when mod is loading, 
	 * and use {@link fle.api.util.ILanguageManager.translateToLocal}
	 * to translate localized name when playing.
	 * @see farcore.util.ILanguageManager
	 */
	public static ILanguageManager lang;

	public static String translateToLocal(String str)
	{
		return translateToLocal(str, new Object[0]);
	}
	public static String translateToLocal(String str, Object...objects)
	{
		return lang.translateToLocal(str, objects);
	}
	
	public static boolean isSimulating()
	{
		return FMLCommonHandler.instance().getEffectiveSide().isClient();
	}
	
	public static EntityPlayer getPlayerInstance()
	{
		return isSimulating() ? 
				Minecraft.getMinecraft().thePlayer : null;
	}

	public static World getWorldInstance(int dim)
	{
		return isSimulating() ? 
				(Minecraft.getMinecraft().theWorld.provider.dimensionId == dim ? 
						Minecraft.getMinecraft().theWorld : null)
				: DimensionManager.getWorld(dim);
	}

	public static File getMinecraftDir()
	{
		return isSimulating() ? Minecraft.getMinecraft().mcDataDir : new File(".");
	}

	public static int getEnviormentTemperature(World world, BlockPos pos)
	{
		return (int) (Math.pow(pos.biome().getFloatTemperature(pos.x, pos.y, pos.z), 0.27403) * 49.6204)
				+ Vs.water_freeze_point - 19;
	}
	
	public static void registerTileEntity(Class<? extends TileEntity> clazz, String id, NBTControlor...controlors)
	{
		GameRegistry.registerTileEntity(clazz, id);
		NBT.register(clazz, controlors);
	}
	
	public static void registerColorMap(IColorMapProvider provider)
	{
		mod.getColorMapHandler().addColorMapProvider(provider);
	}
}