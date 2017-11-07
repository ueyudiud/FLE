package farcore.lib.tree;

import java.util.Random;

import farcore.lib.material.Mat;
import net.minecraft.world.World;

public class TreeVoid extends Tree
{
	public TreeVoid()
	{
		super(Mat.VOID, -1, 0.0F, 0.0F, 0.0F, 0.0F);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		return false;
	}
}
