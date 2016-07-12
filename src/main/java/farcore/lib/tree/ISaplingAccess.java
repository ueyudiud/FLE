package farcore.lib.tree;

import java.util.Random;

import farcore.lib.world.ICoord;
import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public interface ISaplingAccess extends ICoord
{
	ITree tree();
	
	TreeInfo info();
	
	BiomeGenBase biome();
	
	Random rng();

	void killTree();

	Block getBlock(int offsetX, int offsetY, int offsetZ);
}