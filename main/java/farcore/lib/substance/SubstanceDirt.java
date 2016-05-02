package farcore.lib.substance;

import farcore.lib.collection.Register;

public class SubstanceDirt extends SubstanceBlockAbstract
{
	private static final Register<SubstanceDirt> register = new Register();

	public static final SubstanceDirt VOID_DIRT = (SubstanceDirt) new SubstanceDirt(0, "void").setHardness(1.0F).setDensity(1000);
	
	public static SubstanceDirt getSubstance(String tag)
	{
		return register.get(tag, VOID_DIRT);
	}
	
	public static SubstanceDirt getSubstance(int id)
	{
		return register.get(id, VOID_DIRT);
	}
	
	public static Register<SubstanceDirt> getDirts()
	{
		return register;
	}
	
	public SubstanceDirt(String name)
	{
		super(name);
	}

	public SubstanceDirt(int id, String name)
	{
		super(id, name);
	}

	public final String getType(){return "dirt";}
	public Register getRegister(){return register;}
}