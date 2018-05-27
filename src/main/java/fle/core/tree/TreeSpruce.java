/*
 * copyright 2016-2018 ueyudiud
 */
package fle.core.tree;

import java.util.Random;

import farcore.lib.bio.BioData;
import farcore.lib.material.Mat;
import farcore.lib.tree.Tree;
import net.minecraft.world.World;

public class TreeSpruce extends Tree
{
	private final TreeGenClassic generator1 = new TreeGenClassic(this, 0.05F);
	
	public TreeSpruce(Mat material)
	{
		super(material);
		this.generators.add(this.generator1);
		this.isBroadLeaf = false;
	}
	
	@Override
	public boolean generateTreeAt(World world, int x, int y, int z, Random random, BioData info)
	{
		if (info != null)
		{
			this.generator1.setHeight(6 + info.capabilities[0] / 2, 4 + info.capabilities[0] / 4);
		}
		else
		{
			this.generator1.setHeight(6, 4);
		}
		return this.generator1.generateTreeAt(world, x, y, z, random, info);
	}
}
