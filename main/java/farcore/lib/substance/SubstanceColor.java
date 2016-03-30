package farcore.lib.substance;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.lib.collection.Register;

public class SubstanceColor implements ISubstance
{
	private static final Register<SubstanceColor> register = new Register();

	public static final SubstanceColor NO_COLOR = new SubstanceColor(0, "void").setColorRGBa(0, 0, 0, 0);
	
	public static SubstanceColor getSubstance(String tag)
	{
		return register.get(tag, NO_COLOR);
	}
	
	public static SubstanceColor getSubstance(int id)
	{
		return register.get(id, NO_COLOR);
	}
	
	public static Register<SubstanceColor> getWoods()
	{
		return register;
	}
	
	protected byte lightR, lightG, lightB;
	protected byte reflectR, reflectG, reflectB;
	protected byte absorbR, absorbG, absorbB;
	
	protected final String name;

	public SubstanceColor(int id, String name)
	{
		this.name = name;
		register.register(id, name, this);
	}
	public SubstanceColor(String name)
	{
		this.name = name;
		register.register(name, this);
	}
	
	public SubstanceColor setColorRGB(int rgb)
	{
		return setColorRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, (rgb) & 0xFF);
	}
	
	public SubstanceColor setColorRGBa(int rgba)
	{
		return setColorRGBa((rgba >> 24) & 0xFF, (rgba >> 16) & 0xFF, (rgba >> 8) & 0xFF, (rgba) & 0xFF);
	}
	
	public SubstanceColor setColorRGB(int r, int g, int b)
	{
		return setColorRGBa(r, g, b, 255);
	}
	
	public SubstanceColor setColorRGBa(int r, int g, int b, int a)
	{
		return setLightReflect(r, g, b, a, a, a);
	}
	
	public SubstanceColor setLightReflect(int rRGB, int aRGB)
	{
		return setLightReflect(
				(rRGB >> 16) & 0xFF, 
				(rRGB >> 8) & 0xFF, 
				(rRGB) & 0xFF, 
				(aRGB >> 16) & 0xFF, 
				(aRGB >> 8) & 0xFF, 
				(aRGB) & 0xFF);
	}
	
	public SubstanceColor setLightReflect(int rR, int rG, int rB, int aR, int aG, int aB)
	{
		reflectR = (byte) (rR & 0xFF);
		reflectG = (byte) (rG & 0xFF);
		reflectB = (byte) (rB & 0xFF);
		absorbR = (byte) (aR & 0xFF);
		absorbG = (byte) (aG & 0xFF);
		absorbB = (byte) (aB & 0xFF);
		return this;
	}

	public SubstanceColor setLightRGB(int light)
	{
		return setLightRGB((light >> 16) & 0xFF, (light >> 8) & 0xFF, light & 0xFF);
	}
	
	public SubstanceColor setLightRGB(int r, int g, int b)
	{
		this.lightR = (byte) (r & 0xFF);
		this.lightG = (byte) (g & 0xFF);
		this.lightB = (byte) (b & 0xFF);
		return this;
	}
	
	public int getColorRGB()
	{
		return (reflectR << 16) | (reflectG << 8) | reflectB;
	}
	
	public int getColorRGBa()
	{
		return (reflectR << 24) | (reflectG << 16) | (reflectB << 8) | ((absorbB * 5 + absorbG * 2 + absorbR * 3) / 10);
	}
	
	public int getLightReflect()
	{
		return getLightReflect(0xFF, 0xFF, 0xFF);
	}
	
	/**
	 * Get light reflect from item face (Such as dye).
	 * @param lightR
	 * @param lightG
	 * @param lightB
	 * @return
	 */
	public int getLightReflect(int lightR, int lightG, int lightB)
	{
		int r = (int) ((float) (lightR * reflectR) / 255F) + lightR;
		int g = (int) ((float) (lightG * reflectG) / 255F) + lightG;
		int b = (int) ((float) (lightB * reflectB) / 255F) + lightB;
		r = Math.min(r, 255);
		g = Math.min(g, 255);
		b = Math.min(b, 255);
		return (r << 16) | (g << 8) | b;
	}
	
	/**
	 * Get light refract color.
	 * @param lightR
	 * @param lightG
	 * @param lightB
	 * @return
	 */
	public int getLightRefract(int lightR, int lightG, int lightB)
	{
		int r = (int) ((255F - (float) (lightR * reflectR) / 255F) * (float) (255 - absorbR) / 255F) + lightR;
		int g = (int) ((255F - (float) (lightG * reflectG) / 255F) * (float) (255 - absorbG) / 255F) + lightG;
		int b = (int) ((255F - (float) (lightB * reflectB) / 255F) * (float) (255 - absorbB) / 255F) + lightB;
		r = Math.min(r, 255);
		g = Math.min(g, 255);
		b = Math.min(b, 255);
		return (r << 16) | (g << 8) | b;
	}
	
	//Override method
	public final String getName(){return name;}
	public final int getID(){return register.id(this);}
	public final Register<SubstanceColor> getRegister(){return register;}

	public void registerLocalName(String name)
	{
		FarCoreSetup.lang.registerLocal("substance.color." + name, name);
	}
	
	public String getLocalName()
	{
		return FarCore.translateToLocal("substance.color." + name);
	}
}