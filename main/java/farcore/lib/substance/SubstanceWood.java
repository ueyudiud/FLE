package farcore.lib.substance;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.lib.collection.Register;

public class SubstanceWood implements ISubstance
{
	private static final Register<SubstanceWood> register = new Register();

	public static final SubstanceWood WOOD_VOID = new SubstanceWood(0, "void");
	
	public static SubstanceWood getSubstance(String tag)
	{
		return register.get(tag, WOOD_VOID);
	}
	
	public static SubstanceWood getSubstance(int id)
	{
		return register.get(id, WOOD_VOID);
	}
	
	public static Register<SubstanceWood> getWoods()
	{
		return register;
	}
	
	protected final String name;
	/**
	 * Unit : kN
	 */
	public float hardness = 1.0F;
	/**
	 * Unit : kg/m^3
	 */
	public int density = 100;
	/**
	 * Unit : %
	 */
	public float ashcontent = 25.0F;
	/**
	 * Unit : %
	 */
	public float watercontent = 10.0F;
	/**
	 * Unit : J / mol
	 */
	public int burnEnergyPerUnit = 1000;
	
	public boolean canMakeSoftTool = true;
	public boolean canMakeHardTool = false;

	public int maxSoftUses = 100;
	public int maxHardUses = -1;
	
	public SubstanceWood(int id, String name)
	{
		this.name = name;
		register.register(id, name, this);
	}
	public SubstanceWood(String name)
	{
		this.name = name;
		register.register(name, this);
	}
	
	public SubstanceWood setHardness(float hardness)
	{
		this.hardness = hardness;
		return this;
	}
	
	public SubstanceWood setDensity(int density)
	{
		this.density = density;
		return this;
	}
	
	public SubstanceWood setAshcontent(float ashcontent)
	{
		this.ashcontent = ashcontent;
		return this;
	}
	
	public SubstanceWood setWatercontent(float watercontent)
	{
		this.watercontent = watercontent;
		return this;
	}
	
	public SubstanceWood setMaxUses(int maxUses)
	{
		this.maxSoftUses = maxUses;
		return this;
	}
	
	public SubstanceWood setMaxUses(int maxSoftUses, int maxHardUses)
	{
		this.maxSoftUses = maxSoftUses;
		this.maxHardUses = maxHardUses;
		this.canMakeHardTool = true;
		return this;
	}
	
	public SubstanceWood setBurnEnergy(int energy)
	{
		this.burnEnergyPerUnit = energy;
		return this;
	}
	
	//Override method
	public final String getName(){return name;}
	public final int getID(){return register.id(this);}
	public final Register<SubstanceWood> getRegister(){return register;}

	public void registerLocalName(String name)
	{
		FarCoreSetup.lang.registerLocal("substance.wood." + name, name);
	}
	
	public String getLocalName()
	{
		return FarCore.translateToLocal("substance.wood." + name);
	}
}