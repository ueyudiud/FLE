package flapi;

import java.util.ArrayList;
import java.util.List;

import farcore.substance.Matter;
import farcore.util.ILanguageManager;
import flapi.plant.CropCard;
import flapi.plant.ICropSeed;
import flapi.util.ColorMap;
import flapi.util.IFuelHandler;
import flapi.world.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.fluids.FluidStack;

/**
 * The new api of fle.
 * @author ueyudiud
 *
 */
public class FleAPI
{
	/**
	 * The ID of mod FLE, this id is similar with main class in core.
	 * @see fle.FLE
	 */
	public static final String MODID = "fle";
	
	/**
	 * The main and second version here.
	 */
	public static volatile int VERSION = 207;

	/**
	 * This field is main class object of FLE.
	 * @see fle.FLE
	 */
	public static Mod mod;
	
	/**
	 * FLE Language Manager, use to translate localized name.
	 * You need register lang with unlocalized name when mod is loading, 
	 * and use {@link fle.api.util.ILanguageManager.translateToLocal}
	 * to translate localized name when playing.
	 * @see {@link fle.api.util.ILanguageManager}
	 */
	public static ILanguageManager langManager;
	
	/**
	 * UNUSED.
	 * Add recipe with mod.
	 */
	//public static RecipeAdder ra;
	
//	public static Logger getFLELogger()
//	{
//		return FleLog.getLogger();
//	}
	//private static List<IFuelHandler> fuelList = new ArrayList();
	
	/**
	 * Create a color map(Get color of biome, item color, etc).
	 * @param aMapName the textureName of map.
	 * @return a new map, return a default map which return 0xFFFFFF always if this
	 * is server side.
	 */
	public static ColorMap registerColorMap(String aMapName)
	{
		try
		{
			return mod.getColorMapHandler().registerColorMap(aMapName);
		}
		catch(Throwable e)
		{
			return null;
		}
	}
	
	/**
	 * Add a new crop to FLE.
	 * @param aCrop
	 * @param aSeed
	 * @return
	 */
	public static boolean registerCrop(CropCard aCrop, ICropSeed aSeed)
	{
		try
		{
			mod.getCropRegister().registerCrop(aCrop, aSeed);
			return true;
		}
		catch(Throwable e)
		{
			return false;
		}
	}
	
	/**
	 * Register fuel handler, get fuel buffer from handler.
	 * @see fle.api.util.IFuelHandler
	 * @see net.minecraft.item.crafting.FurnaceRecipes
	 * @param aHandler The fuel handler.
	 */
	@Deprecated
	public static void registerFuelHandler(IFuelHandler aHandler)
	{
		//fuelList.add(aHandler);
	}
	
	/**
	 * Get heat value of fluid fuel without air condition.
	 * @param aStack
	 * @return Fuel heat value with unit MJ (Minecraft Joule).
	 */
	@Deprecated
	public static double getFuelBuf(FluidStack aStack)
	{
		try
		{
			return getFuelBuf(aStack, null);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * Get heat value of fluid fuel with air condition
	 * @see flapi.chem.base.material.Matter
	 * @param aStack
	 * @param aAirBase
	 * @return Fuel heat value with unit MJ (Minecraft Joule).
	 */
	@Deprecated
	public static double getFuelBuf(FluidStack aStack, Matter aAirBase)
	{
		return 0;
	}

	/**
	 * Get whether fuel contain population during burning, without air condition.
	 * @param aStack Burning fuel.
	 * @return Whether fuel contain population.
	 */
	@Deprecated
	public static boolean hasSmoke(FluidStack aStack)
	{
		return hasSmoke(aStack, null);
	}
	@Deprecated
	public static boolean hasSmoke(FluidStack aStack, Matter aAirBase)
	{
		return false;
	}

	/**
	 * Get fuel buffer of stack with default air condition.
	 * @param aStack
	 * @return
	 */
	@Deprecated
	public static long getFulBuf(ItemStack aStack)
	{
		return getFulBuf(aStack, null);
	}
	/**
	 * Get fuel buffer of stack with air condition.
	 * @param aStack
	 * @param aAirBase
	 * @return heat value of this fuel each size.
	 */
	@Deprecated
	public static long getFulBuf(ItemStack aStack, Matter aAirBase)
	{
		return TileEntityFurnace.getItemBurnTime(aStack) * 10;
	}
	
	public static int getNextPotionId(int start)
	{
		if(Potion.potionTypes != null)
		{
			while(!((start > 0) && (start < Potion.potionTypes.length) && (Potion.potionTypes[start] == null)))
			{
				++start;
				if(start >= 256) return -1;
			}
			return start;
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * Get wind speed of world tick.
	 * @see {@link fle.api.energy.RotationNet}
	 * @param aPos
	 * @return the wind level of this tick and return default value (1) if FLE
	 * isn't loaded.
	 */
	public static int getWindSpeed(BlockPos aPos)
	{
		try
		{
			return mod.getRotationNet().getWindSpeed(aPos);
		}
		catch(Throwable e)
		{
			return 1;
		}
	}
}