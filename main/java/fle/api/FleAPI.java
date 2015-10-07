package fle.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.crop.CropCard;
import fle.api.crop.ICropSeed;
import fle.api.enums.EnumDamageResource;
import fle.api.fluid.FluidDictionary;
import fle.api.item.ItemFleMetaBase;
import fle.api.material.Matter;
import fle.api.recipe.ItemAbstractStack;
import fle.api.recipe.RecipeAdder;
import fle.api.util.ColorMap;
import fle.api.util.Compact;
import fle.api.util.IFuelHandler;
import fle.api.util.ILanguageManager;
import fle.api.world.BlockPos;

public class FleAPI 
{
	public static final String MODID = "FLE";
	/**
	 * The main and second version here.
	 */
	public static volatile int VERSION = 205;
	/**
	 * The locate of condition icon collection, see {@link fle.api.gui.GuiCondition}.
	 * Use renderEngine to bind this location when used in GUI.
	 */
	@SideOnly(Side.CLIENT)
	public static ResourceLocation conditionLocate = new ResourceLocation("textures/atlas/condition.png");
	@Deprecated
	@SideOnly(Side.CLIENT)
	public static ResourceLocation fontLocate = new ResourceLocation("textures/atlas/fontLocate");
	/**
	 * Condition icon register.
	 * Called when load icons.
	 */
	@SideOnly(Side.CLIENT)
	public static IIconRegister conditionIconRegister;
	@SideOnly(Side.CLIENT)
	public static IIconRegister fontRegister;
	
	/**
	 * To add recipe.
	 */
	public static RecipeAdder ra;
	/**
	 * @see {@link fle.FLE}, this field is main class object of FLE.
	 */
	public static FleModHandler mod;
	/**
	 * Like oreDictionary, this field use to register similar fluid such like
	 * plantOil or animalOil.
	 */
	public static FluidDictionary fluidDictionary;
	/**
	 * FLE Language Manager, use to translate localized name.
	 * You need register lang with unlocalized name when mod is loading, 
	 * and use {@link fle.api.util.ILanguageManager.translateToLocal}
	 * to translate localized name when playing.
	 * @see {@link fle.api.util.ILanguageManager}
	 */
	public static ILanguageManager lm;
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
			((ItemFleMetaBase) aStack.getItem()).damageItem(aStack, aPlayer, aResource, damage);
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
		for(int i = 0; i < 36; ++i)
		{
			if(aPlayer.inventory.getStackInSlot(i) != null)
			{
				if(aStack.isStackEqul(aPlayer.inventory.getStackInSlot(i)))
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
	
	public static boolean fluidMatch(FluidStack aTarget, FluidStack aStack)
	{
		return aTarget == null && aStack == null ? true : 
			(aTarget == null || aStack == null ? false : aTarget.isFluidEqual(aStack) && aStack.amount >= aTarget.amount);
	}
	
	public static void registerFuelHandler(IFuelHandler aHandler)
	{
		fuelList.add(aHandler);
	}
	
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
	public static int getFulBuf(ItemStack aStack)
	{
		return getFulBuf(aStack, Matter.mAir);
	}
	/**
	 * Get fuel buffer of stack with air condition.
	 * @param aStack
	 * @param aAirBase
	 * @return heat value of this fuel each size.
	 */
	public static int getFulBuf(ItemStack aStack, Matter aAirBase)
	{
		for (IFuelHandler tHandler : fuelList)
		{
			if(tHandler.getFuelCalorificValue(aStack, aAirBase) > 0)
				return tHandler.getFuelCalorificValue(aStack, aAirBase);
		}
		return TileEntityFurnace.getItemBurnTime(aStack) * 10;
	}
	
	public static int getIndexFromDirection(ForgeDirection aDirection)
	{
		if(aDirection == ForgeDirection.UNKNOWN || aDirection == null)
			return 2;
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			if(ForgeDirection.VALID_DIRECTIONS[i] == aDirection) return i;
		return 2;
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