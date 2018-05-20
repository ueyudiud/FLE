/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.crop;

import java.util.Map;

import farcore.lib.bio.GeneData;
import farcore.lib.bio.IOrder;
import farcore.lib.bio.IntegratedFamily;
import farcore.lib.bio.IntegratedSpecie;

/**
 * @author ueyudiud
 */
public class CropFamily extends IntegratedFamily<Crop> implements ICropFamily<Crop>
{
	public static Builder builder(String name)
	{
		return new Builder(name);
	}
	
	public static class Builder extends IntegratedFamily.Builder<CropFamily, Crop>
	{
		Builder(String name)
		{
			super(CropOrder.ORDER, 5, name);
		}
		
		@Override
		protected CropFamily build(IOrder order, int capcount, int predicate, String name, Map<String, Crop> species, GeneData[] genes, Crop def, IntegratedSpecie[] checkQue)
		{
			return new CropFamily(order, capcount, predicate, name, species, genes, def, checkQue);
		}
	}
	
	public CropFamily(IOrder order, int capcount, int predicate, String name, Map<String, Crop> species, GeneData[] genes, Crop def, IntegratedSpecie[] checkQue)
	{
		super(order, capcount, predicate, name, species, genes, def, checkQue);
	}
}
