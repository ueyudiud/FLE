package farcore.lib.tree;

import java.util.Random;

import farcore.lib.world.ICoord;
import net.minecraft.world.biome.Biome;

public interface ISaplingAccess extends ICoord
{
	ITree tree();
	
	TreeInfo info();
	
	Biome biome();
	
	Random rng();
	
	void killTree();
}