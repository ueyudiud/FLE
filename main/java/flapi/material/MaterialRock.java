package flapi.material;

import java.util.EnumMap;

import flapi.chem.base.Matter;
import flapi.collection.Register;
import flapi.enums.EnumFLERock;
import flapi.util.SubTag;

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
	
	public static MaterialRock getRockFromType(EnumFLERock type)
	{
		return map.get(type);
	}
	
	private static EnumMap<EnumFLERock, MaterialRock> map = new EnumMap(EnumFLERock.class);
	
	EnumFLERock rockType;
	
	public MaterialRock setRockType(EnumFLERock rock)
	{
		rockType = rock;
		map.put(rock, this);
		return this;
	}
	
	public EnumFLERock getRockType()
	{
		return rockType;
	}

	public final String getRockName() 
	{
		return rockName;
	}
}