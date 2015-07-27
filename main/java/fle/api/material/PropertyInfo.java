package fle.api.material;

import fle.api.enums.MatterState;

/**
 * Far Land Era Implementation
 * 
 * This class is a property (for matter) equivalent to "ToolMaterial." It describes the nature of 
 * the property of a matter.
 * 
 * These properties do not have inherent gameplay mechanics - they are provided so that mods may
 * choose to take advantage of them.
 * 
 * The default values can be used as a reference point for mods adding matter such as oil or heavy
 * water.
 * @author ueyudiud
 *
 */
public final class PropertyInfo 
{
	/**
	 * Get color value of matter.
	 * see {@link net.minecraft.item.Item}
	 */
	private float reflectRedLight = 1.0F;
	private float reflectGreenLight = 1.0F;
	private float reflectBlueLight = 1.0F;
	/**
	 * Use Kelvin degree, in (At 101MPa).
	 */
	private long meltingPoint = 273;
	private long boilingPoint = 373;
	
	/**
	 * In thermodynamics, the triple point of a substance is the temperature and pressure 
	 * at which the three phases (gas, liquid, and solid) of that substance coexist in 
	 * thermodynamic equilibrium. -From wikipedia
	 * 
	 * This property use to get state of matter when get temperature and press.
	 */
	private long triplePointTemperature = 612;
	/**
	 * Use logarithm to count press point, 1atm is 101kPa means this value is about 5.0D.
	 */
	private double triplePointPress = Math.log10(611.73D);
	/**
	 * Press effect is said the effect when press grown higher, and how much the state of 
	 * matter will change.
	 * 
     * Default value is approximately the real-life pressEffect(But this value is not a real
     * value in thermodynamic) of water in Pa/K.
	 */
	private float pressEffect = -0.2F;
	/**
     * Viscosity ("thickness") of the fluid - completely arbitrary; negative values are not
     * permissible.
     *
     * Default value is approximately the real-life density of water in m/s^2 (x10^-3).
     *
     * Higher viscosity means that a fluid flows more slowly, like molasses.
     * Lower viscosity means that a fluid flows more quickly, like helium. 
     * {@link net.minecraftforge.fluids.Fluid}
	 */
	private int viscosity = 1000;
	/**
     * Hardness of the solid - the effect to carve in another item.
     *
     * Higher hardness means that a solid carve more easily, like diamond.
     * Lower hardness means that a solid carve more hardly, like gypsum. 
	 */
	private float hardness = 1.0F;
	/**
     * Thoughness of the solid - completely arbitrary; negative values are not permissible.
     *
     * Higher toughness means that a solid can craft easily, like gold.
     * Lower toughness means that a solid can craft hardly, like pigiron. 
	 */
	private float toughness = 1.0F;
	/**
     * Brittleness of the solid - completely arbitrary; negative values are not permissible.
     *
     * Higher toughness means that a solid can break easily, like rock.
     * Lower toughness means that a solid can break hardly, like metal. 
	 */
	private float brittleness = 0.0F;
	/**
     * Shear Strength of the solid - completely arbitrary; negative values are not permissible.
     *
     * Higher shear strength means that a solid can cut hardly, like tungsten.
     * Lower shear strength means that a solid can cut easily, like lithium. 
     * 
	 * Use logarithm to count press point, 1atm is 101kPa means this value is about 5.0D.
	 */
	private double shearStrength = Math.log10(10.0D);
	/**
	 * Resistance of object(solid or fluid).
	 * 
     * Higher resistance means that a solid will conduct electricity hardly, like rubber.
     * Lower resistance means that a solid will conduct electricity easily, like silver. 
	 */
	private float resistance = -1.0F;
	/**
	 * Thermal conductivity of object(solid or fluid).
	 * 
     * Higher resistance means that a solid will conduct heat easily, like iron.
     * Lower resistance means that a solid will conduct heat hardly, like rock. 
	 */
	private float thermalConductivity = 1.0F;
	/**
     * Specific Heat of the matter(solid or fluid).
     *
     * Default value is approximately the real-life density of water in kJ/(L*K).
     *
     * Higher viscosity means that a matter will contain more heat in high temperature,
     * like water.
     * Lower viscosity means that a matter will contain less heat in high temperature,
     * like stone. 
	 */
	private double specificHeat = 4.2D;
	/**
	 * Minecraft Implementation
	 * 
	 * Showed max damage of material when crafting it as a tool.
	 */
	private int maxUses = 1;

	public PropertyInfo(int aColor, int aMaxUse, int aPointTem, int aPointPress, float aPressEffect, int aViscosity, float aHardness, float aThoughness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity)
	{
		this(aColor, aMaxUse, -1, -1, aPointTem, aPointPress, aPressEffect, aViscosity, aHardness, aThoughness, aBrittleness, aShearStrength, aResistance, aThermalConductivity);
	}
	public PropertyInfo(int aMaxUse, float aHardness, float aThoughness, float aBrittleness, int aShearStrength)
	{
		this(0xFFFFFF, aMaxUse, aHardness, aThoughness, aBrittleness, aShearStrength, -1F, 0.1F);
	}
	public PropertyInfo(int aColor, int aMaxUse, float aHardness, float aThoughness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity)
	{
		this(aColor, aMaxUse, 0xFFFFFF, 0XFFFFFF, 0XFFFFFF, 0XFFFFFF, 0.0F, 1000, aHardness, aThoughness, aBrittleness, aShearStrength, aResistance, aThermalConductivity);
	}
	public PropertyInfo(int aColor, int aMaxUse, int aMeltingPoint, int aBoilingPoint, int aPointTem, int aPointPress, float aPressEffect, int aViscosity, float aHardness, float aThoughness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity)
	{
        reflectRedLight = (aColor >> 16 & 255) / 255.0F;
        reflectGreenLight = (aColor >> 8 & 255) / 255.0F;
        reflectBlueLight = (aColor & 255) / 255.0F;
        maxUses = aMaxUse;
		meltingPoint = aMeltingPoint;
		boilingPoint = aBoilingPoint;
		triplePointTemperature = aPointTem;
		triplePointPress = Math.log10(aPointPress);
		pressEffect = aPressEffect;
		viscosity = aViscosity;
		hardness = aHardness;
		toughness = aThoughness;
		shearStrength = Math.log10(aShearStrength);
		resistance = aResistance;
		thermalConductivity = aThermalConductivity;
		brittleness = aBrittleness;
	}
	
	public float[] getReflectLights() 
	{
		return new float[]{reflectRedLight, reflectGreenLight, reflectBlueLight};
	}
	
	public int getMaxUses() 
	{
		return maxUses;
	}
	
	public MatterState getState(int aTemperature, int aPress)
	{
		double tPress = Math.log10(aPress);
		
		if(tPress < triplePointPress)
		{
			double d1 = tPress / aTemperature;
			double d2 = triplePointPress / triplePointTemperature;
			return d1 > d2 ? MatterState.Solid : MatterState.Gas;
		}
		else if(aTemperature < triplePointTemperature)
		{
			double d3 = aTemperature - tPress * pressEffect;
			return d3 < triplePointTemperature ? MatterState.Solid : MatterState.Liquid;
		}
		else
		{
			double d4 = Math.pow(tPress, 1.5D) / aTemperature;
			double d5 = triplePointPress / triplePointTemperature;
			return d4 > d5 ? MatterState.Liquid : MatterState.Gas;
		}
	}
	
	public MatterState getState(int aTemperature)
	{
		return boilingPoint < 0 && meltingPoint < 0 ? getState(aTemperature, 10000) : aTemperature < meltingPoint ? MatterState.Solid : aTemperature > boilingPoint ? MatterState.Gas : MatterState.Liquid;
	}
	
	public float getHardness()
	{
		return hardness;
	}
	
	public float getToughness() 
	{
		return toughness;
	}
	
	public float getBrittleness() 
	{
		return brittleness;
	}
	
	public int getViscosity()
	{
		return viscosity;
	}
	
	public boolean canConductElectricity()
	{
		return !(resistance < 0);
	}
	
	public float getResistance()
	{
		return canConductElectricity() ? resistance : 0xFFFF;
	}
	
	public float getThermalConductivity() 
	{
		return thermalConductivity;
	}
}