/*
 * copyright 2016-2018 ueyudiud
 */
package farcore.lib.tree;

import java.util.Random;

import farcore.lib.bio.BioData;
import farcore.lib.bio.IOrder;
import farcore.lib.material.Mat;
import net.minecraft.world.World;

public class TreeVoid extends Tree
{
	public TreeVoid()
	{
		super(Mat.VOID);
	}
	
	@Override
	public IOrder getOrder()
	{
		return TreeOrder.ORDER;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, BioData info)
	{
		return false;
	}
	
	@Override
	public Mat material()
	{
		return Mat.VOID;
	}
}
