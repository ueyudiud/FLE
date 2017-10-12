/*
 * copyright© 2016-2017 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import farcore.lib.tree.TreeInfo;
import net.minecraft.world.World;

public class TreeWillow extends Tree
{
	private final TreeGenSimple generator1 = new TreeGenSimple(this, 0.08F, true);
	
	public TreeWillow(Mat material)
	{
		super(material);
		this.generator1.setTreeLeavesShape(1, 6, 2, 3.6F);
		this.leavesCheckRange = 5;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, TreeInfo info)
	{
		if (info != null)
		{
			this.generator1.setTreeLogShape(4 + info.height / 3, 4 + info.height / 3);
		}
		else
		{
			this.generator1.setTreeLogShape(4, 4);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}
