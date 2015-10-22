package fle.api.material;

import net.minecraft.util.IIcon;
import fle.api.util.Register;
import fle.api.util.SubTag;

public class MaterialRock extends MaterialAbstract
{
	private static Register<MaterialRock> register = new Register();
	private static Register<MaterialRock> register1 = new Register();
	private final String rockName;
	
	public MaterialRock(String aName, PropertyInfo aInfo, SubTag...aTags) 
	{
		super(aName, aInfo, aTags);
		rockName = aName;
		if(contain(SubTag.ROCK_base_rock))
			register1.register(this, aName);
		else
			register.register(this, aName);
	}
	
	public MaterialRock(String aName, Matter aMatter, PropertyInfo aInfo,
			SubTag...aTags) 
	{
		super(aName, aMatter, aInfo, aTags);
		rockName = aName;
		if(contain(SubTag.ROCK_base_rock))
			register1.register(this, aName);
		else
			register.register(this, aName);
	}

	public static Register<MaterialRock> getBaseRocks() 
	{
		return register1;
	}

	public static Register<MaterialRock> getRocks() 
	{
		return register;
	}

	public static int getOreID(MaterialRock aMaterial) 
	{
		return register.serial(aMaterial);
	}

	public static MaterialRock getOreFromID(int id) 
	{
		return register.get(id);
	}

	public final String getRockName() 
	{
		return rockName;
	}
}