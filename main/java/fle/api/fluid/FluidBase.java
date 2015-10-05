package fle.api.fluid;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import fle.api.material.IFluidChemInfo;
import fle.api.material.Matter;
import fle.api.material.PropertyInfo;
import fle.api.util.IChemCondition.EnumOxide;
import fle.api.util.IChemCondition.EnumPH;
import fle.api.util.Register;

public class FluidBase extends Fluid implements IFluidChemInfo
{
	public static Register<FluidBase> register = new Register();
	
	final Matter matter;
	
	public FluidBase(String aName) 
	{
		this(aName, (PropertyInfo) null);
	}
	public FluidBase(String aName, Matter aMatter) 
	{
		this(aName, null, aMatter);
	}
	public FluidBase(String aName, PropertyInfo aInfo) 
	{
		this(aName, aInfo, null);
	}
	public FluidBase(String aName, PropertyInfo aInfo, Matter aMatter) 
	{
		super(aName);
		matter = aMatter;
		if(aInfo != null)
		{
			setDensity((int) (aInfo.getDenseness() * 1000));
			setViscosity(aInfo.getViscosity());
			setTemperature((int) ((aInfo.getMeltingPoint() + aInfo.getBoilingPoint()) / 2));
			color = aInfo.getColors()[0];
		}
		register.register(this, aName);
		FluidRegistry.registerFluid(this);
	}
	
	int color = 0xFFFFFF;
	
	@Override
	public int getColor()
	{
		return color;
	}
	
	private String textureName;
	
	public void registerIcon(IIconRegister aIconRegister)
	{
		setIcons(aIconRegister.registerIcon(getTextureName()));
	}
	
	public FluidBase setTextureName(String aTextureName)
	{
		textureName = aTextureName;
		return this;
	}
	
	public String getTextureName()
	{
		return textureName == null ? "MISSING_TEXTURE_" + fluidName : textureName;
	}

	public FluidBase setFluidPH(EnumPH aPH)
	{
		ph = aPH;
		return this;
	}
	public FluidBase setFluidOxide(EnumOxide aO)
	{
		o = aO;
		return this;
	}
	
	private EnumPH ph = EnumPH.Water;
	private EnumOxide o = EnumOxide.Default;

	public EnumPH getFluidPH()
	{
		return ph;
	}
	public EnumOxide getFluidOxide()
	{
		return o;
	}
	
	@Override
	public EnumPH getFluidPH(FluidStack aStack)
	{
		return getFluidPH();
	}
	
	@Override
	public EnumOxide getFluidOxide(FluidStack aStack)
	{
		return getFluidOxide();
	}
}