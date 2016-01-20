package farcore.biology;

import com.google.common.collect.ImmutableList;

/**
 * Biology tag. The general interface of biology, use in plants, animals, germ,
 * etc.
 * 
 * @author ueyudiud
 * @see farcore.biology.SpecieRegistry
 */
public interface ISpecies
{
	default String kindom()
	{
		return "entity";
	}
	
	String phylum();
	
	String family();
	
	/**
	 * Get species name.
	 * 
	 * @return
	 */
	String name();
	
	default String fullName()
	{
		return kindom() + "." + phylum() + "." + family() + "." + name();
	}
	
	ImmutableList<DNAPart> dna();
	
	/**
	 * Create a new biology instance as this species.
	 * 
	 * @return The new instance.
	 */
	IBiology instance();
}