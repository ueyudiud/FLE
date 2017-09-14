/*
 * copyright© 2016-2017 ueyudiud
 */

package fle.core.tree;

import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeAspen extends Tree
{
	private final TreeGenSimple generator1 = new TreeGenSimple(this, 0.09F, false);
	
	public TreeAspen(Mat material)
	{
		super(material);
		this.generator1.setTreeLeavesShape(5, 8, 1, 1.8F);
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if (info != null)
		{
			this.generator1.setTreeLogShape(7 + info.height / 3, 4 + info.height / 2);
		}
		else
		{
			this.generator1.setTreeLogShape(7, 4);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}