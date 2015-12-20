package flapi;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.Logger;

import flapi.enums.EnumDamageResource;
import flapi.item.ItemFle;
import flapi.material.Matter;
import flapi.plant.CropCard;
import flapi.plant.ICropSeed;
import flapi.recipe.RecipeAdder;
import flapi.recipe.stack.ItemAbstractStack;
import flapi.util.ColorMap;
import flapi.util.Compact;
import flapi.util.FleLog;
import flapi.util.IFuelHandler;
import flapi.util.ILanguageManager;
import flapi.world.BlockPos;

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
	public static volatile int VERSION = 206;

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
	 * Add recipe with mod.
	 */
	public static RecipeAdder ra;
	
	public static Logger getFLELogger()
	{
		return FleLog.getLogger();
	}
	private static List<IFuelHandler> fuelList = new ArrayList();
	
	/**
	 * Damage item when use it (throwing, using, crafting, etc).
	 * Which compact with some mods (FLE, GT, etc.).
	 * @see {@link net.minecraft.item.ItemStack}
	 * @param aPlayer the user of this tool, null means no user.
	 * @param aStack the tool which will be damage.
	 * @param aResource the damage type of tool.
	 * @param damage the value of damage level.
	 */
	public static void damageItem(EntityLivingBase aPlayer, ItemStack aStack, EnumDamageResource aResource, float damage)
	{
		if(aStack == null) return;
		else if(Compact.isFLETool(aStack.getItem()))
		{
			((ItemFle) aStack.getItem()).damageItem(aStack, aPlayer, aResource, damage);
			return;
		}
		else if(Compact.isGTTool(aStack.getItem()))
		{
			Compact.damageGTTool(aStack, damage);
			return;
		}
		else
		{
			aStack.damageItem((int) Math.ceil(damage), aPlayer);
			return;
		}
	}
	
	/**
	 * Check does player has a stack equals to target. 
	 * @param aPlayer
	 * @param aStack target to check.
	 * @return slot ID found, -1 means don't have stack.
	 */
	public static int doesPlayerHas(EntityPlayer aPlayer, ItemAbstractStack aStack)
	{
		if(aPlayer == null) return -1;
		for(int i = 0; i < 36; ++i)
		{
			if(aPlayer.inventory.getStackInSlot(i) != null)
			{
				if(aStack.equal(aPlayer.inventory.getStackInSlot(i)))
				{
					return i;
				}
			}
		}
		return -1;
	}
	
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
	 * Match is stack are contain target.
	 * @see net.minecraftforge.fluids.FluidStack
	 * @param aTarget
	 * @param aStack
	 * @return
	 */
	public static boolean fluidMatch(FluidStack aTarget, FluidStack aStack)
	{
		return aTarget == null && aStack == null ? true : 
			(aTarget == null || aStack == null ? false : aTarget.isFluidEqual(aStack) && aStack.amount >= aTarget.amount);
	}
	
	/**
	 * Register fuel handler, get fuel buffer from handler.
	 * @see fle.api.util.IFuelHandler
	 * @see net.minecraft.item.crafting.FurnaceRecipes
	 * @param aHandler The fuel handler.
	 */
	public static void registerFuelHandler(IFuelHandler aHandler)
	{
		fuelList.add(aHandler);
	}
	
	/**
	 * Get heat value of fluid fuel without air condition.
	 * @param aStack
	 * @return Fuel heat value with unit MJ (Minecraft Joule).
	 */
	public static double getFuelBuf(FluidStack aStack)
	{
		try
		{
			return getFuelBuf(aStack, Matter.mAir);
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			return 0;
		}
	}
	/**
	 * Get heat value of fluid fuel with air condition
	 * @see fle.api.material.Matter
	 * @param aStack
	 * @param aAirBase
	 * @return Fuel heat value with unit MJ (Minecraft Joule).
	 */
	public static double getFuelBuf(FluidStack aStack, Matter aAirBase)
	{
		if(aStack == null) return 0;
		else if(aStack.amount <= 0) return 0;
		for (IFuelHandler tHandler : fuelList)
		{
			double buf = tHandler.getFuelCalorificValue(aStack, aAirBase);
			if(buf >= 0)
				return buf;
		}
		return 0;
	}

	/**
	 * Get whether fuel contain population during burning, without air condition.
	 * @param aStack Burning fuel.
	 * @return Whether fuel contain population.
	 */
	public static boolean hasSmoke(FluidStack aStack)
	{
		return hasSmoke(aStack, Matter.mAir);
	}
	public static boolean hasSmoke(FluidStack aStack, Matter aAirBase)
	{
		for (IFuelHandler tHandler : fuelList)
		{
			double buf = tHandler.getFuelCalorificValue(aStack, aAirBase);
			if(buf >= 0)
				return tHandler.getFuelRequireSmoke(aStack, aAirBase);
		}
		return false;
	}

	/**
	 * Get fuel buffer of stack with default air condition.
	 * @param aStack
	 * @return
	 */
	public static long getFulBuf(ItemStack aStack)
	{
		return getFulBuf(aStack, Matter.mAir);
	}
	/**
	 * Get fuel buffer of stack with air condition.
	 * @param aStack
	 * @param aAirBase
	 * @return heat value of this fuel each size.
	 */
	public static long getFulBuf(ItemStack aStack, Matter aAirBase)
	{
		for (IFuelHandler tHandler : fuelList)
		{
			if(tHandler.getFuelCalorificValue(aStack, aAirBase) > 0)
				return tHandler.getFuelCalorificValue(aStack, aAirBase);
		}
		return TileEntityFurnace.getItemBurnTime(aStack) * 10;
	}
	
	/**
	 * Get direction ordinal, without UNKNOWN and null.
	 * @see net.minecraftforge.common.util.ForgeDirection
	 * @param aDirection
	 * @return The index of direction.
	 */
	public static int getIndexFromDirection(ForgeDirection aDirection)
	{
		if(aDirection == ForgeDirection.UNKNOWN || aDirection == null)
			return 2;
		return aDirection.ordinal();
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