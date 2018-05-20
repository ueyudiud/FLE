/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.bio;

/**
 * @author ueyudiud
 */
public interface IBio
{
	ISpecie getSpecie();
	
	default IFamily<?> getFamily()
	{
		return getSpecie().getFamily();
	}
	
	default IOrder<?, ?> getOrder()
	{
		return getFamily().getOrder();
	}
	
	BioData getData();
}
