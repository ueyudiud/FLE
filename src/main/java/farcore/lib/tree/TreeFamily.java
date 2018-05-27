/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import java.util.Map;

import farcore.lib.bio.GeneData;
import farcore.lib.bio.IOrder;
import farcore.lib.bio.IntegratedFamily;
import farcore.lib.bio.IntegratedSpecie;

/**
 * @author ueyudiud
 */
public class TreeFamily extends IntegratedFamily<Tree>
{
	public static Builder builder(String name)
	{
		return new Builder(name);
	}
	
	public static class Builder extends IntegratedFamily.Builder<TreeFamily, Tree>
	{
		Builder(String name)
		{
			super(TreeOrder.ORDER, 4, name);
		}
		
		@Override
		protected TreeFamily build(IOrder order, int capcount, int predicate, String name, Map<String, Tree> species, GeneData[] genes, Tree def, IntegratedSpecie[] checkQue)
		{
			return new TreeFamily(order, capcount, predicate, name, species, genes, def, checkQue);
		}
	}
	
	public TreeFamily(IOrder order, int capcount, int predicate, String name, Map<String, Tree> species, GeneData[] genes, Tree def, IntegratedSpecie[] checkQue)
	{
		super(order, capcount, predicate, name, species, genes, def, checkQue);
	}
}
