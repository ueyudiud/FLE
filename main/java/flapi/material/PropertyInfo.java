package flapi.material;

import flapi.enums.EnumMatterState;

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
	private int colors[] = {0xFFFFFF, 0xFFFFFF};
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
	 * The harvest level of material. Use in ore or block.
	 * The effect to made block more hardly to dig.<br>
	 * 
	 * Higher level means you need get higher level tool to dig this block.
	 * @see net.minecraft.item.ToolMaterial
	 */
	private int harvestLevel = -1;
	/**
     * Hardness of the solid - the effect to carve in another item.<br>
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
     * Denseness of the solid, this value is not show quality of item!
     *
     * Higher denseness means that a solid is compact, like iron.
     * Lower denseness means that a solid is loose, like rust. 
	 */
	private float denseness = 1.0F;
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
	 * Use logarithm to count press point, 1atm is 101kPa means this value is about 2.0D.
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

	public PropertyInfo(int aColor, int aHarvestLevel, int aMaxUse, int aPointTem, int aPointPress, float aPressEffect, int aViscosity, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity)
	{
		this(aColor, aHarvestLevel, aMaxUse, -1, -1, aPointTem, aPointPress, aPressEffect, aViscosity, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, aResistance, aThermalConductivity, 1.0F);
	}
	public PropertyInfo(int[] aColor, int aHarvestLevel, int aMaxUse, int aPointTem, int aPointPress, float aPressEffect, int aViscosity, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity)
	{
		this(aColor, aHarvestLevel, aMaxUse, -1, -1, aPointTem, aPointPress, aPressEffect, aViscosity, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, aResistance, aThermalConductivity, 1.0F);
	}
	public PropertyInfo(int aHarvestLevel, int aMaxUse, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength)
	{
		this(0xFFFFFF, aHarvestLevel, aMaxUse, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength);
	}
	public PropertyInfo(int aColor, int aHarvestLevel, int aMaxUse, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength)
	{
		this(aColor, aHarvestLevel, aMaxUse, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, -1F, 0.1F, 1.0F);
	}
	public PropertyInfo(int[] aColor, int aHarvestLevel, int aMaxUse, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength)
	{
		this(aColor, aHarvestLevel, aMaxUse, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, -1F, 0.1F, 1.0F);
	}
	public PropertyInfo(int aColor, int aHarvestLevel, int aMaxUse, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity, float aSpecificHeat)
	{
		this(aColor, aHarvestLevel, aMaxUse, 0xFFFFFF, 0XFFFFFF, 0XFFFFFF, 0XFFFFFF, 0.0F, 1000, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, aResistance, aThermalConductivity, aSpecificHeat);
	}
	public PropertyInfo(int[] aColor, int aHarvestLevel, int aMaxUse, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity, float aSpecificHeat)
	{
		this(aColor, aHarvestLevel, aMaxUse, 0xFFFFFF, 0XFFFFFF, 0XFFFFFF, 0XFFFFFF, 0.0F, 1000, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, aResistance, aThermalConductivity, aSpecificHeat);
	}
	public PropertyInfo(int aColor, int aMeltingPoint, int aBoilingPoint, int aPointTem, int aPointPress, int aViscosity, float aDenseness, float aResistance, float aThermalConductivity, float aSpecificHeat)
	{
		this(aColor, aMeltingPoint, aBoilingPoint, aPointTem, aPointPress, aViscosity, 0.0F, 0.1F, aDenseness, 0.0F, 100000, aResistance, aThermalConductivity, aSpecificHeat);
	}
	public PropertyInfo(int aColor, int aMeltingPoint, int aBoilingPoint, int aPointTem, int aPointPress, int aViscosity, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity, float aSpecificHeat)
	{
		this(new int[]{aColor, aColor}, -1, 0, aMeltingPoint, aBoilingPoint, aPointTem, aPointPress, 0.0F, aViscosity, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, aResistance, aThermalConductivity, aSpecificHeat);
	}
	public PropertyInfo(int aColor, int aHarvestLevel, int aMaxUse, int aMeltingPoint, int aBoilingPoint, int aPointTem, int aPointPress, float aPressEffect, int aViscosity, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity, float aSpecificHeat)
	{
		this(new int[]{aColor, aColor}, aHarvestLevel, aMaxUse, aMeltingPoint, aBoilingPoint, aPointTem, aPointPress, aPressEffect, aViscosity, aHardness, aThoughness, aDenseness, aBrittleness, aShearStrength, aResistance, aThermalConductivity, aSpecificHeat);
	}
	public PropertyInfo(int[] aColor, int aHarvestLevel, int aMaxUse, int aMeltingPoint, int aBoilingPoint, int aPointTem, int aPointPress, float aPressEffect, int aViscosity, float aHardness, float aThoughness, float aDenseness, float aBrittleness, int aShearStrength, float aResistance, float aThermalConductivity, float aSpecificHeat)
	{
		colors = aColor;
        reflectRedLight = (aColor[0] >> 16 & 255) / 255.0F;
        reflectGreenLight = (aColor[0] >> 8 & 255) / 255.0F;
        reflectBlueLight = (aColor[0] & 255) / 255.0F;
        harvestLevel = aHarvestLevel;
        maxUses = aMaxUse;
		meltingPoint = aMeltingPoint;
		boilingPoint = aBoilingPoint;
		triplePointTemperature = aPointTem;
		triplePointPress = Math.log10(aPointPress);
		pressEffect = aPressEffect;
		viscosity = aViscosity;
		hardness = aHardness;
		toughness = aThoughness;
		denseness = aDenseness;
		shearStrength = Math.log10(aShearStrength);
		resistance = aResistance;
		thermalConductivity = aThermalConductivity;
		specificHeat = aSpecificHeat;
		brittleness = aBrittleness;
	}
		
	public int[] getColors() 
	{
		return colors.clone();
	}
	
	public float[] getReflectLights() 
	{
		return new float[]{reflectRedLight, reflectGreenLight, reflectBlueLight};
	}
	
	public int getHarvestLevel()
	{
		return harvestLevel;
	}
	
	public int getMaxUses() 
	{
		return maxUses;
	}
	
	public EnumMatterState getState(int aTemperature, int aPress)
	{
		double tPress = Math.log10(aPress);
		
		if(tPress < triplePointPress)
		{
			double d1 = tPress / aTemperature;
			double d2 = triplePointPress / triplePointTemperature;
			return d1 > d2 ? EnumMatterState.Solid : EnumMatterState.Gas;
		}
		else if(aTemperature < triplePointTemperature)
		{
			double d3 = aTemperature - tPress * pressEffect;
			return d3 < triplePointTemperature ? EnumMatterState.Solid : EnumMatterState.Liquid;
		}
		else
		{
			double d4 = Math.pow(tPress, 1.5D) / aTemperature;
			double d5 = triplePointPress / triplePointTemperature;
			return d4 > d5 ? EnumMatterState.Liquid : EnumMatterState.Gas;
		}
	}
	
	public EnumMatterState getState(int aTemperature)
	{
		return boilingPoint < 0 && meltingPoint < 0 ? getState(aTemperature, 10000) : aTemperature < meltingPoint ? EnumMatterState.Solid : aTemperature > boilingPoint ? EnumMatterState.Gas : EnumMatterState.Liquid;
	}
	
	public float getHardness()
	{
		return hardness;
	}
	
	public float getToughness() 
	{
		return toughness;
	}
	
	public float getDenseness()
	{
		return denseness;
	}
	
	public float getBrittleness() 
	{
		return brittleness;
	}
	
	public double getShearStrength()
	{
		return shearStrength;
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
	
	public double getSpecificHeat()
	{
		return specificHeat;
	}
	
	public long getMeltingPoint()
	{
		return meltingPoint;
	}
	
	public long getBoilingPoint()
	{
		return boilingPoint;
	}
}