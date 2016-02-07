package farcore.substance;

import farcore.collection.ArrayStandardStackList;

/**
 * The property to particle in chemistry and physics.
 * @author ueyudiud
 * @see farcore.substance.Atom
 */
public interface IParticle
{
	long getSize();
	
	/**
	 * 
	 * @param particle
	 * @param divide To divide ion to pieces
	 * @return
	 */
	long getParticleCount(IParticle particle, boolean divide);
	
	ArrayStandardStackList<Atom> getAtomContain();
}