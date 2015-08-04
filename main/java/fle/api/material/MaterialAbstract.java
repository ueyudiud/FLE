package fle.api.material;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import fle.api.util.ISubTagContainer;
import fle.api.util.Register;
import fle.api.util.SubTag;

/**
 * 
 * @author ueyudiud
 *
 */
public class MaterialAbstract implements ISubTagContainer
{
	private static final Register<MaterialAbstract> register = new Register();
	
	public static final Register<MaterialAbstract> getMaterialRegistry()
	{
		return register;
	}
	
	private final Collection<SubTag> tags = new HashSet(1);
	protected Matter matter;
	protected PropertyInfo info;

	public MaterialAbstract(PropertyInfo aInfo, SubTag...aTags) 
	{
		if(aTags != null)
			tags.addAll(Arrays.asList(aTags));
		info = aInfo;
	}
	public MaterialAbstract(Matter aMatter, PropertyInfo aInfo, SubTag...aTags) 
	{
		matter = aMatter;
		if(aTags != null)
			tags.addAll(Arrays.asList(aTags));
		info = aInfo;
	}
	public MaterialAbstract(String aName, PropertyInfo aInfo, SubTag...aTags) 
	{
		this(aInfo, aTags);
		register.register(this, aName);
	}
	public MaterialAbstract(String aName, Matter aMatter, PropertyInfo aInfo, SubTag...aTags) 
	{
		this(aMatter, aInfo, aTags);
		register.register(this, aName);
	}
	
	public boolean canWriteChemicalFormulaName()
	{
		return matter != null;
	}
	
	public String getChemcalReaction()
	{
		return matter.getChemicalFormulaName();
	}
	
	public PropertyInfo getPropertyInfo() 
	{
		return info;
	}
	
	@Override
	public boolean contain(SubTag aTag) 
	{
		return tags.contains(aTag);
	}

	@Override
	public void add(SubTag...aTags) 
	{
		if(aTags != null)
			for(SubTag tTag : aTags)
				if(tTag != null && (!tags.contains(tTag)))
					tags.add(tTag);
	}

	@Override
	public void remove(SubTag aTag) 
	{
		tags.contains(aTag);
	}
}