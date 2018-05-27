/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.bio.BioData;
import farcore.lib.material.Mat;
import farcore.lib.tree.ISaplingAccess;
import farcore.lib.tree.Tree;
import net.minecraft.world.World;

public class TreeCeiba extends Tree
{
	private final TreeGenJungle generator1 = new TreeGenJungle(this, 1.2E-2F);
	
	public TreeCeiba(Mat material)
	{
		super(material);
		this.generators.add(this.generator1);
		this.leavesCheckRange = 5;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, BioData info)
	{
		if (info != null)
		{
			this.generator1.setHeight(36 + info.capabilities[0] * 2, 4 + info.capabilities[0]);
		}
		else
		{
			this.generator1.setHeight(38, 4);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
	
	@Override
	public int getGrowAge(ISaplingAccess access)
	{
		return 120;
	}
}
