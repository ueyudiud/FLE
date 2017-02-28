/*
 * copyright© 2016-2017 ueyudiud
 */

package farcore.lib.tree;

import java.util.Collection;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IFamily;

/**
 * @author ueyudiud
 */
public class TreeFamily implements IFamily<ISaplingAccess>
{
	public final String name;
	
	public TreeFamily(String name)
	{
		this.name = name;
	}
	
	@Override
	public final String getRegisteredName()
	{
		return this.name;
	}
	
	@Override
	public Collection<? extends ITree> getSpecies()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ITree getSpecieFromGM(GeneticMaterial gm)
	{
		// TODO Auto-generated method stub
		return null;
	}
}