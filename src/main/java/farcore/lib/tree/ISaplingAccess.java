/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import farcore.lib.bio.BioData;
import farcore.lib.bio.IBio;
import nebula.common.world.IModifiableCoord;
import net.minecraft.world.biome.Biome;

/**
 * The sapling access.
 * 
 * @author ueyudiud
 */
public interface ISaplingAccess extends IModifiableCoord, IBio
{
	@Override
	default BioData getData()
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
