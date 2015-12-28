package flapi.fluid;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import flapi.FleAPI;
import flapi.chem.base.IChemCondition.EnumOxide;
import flapi.chem.base.IChemCondition.EnumPH;
import flapi.chem.base.IFluidChemInfo;
import flapi.chem.base.Matter;
import flapi.collection.Register;
import flapi.material.PropertyInfo;

public class FluidBase extends Fluid implements IFluidChemInfo
{
	public static Register<FluidBase> register = new Register();
	
	final Matter matter;
	
	public FluidBase(String aName, String localizedName) 
	{
		this(aName, localizedName, (PropertyInfo) null);
	}
	public FluidBase(String aName, String localizedName, Matter aMatter) 
	{
		this(aName, localizedName, null, aMatter);
	}
	public FluidBase(String aName, String localizedName, PropertyInfo aInfo) 
	{
		this(aName, localizedName, aInfo, null);
	}
	public FluidBase(String aName, String localizedName, PropertyInfo aInfo, Matter aMatter) 
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
		if(localizedName != null)
			FleAPI.langManager.registerLocal(getUnlocalizedName() + ".name", localizedName);
	}
	
	@Override
	public String getLocalizedName(FluidStack stack)
	{
		return FleAPI.langManager.translateToLocal(getUnlocalizedName(stack) + ".name", new Object[0]);
	}
	
	int color = 0xFFFFFF;
	
	public FluidBase setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	@Override
	public int getColor()
	{
		return color;
	}
	
	private String textureName;
	
	public void registerIcon(IIconRegister register)
	{
		setIcons(register.registerIcon(getTextureName()), register.registerIcon(getTextureName() + "_move"));
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