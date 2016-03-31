package farcore.lib.substance;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.lib.collection.Register;

public class SubstanceHandle implements ISubstance
{
	private static final Register<SubstanceHandle> register = new Register();

	public static final SubstanceHandle VOID_TOOL = new SubstanceHandle(0, "void");
	
	public static SubstanceHandle getSubstance(String tag)
	{
		return register.get(tag, VOID_TOOL);
	}
	
	public static SubstanceHandle getSubstance(int id)
	{
		return register.get(id, VOID_TOOL);
	}
	
	public static Register<SubstanceHandle> getHandles()
	{
		return register;
	}
	protected final String name;

	public float usesMul = 1.0F;
	
	/**
	 * The color of tool material, render in items.
	 */
	public int color = 0xFFFFFF;

	public SubstanceHandle(int id, String name)
	{
		this.name = name;
		register.register(id, name, this);
	}
	public SubstanceHandle(String name)
	{
		this.name = name;
		register.register(name, this);
	}
	
	public SubstanceHandle setColor(int color)
	{
		this.color = color;
		return this;
	}
	
	public SubstanceHandle setUseMultipler(float multipler)
	{
		this.usesMul = multipler;
		return this;
	}
	
	//Override method
	public final String getName(){return name;}
	public final int getID(){return register.id(this);}
	public final Register<SubstanceHandle> getRegister(){return register;}

	public void registerLocalName(String name)
	{
		FarCoreSetup.lang.registerLocal("substance.handle." + name, name);
	}
	
	public String getLocalName()
	{
		return FarCore.translateToLocal("substance.handle." + name);
	}
}