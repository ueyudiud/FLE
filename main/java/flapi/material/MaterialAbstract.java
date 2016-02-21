package flapi.material;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import farcore.collection.Register;
import farcore.substance.Matter;
import flapi.FleAPI;
import flapi.util.ISubTagContainer;
import flapi.util.SubTag;

/**
 * 
 * @author ueyudiud
 *
 */
public class MaterialAbstract implements ISubTagContainer
{
	public static File property;
	private static final Register<MaterialAbstract> register = new Register();
	public static final Register<MaterialAbstract> pureMaterials = new Register();
	
	public static final Register<MaterialAbstract> getMaterialRegistry()
	{
		return register;
	}
	
	private final Collection<SubTag> tags = new HashSet(1);
	protected Matter matter;
	protected PropertyInfo info;
	private int index;

	public MaterialAbstract(PropertyInfo aInfo, SubTag...aTags) 
	{
		if(aTags != null)
			tags.addAll(Arrays.asList(aTags));
		info = aInfo;
		index = -1;
	}
	public MaterialAbstract(Matter aMatter, PropertyInfo aInfo, SubTag...aTags) 
	{
		this(aInfo, aTags);
		matter = aMatter;
	}
	public MaterialAbstract(String aName, PropertyInfo aInfo, SubTag...aTags) 
	{
		this(aInfo, aTags);
		index = register.register(this, aName);
	}
	public MaterialAbstract(String aName, Matter aMatter, PropertyInfo aInfo, SubTag...aTags) 
	{
		this(aMatter, aInfo, aTags);
		index = register.register(this, aName);
	}
	
	/**
	 * Get material id, return -1 means not registered to list.
	 * @return
	 */
	public int getIndex()
	{
		return index;
	}
	
	public MaterialAbstract setDisplayName(String name)
	{
		FleAPI.langManager.registerLocal("material." + getName().toLowerCase().replace('_', '.'), name);
		return this;
	}
	
	public String getDisplayName()
	{
		return FleAPI.langManager.translateToLocal("material." + getName().toLowerCase().replace('_', '.'), new Object[0]);
	}
	
	public String getName()
	{
		return register.name(this);
	}
	
	public boolean canWriteChemicalFormulaName()
	{
		return matter != null;
	}
	
	public String getChemcalReaction()
	{
		return matter.getChemName();
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
	
	public Matter getMatter()
	{
		return matter;
	}
}