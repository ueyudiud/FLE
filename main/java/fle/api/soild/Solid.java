package fle.api.soild;

import fle.api.FleAPI;
import fle.api.soild.Solid.SolidState;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class Solid
{
	public Solid(String name)
	{
		SolidRegistry.registrySoild(name, this);
	}
	public Solid(String name, String localizedName)
	{
		this(name);
		FleAPI.lm.registerLocal(getUnlocalizedName() + ".name", localizedName);
	}
	
	public String getSolidName()
	{
		return SolidRegistry.register.name(this);
	}
	
	public String getUnlocalizedName()
	{
		return String.format("solid.%s", getSolidName());
	}
	
	public String getUnlocalizedName(SolidStack aStack)
	{
		return getUnlocalizedName();
	}
	
	public String getLocalizedName()
	{
		return FleAPI.lm.translateToLocal(getUnlocalizedName() + ".name", new Object[0]);
	}
	
	public String getLocalizedName(SolidStack aStack)
	{
		return getLocalizedName();
	}
	
	protected SolidState state;
	protected String textureName;
	protected IIcon icon;
	
	public SolidState getSolidState()
	{
		return state == null ? SolidState.Dust : state;
	}
	
	public String getTextureName()
	{
		return textureName == null ? "MISSING_ICON_NAME_" + getSolidName().toUpperCase() : textureName;
	}
	
	public void registerIcon(IIconRegister register)
	{
		icon = register.registerIcon(getTextureName());
	}

	public IIcon getIcon()
	{
		return icon;
	}
	
	public IIcon getIcon(SolidStack stack)
	{
		return getIcon();
	}
	
	public static enum SolidState
	{
		Chunk,
		Chip,
		Dust,
		Sick_Dust;
	}
	
	public boolean canPickup()
	{
		return state != SolidState.Dust;
	}
	
	public Solid setTextureName(String string)
	{
		textureName = string;
		return this;
	}
	
	public Solid setType(SolidState aState)
	{
		state = aState;
		return this;
	}
	
	public int getColor()
	{
		return 0xFFFFFF;
	}
	public int getColor(SolidStack solid)
	{
		return getColor();
	}
}