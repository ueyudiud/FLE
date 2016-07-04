package farcore.lib.substance;

import farcore.FarCore;
import farcore.FarCoreSetup;
import farcore.alpha.interfaces.IRegisteredNameable;
import farcore.lib.collection.Register;

/**
 * Use for abstract substance.
 * @author ueyudiud
 *
 */
@Deprecated
public interface ISubstance extends IRegisteredNameable
{
	String getType();
	
	default String getRegisteredName() 
	{
		return getName();
	}
	
	String getName();
	
	int getID();
	
	Register<? extends ISubstance> getRegister();
	
	default void registerLocalName(String name)
	{
		if(FarCoreSetup.lang != null)
		{
			FarCoreSetup.lang.registerLocal("substance." + getType() + "." + getName(), name);
		}
	}	
	
	default String getLocalName()
	{
		return FarCore.translateToLocal("substance." + getType() + "." + getName());
	}
}