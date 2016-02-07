package farcore.biology;

import com.google.common.collect.ImmutableList;

/**
 * Biology tag. The general interface of biology, use in plants, animals, germ,
 * etc.
 * 
 * @author ueyudiud
 * @see farcore.biology.SpecieRegistry
 */
public abstract class ISpecies
{
	public String kindom()
	{
		return "entity";
	}
	
	public abstract String phylum();
	
	public abstract String family();
	
	/**
	 * Get species name.
	 * 
	 * @return
	 */
	public abstract String name();
	
	public String fullName()
	{
		return kindom() + "." + phylum() + "." + family() + "." + name();
	}
	
	public abstract ImmutableList<DNAPart> dna();
	
	/**
	 * Create a new biology instance as this species.
	 * 
	 * @return The new instance.
	 */
	public abstract IBiology instance();
}