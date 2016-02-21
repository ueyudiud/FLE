package flapi.fluid;

import farcore.collection.Register;
import farcore.substance.Matter;
import flapi.FleAPI;
import flapi.material.PropertyInfo;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidBase extends Fluid
{
	public static Register<FluidBase> register = new Register();
	
	Fluid baseFluid;
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
	public FluidBase(String name, String localizedName, PropertyInfo aInfo, Matter aMatter) 
	{
		super("fle." + name);
		matter = aMatter;
		if(aInfo != null)
		{
			setDensity((int) (aInfo.getDenseness() * 1000));
			setViscosity(aInfo.getViscosity());
			setTemperature((int) ((aInfo.getMeltingPoint() + aInfo.getBoilingPoint()) / 2));
			color = aInfo.getColors()[0];
		}
		register.register(this, name);
		if(FluidRegistry.isFluidRegistered(name))
		{
			
		}
		else
		{
			FluidRegistry.registerFluid(this);
		}
		if(localizedName != null)
			FleAPI.langManager.registerLocal(getUnlocalizedName() + ".name", localizedName);
	}
	
	public Fluid getDefaultFluid()
	{
		if(baseFluid == null)
		{
			if(FluidRegistry.isFluidRegistered(getName().substring(4)))
			{
				baseFluid = FluidRegistry.getFluid(getName().substring(4));
			}
			else
			{
				baseFluid = this;
			}
		}
		return baseFluid;
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
}