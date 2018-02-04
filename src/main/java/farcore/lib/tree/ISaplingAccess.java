/*
 * copyright© 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IBiology;
import nebula.common.world.IModifiableCoord;
import net.minecraft.world.biome.Biome;

/**
 * The sapling access.
 * 
 * @author ueyudiud
 */
public interface ISaplingAccess extends IModifiableCoord, IBiology
{
	@Override
	default GeneticMaterial getGeneticMaterial()
	{
		return info().gm;
	}
	
	@Override
	default Tree getSpecie()
	{
		return tree();
	}
	
	Tree tree();
	
	TreeInfo info();
	
	@Deprecated
	Biome biome();
	
	void killTree();
}
