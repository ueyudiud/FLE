package farcore.interfaces;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public interface ITreeGenerator
{
	void initLogBlock(Block log, Block leaves);
	
	/**
	 * Generate a tree with root coord.<br>
	 * @param world
	 * @param random
	 * @param x
	 * @param y
	 * @param z
	 * @param isNatural Is tree grown by sapling or generate in chunk decorating.
	 * @return Whether the tree is successfully generated.
	 */
	boolean generate(World world, Random random, int x, int y, int z, boolean isNatural);
	
//	void generate(World world, Random random, int checkScale, int maxSize, int x, int y, int z);
}