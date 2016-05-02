package farcore.lib.substance;

import farcore.lib.collection.Register;

public class SubstanceSand extends SubstanceBlockAbstract
{
	private static final Register<SubstanceSand> register = new Register();

	public static final SubstanceSand VOID_SAND = (SubstanceSand) new SubstanceSand(0, "void").setHardness(1.0F).setDensity(1000);
	
	public static SubstanceSand getSubstance(String tag)
	{
		return register.get(tag, VOID_SAND);
	}
	
	public static SubstanceSand getSubstance(int id)
	{
		return register.get(id, VOID_SAND);
	}
	
	public static Register<SubstanceSand> getSands()
	{
		return register;
	}
	
	public SubstanceSand(String name)
	{
		super(name);
	}

	public SubstanceSand(int id, String name)
	{
		super(id, name);
	}

	public SubstanceSand(int id, String name, String local)
	{
		this(id, name);
		registerLocalName(local);
	}

	public final String getType(){return "sand";}
	public Register getRegister(){return register;}
}