package farcore.lib.substance;

import farcore.lib.collection.Register;

/**
 * Use for abstract substance.
 * @author ueyudiud
 *
 */
public interface ISubstance
{
	String getName();
	
	int getID();
	
	Register<? extends ISubstance> getRegister();
}