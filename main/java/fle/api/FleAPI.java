package fle.api;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import fle.api.util.ColorMap;
import fle.api.util.Compact;
import fle.api.util.IFuelHandler;

public class FleAPI 
{
	public static final String MODID = "FLE";
	public static volatile int VERSION = 202;
	@SideOnly(Side.CLIENT)
	public static ResourceLocation conditionLocate = new ResourceLocation("textures/atlas/condition.png");
	@SideOnly(Side.CLIENT)
	public static IIconRegister conditionIconRegister;
	
	public static FleModHandler mod;
	public static FluidDictionary fluidDictionary;
	private static List<IFuelHandler> fuelList = new ArrayList();
	
	public static void damageItem(EntityPlayer aPlayer, ItemStack aStack, EnumDamageResource aResource, float damage)
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
	public static int dosePlayerHas(EntityPlayer aPlayer, ItemAbstractStack aStack)
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
	
	public static int getIndexFromDirection(ForgeDirection aDirection)
	{
		if(aDirection == ForgeDirection.UNKNOWN || aDirection == null)
			return 2;
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			if(ForgeDirection.VALID_DIRECTIONS[i] == aDirection) return i;
		return 2;
	}
}