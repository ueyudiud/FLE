package farcore.lib.tree;

import farcore.lib.bio.GeneticMaterial;
import farcore.lib.bio.IBiology;
import nebula.common.world.IModifiableCoord;
import net.minecraft.world.biome.Biome;

public interface ISaplingAccess extends IModifiableCoord, IBiology
{
	@Override
	default GeneticMaterial getGeneticMaterial()
	{
		return info().gm;
	}
	
	@Override
	default ITree getSpecie()
	{
		return tree();
	}
	
	ITree tree();
	
	TreeInfo info();
	
	@Deprecated
	Biome biome();
	
	void killTree();
}