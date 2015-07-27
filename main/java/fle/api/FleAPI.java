package fle.api;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fle.api.crop.CropCard;
import fle.api.crop.ICropSeed;
import fle.api.fluid.FluidDictionary;

public class FleAPI 
{
	public static final String MODID = "FLE";
	public static volatile int VERSION = 202;
	@SideOnly(Side.CLIENT)
	public static IIconRegister blockIconRegister;
	@SideOnly(Side.CLIENT)
	public static IIconRegister itemIconRegister;
	
	public static FleModHandler mod;
	public static FluidDictionary fluidDictionary;
	
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
}