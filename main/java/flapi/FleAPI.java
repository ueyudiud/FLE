package flapi;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;

import farcore.substance.SStack;
import farcore.substance.Substance;
import farcore.util.FleLog;
import farcore.util.Part;
import fle.init.Substances;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
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
	 * @see fle.core.FLE
	 */
	public static final String MODID = "fle";
	
	/**
	 * The main and second version here.
	 */
	public static volatile int VERSION = 206;
	
	/**
	 * Add recipe with mod.
	 */
	//public static RecipeAdder ra;
	
	public static Logger getFLELogger()
	{
		return FleLog.getLogger();
	}
	
	/**
	 * Damage item when use it (throwing, using, crafting, etc).
	 * Which compact with some mods (FLE, GT, etc.).
	 * @see {@link net.minecraft.item.ItemStack}
	 * @param aPlayer the user of this tool, null means no user.
	 * @param aStack the tool which will be damage.
	 * @param aResource the damage type of tool.
	 * @param damage the value of damage level.
	 */
//	public static void damageItem(EntityLivingBase aPlayer, ItemStack aStack, EnumDamageResource aResource, float damage)
//	{
//		if(aStack == null) return;
//		else if(Compact.isFLETool(aStack.getItem()))
//		{
//			((ItemFle) aStack.getItem()).damageItem(aStack, aPlayer, aResource, damage);
//			return;
//		}
//		else if(Compact.isGTTool(aStack.getItem()))
//		{
//			Compact.damageGTTool(aStack, damage);
//			return;
//		}
//		else
//		{
//			aStack.damageItem((int) Math.ceil(damage), aPlayer);
//			return;
//		}
//	}
	
	/**
	 * Check does player has a stack equals to target. 
	 * @param aPlayer
	 * @param aStack target to check.
	 * @return slot ID found, -1 means don't have stack.
	 */
//	public static int doesPlayerHas(EntityPlayer aPlayer, ItemAbstractStack aStack)
//	{
//		if(aPlayer == null) return -1;
//		for(int i = 0; i < 36; ++i)
//		{
//			if(aPlayer.inventory.getStackInSlot(i) != null)
//			{
//				if(aStack.equal(aPlayer.inventory.getStackInSlot(i)))
//				{
//					return i;
//				}
//			}
//		}
//		return -1;
//	}
	
	/**
	 * Create a color map(Get color of biome, item color, etc).
	 * @param aMapName the textureName of map.
	 * @return a new map, return a default map which return 0xFFFFFF always if this
	 * is server side.
	 */
//	public static ColorMap registerColorMap(String aMapName)
//	{
//		try
//		{
//			return mod.getColorMapHandler().registerColorMap(aMapName);
//		}
//		catch(Throwable e)
//		{
//			return null;
//		}
//	}
	
	/**
	 * Add a new crop to FLE.
	 * @param aCrop
	 * @param aSeed
	 * @return
	 */
//	public static boolean registerCrop(CropCard aCrop, ICropSeed aSeed)
//	{
//		try
//		{
//			mod.getCropRegister().registerCrop(aCrop, aSeed);
//			return true;
//		}
//		catch(Throwable e)
//		{
//			return false;
//		}
//	}
	
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
	 * Get direction ordinal, without UNKNOWN and null.
	 * @see net.minecraftforge.common.util.ForgeDirection
	 * @param aDirection
	 * @return The index of direction.
	 */
//	public static int getIndexFromDirection(ForgeDirection aDirection)
//	{
//		if(aDirection == ForgeDirection.UNKNOWN || aDirection == null)
//			return 2;
//		return aDirection.ordinal();
//	}
	
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

	private static boolean environmentSetup = false;
	private static List<SStack> surfaceEnvironment;
	private static List<SStack> netherEnvironment;
	private static List<SStack> endEnvironment;

	public static List<SStack> getEnvironment(int dim)
	{
		if(!environmentSetup)
		{
			surfaceEnvironment = 
					ImmutableList.of(
							new SStack(Substance.getRegister().get("nitrogen"), Part.gas, 158), 
							new SStack(Substance.getRegister().get("oxygen"), Part.gas, 40),
							new SStack(Substance.getRegister().get("carbon dioxide"), Part.gas),
							new SStack(Substance.getRegister().get("vapor"), Part.gas));
			netherEnvironment =
					ImmutableList.of(
							new SStack(Substance.getRegister().get("nitrogen"), Part.gas, 89),
							new SStack(Substance.getRegister().get("oxygen"), Part.gas, 40),
							new SStack(Substance.getRegister().get("carbon dioxide"), Part.gas, 37),
							new SStack(Substance.getRegister().get("sulfur dioxide"), Part.gas, 32),
							new SStack(Substance.getRegister().get("sulfuric acid"), Part.gas, 2));
			endEnvironment =
					ImmutableList.of(
							new SStack(Substance.getRegister().get("nitrogen"), Part.gas, 200));
			environmentSetup = true;
		}
		switch (dim)
		{
		case 0 : return surfaceEnvironment;
		case -1: return netherEnvironment;
		case 1 : return endEnvironment;
		default: break;
		}
		return ImmutableList.of();
	}
	
	/**
	 * Get wind speed of world tick.
	 * @see {@link fle.api.energy.RotationNet}
	 * @param aPos
	 * @return the wind level of this tick and return default value (1) if FLE
	 * isn't loaded.
	 */
//	public static int getWindSpeed(BlockPos aPos)
//	{
//		try
//		{
//			return mod.getRotationNet().getWindSpeed(aPos);
//		}
//		catch(Throwable e)
//		{
//			return 1;
//		}
//	}
}