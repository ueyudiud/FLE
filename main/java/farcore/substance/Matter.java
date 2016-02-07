package farcore.substance;

import java.util.Locale;

import farcore.collection.Register;
import farcore.collection.abs.IRegister;

public class Matter
{
	private static final Register<Matter> matterRegister = new Register<Matter>();

	private static String $(String name)
	{
		return name.toLowerCase(Locale.ENGLISH).replace('_', ' ').trim();
	}
	
	/**
	 * Get a matter, if matter is already registered, return
	 * registered matter.
	 * @param name The matter name.
	 * @param chemName The chemical name of matter.
	 * @return
	 */
	public static Matter matter(String name, String chemName)
	{
		if(name == null || name.length() == 0) return null;
		if(matterRegister.contain($(name)))
			return matterRegister.get($(name));
		return new Matter($(name), chemName);
	}
	public static Matter matter(String name)
	{
		if(name == null || name.length() == 0) return null;
		if(matterRegister.contain($(name)))
			return matterRegister.get($(name));
		return new Matter($(name));
	}
	public static IRegister<Matter> getMatterList()
	{
		return matterRegister;
	}
	
	private final String name;
	private String chemName;
	/**
	 * In far land era, items doesn't have mass, so use the molar
	 * count instead of mass.<br>
	 * The each one size of stack current one divide molar mass of 
	 * mass.
	 */
	private long molarMass;
	private AtomRadical radical;
	
	protected Matter(String name)
	{
		this.name = name;
		molarMass = 1;
	}
	protected Matter(String name, String chemName)
	{
		this.name = name;
		this.chemName = chemName;
		radical = AtomRadical.radical(chemName);
		molarMass = radical.getSize();
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getChemName()
	{
		return chemName;
	}
	
	public long getMolarMass()
	{
		return molarMass;
	}
	
	public AtomRadical get()
	{
		return radical;
	}
}