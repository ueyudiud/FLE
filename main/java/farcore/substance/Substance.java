package farcore.substance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import farcore.collection.Register;
import farcore.substance.PhaseDiagram.Phase;
import farcore.util.U;
import flapi.FleAPI;
import flapi.util.ISubTagContainer;
import flapi.util.SubTag;
import fle.core.util.Util;

/**
 * Far Land Era Implementation
 * 
 * This class similar to class PropertyInfo in version 2.06 before. This class 
 * contain a property (for material) equivalent to "ToolMaterial." It describes 
 * the nature of the property of a matter.
 * 
 * These properties do not have inherent gameplay mechanics - they are provided
 * so that mods may choose to take advantage of them.
 * 
 * The default values can be used as a reference point for mods adding matter
 * such as copper or oil.
 * 
 * @author ueyudiud
 * 		
 */
public class Substance implements ISubTagContainer
{
	private static final Register<Substance> register = new Register<Substance>();
	
	public static Register<Substance> getRegister()
	{
		return register;
	}
	
	private final String name;
	public Matter matter;
	/**
	 * The ore output per ore.
	 */
	public long oreMultiplier = 1;
	/**
	 * The furnace burning time.
	 * 
	 * Unit : tick.
	 */
	public long burnTime = 0L;
	// Tool properties
	public int toolMineLevel = 0;
	public long toolDurability = 0L;
	public float toolSpeed = 1.0F;
	/**
	 * Brittleness of the solid - completely arbitrary; negative values are not
	 * permissible.<br>
	 *
	 * Higher toughness means that a solid can break easily, like rock. Lower
	 * toughness means that a solid can break hardly, like metal.
	 */
	public float toolBrittleness = 0.0F;
	/**
	 * Viscosity ("thickness") of the fluid - completely arbitrary; negative
	 * values are not permissible.
	 *
	 * Default value is approximately the real-life density of water in m/s^2
	 * (x10^-3).
	 *
	 * Higher viscosity means that a fluid flows more slowly, like molasses.
	 * Lower viscosity means that a fluid flows more quickly, like helium.
	 * {@link net.minecraftforge.fluids.Fluid}
	 */
	public int viscosity = 1000;
	/**
	 * Toughness of the solid; negative values are not permissible.<br>
	 *
	 * Higher toughness means that material can be extended to a long wire, like
	 * gold. Lower shear strength means that material is hard to extended, like
	 * pig iron.
	 * 
	 * The default value is 1000Pa/(m·mol) (Unit : Pa/(m·mol)).
	 */
	public long toughness = 1000;
	/**
	 * Shear Strength of the solid; negative values are not permissible.<br>
	 *
	 * Higher shear strength means that material can cut hardly, like tungsten.
	 * Lower shear strength means that material can cut easily, like lithium.
	 * 
	 * The default value is 1000kPa (Unit : kPa).
	 */
	public long shearStrength = 1000;
	/**
	 * Young Strength is a number that measures an object or substance's resistance 
	 * to being deformed elastically when a force is applied to it.<br>
	 *
	 * Higher shear strength means that material can cut hardly, like tungsten.
	 * Lower shear strength means that material can cut easily, like lithium.
	 * 
	 * The default value is 1000kPa (Unit : kPa).
	 */
	public long youngStrength = 1000;
	/**
	 * Resistance of object(solid or fluid).<br>
	 * 
	 * Higher resistance means that a solid will conduct electricity hardly,
	 * like rubber. Lower resistance means that a solid will conduct electricity
	 * easily, like silver.
	 * 
	 * Positive value means can not conduct electricity.
	 * 
	 * The default value is infinite (Unit : nΩ·m).
	 */
	public float resistance = -1.0F;
	/**
	 * Reactance of object(solid or fluid).<br>
	 * 
	 * Different from resistance. The resistance changed when current changed.
	 * Positive value means can not conduct electricity.
	 */
	public float reactance = -1.0F;
	/**
	 * Thermal conductivity of object(solid or fluid).<br>
	 * 
	 * Unit : W / (m·K)<br>
	 * 
	 * Higher resistance means that a solid will conduct heat easily, like iron.
	 * Lower resistance means that a solid will conduct heat hardly, like rock.
	 */
	public float thermalConductivity = 1.0F;
	/**
	 * Specific Heat of the matter(solid or fluid).<br>
	 *
	 * The default value is 7535 (Unit : cJ / mol)<br>
	 *
	 * Higher viscosity means that a matter will contain more heat in high
	 * temperature, like water. 
	 * Lower viscosity means that a matter will contain less heat in high 
	 * temperature, like stone.
	 */
	public long specificHeat = 7535;
	
	/**
	 * PH level of substance.<br>
	 * Higher PH has more value means it has higher alkali level, less  value
	 * means it has higher acid level.<br>
	 * Use default level (water) is 700.
	 */
	public int ph = 700;
	
	public float heatDamage = 0.0F;
	
	/**
	 * The point when substance has change into super conductivity.
	 */
	public long superconductivityPoint = -1;
	/**
	 * Use Kelvin degree.
	 */
	private PhaseDiagram<IPhaseCondition, Phase> phasePhaseDiagram;
	/**
	 * Use for X-ray machine.
	 */
	public short xRayReflectLevel = -1;
	/**
	 * 
	 */
	public short[] luminosity = {
			// Light level
			0, 0, 100, 255, 0, 0, 0, 0};
	public short[][] color = {
			// R G B a
			{255, 255, 255, 255}, 
			{255, 255, 255, 180}, 
			{255, 255, 255, 70},
			{255, 255, 255, 5}, 
			{255, 255, 255, 20}, 
			{255, 255, 255, 255},
			{255, 255, 255, 190}};
	private IPhaseSubstanceCondition substancePhase;
	private final List<SubTag> tagList = new ArrayList();
	
	/**
	 * The mass per kL.(Because of gas density is too small).
	 * 
	 * Unit : g / kL
	 */
	public long gasDensity = 1000;
	public long liquidDensity = 1000000;
	public long solidDensity = 1000000;
	
	/**
	 * The hardness of block. Higher value means more harder to mine.
	 */
	public long hardness = 250;
	/**
	 * Different from toolMine level. This level check as block break level.
	 */
	public int blockMineLevel = 0;
	
	public Substance(String name)
	{
		if (register.contain(name))
			throw new RuntimeException(
					"The substance " + name + " had already registied.");
		register.register(this, name);
		this.name = name;
	}
	
	public Substance setTranslate(String localized)
	{
		FleAPI.langManager.registerLocal(getTranslateName(), localized);
		return this;
	}
	
	public Substance setTranslate(String locale, String localized)
	{
		FleAPI.langManager.registerLocal(locale, getTranslateName(), localized);
		return this;
	}
	
	public String getTranslateName()
	{
		return "substance." + name.trim().replaceAll(" ", ".") + ".name";
	}
	
	public String getName()
	{
		return name;
	}
	
	public Substance setPhasePhaseDiagram(
			PhaseDiagram<IPhaseCondition, Phase> p)
	{
		this.phasePhaseDiagram = p;
		return this;
	}
	
	public Substance setSuperconductivityPoint(long point)
	{
		this.superconductivityPoint = point;
		return this;
	}
	
	public Substance setFurnaceBurnTime(long time)
	{
		this.burnTime = time;
		return this;
	}
	
	public Substance setPH(int ph)
	{
		this.ph = ph;
		return this;
	}
	
	/**
	 * 
	 * @param mineLevel
	 * @param speed
	 *            The dig effective, example iron tools is 6.0F.
	 * @param durability
	 *            The durability base on tool, 100 units per 1 minecraft damage.
	 * @param brittleness
	 * @return
	 */
	public Substance setToolInfo(int mineLevel, float speed, long durability,
			float brittleness)
	{
		this.toolMineLevel = mineLevel;
		this.toolSpeed = speed;
		this.toolDurability = durability;
		this.toolBrittleness = brittleness;
		return this;
	}
	
	public Substance setMatter(Matter matter)
	{
		this.matter = matter;
		return this;
	}
	
	/**
	 * Set matter by chemistry name and this substance name.
	 * 
	 * @param chemName
	 * @return
	 */
	public Substance setMatter(String chemName)
	{
		return setMatter(Matter.matter(name, chemName));
	}
	
	public Substance setViscosity(int viscosity)
	{
		this.viscosity = viscosity;
		return this;
	}
	
	public Substance setDensity(long density)
	{
		this.solidDensity = this.liquidDensity = this.gasDensity = density;
		return this;
	}
	
//	public Substance setMolarMass(double mass)
//	{
//		this.gasDensity = (long) (Vs.standard_condition_gas * mass);
//		return this;
//	}
	
	public Substance setDensity(long gasDensity, long slDensity)
	{
		this.gasDensity = gasDensity;
		this.liquidDensity = this.solidDensity = slDensity;
		return this;
	}
	
	public Substance setDensity(long gasDensity, long liquidDensity, long solidDensity)
	{
		this.gasDensity = gasDensity;
		this.liquidDensity = liquidDensity;
		this.solidDensity = solidDensity;
		return this;
	}
	
	/**
	 * Set block information.
	 * 
	 * @param mineLevel
	 *            The minimum mine level of block.
	 * @param hardness
	 *            The hardness of block, per 250 unit for 1.0 float value in
	 *            block.
	 * @return
	 */
	public Substance setBlockInfo(int mineLevel, long hardness)
	{
		this.blockMineLevel = mineLevel;
		this.hardness = hardness;
		return this;
	}
	
	public Substance setCrafting(long shearStrengh, long youngStrength)
	{
		this.shearStrength = shearStrengh;
		this.youngStrength = youngStrength;
		return this;
	}
	
	public Substance setSubstance(Phase phase, Substance substance)
	{
		if(substancePhase == null)
		{
			substancePhase = new PhaseSubstanceCondition(this);
		}
		if(substancePhase instanceof PhaseSubstanceCondition)
		{
			((PhaseSubstanceCondition) substancePhase).map.put(phase, substance);
		}
		else throw new IllegalArgumentException("This substance has already has a substance phase.");
		return this;
	}
	
	public Substance setSubstance(IPhaseSubstanceCondition condition)
	{
		this.substancePhase = condition;
		return this;
	}
	
	/**
	 * Use 32-bits by color.
	 * 
	 * @param phase
	 *            The base substance.
	 * @param color
	 *            By 0xRRGGBBaa.
	 * @return
	 */
	public Substance setColor(Phase phase, int color)
	{
		return setColor(phase,
				new short[]{(short) ((color & 0xFF000000) >> 24), // R
						(short) ((color & 0x00FF0000) >> 16), // G
						(short) ((color & 0x0000FF00) >> 8), // B
						(short) ((color & 0x000000FF))// A
		});
	}
	
	public Substance setColor(Phase phase, short[] color)
	{
		this.color[phase.ordinal()] = color;
		return this;
	}
	
	public Substance setXReflectLevel(short lv)
	{
		this.xRayReflectLevel = lv;
		return this;
	}
	
	public Substance setThermalCfg(long sh, float tc)
	{
		this.specificHeat = sh;
		this.thermalConductivity = tc;
		return this;
	}
	
	public Substance setElectrologyCfg(float r1, float r2)
	{
		this.resistance = r1;
		this.reactance = r2;
		return this;
	}
	
	public Substance getSubstance(IPhaseCondition condition)
	{
		return substancePhase == null ? this : substancePhase.getPhase(condition);
	}
	
	public Phase getPhase(IPhaseCondition condition)
	{
		return phasePhaseDiagram == null ? Phase.SOLID : phasePhaseDiagram.getPhase(condition);
	}
	
	public float getBlockHardness()
	{
		return (float) hardness / 250F;
	}
	
	@Override
	public boolean contain(SubTag tag)
	{
		return tagList.contains(tag);
	}
	
	public Substance addTag(SubTag... tags)
	{
		add(tags);
		return this;
	}
	
	@Override
	public void add(SubTag... tag)
	{
		tagList.addAll(Arrays.asList(tag));
	}
	
	@Override
	public void remove(SubTag tag)
	{
		tagList.remove(tag);
	}
	
	public short[] getColor(Phase phase)
	{
		return color[phase.ordinal()];
	}
	
	public int getRGB(Phase phase)
	{
		short[] ss = color[phase.ordinal()];
		return ss[0] << 24 | ss[1] << 16 | ss[2] << 8 | ss[3];
	}
	
	public int getRGBa(Phase phase)
	{
		short[] ss = color[phase.ordinal()];
		return ss[0] << 16 | ss[1] << 8 | ss[2];
	}
	
	public int getLuminosity(Phase phase)
	{
		return luminosity[phase.ordinal()];
	}
}