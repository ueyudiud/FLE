package fle.api;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.crop.CropCard;
import fle.api.crop.ICropSeed;
import fle.api.fluid.FluidDictionary;
import fle.api.util.ColorMap;

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
	
	public static int getIndexFromDirection(ForgeDirection aDirection)
	{
		if(aDirection == ForgeDirection.UNKNOWN || aDirection == null)
			return 2;
		for(int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; ++i)
			if(ForgeDirection.VALID_DIRECTIONS[i] == aDirection) return i;
		return 2;
	}
}