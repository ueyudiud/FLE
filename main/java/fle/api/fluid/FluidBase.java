package fle.api.fluid;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import fle.api.material.Matter;
import fle.api.material.PropertyInfo;
import fle.api.util.Register;

public class FluidBase extends Fluid
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
		}
		register.register(this, aName);
		FluidRegistry.registerFluid(this);
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
}