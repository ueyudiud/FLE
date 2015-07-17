package fla.api.recipe;

import net.minecraft.util.IIcon;
import fla.api.util.Registry;

public class FuelOilLamp extends Fuel
{
	private final String fuelName;
	private String fuelTextureName;
	private IIcon icon;
	
	private boolean hasSmoke = false;

	private float buffer;
	public FuelOilLamp(String name, float buffer, String textName)
	{
		this.fuelName = name;
		this.buffer = buffer;
		this.fuelTextureName = textName;
	}
	public FuelOilLamp(String name)
	{
		this.fuelName = name;
		this.buffer = 1.0F;
	}
	
	@Override
	public String getName() 
	{
		return this.fuelName;
	}
	@Override
	public float getFuelBuffer() 
	{
		return buffer;
	}

	@Override
	public String getFuelTextureName() 
	{
		return fuelTextureName;
	}
	
	@Override
	public IIcon getIcon() 
	{
		return icon;
	}
	
	public boolean hasSmoke()
	{
		return hasSmoke;
	}
	
	public FuelOilLamp setFuelBuffer(float buffer)
	{
		this.buffer = buffer;
		return this;
	}
	
	public FuelOilLamp seTextureName(String textureName)
	{
		this.fuelTextureName = textureName;
		return this;
	}
	
	public FuelOilLamp setHasSmoke()
	{
		this.hasSmoke = true;
		return this;
	}
	
	public FuelOilLamp setIcon(IIcon icon)
	{
		this.icon = icon;
		return this;
	}

}
