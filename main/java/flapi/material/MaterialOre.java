package flapi.material;

import farcore.collection.Register;
import farcore.substance.Matter;
import flapi.util.SubTag;

public class MaterialOre extends MaterialAbstract
{
	private static final Register<MaterialOre> register = new Register();

	public static MaterialOre getOreFromID(int index)
	{
		return register.get(index);
	}
	public static int getOreID(MaterialOre ore)
	{
		return register.serial(ore);
	}
	public static MaterialOre getOreFromName(String oreName)
	{
		return register.get(oreName);
	}
	public static Register<MaterialOre> getOres()
	{
		return register;
	}
	
	private final String oreName;
	
	public MaterialOre(String aName, PropertyInfo aInfo, SubTag...aTags)
	{
		super(aName, aInfo, aTags);
		oreName = aName;
		register.register(this, oreName);
	}
	public MaterialOre(String aName, Matter aMatter, PropertyInfo aInfo, SubTag...aTags)
	{
		super(aName, aMatter, aInfo, aTags);
		oreName = aName;
		register.register(this, oreName);
	}
	
	public String getOreName() 
	{
		return oreName;
	}
}