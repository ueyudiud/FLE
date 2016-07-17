package farcore.lib.tree;

import java.util.Random;

import net.minecraft.world.World;

public interface ITreeGenerator
{	
	boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info);
}